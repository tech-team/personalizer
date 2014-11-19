package content.source.linkedin;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.net.Headers;
import util.net.HttpDownloader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    public static List<String> getPersonUrls(HttpDownloader.Response response) {
        List<String> urls = new LinkedList<>();
        switch (response.getResponseCode()) {
            case 200:
                Document doc = Jsoup.parse(response.getBody());
                Elements elements = doc.select(".vcard");
                for (Element element : elements) {
                    String link = element.select("h2 a").attr("href");
                    urls.add(link);
                }
                break;

            case 302:
                Headers headers = response.getHeaders();
                urls.add(headers.getHeader("Location").getValue());
                break;
        }


        return urls;
    }

    public static void getPersonByLink(HttpDownloader.Response response) {
        /*UserPageObject user = new UserPageObject(response.getBody());
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getCountry());
        System.out.println(user.getHeadline());
        System.out.println(user.getCurrentWork());
        System.out.println(user.getCurrentEducation());
        System.out.println(user.getEducations().getList());*/
        Document document = Jsoup.parse(response.getBody());
        Elements elements = document.select(".account-settings-tab");
        System.out.println(elements.size());
    }

    public static void main(String[] args) {
        LinkedInRequest request = new LinkedInRequest();
        HttpDownloader.Response response = request.makeLoginRequest();
        Cookie cookie = new Cookie(response.getHeaders().getHeader("Set-Cookie"));
        request.makeHeaders(cookie.getCookie());
        try {
            System.out.println(HttpDownloader.httpGet(new HttpDownloader.Request("http://ru.linkedin.com/pub/%D0%B8%D0%B2%D0%B0%D0%BD-%D1%81%D0%BF%D0%B8%D1%80%D0%B8%D0%B4%D0%BE%D0%BD%D0%BE%D0%B2/80/847/b12", null, request.headers)).getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> urls = getPersonUrls(request.makeFindRequest("Иван", "Спиридонов"));
        for(String url: urls) {
            System.out.println(url);
            getPersonByLink(request.makePersonRequest(url));
        }
    }
}
