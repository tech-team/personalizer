package content.source.fb;

import content.PersonCard;
import content.PersonList;
import content.SocialLink;
import org.json.JSONArray;
import org.json.JSONObject;
import util.net.HttpDownloader;
import util.net.HttpRequest;
import util.net.UrlParams;

import java.io.IOException;
import java.net.URL;

import static content.source.fb.FBData.*;

public class FBRequest {

    private static UrlParams params = new UrlParams();

    private static UrlParams searchParams = new UrlParams().add("type", "user")
                                                           .add("limit", "50");

    private static UrlParams picParams = new UrlParams().add("redirect", "false")
                                                        .add("height", "160")
                                                        .add("width", "160");


    FBRequest() {
        setAccessToken();
    }

    private void setAccessToken() {
        UrlParams loginParams = new UrlParams()
                .add("client_id", appId)
                .add("redirect_uri", appUrl)
                .add("response_type", loginResponseType);

        HttpRequest request = new HttpRequest(loginUrl, loginParams);
        request.setFollowRedirects(false);

//        try {
//            String location = HttpDownloader.httpGet(request).getHeaders().getHeader("Location").getValue();
//
//            request = new HttpRequest(location);
//            request.setFollowRedirects(false);
//            String response = HttpDownloader.httpGet(location).getUrl().toString();
//
//            //TODO: Добиться редиректа
//            String accessToken = new URL(response).getQuery().split("=")[1];
            String accessToken = "CAACEdEose0cBAH8gwZCSLqnejLjiYjQJC663I6sItQcnB1ZCqzY8T0K3L0GPNsBiadLVUPdl99Jobv10rdc8ZAEWoyUpbtbYnWBISiLlZB6HIlzUubAbDdGvnufjs4oTQZCDKRzBYDwCn6LBuCzZATIZBK30WmdUKBJrc1ZAywCaZBcnhgMBcFZCYaxSjpsu1ddOsbZCBzn5VnU7jTJ6ZBuWf6DXicOJ6BCykQ4ZD";

            params.add("access_token", accessToken);
            searchParams.add("access_token", accessToken);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    void search(String name, PersonList dest) throws IOException {

        searchParams.add("q", name);

        HttpRequest request = new HttpRequest( baseUrl + "search", searchParams);
        JSONArray people = new JSONObject(HttpDownloader.httpGet(request).getBody()).getJSONArray("data");

        PersonList personList = new PersonList();
        for (int i = 0; i < people.length(); ++i) {
            PersonCard person = new PersonCard();

            String id = people.getJSONObject(i).getString("id");

            JSONObject cardObject = new JSONObject(HttpDownloader.httpGet(baseUrl + id, params).getBody());
            person.setName(cardObject.getString("first_name"))
                  .setSurname(cardObject.getString("last_name"));

            person.setPersonLink(new SocialLink(SocialLink.LinkType.FB,
                    cardObject.getString("id"), cardObject.getString("link")));

            String picture = new JSONObject(HttpDownloader.httpGet(baseUrl + id + "/picture", picParams).getBody()).getJSONObject("data").getString("url");
            person.addAvatar(picture);

            dest.addPerson(person);
        }
    }

}
