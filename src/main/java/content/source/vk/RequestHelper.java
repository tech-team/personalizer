package content.source.vk;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHelper {
    public static String getResponse(String url){
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) new URL(url).openConnection();
            httpConnection.setRequestMethod("GET");
            InputStream is = httpConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONArray getResponseJSON(String response){
        JSONObject jsonObject = new JSONObject(response);
        jsonObject = jsonObject.getJSONObject("response");
        return jsonObject.getJSONArray("items");
    }
}
