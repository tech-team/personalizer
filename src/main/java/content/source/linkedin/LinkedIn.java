package content.source.linkedin;

import content.ContentData;
import content.source.ContentSource;
import org.json.JSONException;
import org.json.JSONObject;
import util.net.Headers;
import util.net.HttpDownloader;
import util.net.UrlParams;

import java.io.IOException;

public class LinkedIn implements ContentSource {
    public static final String HOST = "Host";
    public static final String CONNECTION = "Connection";
    public static final String AUTHORIZATION = "Authorization";

    public static final String JSON_EXPIRES = "expires_in";
    public static final String JSON_TOKEN = "access_token";

    public static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    public static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";

    public static  String STATE = "db64db471444f2a6a7f9ed93314c9e43";

    public static String token = "AQX7E09cq7vo9kWHPyz70ENZdYOmcG0xHr1yI0QDNtjZr5DaTIIxb33uWpdvk2DYSVmchi4sBdJs6pUO6qlEo42nSR8ENawmn6KEDC9xoDaTqfwDOtIqhqmr0shc46RcpPU-GW5bRa1rERXlB3at2_K5EWmren84lE8bE_WEbsW5tEjYP1c";

    @Override
    public ContentData retrieve(ContentData data) {


        Headers headers = new Headers();
        headers.add(HOST, "api.linkedin.com");
        headers.add(CONNECTION, "Keep-Alive");
        headers.add(AUTHORIZATION, "Bearer " + token);

        UrlParams params = new UrlParams();


        return null;
    }


    public static void getToken() {

        UrlParams params = new UrlParams();
        params.add(Parameters.RESPONSE_TYPE, "code");
        params.add(Parameters.CLIENT_ID, "YOUR_API_KEY");
        params.add(Parameters.STATE, STATE);
        params.add(Parameters.REDIRECT_URI, "YOUR_REDIRECT_URI");


        String code = "";
        token = requestAccessToken(code);

    }


    public static String requestAccessToken(String code) {
        UrlParams params = new UrlParams();
        params.add(Parameters.GRANT_TYPE, "authorization_code");
        params.add(Parameters.CODE, code);
        params.add(Parameters.REDIRECT_URI, "https://site.com");
        params.add(Parameters.CLIENT_ID, "YOUR_API_KEY");
        params.add(Parameters.CLIENT_SECRET, "YOUR_SECRET");
        HttpDownloader.Request request = new HttpDownloader.Request(ACCESS_TOKEN_URL, params, null);
        try {
            String response = HttpDownloader.httpPost(request);
            if(response != null) {
                return getTokenFromResponse(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTokenFromResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            int expires = json.getInt(JSON_EXPIRES);
            return json.getString(JSON_TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class Parameters {
        public static final String RESPONSE_TYPE = "response_type";
        public static final String CLIENT_ID = "client_id";
        public static final String STATE = "state";
        public static final String REDIRECT_URI = "redirect_uri";
        public static final String GRANT_TYPE = "grant_type";
        public static final String CODE = "code";
        public static final String CLIENT_SECRET = "client_secret";
    }

    public static void response() {
        Headers headers = new Headers();
        headers.add(HOST, "api.linkedin.com");
        headers.add(CONNECTION, "Keep-Alive");
        headers.add(AUTHORIZATION, "Bearer " + token);

        UrlParams params = new UrlParams();
        params.add("last-name", "Standish");

        try {
            String response = HttpDownloader.httpGet(new HttpDownloader.Request("https://api.linkedin.com/v1/people-search", params, headers));
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
