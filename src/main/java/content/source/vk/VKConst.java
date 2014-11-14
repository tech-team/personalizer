package content.source.vk;


import java.util.Map;

public class VKConst {
    public static final String token =
            "f42b8812d15eeeaa84740414e0831d17bc65fcb502d077dd4e6c48465c975d3c9cb50d63b16cd9d14fcaa";
    private static final String usersSearchUrl =
            "https://api.vk.com/method/users.search?&v=5.26&count=5";
    private static final String usersGetUrl =
            "https://api.vk.com/method/users.get?&v=5.26";
    private static final String getCountriesUrl =
            "https://api.vk.com/method/database.getCountries?&v=5.26";
    private static final String getCityUrl =
            "https://api.vk.com/method/database.getCities?&v=5.26&count=1";
    private static final String getUniversityUrl =
            "https://api.vk.com/method/database.getUniversities?&v=5.26&count=1";

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
        return usersSearchUrl + "&access_token=" + token;
    }

    public static String getUsersGetUrl(String token){
        return usersGetUrl + "&access_token=" + token;
    }


}
