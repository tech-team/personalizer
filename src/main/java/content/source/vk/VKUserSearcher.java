package content.source.vk;


import content.PersonCard;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import util.net.HttpDownloader;
import util.net.UrlParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class VKUserSearcher {

    public ArrayList<VKPerson> getPersons(PersonCard data){
        return getPersonsByIds((usersSearch(getQueryParams(data))));
    }

    public UrlParams getQueryParams(PersonCard data){
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

        UrlParams params = new UrlParams();
        try {

            if (!country.equals("") &&
                    VKConst.countries != null &&
                    VKConst.countries.containsKey(country)){
                Integer countryId = VKConst.countries.get(country);
                params.add("country", countryId.toString());
                if (!city.equals("")){
                    Integer cityId = VKDataHelper.getCityId(countryId, city);
                    if (cityId != -1)
                        params.add("city", cityId.toString());
                }
            }
            if (!university.equals("")){
                Integer universityId = VKDataHelper.getUniversityId(university);
                if (universityId != -1)
                    params.add("university", universityId.toString());
            }
            if (ageFrom != null){
                params.add("age_from", ageFrom.toString());
            }
            if (ageTo != null){
                params.add("age_to", ageTo.toString());
            }
            if (birthDay != null){
                params.add("birth_day", birthDay.toString());
            }
            if (birthMonth != null){
                params.add("birth_month", birthMonth.toString());
            }
            if (birthYear != null){
                params.add("birth_year", birthYear.toString());
            }
            if (universityYear != null){
                params.add("university_year", universityYear.toString());
            }
            if (company != null){
                params.add("company", company.toString());
            }
            params.add("q", (name!=null?name:"") + (second_name!=null?" "+second_name:""));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }

    public String usersSearch(UrlParams params){
        try {
            String request = VKConst.getUsersSearchUrl(VKConst.token);
            String response = HttpDownloader.httpGet(request, params);
            JSONArray responseArray = RequestHelper.getResponseJSONitems(response);

            return getPersonsIdsByArray(responseArray);

        } catch (IOException e) {
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

    public ArrayList<VKPerson> getPersonsByIds(String ids){
        try {
            String parameters = "";
            parameters = RequestHelper.addParam(parameters, new Pair<>("user_ids", ids));
            parameters = RequestHelper.addParam(parameters, new Pair<>("fields", VKPerson.fields + ",connections"));
            String request = VKConst.getUsersGetUrl(VKConst.token) + "&" + parameters;
            String response = HttpDownloader.httpGet(request);
            JSONArray responseArray = new JSONObject(response).getJSONArray("response");

            ArrayList<VKPerson> persons = new ArrayList<>();
            for (int i = 0; i < responseArray.length(); i++){
                JSONObject item = responseArray.getJSONObject(i);
                VKPerson person = new VKPerson(item);
                persons.add(person);
            }
            return persons;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
