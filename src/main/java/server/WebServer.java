package server;

import org.eclipse.jetty.rewrite.handler.RedirectRegexRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import server.FrontendServlet;

public class WebServer {
    Server server;
    boolean running = false;

    public WebServer(int port) throws Exception {
        FrontendServlet frontendServlet = new FrontendServlet(this);

        server = new Server(port);

        RewriteHandler rewriteHandler = new RewriteHandler();
        RedirectRegexRule rule = new RedirectRegexRule();
        rule.setRegex("/");
        rule.setReplacement("/index");
        rule.setHandling(true);

        rewriteHandler.addRule(rule);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontendServlet), "/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{rewriteHandler, resourceHandler, context});
        server.setHandler(handlers);
    }

    public void start() throws Exception {
        server.start();
        running = true;
        server.join();
        running = false;

    }

    public void stop() throws Exception {
        server.stop();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
