package content.source.vk;


import content.SocialLink;
import org.json.JSONObject;

import java.util.Map;

public class VKPerson {
    public static final String fields =
            "sex,bdate,city,country,photo_100,education,contacts";

    private String firstName;
    private String lastName;
    private String sex;
    private String bdate;
    private String city;
    private String country;
    private String photo;
    private String university;
    private Integer graduation;
    private Integer id;
    private String mobilePhone;
    private Map<SocialLink.LinkType, String> socialLinks;


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setGraduation(Integer graduation) {
        this.graduation = graduation;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex==2?"M":"W";
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public int getGraduation() {
        return graduation;
    }

    public void setGraduation(int graduation) {
        this.graduation = graduation;
    }

    public VKPerson(JSONObject person){
        setId(person.getInt("id"));
        if (person.optJSONObject("country") != null){
            setCountry(person.optJSONObject("country").getString("title"));
        }
        if (person.optJSONObject("city") != null){
            setCity(person.optJSONObject("city").getString("title"));
        }
        if (person.optInt("sex") != 0){
            setSex(person.optInt("sex"));
        }
        if (person.optString("photo_100") != null){
            setPhoto(person.optString("photo_100"));
        }
        if (person.optString("university_name") != null){
            setUniversity(person.optString("university_name"));
        }
        if (person.optInt("graduation") != 0){
            setGraduation(person.optInt("graduation"));
        }
        if (person.optString("bdate") != null){
            setBdate(person.optString("bdate"));
        }
        if (person.optString("first_name") != null){
            setFirstName(person.optString("first_name"));
        }
        if (person.optString("last_name") != null){
            setLastName(person.optString("last_name"));
        }
        if (person.optString("mobile_phone") != null){
            setMobilePhone(person.optString("mobile_phone"));
        }
        if (person.optString("skype") != null){
            socialLinks.put(SocialLink.LinkType.Skype, person.getString("skype"));
        }
        if (person.optString("facebook") != null){
            socialLinks.put(SocialLink.LinkType.FB, person.getString("facebook"));
        }
        if (person.optString("twitter") != null){
            socialLinks.put(SocialLink.LinkType.Twitter, person.getString("twitter"));
        }

    }

}
