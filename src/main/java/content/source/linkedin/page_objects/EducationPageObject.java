package content.source.linkedin.page_objects;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

import static content.source.linkedin.LinkedInPerson.Education;

public class EducationPageObject {
    public static final String EDUCATION_ELEMENT_SELECTOR = "#background-education .editable-item";
    public static final String EDUCATION_LOCALE_SELECTOR = "header .summary a";
    public static final String EDUCATION_DEGREE_SELECTOR = "header .degree";
    public static final String EDUCATION_MAJOR_SELECTOR = "header .major";
    public static final String EDUCATION_DATE_SELECTOR = ".education-date";

    private Element root;

    public EducationPageObject(Element element) {
        root = element;
    }

    public List<Education> getList() {
        List<Education> list = new LinkedList<>();
        for(Element educationEl : root.select(EDUCATION_ELEMENT_SELECTOR)) {
            Education education = new Education();
            education.setLocale(getString(educationEl, EDUCATION_LOCALE_SELECTOR));
            education.setDegree(getString(educationEl, EDUCATION_DEGREE_SELECTOR));
            education.setSpec(getString(educationEl, EDUCATION_MAJOR_SELECTOR));
            Integer[] dates = getDate(educationEl);
            education.setDateFrom(dates[0]);
            education.setDateTo(dates[1]);
            list.add(education);
        }
        return list;
    }

    private String getString(Element education, String selector) {
        Element element = education.select(EDUCATION_LOCALE_SELECTOR).first();
        if(element != null) {
            return element.text();
        }
        return null;
    }

    private Integer[] getDate(Element education) {
        Elements dates = education.select(EDUCATION_DATE_SELECTOR);
        Integer dateFrom = Integer.parseInt(dates.first().text());
        Integer dateTo = Integer.parseInt(dates.last().text());
        return new Integer[] { dateFrom, dateTo };
    }
}
