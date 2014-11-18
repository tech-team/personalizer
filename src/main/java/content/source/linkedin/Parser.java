package content.source.linkedin;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.net.HttpDownloader;
import util.net.UrlParams;

import java.io.IOException;

import static util.net.HttpDownloader.httpGet;
import static util.net.HttpDownloader.httpPost;

public class Parser {

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

    public static void getPersons(String name, String last_name) {
        String url = "http://www.linkedin.com/pub/dir/" + name + "/" + last_name;
        HttpDownloader.Request request = new HttpDownloader.Request(url, null, null);
        try {
            String page = httpGet(request).getBody();
            Document doc = Jsoup.parse(page);
            for(Element element :doc.select(".vcard")) {
                String link = element.select("h2 a").attr("href");
                getPersonByLink(link);
                System.out.println(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getPersonByLink(String link) {
        HttpDownloader.Request request = new HttpDownloader.Request(link, null, null);
        UserPageObject user = new UserPageObject(request);
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getCountry());
        System.out.println(user.getHeadline());
        System.out.println(user.getCurrentWork());
        System.out.println(user.getCurrentEducation());

    }

    public static void main(String[] args) {
        HttpDownloader.Request request = new HttpDownloader.Request("https://www.linkedin.com/uas/login-submit",
                getInputParams(),
                null);
        try {
            HttpDownloader.Response response = httpPost(request);

            Document doc = Jsoup.parse(response.getBody());
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getPersons("Sam", "Kil");
    }

}
