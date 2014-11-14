package content.source.vk;


import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RequestHelper {

    public static JSONArray getResponseJSONitems(String response){
        /*
        Возвращает массив items из ответа
         */
        JSONObject jsonObject = new JSONObject(response);
        jsonObject = jsonObject.getJSONObject("response");
        return jsonObject.getJSONArray("items");
    }


    public static int getCount(String response){
        /*
        Возвращает count из ответа
         */
        JSONObject jsonObject = new JSONObject(response);
        jsonObject = jsonObject.getJSONObject("response");
        return jsonObject.getInt("count");
    }

    public static String addParam(String request, Pair<Object, Object> param) throws UnsupportedEncodingException {
        return request + String.format("&"+param.getKey()+"=%s",
                URLEncoder.encode(param.getValue().toString(), "UTF-8"));
    }

}
