package util.net;

import org.eclipse.jetty.http.HttpHeader;

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

    public static class Response {
        private int responseCode;
        private String protocol;
        private Headers headers;
        private String body;

        public Response(int responseCode, String protocol, Headers headers, String body) {
            this.responseCode = responseCode;
            this.protocol = protocol;
            this.headers = headers;
            this.body = body;
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
    }


    public static Response httpGet(String url) throws IOException {
        return httpGet(url, null, null, DEFAULT_ENCODING);
    }

    public static Response httpGet(String url, UrlParams params) throws IOException {
        return httpGet(url, params, null, DEFAULT_ENCODING);
    }

    public static Response httpGet(Request request) throws IOException {
        return httpGet(request.getUrl(), request.getParams(), request.getHeaders(), request.getEncoding());
    }

    public static Response httpGet(String url, UrlParams params, Headers headers, String encoding) throws IOException {
        URL urlObj = constructUrl(url, params, encoding);
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) urlObj.openConnection();

            connection.setRequestMethod("GET");
            fillHeaders(headers, connection);
            connection.connect();

            return parseConnection(connection, encoding);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
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
                    headers.add(key, connection.getHeaderField(key));
                }
            }

            if (status == HttpURLConnection.HTTP_OK) {
                in = connection.getInputStream();
            } else {
                in = connection.getErrorStream();
            }
            body = handleInputStream(in, encoding);

            return new Response(status, protocol, headers, body);
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    public static Response httpPost(String url) throws IOException {
        return httpPost(url, null, null, DEFAULT_ENCODING);
    }

    public static Response httpPost(Request request) throws IOException {
        return httpPost(request.getUrl(), request.getParams(), request.getHeaders(), request.getEncoding());
    }

    public static Response httpPost(String url, UrlParams data, Headers headers, String encoding) throws IOException {
        URL urlObj = constructUrl(url, null, encoding);
        HttpURLConnection connection = null;
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

            return parseConnection(connection, encoding);
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
