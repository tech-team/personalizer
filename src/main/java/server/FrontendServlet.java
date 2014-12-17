package server;

import content.PersonCard;
import content.SocialLink;
import org.json.JSONObject;
import util.NC;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
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

        public static final String GET_FILTER_CARDS = "/api/get_filter_cards";
        public static final String GET_RESULT_CARDS = "/api/get_result_cards";
        public static final String POST_FILTER_CARDS = "/api/post_filter_cards";
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

    Map<String, Session> queries = new HashMap<>();

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

            case Locations.GET_FILTER_CARDS:
                getCardsHandler(request, response);
                return;

            case Locations.GET_RESULT_CARDS:
                getCardsHandler(request, response);
                return;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        switch (request.getPathInfo()) {
            case Locations.POST_FILTER_CARDS:
                filterCardsHandler(request, response);
                return;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void staticHandler(HttpServletRequest request, HttpServletResponse response, String fileName)
            throws ServletException, IOException  {
        response.getWriter().println(PageGenerator.getStaticPage(fileName));
    }

    private void filterHandler(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {

        //create request
        PersonCard card = new PersonCard();

        card.setName(request.getParameter("name"));
        card.setAgeFrom(NC.parseInt(request.getParameter("age_from")));
        card.setAgeFrom(NC.parseInt(request.getParameter("age_to")));
        card.addEmail(request.getParameter("email"));

        card.addSocialLink(SocialLink.LinkType.VK,
                new SocialLink(SocialLink.LinkType.VK, request.getParameter("vk")));

        card.addSocialLink(SocialLink.LinkType.FB,
                new SocialLink(SocialLink.LinkType.FB, request.getParameter("fb")));

        card.addSocialLink(SocialLink.LinkType.LINKED_IN,
                new SocialLink(SocialLink.LinkType.LINKED_IN, request.getParameter("li")));

        //generate qid
        String qid = "";
        do {
            qid = UUID.randomUUID().toString();
        }
        while (queries.containsKey(qid));

        //create session
        Session session = new Session();
        try {
            //start search request asynchronously
            session.getCP().request(card);
        } catch (InterruptedException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        System.out.println("Search started for qid = " + qid);

        //save session
        queries.put(qid, session);

        //set query id as cookie
        response.addCookie(new Cookie("qid", qid));

        staticHandler(request, response, Templates.FILTER);
    }


    private void getCardsHandler(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        response.addHeader("Cache-Control", "no-cache");

        System.out.println(Arrays.toString(request.getCookies()));

        //obtain new cards from CP
        boolean newCardsReady = true;

        if (newCardsReady) {
            JSONObject json = new JSONObject();
            json.put("status", ApiRequestStatus.OK);

            HashMap<String, Object> card = new HashMap<>();
            PersonCard personCard = new PersonCard();
            card.put("name", NC.toString(personCard.getName()));
            card.put("surname", NC.toString(personCard.getSurname()));
            card.put("birthdate", NC.toString(personCard.getBirthDate()));
            card.put("age", NC.toString(personCard.getAge()));

            card.put("avatars", personCard.getAvatars());

            card.put("country", NC.toString(personCard.getCountry()));
            card.put("city", NC.toString(personCard.getCity()));
            card.put("phone", NC.toString(personCard.getMobilePhone()));

            card.put("socialLinks", personCard.getSocialLinks().values());
            card.put("universities", personCard.getUniversities());
            card.put("jobs", personCard.getJobs());

            json.put("data", PageGenerator.getTemplatePage(Templates.CARD, card));

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
}
