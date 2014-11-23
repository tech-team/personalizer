package util.net;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class CookieManager {

    private Map<String, Cookie> store = new HashMap<>();

    private static final String SET_COOKIE = "Set-Cookie";
    private static final String COOKIE_VALUE_DELIMITER = ";";
    private static final String PATH = "path";
    private static final String EXPIRES = "expires";
    private static final String DATE_FORMAT = "EEE, dd-MMM-yyyy hh:mm:ss z";
    private static final String DATE_FORMAT2 = "EEE, dd MMM yyyy hh:mm:ss z";
    private static final String SET_COOKIE_SEPARATOR = "; ";
    private static final String COOKIE = "Cookie";
    private static final String DELETE_ME = "delete me";

    private static final char NAME_VALUE_SEPARATOR = '=';
    private static final char DOT = '.';

    private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private DateFormat dateFormat2 = new SimpleDateFormat(DATE_FORMAT2);

    public CookieManager() {
    }

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
    }

    public void storeCookies(Headers headers) {
        List<String> cookies = headers.getHeader(SET_COOKIE).getValues();

        for (String cookieField : cookies) {
            Cookie cookie = new Cookie();

            StringTokenizer st = new StringTokenizer(cookieField, COOKIE_VALUE_DELIMITER);

            // the specification dictates that the first name/value pair
            // in the string is the cookie name and value, so let's handle
            // them as a special case:

            if (st.hasMoreTokens()) {
                String token = st.nextToken().trim();
                String name = token.substring(0, token.indexOf(NAME_VALUE_SEPARATOR));
                String value = token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1, token.length());

                if (value.equalsIgnoreCase(DELETE_ME)) {
                    continue;
                }

                cookie.set(name, value);
                store.put(name, cookie);

                while (st.hasMoreTokens()) {
                    token = st.nextToken().trim();
                    int pos = token.indexOf(NAME_VALUE_SEPARATOR);
                    if (pos == -1) { // like Secure or HttpOnly
                        cookie.addData(token, null);
                    } else {
                        cookie.addData(token.substring(0, pos).toLowerCase(),
                                token.substring(pos + 1, token.length()));
                    }
                }
            }
        }
    }


    /**
     * Prior to opening a URLConnection, calling this method will set all
     * unexpired cookies that match the path or subpaths for thi underlying URL
     * <p>
     * The connection MUST NOT have been opened
     * method or an IOException will be thrown.
     *
     * @param conn a java.net.URLConnection - must NOT be open, or IOException will be thrown
     * @throws java.io.IOException Thrown if conn has already been opened.
     */

    public void setCookies(URLConnection conn) throws IOException {
        setCookies(conn, true);
    }

    public void setCookies(URLConnection conn, boolean checkCookieData) throws IOException {

        // let's determine the domain and path to retrieve the appropriate cookies
        URL url = conn.getURL();
        String path = url.getPath();

        StringBuilder cookieSB = new StringBuilder();

        Iterator cookieNames = store.keySet().iterator();
        while (cookieNames.hasNext()) {
            String cookieName = (String) cookieNames.next();
            Cookie cookie = store.get(cookieName);
            // check cookie to ensure path matches  and cookie is not expired
            // if all is cool, add cookie to header string
            if (!checkCookieData || comparePaths(cookie.getData(PATH), path) && isNotExpired(cookie.getData(EXPIRES))) {
                System.out.println(cookieName);
                cookieSB.append(cookieName);
                cookieSB.append("=");
                cookieSB.append(cookie.getValue());
                if (cookieNames.hasNext()) cookieSB.append(SET_COOKIE_SEPARATOR);
            }
        }
        try {
            conn.setRequestProperty(COOKIE, cookieSB.toString());
        } catch (java.lang.IllegalStateException ise) {
            IOException ioe = new IOException("Illegal State! Cookies cannot be set on a URLConnection that is already connected. "
                    + "Only call setCookies(java.net.URLConnection) BEFORE calling java.net.URLConnection.connect().");
            throw ioe;
        }
    }


    public void addExtra(CookieManager anotherCM) {

    }

    public CookieManager addCookie(Cookie cookie) {
        store.put(cookie.getName(), cookie);
        return this;
    }


    private String getDomainFromHost(String host) {
        if (host.indexOf(DOT) != host.lastIndexOf(DOT)) {
            return host.substring(host.indexOf(DOT) + 1);
        } else {
            return host;
        }
    }

    private boolean isNotExpired(String cookieExpires) {
        if (cookieExpires == null) return true;
        Date now = new Date();
        try {
            return (now.compareTo(dateFormat.parse(cookieExpires))) <= 0;
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
            System.err.println("Trying to use another format");

            try {
                return (now.compareTo(dateFormat2.parse(cookieExpires))) <= 0;
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                System.err.println("Failed again");
                return false;
            }
        }
    }

    private boolean comparePaths(String cookiePath, String targetPath) {
        if (cookiePath == null) {
            return true;
        } else if (cookiePath.equals("/")) {
            return true;
        } else if (targetPath.regionMatches(0, cookiePath, 0, cookiePath.length())) {
            return true;
        } else {
            return false;
        }

    }

    public String toString() {
        return store.toString();
    }

    public static void main(String[] args) {
        CookieManager cm = new CookieManager();
        try {
            URL url = new URL("https://vk.com");
            URLConnection conn = url.openConnection();
            conn.connect();
//            cm.storeCookies(conn);
            System.out.println(cm);
            cm.setCookies(url.openConnection());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}