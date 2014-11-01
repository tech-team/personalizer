import server.WebServer;

public class Main {
    public static void main(String[] args) throws Exception {
        WebServer server = new WebServer(8083);
        server.start();
    }
}