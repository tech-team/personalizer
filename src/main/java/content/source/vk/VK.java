package content.source.vk;

import content.PersonCard;
import content.Persons;
import content.source.ContentSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class VK implements ContentSource {

//    public static final String AuthorizeURL =
//            "https://oauth.vk.com/authorize?client_id=4628886&redirect_uri=https://oauth.vk.com/blank.html&display=page&response_type=token";
//    private static Boolean isAuthorized = false;

    public static final String token =
            "77ba834f246f635e262b38bcf76af9fd6e1f3326117dd9e2c4554d6229ff2c047d8b178dda4f6186a80f4";
    private static final String usersSearchUrl =
            "https://api.vk.com/method/users.search?&access_token="+token+"&v=5.26&count=1000";

    @Override
    public Persons retrieve(Persons data) {

        return null;
    }

    @Override
    public Persons retrieve(PersonCard data) {
        usersSearch("Вася Бабич");
        return null;
    }

    public void getInfo(String name, String surName) {

    }

    public ArrayList<VKPerson> usersSearch(String query){
        try {
            String parameters = "";
            parameters = UsersSearchRequest.addQueryParameter(parameters, query);
            parameters = UsersSearchRequest.addAgeToParameter(parameters, 25);
            parameters = UsersSearchRequest.addFieldsParameter(parameters);

            String request = usersSearchUrl + "&" + parameters;
            String response = RequestHelper.getResponse(request);
            JSONArray responseArray = RequestHelper.getResponseJSON(response);
            System.out.print(response);

            return getPersonsByArray(responseArray);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<VKPerson> getPersonsByArray(JSONArray response){
        ArrayList<VKPerson> persons = new ArrayList<>();
        for (int i = 0; i < response.length(); i++){
            JSONObject item = response.getJSONObject(i);
            VKPerson vkPerson = new VKPerson(item);
            persons.add(vkPerson);
        }
        return persons;
    }

}
