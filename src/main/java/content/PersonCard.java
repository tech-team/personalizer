package content;

import content.source.ContentSource;

import java.util.*;

public class PersonCard {


    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public PersonCard copy() {
        PersonCard card = new PersonCard(getId());
        card.setType(getType());
        card.setAgeFrom(getAgeFrom());
        card.setAgeTo(getAgeTo());
        card.setBirthDate(getBirthDate());
        card.setCity(getCity());
        card.setCountry(getCountry());
        card.setEmails(getEmails());
        card.setFamous(getFamous());
        card.setJobs(getJobs());
        card.setMobilePhone(getMobilePhone());
        card.setName(getName());
        card.setPersonLink(getPersonLink());
        card.setSex(getSex());
        card.setSocialLinks(getSocialLinks());
        card.setSurname(getSurname());
        card.setUniversities(getUniversities());
        return card;
    }

    public static class Date {
        private Integer day;
        private Integer month;
        private Integer year;

        public Date(Integer day, Integer month, Integer year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        public Integer getDay() {
            return day;
        }

        public Integer getMonth() {
            return month;
        }

        public Integer getYear() {
            return year;
        }

        @Override
        public String toString() {
            return String.format("%d-%d-%d", getYear(), getMonth(), getDay());
        }
    }

    private PersonId id = null;
    private SocialLink personLink;
    private String name;
    private String surname;
    private Integer ageFrom;
    private Integer ageTo;
    private Date birthDate;
    private List<String> emails = new LinkedList<>();
    private Boolean famous;
    private String country;
    private String city;
    private String mobilePhone;
    private String sex;
    private List<University> universities = new LinkedList<>();
    private List<String> jobs = new LinkedList<>();
    private List<String> avatars = new LinkedList<>();

    private Map<SocialLink.LinkType, SocialLink> socialLinks = new EnumMap<>(SocialLink.LinkType.class);

    private List<PersonCard> linkedCards = new LinkedList<>();


    PersonCard(PersonId id) {
        this.id = id;
    }

    public PersonCard() {
    }

    public SocialLink getPersonLink() {
        return personLink;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    /**
     *
     * @return Difference between current date and person's birthday in years
     * or null if some fields is missing in birthday object
     */
    public Integer getAge() {
        if (birthDate == null || birthDate.getYear() == null)
            return null;

        Calendar calendar = Calendar.getInstance();

        Integer age = calendar.get(Calendar.YEAR) - birthDate.getYear();

        if (birthDate.getMonth() == null || birthDate.getDay() == null)
            return age;

        if (calendar.get(Calendar.MONTH) < birthDate.month
                || (calendar.get(Calendar.MONTH) == birthDate.month
                    && calendar.get(Calendar.DAY_OF_MONTH) == birthDate.day)) {
            return age - 1;
        }

        return age;
    }

    public Integer getAgeFrom() {
        return ageFrom;
    }

    public Integer getAgeTo() {
        return ageTo;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public List<String> getEmails() {
        return emails;
    }

    public Boolean getFamous() {
        return famous;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public List<University> getUniversities() {
        return universities;
    }

    public List<String> getJobs() {
        return jobs;
    }

    public Map<SocialLink.LinkType, SocialLink> getSocialLinks() {
        return socialLinks;
    }

    public List<String> getAvatars() {
        return avatars;
    }

    public PersonCard setPersonLink(SocialLink personLink) {
        this.personLink = personLink;
        return this;
    }

    public PersonCard setName(String name) {
        this.name = name;
        return this;
    }

    public PersonCard setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public PersonCard setAgeFrom(Integer ageFrom) {
        this.ageFrom = ageFrom;
        return this;
    }

    public PersonCard setAgeTo(Integer ageTo) {
        this.ageTo = ageTo;
        return this;
    }

    public PersonCard setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PersonCard setEmails(List<String> emails) {
        this.emails = emails;
        return this;
    }

    public PersonCard addEmail(String email) {
        this.emails.add(email);
        return this;
    }

    public PersonCard setFamous(Boolean famous) {
        this.famous = famous;
        return this;
    }

    public PersonCard setCountry(String country) {
        this.country = country;
        return this;
    }

    public PersonCard setCity(String city) {
        this.city = city;
        return this;
    }

    public PersonCard setUniversities(List<University> universities) {
        this.universities = universities;
        return this;
    }

    public PersonCard setJobs(List<String> jobs) {
        this.jobs = jobs;
        return this;
    }

    public PersonCard setSocialLinks(Map<SocialLink.LinkType, SocialLink> socialLinks) {
        this.socialLinks = socialLinks;
        return this;
    }

    public PersonCard addUniversity(University university) {
        this.universities.add(university);
        return this;
    }

    public PersonCard addJob(String job) {
        this.jobs.add(job);
        return this;
    }

    public PersonCard addSocialLink(SocialLink.LinkType type, SocialLink link) {
        this.socialLinks.put(type, link);
        return this;
    }

    public PersonCard addAvatar(String link) {
        avatars.add(link);
        return this;
    }

    public PersonId getId() {
        return id;
    }

    public ContentSource.Type getType() {
        return id.getType();
    }

    void setId(PersonId id) {
        this.id = id;
    }

    void setType(ContentSource.Type type) {
        this.id.setType(type);
    }

    public void linkWith(PersonCard card) {
        linkedCards.add(card);
    }

    public void linkWith(Collection<PersonCard> cards) {
        linkedCards.addAll(cards);
    }

    public boolean isLinkedWith(PersonCard card) {
        return this == card || linkedCards.contains(card);
    }

}
