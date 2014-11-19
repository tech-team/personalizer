package content.source.linkedin;


import content.source.linkedin.page_objects.UserPageObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.net.Headers;
import util.net.HttpDownloader;

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
                String location = headers.getHeader("Location").getValue();
                int parametersIndex = location.indexOf('?');
                if(parametersIndex > 0) {
                    location = location.substring(0, parametersIndex);
                }
                urls.add(location);
                break;
        }


        return urls;
    }

    public static void getPersonByLink(HttpDownloader.Response response) {
        UserPageObject user = new UserPageObject(response.getBody());
        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getCountry());
        System.out.println(user.getHeadline());
        System.out.println(user.getCurrentWork());
        System.out.println(user.getCurrentEducation());
        System.out.println(user.getEducations().getList());
    }

    public static void main(String[] args) {
        LinkedInRequest request = new LinkedInRequest();
        HttpDownloader.Response response = request.makeLoginRequest();
        Cookie cookie = new Cookie(response.getHeaders().getHeader("Set-Cookie"));
        request.makeHeaders(cookie.getCookie());
        List<String> urls = getPersonUrls(request.makeFindRequest("Иван", "Спиридонов"));
        for(String url: urls) {
            System.out.println(url);
            getPersonByLink(request.makePersonRequest(url));
        }
    }
}
