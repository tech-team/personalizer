package util.net;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class HttpDownloader {
    // TODO: add proxy

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String USER_AGENT = "Personalizer v0.1";

    public static class Request {
        private String url;
        private UrlParams params;
        private Headers headers;
        private String encoding;

        public Request(String url, String encoding) {
            this(url, null, null, encoding);
        }

        public Request(String url, UrlParams params) {
            this(url, params, null, DEFAULT_ENCODING);
        }

        public Request(String url, UrlParams params, Headers headers) {
            this(url, params, headers, DEFAULT_ENCODING);
        }

        public Request(String url, UrlParams params, Headers headers, String encoding) {
            this.url = url;
            this.params = params;
            this.headers = headers;
            this.encoding = encoding;
        }

        public String getUrl() {
            return url;
        }

        public UrlParams getParams() {
            return params;
        }

        public Headers getHeaders() {
            return headers;
        }

        public String getEncoding() {
            return encoding;
        }
    }

    public static String httpGet(String url) throws IOException {
        return httpGet(url, null, null, DEFAULT_ENCODING);
    }

    public static String httpGet(String url, UrlParams params) throws IOException {
        return httpGet(url, params, null, DEFAULT_ENCODING);
    }

    public static String httpGet(Request request) throws IOException {
        return httpGet(request.getUrl(), request.getParams(), request.getHeaders(), request.getEncoding());
    }

    public static String httpGet(String url, UrlParams params, Headers headers, String encoding) throws IOException {
        URL urlObj = constructUrl(url, params, encoding);
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            connection = (HttpURLConnection) urlObj.openConnection();

            connection.setRequestMethod("GET");
            fillHeaders(headers, connection);
            connection.connect();

            String res = null;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                res = handleInputStream(in, encoding);
            }
            return res;
        } finally {
            if (in != null) {
                in.close();
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String httpPost(String url) throws IOException {
        return httpPost(url, null, null, DEFAULT_ENCODING);
    }

    public static String httpPost(Request request) throws IOException {
        return httpPost(request.getUrl(), request.getParams(), request.getHeaders(), request.getEncoding());
    }

    public static String httpPost(String url, UrlParams data, Headers headers, String encoding) throws IOException {
        URL urlObj = constructUrl(url, null, encoding);
        HttpURLConnection connection = null;
        InputStream in = null;
        OutputStream out = null;

        try {
            connection = (HttpURLConnection) urlObj.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            fillHeaders(headers, connection);

            connection.connect();

            String dataString = constructParams(data, encoding);
            out = new BufferedOutputStream(connection.getOutputStream());
            out.write(dataString.getBytes());
            //clean up
            out.flush();

            String res = null;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
                res = handleInputStream(in, encoding);
            }
            return res;
        } finally {
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static URL constructUrl(String url, UrlParams params, String encoding) throws MalformedURLException, UnsupportedEncodingException {
        if (params == null || params.isEmpty()) {
            return new URL(url);
        }
        return new URL(url + "?" + constructParams(params, encoding));
    }

    private static String constructParams(UrlParams params, String encoding) throws UnsupportedEncodingException {
        if (params == null) {
            return "";
        }

        String newUrl = "";
        for (UrlParams.UrlParam p : params) {
            newUrl += URLEncoder.encode(p.getKey(), encoding) + "=" + URLEncoder.encode(p.getValue(), encoding) + "&";
        }
        return newUrl.substring(0, newUrl.length() - 1);
    }

    private static void fillHeaders(Headers headers, HttpURLConnection connection) {
        if (headers != null) {
            for (Headers.Header h : headers) {
                connection.setRequestProperty(h.getName(), h.getValue());
            }
        }
        connection.setRequestProperty("User-Agent", USER_AGENT);
    }


    private static String handleInputStream(InputStream in, String encoding) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(encoding)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
