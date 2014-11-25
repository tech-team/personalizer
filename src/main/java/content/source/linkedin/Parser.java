package content.source.linkedin;


import content.source.linkedin.page_objects.UserPageObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.net.Headers;
import util.net.HttpResponse;

import java.util.LinkedList;
import java.util.List;

public class Parser {

    public static List<String> getPersonUrls(HttpResponse response) {
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

    public static LinkedInPerson getPersonByLink(HttpResponse response) {
        UserPageObject user = new UserPageObject(response.getBody());
        LinkedInPerson person = new LinkedInPerson();
        person.setUrl(response.getUrl().toString());
        person.setCountry(user.getCountry());
        person.setFirstName(user.getFirstName());
        person.setHeadline(user.getHeadline());
        person.setAvatar(user.getAvatar());
        person.setLastName(user.getLastName());
        person.setEducations(user.getEducations().getList());
        person.setJobs(user.getJobs().getList());
        person.setBirthdate(user.getDOB());
        person.setRelation(user.getRelStatus());
        return person;
    }

    public static void main(String[] args) {
        LinkedInRequest request = new LinkedInRequest();
        HttpResponse response = request.makeLoginRequest();
        Cookie cookie = new Cookie(response.getHeaders().getHeader("Set-Cookie"));
        request.makeHeaders(cookie.getCookie());
        List<String> urls = getPersonUrls(request.makeFindRequest("person", "personalizer"));
        for(String url: urls) {
            System.out.println(url);
            LinkedInPerson person = getPersonByLink(request.makePersonRequest(url));
            System.out.println();
        }
    }
}
