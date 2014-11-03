package content;

import java.util.*;

public class Request {
    private String name;
    private String surname;
    private Integer ageFrom;
    private Integer ageTo;
    private String email;
    private Boolean famous;

    private Map<SocialLink.LinkType, SocialLink> socialLinks = new EnumMap<>(SocialLink.LinkType.class);

    public Request(String name, String surname, Integer ageFrom, Integer ageTo, String email, Boolean famous, Map<SocialLink.LinkType, SocialLink> socialLinks) {
        this.name = name;
        this.surname = surname;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.email = email;
        this.famous = famous;
        this.socialLinks = socialLinks;
    }

    public Request() {
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getAgeFrom() {
        return ageFrom;
    }

    public Integer getAgeTo() {
        return ageTo;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getFamous() {
        return famous;
    }

    public Map<SocialLink.LinkType, SocialLink> getSocialLinks() {
        return socialLinks;
    }

    public SocialLink getSocialLink(SocialLink.LinkType type) {
        return socialLinks.get(type);
    }


    public Request setName(String name) {
        this.name = name;
        return this;
    }

    public Request setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public Request setAgeFrom(Integer ageFrom) {
        this.ageFrom = ageFrom;
        return this;
    }

    public Request setAgeTo(Integer ageTo) {
        this.ageTo = ageTo;
        return this;
    }

    public Request setEmail(String email) {
        this.email = email;
        return this;
    }

    public Request setFamous(Boolean famous) {
        this.famous = famous;
        return this;
    }

    public Request setSocialLinks(Map<SocialLink.LinkType, SocialLink> socialLinks) {
        this.socialLinks = socialLinks;
        return this;
    }

    public Request setLink(SocialLink.LinkType type, SocialLink link) {
        this.socialLinks.put(type, link);
        return this;
    }
}
