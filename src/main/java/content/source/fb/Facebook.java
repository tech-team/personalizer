package content.source.fb;

import content.PersonCard;
import content.PersonList;
import content.SocialLink;
import content.source.ContentSource;
import org.json.JSONArray;
import org.json.JSONObject;
import util.net.HttpDownloader;
import util.net.UrlParams;

import java.io.IOException;

public class Facebook implements ContentSource {

    public static final String baseUrl = "https://graph.facebook.com/v2.2/";
    public static final String appId = "";
    public static final String appSecret = "";
    public static final String pic = "/picture?redirect=false&height=150&width=150";
    public static final String userAccessToken = "";

    @Override
    public void retrieve(PersonCard personCard, PersonList dest) {

        UrlParams params = new UrlParams();
        params.add("access_token", userAccessToken);

        UrlParams searchParams = new UrlParams();
        searchParams.add("q", "maxim myalkin")
                .add("type", "user")
                .add("limit", "50")
                .add("access_token", userAccessToken);

        //FacebookApi
        HttpDownloader.Request request = new HttpDownloader.Request( baseUrl + "search", searchParams);

        try {
            HttpDownloader.Response resp = HttpDownloader.httpGet(request);
            JSONArray ids = new JSONObject(HttpDownloader.httpGet(request)).getJSONArray("data");

            params = new UrlParams();
            params.add("access_token", userAccessToken);

            for (int i = 0; i < ids.length(); ++i) {
                PersonCard person = new PersonCard();

                String id = ids.getJSONObject(i).getString("id");

                JSONObject cardObject = new JSONObject(HttpDownloader.httpGet(baseUrl + id, params));
                person.setName(cardObject.getString("first_name"))
                      .setSurname(cardObject.getString("last_name"));

                person.setPersonLink(new SocialLink(SocialLink.LinkType.FB,
                                     cardObject.getString("id"), cardObject.getString("link")));

                UrlParams urlParams = new UrlParams().add("redirect", "false")
                                                     .add("height", "160")
                                                     .add("width", "160");
                String picture = new JSONObject(HttpDownloader.httpGet(baseUrl + id + "/picture", urlParams)).getJSONObject("data").getString("url");
                person.addAvatar(picture);

                dest.addPerson(person);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Type getType() {
        return Type.FB;
    }

    public static void main(String[] args) {
        new Facebook().retrieve(null, new PersonList());
    }
}
