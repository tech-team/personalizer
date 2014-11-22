package content.source.vk;

import content.*;
import content.source.ContentSource;
import util.net.Headers;
import util.net.HttpDownloader;
import util.net.UrlParams;

import java.io.IOException;
import java.util.*;

public class VK implements ContentSource {

    public static final String AUTHORIZE_URL =
            "https://oauth.vk.com/authorize?client_id=4628886&redirect_uri=https://oauth.vk.com/blank.html&display=page&response_type=token";
    public static final String LOGIN_URL = "https://login.vk.com?act=login";
    public static final String LOGIN = "prsnlzr@gmail.com";
    public static final String PASS = "prsnlzr123";

    private VKUserSearcher searcher;

    public VK(){
        try {
            VKDataHelper.initCountries();
            VKConst.countries.put("Россия", 1);
            Headers.Header cookie = login();
            String token = getToken(cookie);
            searcher = new VKUserSearcher(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void retrieve(PersonCard data, PersonList dest) {
        ArrayList<PersonCard> personCards = searcher.getPersons(data);
        for (PersonCard personCard: personCards){
            dest.addPerson(personCard);
        }
    }

    @Override
    public Type getType() {
        return Type.VK;
    }

    public Headers.Header login() throws IOException {
        HttpDownloader.Request request = new HttpDownloader.Request(LOGIN_URL);
        UrlParams urlParams = new UrlParams();
        urlParams.add("email", LOGIN);
        urlParams.add("pass", PASS);
        request.setParams(urlParams);
        request.setFollowRedirects(false);
        HttpDownloader.Response response = HttpDownloader.httpPost(request);

        Headers.Header redirectHeader = response.getHeaders().getHeader("Location");
        String redirect = redirectHeader.getValue();
        Headers.Header cookie = response.getHeaders().getHeader("Set-Cookie");
        List<String> cookies = cookie.getValues();
        String cook = "";
        for (String coco: cookies){
            cook += coco + ";";
        }
        Headers headers = new Headers();
        headers.add("Cookie", cook);
        request = new HttpDownloader.Request(redirect);
        request.setHeaders(headers);
        request.setFollowRedirects(false);
        response = HttpDownloader.httpGet(request);

        cookie = response.getHeaders().getHeader("Set-Cookie");
        cookies = cookie.getValues();
        for (String coco: cookies){
            cook += coco + ";";
        }
        headers.add("Cookie", cook);

        return headers.getHeader("Cookie");
    }

    public String getToken(Headers.Header cookie) throws IOException {
        HttpDownloader.Request request = new HttpDownloader.Request(AUTHORIZE_URL);
        List<String> cookies = cookie.getValues();
        String cook = "";
        for (String coco: cookies){
            cook += coco + ";";
        }
        Headers headers = new Headers();
        headers.add("Cookie", cook);
        request.setHeaders(headers);
        HttpDownloader.Response response = HttpDownloader.httpGet(request);
        String url = response.getUrl();
        int tokenIndex = url.indexOf("access_token=") + 13;
        int expiresIndex = url.indexOf("&expires");
        String token = url.substring(tokenIndex, expiresIndex);
        return token;
    }

}
