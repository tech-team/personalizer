package content.source.vk;

import content.ContentData;
import content.source.ContentSource;

import java.net.HttpURLConnection;
import java.net.URL;

public class VK implements ContentSource {

    public static final String 

    @Override
    public ContentData retrieve(ContentData data) {

        return null;
    }

    public void getInfo(String name, String surName){
        HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
        httpConnection.setRequestMethod("POST");
    }

}
