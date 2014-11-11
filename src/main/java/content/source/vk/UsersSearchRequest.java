package content.source.vk;


import javafx.util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UsersSearchRequest {
    public static String addQueryParameter(String request, String query) throws UnsupportedEncodingException {
        return request + String.format("&q=%s",
                URLEncoder.encode(query, "UTF-8"));
    }

    public static String addUserIdsParameter(String request, String ids) throws UnsupportedEncodingException {
        return request + String.format("&user_ids=%s",
                URLEncoder.encode(ids, "UTF-8"));
    }


    public static String addCityParameter(String request, Integer city) throws UnsupportedEncodingException {
        return request + String.format("&city=%s",
                URLEncoder.encode(city.toString(), "UTF-8"));
    }

    public static String addCountryParameter(String request, Integer country) throws UnsupportedEncodingException {
        return request + String.format("&country=%s",
                URLEncoder.encode(country.toString(), "UTF-8"));
    }

    public static String addUniversityParameter(String request, Integer university) throws UnsupportedEncodingException {
        return request + String.format("&university=%s",
                URLEncoder.encode(university.toString(), "UTF-8"));
    }

    public static String addAgeFromParameter(String request, Integer ageFrom) throws UnsupportedEncodingException {
        return request + String.format("&age_from=%s",
                URLEncoder.encode(ageFrom.toString(), "UTF-8"));
    }

    public static String addAgeToParameter(String request, Integer ageTo) throws UnsupportedEncodingException {
        return request + String.format("&age_to=%s",
                URLEncoder.encode(ageTo.toString(), "UTF-8"));
    }

    public static String addFieldsParameter(String request, String fields) throws UnsupportedEncodingException {
        if (fields != null)
            return request + String.format("&fields=%s",
                    URLEncoder.encode(fields, "UTF-8"));
        return request + String.format("&fields=%s",
                URLEncoder.encode(VKPerson.fields, "UTF-8"));
    }



}
