import content.AsyncContentProvider;
import content.Request;
import content.Response;
import server.WebServer;

public class Main {
    public static void main(String[] args) throws Exception {

        AsyncContentProvider cp = new AsyncContentProvider();
        cp.execute(new Request(), response -> System.out.println("hi"));

        WebServer server = new WebServer(8083);
        server.start();
    }
}