package util.net;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class HttpDownloader {
    // TODO: add proxy

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String USER_AGENT = "Personalizer v0.1";

    public static class Request {
        private String url;
        private UrlParams params;
        private Headers headers;
        private String encoding;
        private boolean followRedirects = true;

        public Request(String url) {
            this(url, null, null);
        }

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

        public boolean isFollowRedirects() {
            return followRedirects;
        }

        public Request setUrl(String url) {
            this.url = url;
            return this;
        }

        public Request setParams(UrlParams params) {
            this.params = params;
            return this;
        }

        public Request setHeaders(Headers headers) {
            this.headers = headers;
            return this;
        }

        public Request setEncoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Request setFollowRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }
    }

    public static class Response {
        private int responseCode;
        private String protocol;
        private Headers headers;
        private String body;
        private String url;

        public Response(int responseCode, String protocol, Headers headers, String body, String url) {
            this.responseCode = responseCode;
            this.protocol = protocol;
            this.headers = headers;
            this.body = body;
            this.url = url;
        }

        public Response() {
        }

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public Headers getHeaders() {
            return headers;
        }

        public void setHeaders(Headers headers) {
            this.headers = headers;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


    public static Response httpGet(String url) throws IOException {
        Request req = new Request(url);
        return httpGet(req);
    }

    public static Response httpGet(String url, UrlParams params) throws IOException {
        Request req = new Request(url, params);
        return httpGet(req);
    }

    public static Response httpGet(Request request) throws IOException {
        URL urlObj = constructUrl(request.getUrl(), request.getParams(), request.getEncoding());
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) urlObj.openConnection();

            connection.setRequestMethod("GET");
            fillHeaders(request.getHeaders(), connection);
            connection.setInstanceFollowRedirects(request.isFollowRedirects());
            connection.connect();

            return parseConnection(connection, request.getEncoding());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static Response httpPost(String url) throws IOException {
        Request req = new Request(url);
        return httpPost(req);
    }

    public static Response httpPost(Request request) throws IOException {
        URL urlObj = constructUrl(request.getUrl(), null, request.getEncoding());
        HttpURLConnection connection = null;
        OutputStream out = null;

        try {
            connection = (HttpURLConnection) urlObj.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(request.isFollowRedirects());
            fillHeaders(request.getHeaders(), connection);

            connection.connect();

            String dataString = constructParams(request.getParams(), request.getEncoding());
            out = new BufferedOutputStream(connection.getOutputStream());
            out.write(dataString.getBytes());
            out.flush();

            return parseConnection(connection, request.getEncoding());
        } finally {
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
                connection.setRequestProperty(h.getName(), h.getValuesSeparated());
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

    private static Response parseConnection(HttpURLConnection connection, String encoding) throws IOException {
        InputStream in = null;
        try {
            String protocol = null;
            String body = null;
            int status = connection.getResponseCode();
            Headers headers = new Headers();

            for (Map.Entry<String, List<String>> e : connection.getHeaderFields().entrySet()) {
                String key = e.getKey();
                if (key == null) {
                    protocol = connection.getHeaderField(null).split("\\s")[0];
                } else {
                    for (String v : e.getValue()) {
                        headers.add(key, v);
                    }
                }
            }

            if (status < 400) {
                in = connection.getInputStream();
            } else {
                in = connection.getErrorStream();
            }
            body = handleInputStream(in, encoding);
            String url = connection.getURL().toString();
            return new Response(status, protocol, headers, body, url);
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    public static void main(String[] args) throws IOException {
        Headers hh = new Headers();
        hh.add("Cookie", "hello=1");
        hh.add("Cookie", "pip=2");
        Response r = HttpDownloader.httpGet(new Request("http://vk.com").setHeaders(hh));
    }
}
