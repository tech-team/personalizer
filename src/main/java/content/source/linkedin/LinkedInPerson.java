package content.source.linkedin;

import content.PersonCard;

import java.util.List;

public class LinkedInPerson {
    private String lastName;
    private String firstName;
    private String country;
    private String headline;
    private String url;
    private String avatar;
    private List<Job> jobs;
    private List<Education> educations;
    private PersonCard.Date birthdate;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public PersonCard.Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(PersonCard.Date birthdate) {
        this.birthdate = birthdate;
    }

    public static class Job {
        private String company;
        private String dateFrom;
        private String dateTo;
        private String region;
        private String position;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getDateFrom() {
            return dateFrom;
        }

        public void setDateFrom(String dateFrom) {
            this.dateFrom = dateFrom;
        }

        public String getDateTo() {
            return dateTo;
        }

        public void setDateTo(String dateTo) {
            this.dateTo = dateTo;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }

    public static class Education {
        private String locale;
        private String degree;
        private String spec;
        private Integer dateFrom;
        private Integer dateTo;

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public Integer getDateFrom() {
            return dateFrom;
        }

        public void setDateFrom(Integer dateFrom) {
            this.dateFrom = dateFrom;
        }

        public Integer getDateTo() {
            return dateTo;
        }

        public void setDateTo(Integer dateTo) {
            this.dateTo = dateTo;
        }
    }
}
