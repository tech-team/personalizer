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
        public static final String INDEX = "/index";
        public static final String POLL = "/poll";
        public static final String STOP = "/stop";
        public static final String LINKED_IN = "/linkedin";
    }

    public abstract class Templates {
        public static final String INDEX = "index.html";
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
            case Locations.INDEX: {
                indexView(request, response);

                return;
            }
            case Locations.POLL: {
                pollView(request, response);

                return;
            }
            case Locations.STOP: {
                try {
                    webServer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }
            case Locations.LINKED_IN: {
                //пока так
                String code = request.getParameter("code");
                Request.token = Request.requestAccessToken(code);
            }

            default: {
                response.sendRedirect(Locations.INDEX);

                return;
            }
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }



    private void indexView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();

        response.getWriter().println(PageGenerator.getPage(Templates.INDEX, pageVariables));
    }

    private void pollView(HttpServletRequest request, HttpServletResponse response)
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
