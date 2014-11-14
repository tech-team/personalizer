package content.source.vk;

import content.PersonCard;
import content.PersonList;
import content.source.ContentSource;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

public class VK implements ContentSource {

//    public static final String AuthorizeURL =
//            "https://oauth.vk.com/authorize?client_id=4628886&redirect_uri=https://oauth.vk.com/blank.html&display=page&response_type=token";
//    private static Boolean isAuthorized = false;

    public static final String token =
            "d7254a38f3ca97c9f901ef135809d3d0719261092da4c8c5f103483994fae28479c9dada8bcba15f51461";
    private static final String usersSearchUrl =
            "https://api.vk.com/method/users.search?&access_token="+token+"&v=5.26&count=5";
    private static final String usersGetUrl =
            "https://api.vk.com/method/users.get?&access_token="+token+"&v=5.26";
    private static Map<String, Integer> contries;

    public VK(){
        try {
            if (contries == null){
                contries = VKDataMaps.getCountries();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void retrieve(PersonCard data, PersonList dest) {
//        personCards.setInitial(data);

        ArrayList<Pair<Object, Object>> params = getQueryParams(data);
        String usersIds = usersSearch(params);
        ArrayList<VKPerson> persons = getPersonsByIds(usersIds);

        for (VKPerson person: persons){
            dest.addPerson(transformVKPersonToPersonCard(person, data));
        }
//        return personCards;
    }

    @Override
    public Type getType() {
        return Type.VK;
    }

    public ArrayList<VKPerson> getPersonsByIds(String ids){
        try {
            String parameters = "";
            parameters = addParam(parameters, new Pair<>("user_ids", ids));
            parameters = addParam(parameters, new Pair<>("fields", VKPerson.fields + ",connections"));
            String request = usersGetUrl + "&" + parameters;
            String response = RequestHelper.getResponse(request);
            JSONArray responseArray = new JSONObject(response).getJSONArray("response");

            ArrayList<VKPerson> persons = new ArrayList<>();
            for (int i = 0; i < responseArray.length(); i++){
                JSONObject item = responseArray.getJSONObject(i);
                VKPerson person = new VKPerson(item);
                persons.add(person);
            }
            return persons;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String usersSearch(ArrayList<Pair<Object, Object>> params){
        try {
            String parameters = "";
            for (Pair<Object, Object> param: params)
                parameters = addParam(parameters, param);

            String request = usersSearchUrl + "&" + parameters;
            String response = RequestHelper.getResponse(request);
            JSONArray responseArray = RequestHelper.getResponseJSONitems(response);

            return getPersonsIdsByArray(responseArray);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPersonsIdsByArray(JSONArray response){
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < response.length(); i++){
            ids.append(response.getJSONObject(i).getInt("id"));
            ids.append(",");
        }
        return ids.toString();
    }

    public ArrayList<Pair<Object, Object>> getQueryParams(PersonCard data){
        //TODO получить все параметры из карты
        String name = "Игорь";
        String second_name = "Латкин";
        String country = "Россия";
        String city = "Москва";
        String university = "МГТУ им. Баумана";
        Integer ageFrom = 5;
        Integer ageTo = 25;
        Integer birthDay = null;
        Integer birthMonth = null;
        Integer birthYear = null;
        Integer universityYear = null;
        String company = null;

        ArrayList<Pair<Object, Object>> params = new ArrayList<>();
        try {

            if (!country.equals("") && contries.containsKey(country)){
                Integer countryId = contries.get(country);
                params.add(new Pair<>("country", countryId));
                if (!city.equals("")){
                    Integer cityId = VKDataMaps.getCityId(countryId, city);
                    params.add(new Pair<>("city", cityId));
                }
            }
            if (!university.equals("")){
                Integer universityId = VKDataMaps.getUniversityId(university);
                params.add(new Pair<>("university", universityId));
            }
            if (ageFrom != null){
                params.add(new Pair<>("age_from", ageFrom));
            }
            if (ageTo != null){
                params.add(new Pair<>("age_to", ageTo));
            }
            if (birthDay != null){
                params.add(new Pair<>("birth_day", birthDay));
            }
            if (birthMonth != null){
                params.add(new Pair<>("birth_month", birthMonth));
            }
            if (birthYear != null){
                params.add(new Pair<>("birth_year", birthYear));
            }
            if (universityYear != null){
                params.add(new Pair<>("university_year", universityYear));
            }
            if (company != null){
                params.add(new Pair<>("company", company));
            }
            params.add(new Pair<>("q", (name!=null?name:"") + (second_name!=null?" "+second_name:"")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }

    public PersonCard transformVKPersonToPersonCard(VKPerson person, PersonCard init){
        PersonCard personCard = new PersonCard();
//        if (init.getAgeFrom()) TODO выставить года, как в ините
        return personCard;
    }

    public static String addParam(String request, Pair<Object, Object> param) throws UnsupportedEncodingException {
        return request + String.format("&"+param.getKey()+"=%s",
                URLEncoder.encode(param.getValue().toString(), "UTF-8"));
    }
}
