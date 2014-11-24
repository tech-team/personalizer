package content.source.linkedin;

import util.net.Headers;

import java.util.HashMap;
import java.util.List;

public class Cookie {

    private HashMap<String, String> map = new HashMap<>();

    public void add(Headers.Header cookieList) {
        List<String> list = cookieList.getValues();
        for (String cookieString : list) {
            int delimiterIndex = cookieString.indexOf(";");
            if(delimiterIndex > 0) {
                String beforeDelimiter = cookieString.substring(0, delimiterIndex + 1);
                int keyDelimiter = beforeDelimiter.indexOf("=");
                if(keyDelimiter > 0) {
                    String key = beforeDelimiter.substring(0, keyDelimiter);
                    String value = beforeDelimiter.substring(keyDelimiter + 1, delimiterIndex);
                    String test = value.replaceAll("[/\\W+/]", "").toUpperCase();
                    if(!test.equals("DELETEME"))
                        map.put(key, value);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(String key : map.keySet()) {
            builder.append(key)
                    .append("=")
                    .append(map.get(key))
                    .append(";");
        }
        return builder.toString();
    }
}

