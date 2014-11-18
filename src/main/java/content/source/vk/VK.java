package content.source.vk;

import content.PersonCard;
import content.PersonId;
import content.PersonList;
import content.SocialLink;
import content.source.ContentSource;
import content.source.University;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import util.net.HttpDownloader;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VK implements ContentSource {

//    public static final String AuthorizeURL =
//            "https://oauth.vk.com/authorize?client_id=4628886&redirect_uri=https://oauth.vk.com/blank.html&display=page&response_type=token";
//    private static Boolean isAuthorized = false;

    private VKUserSearcher searcher;

    public VK(){
        VKDataHelper.initCountries();
        VKConst.countries.put("Россия", 1);
        searcher = new VKUserSearcher();
    }

    @Override
    public void retrieve(PersonCard data, PersonList dest) {
        ArrayList<VKPerson> persons = searcher.getPersons(data);

        for (VKPerson person: persons){
            dest.addPerson(transformVKPersonToPersonCard(person, data));
        }
    }

    @Override
    public Type getType() {
        return Type.VK;
    }

    public PersonCard transformVKPersonToPersonCard(VKPerson person, PersonCard init){
        PersonCard personCard = new PersonCard();
        String name = person.getFirstName();
        String surname = person.getLastName();
        Integer id = person.getId();
        Integer bday = person.getBDay();
        Integer bmonth = person.getBMonth();
        Integer byear = person.getBYear();
        String country = person.getCountry();
        String city = person.getCity();
        String mobile = person.getMobilePhone();
        String photo = person.getPhoto();
        String sex = person.getSex();
        ArrayList<University> universities = person.getUniversities();
        Map<SocialLink.LinkType, String>socialLinks = person.getSocialLinks();

        if (name != null && !name.equals(""))
            personCard.setName(name);
        if (surname != null && !surname.equals(""))
            personCard.setSurname(surname);

        if (id != null && id != -1)
            personCard.setId(new PersonId(Type.VK, id));

        if (country != null && !country.equals(""))
            personCard.setCountry(country);
        if (city != null && !city.equals(""))
            personCard.setCity(city);

        if (mobile != null && !mobile.equals(""))
            personCard.setMobilePhone(mobile);

        if (photo != null && !photo.equals(""))
            personCard.addAvatar(photo);

        if (sex != null && !sex.equals(""))
            personCard.setSex(sex);

        if (universities != null && universities.size() != 0)
            personCard.setUniversities(universities);

        if (socialLinks != null && socialLinks.size() != 0){
            Map<SocialLink.LinkType, SocialLink> links = new HashMap<>();
            Set<SocialLink.LinkType> social = socialLinks.keySet();
            for (SocialLink.LinkType s: social){
                String identity = socialLinks.get(s);
                SocialLink link = new SocialLink(s, identity, identity);
                links.put(s, link);
            }
            personCard.setSocialLinks(links);
        }

        if (bday != null){
            personCard.setBirthDate(new PersonCard.Date(bday, bmonth, byear));
        }

        personCard.setType(Type.VK);
        personCard.setPersonLink(new SocialLink(SocialLink.LinkType.VK, id.toString(), id.toString()));

        return personCard;
    }


}
