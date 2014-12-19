package content.source.linkedin;

import content.PersonCard;
import content.PersonList;
import content.SocialLink;
import content.University;
import content.source.ContentSource;

import java.util.LinkedList;
import java.util.List;

public class LinkedIn implements ContentSource {


    @Override
    public void retrieve(PersonCard data, PersonList dest) {
        LinkedInRequest request = new LinkedInRequest();
        request = LinkedInRequest.login(request);
        List<LinkedInPerson> persons = LinkedInRequest.getPersons(request, data);
        addPersonsToList(persons, dest);
    }

    @Override
    public Type getType() {
        return Type.LI;
    }


    private void addPersonsToList(List<LinkedInPerson> persons, PersonList list) {
        for(LinkedInPerson person : persons) {
            PersonCard card = new PersonCard();
            card.addAvatar(person.getAvatar());
            card.setUniversities(getUniversityFromEducation(person.getEducations()));
            card.setJobs(getJobs(person.getJobs()));
            card.addSocialLink(SocialLink.LinkType.LINKED_IN, new SocialLink(SocialLink.LinkType.LINKED_IN, null, person.getUrl()));
            list.addPerson(card);
        }
    }

    private List<University> getUniversityFromEducation(List<LinkedInPerson.Education> educations) {
        List<University> universities = new LinkedList<>();
        for(LinkedInPerson.Education education : educations) {
            University university = new University(education.getLocale(), education.getDateTo());
            universities.add(university);
        }
        return universities;
    }

    private List<String> getJobs(List<LinkedInPerson.Job> jobs) {
        List<String> list = new LinkedList<>();
        for(LinkedInPerson.Job job : jobs) {
            list.add(job.toString());
        }
        return list;
    }

}
