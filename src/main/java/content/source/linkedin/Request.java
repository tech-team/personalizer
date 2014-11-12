package content.source.linkedin;


import org.json.JSONException;
import org.json.JSONObject;
import util.net.Headers;
import util.net.HttpDownloader;
import util.net.UrlParams;

import java.io.IOException;

public class Request {

    public static final String HEADER_HOST = "Host";
    public static final String HEADER_CONNECTION = "Connection";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static String token = "token";

    public static final String URL_PROFILE = "https://api.linkedin.com/v1/people";
    public static final String URL_ACCESS_TOKEN = "https://www.linkedin.com/uas/oauth2/accessToken";

    public HttpDownloader.Request makeRequest(String url, UrlParams params) {
        Headers headers = new Headers();
        headers.add(HEADER_HOST, "api.linkedin.com");
        headers.add(HEADER_CONNECTION, "Keep-Alive");
        headers.add(HEADER_AUTHORIZATION, "Bearer " + token);
        return new HttpDownloader.Request(url, params, headers);
    }

    public String getProfileById(String id) {
        HttpDownloader.Request request = makeRequest(URL_PROFILE + "/id=" + id, null);
        String response = null;
        try {
            response= HttpDownloader.httpGet(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String requestAccessToken(String code) {
        if(code != null) {
            UrlParams params = new UrlParams();
            params.add(Parameters.GRANT_TYPE, "authorization_code");
            params.add(Parameters.CODE, code);
            params.add(Parameters.REDIRECT_URI, "https://site.com");
            params.add(Parameters.CLIENT_ID, "YOUR_API_KEY");
            params.add(Parameters.CLIENT_SECRET, "YOUR_SECRET");
            HttpDownloader.Request request = new HttpDownloader.Request(URL_ACCESS_TOKEN, params, null);
            try {
                String response = HttpDownloader.httpPost(request);
                if (response != null) {
                    return getTokenFromResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getTokenFromResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            return json.getString("token");
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
}
