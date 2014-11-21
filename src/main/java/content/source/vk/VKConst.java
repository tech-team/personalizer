package content.source.vk;


import util.net.UrlParams;

import java.util.Map;

public class VKConst {
    public static final String token =
            "1095b020d45781b99a9835b6a2b8c12f15b27bb04e55736c2083fbaa874ca61a5fecf13c27901b0a957";
    private static final String usersSearchUrl =
            "https://api.vk.com/method/users.search";//?v=5.26&count=5";
    private static final String usersGetUrl =
            "https://api.vk.com/method/users.get";//?v=5.26";
    private static final String getCountriesUrl =
            "https://api.vk.com/method/database.getCountries?&v=5.26";
    private static final String getCityUrl =
            "https://api.vk.com/method/database.getCities?&v=5.26&count=1";
    private static final String getUniversityUrl =
            "https://api.vk.com/method/database.getUniversities?&v=5.26&count=1";


    public static final String fields =
            "sex,bdate,city,country,photo_200_orig,universities,contacts,connections";

    public static Map<String, Integer> countries;

    public static String getGetCountriesUrl() {
        return getCountriesUrl;
    }

    public static String getGetCityUrl() {
        return getCityUrl;
    }

    public static String getGetUniversityUrl() {
        return getUniversityUrl;
    }

    public static String getUsersSearchUrl(String token){
        return usersSearchUrl;// + "&access_token=" + token;
    }

    public static String getUsersGetUrl(String token){
        return usersGetUrl;
    }// + "&access_token=" + token;

    public static void addAccessTokenParam(UrlParams urlParams){
        urlParams.add("access_token", token);
    }

    public static void addVersionParam(UrlParams urlParams){
        urlParams.add("v", "5.26");
    }





}
