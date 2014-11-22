package content.source.linkedin.page_objects;

import content.source.linkedin.LinkedInPerson;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class JobPageObject {
    public static final String JOB_ELEMENT_SELECTOR = ".section-item";
    public static final String DATE_ELEMENT_SELECTOR = ".experience-date-locale time";
    public static final String COMPANY_ELEMENT_SELECTOR = "header h5";
    public static final String POSITION_ELEMENT_SELECTOR = "header h4";
    public static final String REGION_ELEMENT_SELECTOR = ".experience-date-locale .locality";


    private Element root;

    public JobPageObject(Element element) {
        root = element;
    }

    public List<LinkedInPerson.Job> getList() {
        List<LinkedInPerson.Job> jobs = new LinkedList<>();
        if(root != null) {
            for(Element jobElement : root.select(JOB_ELEMENT_SELECTOR)) {
                LinkedInPerson.Job job = new LinkedInPerson.Job();
                job.setCompany(EducationPageObject.getString(jobElement, COMPANY_ELEMENT_SELECTOR));
                job.setPosition(EducationPageObject.getString(jobElement, POSITION_ELEMENT_SELECTOR));
                job.setRegion(EducationPageObject.getString(jobElement, REGION_ELEMENT_SELECTOR));
                String[] dates = getDate(jobElement);
                if(dates != null) {
                    job.setDateFrom(dates[0]);
                    job.setDateTo(dates[1]);
                }
                System.out.println(job.getCompany());
                System.out.println(job.getPosition());
                System.out.println(job.getRegion());
                System.out.println(job.getDateFrom());
                System.out.println(job.getDateTo());
                jobs.add(job);
            }
        }
        return jobs;
    }

    public String[] getDate(Element parent) {
        String[] dates = null;
        Elements dateEl = parent.select(DATE_ELEMENT_SELECTOR);
        if(dateEl.size() > 0) {
            dates = new String[2];
            dates[0] = dateEl.first().text();
            if(dateEl.size() == 2) {
                dates[1] = dateEl.last().text();
            } else {
                dates[1] = "Настоящее время";
            }
        }
        return dates;
    }
}
