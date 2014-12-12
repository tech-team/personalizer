package server;

import content.PersonCard;
import org.json.JSONObject;
import util.NC;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//NB: Locations and ApiRequestStatus classes
// should be same as in static/js/common.js

public class FrontendServlet extends HttpServlet {
    public abstract class Locations {
        //static
        public static final String INDEX = "/";
        public static final String REQUEST = "/request";
        public static final String FILTER = "/filter";
        public static final String RESULTS = "/results";

        public static final String GET_CARDS = "/api/get_cards";
        public static final String FILTER_CARDS = "/api/filter_cards";
    }

    public abstract class Templates {
        public static final String REQUEST = "request.html";
        public static final String FILTER = "filter.html";
        public static final String RESULTS = "results.html";
        public static final String CARD = "card.tml";
    }

    public abstract class ApiRequestStatus {
        public static final String OK = "OK";
        public static final String ERROR = "ERROR";
        public static final String WAIT = "WAIT";
        public static final String FINISHED = "FINISHED";
    }

    Map<String, BufferedAsyncContentProvider> queries = new HashMap<>();

    public FrontendServlet() {

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
                staticHandler(request, response, Templates.REQUEST);
                return;

            case Locations.FILTER:
                filterHandler(request, response);
                return;

            case Locations.RESULTS:
                staticHandler(request, response, Templates.RESULTS);
                return;

            case Locations.GET_CARDS:
                getCardsHandler(request, response);
                return;

            case Locations.FILTER_CARDS:
                filterCardsHandler(request, response);
                return;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void staticHandler(HttpServletRequest request, HttpServletResponse response, String templateName)
            throws ServletException, IOException  {
        Map<String, Object> pageVariables = new HashMap<>();

        response.getWriter().println(PageGenerator.getPage(templateName, pageVariables));
    }

    private void filterHandler(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {

        //TODO: GET params

        //start new query asynchronously
        String qid = "";
        do {
            qid = UUID.randomUUID().toString();
        }
        while (queries.containsKey(qid));
        queries.put(qid, new BufferedAsyncContentProvider(new BufferedContentReceiver()));

        //set query id as cookie
        response.addCookie(new Cookie("qid", qid));

        staticHandler(request, response, Templates.FILTER);
    }


    private void getCardsHandler(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        response.addHeader("Cache-Control", "no-cache");

        //obtain new cards from CP
        boolean newCardsReady = false;

        if (newCardsReady) {
            JSONObject json = new JSONObject();
            json.put("status", ApiRequestStatus.OK);

            HashMap<String, Object> card = new HashMap<>();
            PersonCard personCard = new PersonCard();
            card.put("name", NC.toString(personCard.getName()));
            card.put("surname", NC.toString(personCard.getSurname()));
            card.put("birthdate", NC.toString(personCard.getBirthDate().toString()));
            card.put("age", NC.toString(personCard.getAge()));

            card.put("avatars", personCard.getAvatars());

            card.put("country", NC.toString(personCard.getCountry()));
            card.put("city", NC.toString(personCard.getCity()));
            card.put("phone", NC.toString(personCard.getMobilePhone()));

            card.put("socialLinks", personCard.getSocialLinks());
            card.put("universities", personCard.getUniversities());
            card.put("jobs", personCard.getJobs());

            json.put("data", PageGenerator.getPage(Templates.CARD, card));

            response.getWriter().println(json.toString());
        } else {
            JSONObject json = new JSONObject();
            json.put("status", ApiRequestStatus.WAIT);

            response.getWriter().println(json.toString());
        }
    }

    private void filterCardsHandler(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        JSONObject json = new JSONObject();
        json.put("status", ApiRequestStatus.OK);

        response.getWriter().println(json.toString());
    }

    private static String getTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }
}
