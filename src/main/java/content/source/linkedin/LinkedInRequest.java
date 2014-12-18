package content.source.linkedin;

import content.PersonCard;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.net.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static content.source.linkedin.Parser.getPersonByLink;
import static content.source.linkedin.Parser.getPersonUrls;
import static util.net.HttpDownloader.httpGet;
import static util.net.HttpDownloader.httpPost;

public class LinkedInRequest {

    public CookieManager manager;

    private Headers makeHeaders() {
        Headers headers = new Headers();
        headers.add("Host", "ru.linkedin.com")
               .add("Connection", "keep-alive");
        return headers;
    }

    public void setCookie(CookieManager manager) {
        this.manager = manager;
    }

    public HttpResponse getMainPage() {
        return makeGetRequest("https://www.linkedin.com/", false);
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
        return makePostRequest("https://www.linkedin.com/uas/login-submit", getInputParams(), true);
    }

    public HttpResponse makeFindRequest(String name, String lastName) {
        if (name == null || lastName == null)
            return null;
        try {
            name = URLEncoder.encode(name, "UTF-8");
            lastName = URLEncoder.encode(lastName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://www.linkedin.com/pub/dir/" + name + "/" + lastName;
        return makeGetRequest(url, false);
    }

    public HttpResponse makePersonRequest(String url) {
        return makeGetRequest(url, true);
    }

    private HttpResponse makeGetRequest(String url, boolean withCookie) {
        HttpRequest request = new HttpRequest(url, null, makeHeaders());
        request.setFollowRedirects(false);
        if(withCookie)
            request.setCookies(manager);
        HttpResponse response = null;
        try {
            response = httpGet(request);
            if(response.getResponseCode() == 301) {
                Headers headers = response.getHeaders();
                String location = headers.getHeader("Location").getValue();
                int parametersIndex = location.indexOf('?');
                if(parametersIndex > 0) {
                    location = location.substring(0, parametersIndex);
                }
                response = httpGet(new HttpRequest(location, null, makeHeaders()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    private HttpResponse makePostRequest(String url, UrlParams params, boolean withCookie) {
        HttpRequest request = new HttpRequest(url,
                params,
                makeHeaders());
        if(withCookie)
            request.setCookies(manager);
        HttpResponse response = null;
        try {
            response = httpPost(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static LinkedInRequest login(LinkedInRequest request) {
        HttpResponse response = request.getMainPage();
        CookieManager manager = response.getCookieManager();
        request.setCookie(manager);
        response = request.makeLoginRequest();
        response.getCookieManager().addExtra(manager);
        manager = response.getCookieManager();
        request.setCookie(manager);
        return request;
    }

    public static  LinkedList<LinkedInPerson> getPersons(LinkedInRequest request, PersonCard card) {
        LinkedList<LinkedInPerson> persons = new LinkedList<>();
        Map<String, String> names = getNames(card.getName());
        List<String> urls = getPersonUrls(request.makeFindRequest(names.get("first"), names.get("last")));
        for(String url: urls) {
            LinkedInPerson person = getPersonByLink(request.makePersonRequest(url));
            persons.add(person);
        }
        return persons;
    }

    private static Map<String, String> getNames(String name) {
        Map<String, String> names = new HashMap<>();
        if(name != null) {
            int delimeterIndex = name.indexOf(" ");
            if(delimeterIndex > 0) {
                String lastName = name.substring(0, delimeterIndex);
                String firstName = name.substring(delimeterIndex + 1, name.length());
                names.put("last", lastName);
                names.put("first", firstName);
            }
        }
        return names;
    }

}
