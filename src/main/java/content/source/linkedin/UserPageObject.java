package content.source.linkedin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.net.HttpDownloader;

import java.io.IOException;

import static util.net.HttpDownloader.httpGet;

public class UserPageObject {
    public static final String NAME_SELECTOR = ".full-name";
    public static final String COUNTRY_SELECTOR = ".locality";
    public static final String HEADLINE_SELECTOR = ".title";
    public static final String CURRENT_WORK_SELECTOR = "#overview-summary-current li";
    public static final String CURRENT_EDUCATION_SELECTOR = "#overview-summary-education li a";

    private Document document;

    public UserPageObject(HttpDownloader.Request request) {
        try {
            String page = httpGet(request).getBody();
            document = Jsoup.parse(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHeadline() {
        return getStringBySelector(HEADLINE_SELECTOR);
    }

    public String getFirstName() {
        String fullname = getStringBySelector(NAME_SELECTOR);
        String[] names;
        if(fullname != null && (names = fullname.split(" ")).length > 0 ) {
            return names[0];
        }
        return null;
    }

    public String getLastName() {
        String fullname = getStringBySelector(NAME_SELECTOR);
        String[] names;
        if(fullname != null && (names = fullname.split(" ")).length > 1 ) {
            return names[1];
        }
        return null;
    }

    public String getCountry() {
        return getStringBySelector(COUNTRY_SELECTOR);
    }

    public String getCurrentWork() {
        return getStringBySelector(CURRENT_WORK_SELECTOR);
    }

    public String getCurrentEducation() {
        return getStringBySelector(CURRENT_EDUCATION_SELECTOR);
    }

    private String getStringBySelector(String selector) {
        Elements elements = document.select(selector);
        if(elements.size() > 0) {
            Element element = elements.get(0);
            return element.text();
        }
        return null;
    }
}
