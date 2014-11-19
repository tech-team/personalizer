package content.source.linkedin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.net.Headers;
import util.net.HttpDownloader;
import util.net.UrlParams;

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

    public HttpDownloader.Response makeLoginRequest() {
        return makePostRequest("https://www.linkedin.com/uas/login-submit", getInputParams(), null);
    }

    public HttpDownloader.Response makeFindRequest(String name, String lastName) {
        try {
            name = URLEncoder.encode(name, "UTF-8");
            lastName = URLEncoder.encode(lastName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://www.linkedin.com/pub/dir/" + name + "/" + lastName;
        return makeGetRequest(url, null);
    }

    public HttpDownloader.Response makePersonRequest(String url) {
        return makeGetRequest(url, headers);
    }

    private HttpDownloader.Response makeGetRequest(String url, Headers headers) {
        HttpDownloader.Request request = new HttpDownloader.Request(url, null, headers);
        HttpDownloader.Response response = null;
        try {
            response = httpGet(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    private HttpDownloader.Response makePostRequest(String url, UrlParams params, Headers headers) {
        HttpDownloader.Request request = new HttpDownloader.Request(url,
                params,
                headers);
        HttpDownloader.Response response = null;
        try {
            response = httpPost(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
