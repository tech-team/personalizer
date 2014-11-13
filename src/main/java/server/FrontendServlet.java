package server;

import content.source.linkedin.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FrontendServlet extends HttpServlet {
    public abstract class Locations {
        //static
        public static final String INDEX = "/";
        public static final String REQUEST = "/request";
        public static final String SEARCHING = "/searching";
        public static final String RESULTS = "/results";

        //AJAX
        public static final String STATUS = "/status";

        //not needed
        public static final String LINKED_IN = "/linkedin";
    }

    public abstract class Templates {
        public static final String REQUEST = "request.html";
        public static final String SEARCHING = "searching.html";
        public static final String RESULTS = "results.html";
    }

    private WebServer webServer;

    public FrontendServlet(WebServer webServer) {
        this.webServer = webServer;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        switch (request.getPathInfo()) {
            case Locations.INDEX:
                response.sendRedirect(Locations.REQUEST);
                return;

            case Locations.REQUEST:
                staticView(request, response, Templates.REQUEST);
                return;

            case Locations.SEARCHING:
                staticView(request, response, Templates.SEARCHING);
                return;

            case Locations.RESULTS:
                staticView(request, response, Templates.RESULTS);
                return;

            case Locations.STATUS:
                statusView(request, response);
                return;


            case Locations.LINKED_IN: {
                //пока так
                String code = request.getParameter("code");
                Request.token = Request.requestAccessToken(code);
            }

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }



    private void staticView(HttpServletRequest request, HttpServletResponse response, String templateName)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();

        response.getWriter().println(PageGenerator.getPage(templateName, pageVariables));
    }

    private void statusView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        response.addHeader("Cache-Control", "no-cache");
        response.getWriter().println(getTime());
    }

    private static String getTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }
}
