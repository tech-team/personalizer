package content.source.linkedin;

import util.net.Headers;

import java.util.List;

public class Cookie {

    private Headers.Header list;

    public Cookie(Headers.Header cookieList) {
        list = cookieList;
    }

    public String getCookie() {
        if(list != null) {
            List<String> cookieList = list.getValues();
            StringBuilder cookieBuilder = new StringBuilder();
            for (String cookieString : cookieList) {
                int delimiterIndex = cookieString.indexOf(";");
                String beforeDelimiter = cookieString.substring(0, delimiterIndex + 1);
                cookieBuilder.append(beforeDelimiter);
            }
            return cookieBuilder.toString();
        }
        return null;
    }
}

