package content.source.vk;

import content.PersonCard;
import content.Persons;
import content.source.ContentSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

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


    @Override
    public Persons retrieve(Persons data) {

        return null;
    }

    @Override
    public Persons retrieve(PersonCard data) {
        String usersIds = usersSearch("Вася Бабич");
        ArrayList<VKPerson> persons = getPersonsByIds(usersIds);
        return null;
    }

    public ArrayList<VKPerson> getPersonsByIds(String ids){
        try {
            String parameters = "";
            parameters = UsersSearchRequest.addUserIdsParameter(parameters, ids);
            parameters = UsersSearchRequest.addFieldsParameter(parameters, VKPerson.fields+",connections");
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

    public String usersSearch(String query){
        try {
            String parameters = "";
            parameters = UsersSearchRequest.addQueryParameter(parameters, query);
            parameters = UsersSearchRequest.addAgeToParameter(parameters, 25);

            String request = usersSearchUrl + "&" + parameters;
            String response = RequestHelper.getResponse(request);
            JSONArray responseArray = RequestHelper.getResponseJSONitems(response);
            System.out.print(response);

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

}
