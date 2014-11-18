package content.source.linkedin;


import org.json.JSONObject;
import util.net.HttpDownloader;

import java.io.IOException;

public class Parser {

    public static final String FIELD_FIRST_NAME = "firstName";
    public static final String FIELD_LAST_NAME = "lastName";
    public static final String FIELD_HEADLINE = "headline";
    public static final String FIELD_URL = "url";
    public static final String FIELD_URL_WRAPPER = "siteStandardProfileRequest";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_ID = "id";


    public static LinkedInPerson parsePerson(String person) {
        LinkedInPerson linkedInPerson = new LinkedInPerson();
        if(person != null) {
            JSONObject json = new JSONObject(person);
            linkedInPerson.setFirstName(getProfileField(FIELD_FIRST_NAME, json));
            linkedInPerson.setLastName(getProfileField(FIELD_LAST_NAME, json));
            linkedInPerson.setHeadline(getProfileField(FIELD_HEADLINE, json));
            linkedInPerson.setUrl(getProfileField(FIELD_URL_WRAPPER, json));
            linkedInPerson.setCountry(getProfileField(FIELD_LOCATION, json));
            linkedInPerson.setId(getProfileField(FIELD_ID, json));
        }
        return linkedInPerson;
    }

    public static void main(String[] args) {
        HttpDownloader.Request request = Request.makeRequest(Request.URL_PROFILE + "/~:(first-name,headline,last-name,location,id,industry,educations,site-standard-profile-request)", null);
        try {
            String response = HttpDownloader.httpGet(request).getBody();
            LinkedInPerson person = parsePerson(response);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProfileField(String name, JSONObject profile) {
        if(!profile.isNull(name)) {
            switch (name) {
                case FIELD_URL_WRAPPER:
                    JSONObject wrapper = profile.getJSONObject(FIELD_URL_WRAPPER);
                    return getProfileField(FIELD_URL, wrapper);
                case FIELD_LOCATION:
                    JSONObject location = profile.getJSONObject(FIELD_LOCATION);
                    JSONObject country = getProfileObject(FIELD_COUNTRY, location);
                    return getProfileField(FIELD_CODE, country);
                default:
                    return profile.getString(name);
            }
        }
        return null;
    }

    public static JSONObject getProfileObject(String name, JSONObject profile) {
        if(!profile.isNull(name)) {
            return profile.getJSONObject(name);
        }
        return null;
    }

}
