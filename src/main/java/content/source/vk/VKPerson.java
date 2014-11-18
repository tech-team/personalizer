package content.source.vk;


import content.SocialLink;
import content.source.University;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VKPerson {
    public static final String fields =
            "sex,bdate,city,country,photo_100,universities,contacts";

    private String firstName;
    private String lastName;
    private String sex;
    private String bdate;
    private String city;
    private String country;
    private String photo;
    private ArrayList<University> universities;
    private Integer id;
    private String mobilePhone;
    private Map<SocialLink.LinkType, String> socialLinks = new HashMap<>();


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
        if (!person.optString("photo_100").equals("")){
            setPhoto(person.optString("photo_100"));
        }

        JSONArray univers;
        if ((univers = person.optJSONArray("universities")) != null){
            universities = new ArrayList<>();
            for (int i = 0; i < univers.length(); i++){
                JSONObject item = univers.getJSONObject(i);
                universities.add(new University
                                (item.getString("name"),
                                (item.optInt("graduation")==0?null:item.getInt("graduation")))
                );
            }
        }

        if (!person.optString("bdate").equals("")){
            setBdate(person.optString("bdate"));
        }
        if (!person.optString("first_name").equals("")){
            setFirstName(person.optString("first_name"));
        }
        if (!person.optString("last_name").equals("")){
            setLastName(person.optString("last_name"));
        }
        if (!person.optString("mobile_phone").equals("")){
            setMobilePhone(person.optString("mobile_phone"));
        }
        if (!person.optString("skype").equals("")){
            String skype = person.optString("skype");
            socialLinks.put(SocialLink.LinkType.SKYPE, skype);
        }
        if (!person.optString("facebook").equals("")){
            String facebook = person.optString("facebook");
            socialLinks.put(SocialLink.LinkType.FB, facebook);

        }
        if (!person.optString("twitter").equals("")){
            socialLinks.put(SocialLink.LinkType.TWITTER, person.optString("twitter"));
        }

    }

}
