package content.source.vk;


import content.PersonCard;
import org.json.JSONArray;
import org.json.JSONObject;
import util.net.HttpDownloader;
import util.net.UrlParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class VKUserSearcher {

    private String token;

    public VKUserSearcher(String _token){
        this.token = _token;
    }

    public ArrayList<PersonCard> getPersons(PersonCard data){
        return getPersonsByIds((usersSearch(getQueryParams(data))));
    }

    public UrlParams getQueryParams(PersonCard data){
        String name = data.getName();
        String second_name = data.getSurname();
        String country = data.getCountry();
        String city = data.getCity();
        String university = null;
        Integer universityYear = null;
        if (!data.getUniversities().isEmpty()) {
            university = data.getUniversities().get(0).getName();
            universityYear = data.getUniversities().get(0).getGraduation();
        }
        Integer ageFrom = data.getAgeFrom();
        Integer ageTo = data.getAgeTo();
        PersonCard.Date bdate = data.getBirthDate();
        Integer birthDay = null;
        Integer birthMonth = null;
        Integer birthYear = null;
        if (bdate != null){
            birthDay = bdate.getDay();
            birthMonth = bdate.getMonth();
            birthYear = bdate.getYear();
        }

        String company = null;
        if (!data.getJobs().isEmpty()){
            company = data.getJobs().get(0);
        }

        UrlParams params = new UrlParams();
        try {

            if (country != null && !country.equals("") &&
                    VKConst.countries != null &&
                    VKConst.countries.containsKey(country)){
                Integer countryId = VKConst.countries.get(country);
                params.add("country", countryId.toString());
                if (city != null && !city.equals("")){
                    Integer cityId = VKDataHelper.getCityId(countryId, city);
                    if (cityId != -1)
                        params.add("city", cityId.toString());
                }
            }
            if (university != null && !university.equals("")){
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
            String request = VKConst.getUsersSearchUrl(token);
            VKConst.addAccessTokenParam(params, token);
            VKConst.addVersionParam(params);
            String response = HttpDownloader.httpGet(request, params).getBody();
            JSONArray responseArray = VKResponseParser.getResponseJSONitems(response);

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

    public ArrayList<PersonCard> getPersonsByIds(String ids){
        try {
            UrlParams urlParams = new UrlParams();
            urlParams.add("user_ids", ids);
            urlParams.add("fields", VKConst.fields);
            VKConst.addVersionParam(urlParams);
            VKConst.addAccessTokenParam(urlParams, token);
            String request = VKConst.getUsersGetUrl(token);
            String response = HttpDownloader.httpGet(request, urlParams).getBody();
            JSONArray responseArray = new JSONObject(response).getJSONArray("response");
            return VKResponseParser.getPersons(responseArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
