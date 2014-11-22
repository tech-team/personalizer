package content.source.linkedin.page_objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UserPageObject {
    public static final String NAME_SELECTOR = ".full-name";
    public static final String COUNTRY_SELECTOR = ".locality";
    public static final String HEADLINE_SELECTOR = ".title";
    public static final String CURRENT_JOB_SELECTOR = "#overview-summary-current li";
    public static final String CURRENT_EDUCATION_SELECTOR = "#overview-summary-education li a";
    public static final String AVATAR_SELECTOR = ".profile-picture img";
    public static final String JOB_SELECTOR = "#background-experience";
    public static final String EDUCATION_SELECTOR = "#background-education";
    public static final String PERSONAL_INFO_SELECTOR = "#personal-info";

    private Document document;
    private JobPageObject jobs;
    private EducationPageObject educations;

    public UserPageObject(String body) {
        document = Jsoup.parse(body);
        Element jobs = getElementBySelector(JOB_SELECTOR);
        Element educations = getElementBySelector(EDUCATION_SELECTOR);

        this.educations = new EducationPageObject(educations);
        this.jobs = new JobPageObject(jobs);
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
        return getStringBySelector(CURRENT_JOB_SELECTOR);
    }

    public String getCurrentEducation() {
        return getStringBySelector(CURRENT_EDUCATION_SELECTOR);
    }

    public String getAvatar() {
        Element avatar = getElementBySelector(AVATAR_SELECTOR);
        if(avatar != null) {
            return avatar.attr("src");
        }
        return null;
    }

    public String getDOB() {                                //личные данные недоступны неавторизованному пользователю
        if(document != null) {
            Elements elements = document.getElementsContainingOwnText("Дата рождения");
            if(elements != null && elements.first() != null) {
                Elements dob = elements.first().siblingElements();
                if(dob != null) {
                    return dob.text();
                }
            }
        }
        return null;
    }

    public String getRelStatus() {
        if(document != null) {
            Elements elements = document.getElementsContainingOwnText("Семейное положение");
            if(elements != null && elements.first() != null) {
                Elements relation = elements.first().siblingElements();
                if(relation != null) {
                    return relation.text();
                }
            }
        }
        return null;
    }

    public JobPageObject getJobs() {
        return jobs;
    }

    public EducationPageObject getEducations() {
        return educations;
    }

    private String getStringBySelector(String selector) {
        Elements elements = document.select(selector);
        if(elements.size() > 0) {
            Element element = elements.get(0);
            return element.text();
        }
        return null;
    }

    private Element getElementBySelector(String selector) {
        Elements elements = document.select(selector);
        if(elements.size() > 0) {
            return elements.get(0);
        }
        return null;
    }
}
