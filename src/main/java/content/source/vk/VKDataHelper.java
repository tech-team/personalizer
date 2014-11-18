package content.source.vk;


import org.json.JSONArray;
import org.json.JSONObject;
import util.net.HttpDownloader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class VKDataHelper {



    public static void initCountries(){
        if (VKConst.countries == null){
            VKConst.countries = new HashMap<>();
            //VKDataHelper.getCountries();
            VKConst.countries.put("Россия", 1);
        }
    }

    public static Map<String, Integer> getCountries() throws IOException {
        String parameters = String.format("need_all=%s",
                URLEncoder.encode("1", "UTF-8"));
        String request = VKConst.getGetCountriesUrl() + "&" + parameters;
        String response = HttpDownloader.httpGet(request).getBody();
        JSONArray responseArray = RequestHelper.getResponseJSONitems(response);
        Map <String, Integer> countries = new HashMap<>();
        for (int i = 0; i < responseArray.length(); i++){
            String title = responseArray.getJSONObject(i).getString("title");
            int id = responseArray.getJSONObject(i).getInt("id");
            countries.put(title, id);
        }
        return countries;
    }

    public static int getCityId(Integer country, String city) throws UnsupportedEncodingException {
        String parameters = String.format("&q=%s&country_id=%s",
                URLEncoder.encode(city, "UTF-8"),
                URLEncoder.encode(country.toString(), "UTF-8"));
        String request = VKConst.getGetCityUrl() + "&" + parameters;
        return getId(request);
    }

    public static int getUniversityId(String university) throws UnsupportedEncodingException {
        String parameters = String.format("&q=%s",
                URLEncoder.encode(university, "UTF-8"));
        String request = VKConst.getGetUniversityUrl() + "&" + parameters;
        return getId(request);
    }

    public static int getId(String request){
        String response = null;
        try {
            response = HttpDownloader.httpGet(request).getBody();
            JSONArray responseArray = RequestHelper.getResponseJSONitems(response);
            JSONObject entityObj = responseArray.optJSONObject(0);
            if (entityObj != null)
                return entityObj.getInt("id");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
