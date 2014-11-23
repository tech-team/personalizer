package content.source.linkedin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.net.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static util.net.HttpDownloader.httpGet;
import static util.net.HttpDownloader.httpPost;

public class LinkedInRequest {

    public Headers headers = new Headers();

    public void makeHeaders(String cookie) {
        if(cookie != null && !cookie.equals("")) {
            headers.add("Cookie", cookie);
        }
        headers.add("Host", "ru.linkedin.com")
               .add("Connection", "keep-alive");
    }

    public static UrlParams getInputParams() {
        UrlParams params = new UrlParams();
        try {
            Document doc = Jsoup.connect("https://www.linkedin.com/").get();
            params.add("session_key", "prsnlzr@gmail.com")
                    .add("session_password", "tech-team")
                    .add("loginCsrfParam", doc.select("#loginCsrfParam-login").attr("value"))
                    .add("csrfToken", doc.select("#csrfToken-login").attr("value"))
                    .add("sourceAlias", doc.select("#sourceAlias-login").attr("value"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return params;
    }

    public HttpResponse makeLoginRequest() {
        return makePostRequest("https://www.linkedin.com/uas/login-submit", getInputParams(), null);
    }

    public HttpResponse makeFindRequest(String name, String lastName) {
        try {
            name = URLEncoder.encode(name, "UTF-8");
            lastName = URLEncoder.encode(lastName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://www.linkedin.com/pub/dir/" + name + "/" + lastName;
        return makeGetRequest(url, null);
    }

    public HttpResponse makePersonRequest(String url) {
        return makeGetRequest(url, headers);
    }

    private HttpResponse makeGetRequest(String url, Headers headers) {
        HttpRequest request = new HttpRequest(url, null, headers);
        request.setFollowRedirects(false);
        HttpResponse response = null;
        try {
            response = httpGet(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    private HttpResponse makePostRequest(String url, UrlParams params, Headers headers) {
        HttpRequest request = new HttpRequest(url,
                params,
                headers);
        HttpResponse response = null;
        try {
            response = httpPost(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
