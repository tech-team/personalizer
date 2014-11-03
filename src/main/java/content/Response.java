package content;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.EnumMap;
import java.util.Map;

public class Response {
    private Map<SocialLink.LinkType, String> name = new EnumMap<>(SocialLink.LinkType.class);
    private Map<SocialLink.LinkType, String> surname = new EnumMap<>(SocialLink.LinkType.class);
    private Map<SocialLink.LinkType, Integer> ageFrom = new EnumMap<>(SocialLink.LinkType.class);
    private Map<SocialLink.LinkType, Integer> ageTo = new EnumMap<>(SocialLink.LinkType.class);
    private Map<SocialLink.LinkType, String> email = new EnumMap<>(SocialLink.LinkType.class);
    private Map<SocialLink.LinkType, Boolean> famous = new EnumMap<>(SocialLink.LinkType.class);
    private Map<SocialLink.LinkType, SocialLink> socialLinks = new EnumMap<>(SocialLink.LinkType.class);

    public Response(Map<SocialLink.LinkType, String> name, Map<SocialLink.LinkType, String> surname, Map<SocialLink.LinkType, Integer> ageFrom, Map<SocialLink.LinkType, Integer> ageTo, Map<SocialLink.LinkType, String> email, Map<SocialLink.LinkType, Boolean> famous, Map<SocialLink.LinkType, SocialLink> socialLinks) {
        this.name = name;
        this.surname = surname;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.email = email;
        this.famous = famous;
        this.socialLinks = socialLinks;
    }

    public Response() {
    }

    public Map<SocialLink.LinkType, String> getNameMap() {
        return name;
    }

    public Map<SocialLink.LinkType, String> getSurnameMap() {
        return surname;
    }

    public Map<SocialLink.LinkType, Integer> getAgeFromMap() {
        return ageFrom;
    }

    public Map<SocialLink.LinkType, Integer> getAgeToMap() {
        return ageTo;
    }

    public Map<SocialLink.LinkType, String> getEmailMap() {
        return email;
    }

    public Map<SocialLink.LinkType, Boolean> getFamousMap() {
        return famous;
    }

    public Map<SocialLink.LinkType, SocialLink> getSocialLinksMap() {
        return socialLinks;
    }


    public String getName(SocialLink.LinkType linkType) {
        return name.get(linkType);
    }

    public String getSurname(SocialLink.LinkType linkType) {
        return surname.get(linkType);
    }

    public Integer getAgeFrom(SocialLink.LinkType linkType) {
        return ageFrom.get(linkType);
    }

    public Integer getAgeTo(SocialLink.LinkType linkType) {
        return ageTo.get(linkType);
    }

    public String getEmail(SocialLink.LinkType linkType) {
        return email.get(linkType);
    }

    public Boolean getFamous(SocialLink.LinkType linkType) {
        return famous.get(linkType);
    }

    public SocialLink getSocialLinks(SocialLink.LinkType linkType) {
        return socialLinks.get(linkType);
    }



    public Response setName(SocialLink.LinkType linkType, String name) {
        this.name.put(linkType, name);
        return this;
    }

    public Response setSurname(SocialLink.LinkType linkType, String surname) {
        this.surname.put(linkType, surname);
        return this;
    }

    public Response setAgeFrom(SocialLink.LinkType linkType, Integer ageFrom) {
        this.ageFrom.put(linkType, ageFrom);
        return this;
    }

    public Response setAgeTo(SocialLink.LinkType linkType, Integer ageTo) {
        this.ageTo.put(linkType, ageTo);
        return this;
    }

    public Response setEmail(SocialLink.LinkType linkType, String email) {
        this.email.put(linkType, email);
        return this;
    }

    public Response setFamous(SocialLink.LinkType linkType, Boolean isFamous) {
        this.famous.put(linkType, isFamous);
        return this;
    }

    public Response setSocialLink(SocialLink.LinkType linkType, SocialLink link) {
        this.socialLinks.put(linkType, link);
        return this;
    }
}
