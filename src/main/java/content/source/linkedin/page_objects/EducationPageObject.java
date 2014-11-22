package content.source.linkedin.page_objects;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

import static content.source.linkedin.LinkedInPerson.Education;

public class EducationPageObject {
    public static final String EDUCATION_ELEMENT_SELECTOR = ".section-item";
    public static final String EDUCATION_LOCALE_SELECTOR = "header .summary";
    public static final String EDUCATION_DEGREE_SELECTOR = "header h5 .degree";
    public static final String EDUCATION_MAJOR_SELECTOR = "header h5 .major";
    public static final String EDUCATION_DATE_SELECTOR = ".education-date time";

    private Element root;

    public EducationPageObject(Element element) {
        root = element;
    }

    public List<Education> getList() {
        List<Education> list = new LinkedList<>();
        if(root != null) {
            for (Element educationEl : root.select(EDUCATION_ELEMENT_SELECTOR)) {
                Education education = new Education();
                education.setLocale(getLocale(educationEl));
                education.setDegree(getString(educationEl, EDUCATION_DEGREE_SELECTOR));
                education.setSpec(getString(educationEl, EDUCATION_MAJOR_SELECTOR));
                Integer[] dates = getDate(educationEl);
                if(dates != null) {
                    education.setDateFrom(dates[0]);
                    education.setDateTo(dates[1]);
                }
                list.add(education);
                System.out.println(education.getLocale());
                System.out.println(education.getDegree());
                System.out.println(education.getSpec());
                System.out.println(education.getDateFrom());
                System.out.println(education.getDateTo());
            }
        }
        return list;
    }

    public static String getString(Element parent, String selector) {
        Element element = parent.select(selector).first();
        if(element != null && element.children().size() == 0) {
            return element.text();
        }
        return null;
    }

    private String getLocale(Element education) {
        Element element = education.select(EDUCATION_LOCALE_SELECTOR).first();
        if(element != null) {
            if (element.children().size() == 0) {
                return element.text();
            } else {
                return getString(element, "a");
            }
        }
        return null;
    }

    private Integer[] getDate(Element education) {
        Elements dates = education.select(EDUCATION_DATE_SELECTOR);
        if(dates != null && dates.size() > 0) {
            Integer dateFrom = Integer.parseInt(dates.first().text());
            Integer dateTo = Integer.parseInt(dates.last().text().replaceAll("[\\D]", ""));
            return new Integer[]{dateFrom, dateTo};
        }
        return null;
    }

}
