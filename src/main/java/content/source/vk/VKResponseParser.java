package content.source.vk;


import content.PersonCard;
import content.PersonId;
import content.SocialLink;
import content.University;
import content.source.ContentSource;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class VKResponseParser {

    public static JSONArray getResponseJSONitems(String response){
        /*
        Возвращает массив items из ответа
         */
        JSONObject jsonObject = new JSONObject(response);
        jsonObject = jsonObject.getJSONObject("response");
        return jsonObject.getJSONArray("items");
    }

    public static ArrayList<PersonCard> getPersons(JSONArray response){
        ArrayList<PersonCard> personCards = new ArrayList<>();
        for (int i = 0; i < response.length(); i++){
            JSONObject item = response.getJSONObject(i);
            PersonCard person = getPerson(item);
            personCards.add(person);
        }
        return personCards;
    }

    public static PersonCard getPerson(JSONObject item){
        PersonCard person = new PersonCard();

        person.setPersonLink(new SocialLink(SocialLink.LinkType.VK,
                String.valueOf(item.getInt("id")),
                String.valueOf(item.getInt("id"))));
        if (!item.optString("first_name").equals("")){
            person.setName(item.getString("first_name"));
        }
        if (!item.optString("last_name").equals("")){
            person.setSurname(item.getString("last_name"));
        }

        if (item.optJSONObject("country") != null){
            person.setCountry(item.getJSONObject("country").getString("title"));
        }
        if (item.optJSONObject("city") != null){
            person.setCity(item.getJSONObject("city").getString("title"));
        }

        if (item.optInt("sex") != 0){
            person.setSex(getSex(item.getInt("sex")));
        }

        if (!item.optString("photo_200_orig").equals("")){
            person.addAvatar(item.getString("photo_200_orig"));
        }

        JSONArray universities;
        if ((universities = item.optJSONArray("universities")) != null){
            setUniversities(universities, person);
        }

        if (!item.optString("bdate").equals("")){
            setBdate(item.getString("bdate"), person);
        }

        if (!item.optString("mobile_phone").equals("")){
            person.setMobilePhone(item.getString("mobile_phone"));
        }

        setSocialLinks(person, item);

        return person;
    }

    public static String getSex(int sex){
        if (sex == 1)
            return "F";
        if (sex == 0)
            return "M";
        return null;
    }

    public static void setUniversities(JSONArray universities, PersonCard personCard){
        for (int i = 0; i < universities.length(); i++){
            JSONObject item = universities.getJSONObject(i);
            personCard.addUniversity(new University
                            (item.getString("name"),
                                    (item.optInt("graduation") == 0 ? null : item.getInt("graduation")))
            );
        }
    }

    public static void setBdate(String bdate, PersonCard personCard){
        String[] bdates = bdate.split("\\.");
        Integer bDay = null;
        Integer bMonth = null;
        Integer bYear = null;
        if (bdates.length >= 1){
            bDay = Integer.parseInt(bdates[0]);
        }
        if (bdates.length >= 2){
            bMonth = Integer.parseInt(bdates[1]);
        }
        if (bdates.length >= 3){
            bYear = Integer.parseInt(bdates[2]);
        }
        personCard.setBirthDate(new PersonCard.Date(bDay, bMonth, bYear));
    }

    public static void setSocialLinks(PersonCard personCard, JSONObject item){
        if (!item.optString("skype").equals("")){
            String skype = item.getString("skype");
            personCard.addSocialLink(SocialLink.LinkType.SKYPE,
                    new SocialLink(SocialLink.LinkType.SKYPE, skype, skype));
        }
        if (!item.optString("facebook").equals("")){
            String facebook = item.getString("facebook");
            personCard.addSocialLink(SocialLink.LinkType.FB,
                    new SocialLink(SocialLink.LinkType.FB, facebook, facebook));

        }
        if (!item.optString("twitter").equals("")){
            String twitter = item.getString("twitter");
            personCard.addSocialLink(SocialLink.LinkType.TWITTER,
                    new SocialLink(SocialLink.LinkType.TWITTER, twitter, twitter));
        }
    }

}
