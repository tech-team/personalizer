package server;

import content.*;
import content.source.ContentSource;
import org.json.JSONArray;
import org.json.JSONObject;
import util.NC;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

//NB: Locations and ApiRequestStatus classes
// should be same as in static/js/common.js

public class FrontendServlet extends HttpServlet {
    public abstract class Locations {
        //static
        public static final String INDEX = "/";
        public static final String REQUEST = "/request";
        public static final String FILTER = "/filter";
        public static final String RESULTS = "/results";

        public static final String GET_PERSON_LIST = "/api/get_person_list";
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

    public abstract class Cookies {
        public static final String QID = "qid";
    }

    Map<String, Session> sessions = new HashMap<>();

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

            case Locations.GET_PERSON_LIST:
                getPersonListHandler(request, response);
                return;

            case Locations.GET_RESULT_CARDS:
                getPersonListHandler(request, response);
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
        while (sessions.containsKey(qid));

        //create session
        Session session = new Session();
        try {
            //start search request asynchronously
            session.getCP().request(card, true);
        } catch (InterruptedException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        System.out.println("Search started for qid = " + qid);

        //save session
        sessions.put(qid, session);

        //set query id as cookie
        response.addCookie(new Cookie(Cookies.QID, qid));

        staticHandler(request, response, Templates.FILTER);
    }


    private void getPersonListHandler(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        response.addHeader("Cache-Control", "no-cache");
        response.setContentType("application/json");

        String qid = getQueryId(request);

        if (qid == null) {
            JSONObject json = new JSONObject();
            json.put("status", ApiRequestStatus.ERROR);
            json.put("data", "No QID cookie provided");
            response.getWriter().println(json.toString());

            return;
        }

        //obtain new cards from CP
        Session session = sessions.get(qid);
        BufferedContentReceiver receiver = session.getCP().getContentReceiver();

        PersonList personList =  receiver.getNextPersonList();

        if (personList != null) {
            JSONObject json = new JSONObject();
            json.put("status", ApiRequestStatus.OK);
            json.put("source", personList.getType().toString());

            for (PersonCard personCard: personList.getPersons().values()) {
                HashMap<String, Object> card = new HashMap<>();

                card.put("source-id", personCard.getType().toString().toLowerCase());
                card.put("id", personCard.getId());

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

                json.append("data", PageGenerator.getTemplatePage(Templates.CARD, card));
            }

            response.getWriter().println(json.toString());
        } else {
            JSONObject json = new JSONObject();

            if (receiver.isFinishedListsRetrieval())
                json.put("status", ApiRequestStatus.FINISHED);
            else
                json.put("status", ApiRequestStatus.WAIT);

            response.getWriter().println(json.toString());
        }
    }

    private void filterCardsHandler(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        response.addHeader("Cache-Control", "no-cache");
        response.setContentType("application/json");

        String qid = getQueryId(request);
        if (qid == null) {
            JSONObject json = new JSONObject();
            json.put("status", ApiRequestStatus.ERROR);
            json.put("data", "No QID cookie provided");
            response.getWriter().println(json.toString());

            return;
        }
        Session session = sessions.get(qid);
        BufferedAsyncContentProvider cp = session.getCP();

        JSONObject jsonRequest = getJsonData(request);

        try {
            //handle deleted cards
            JSONArray deletedCards = jsonRequest.getJSONArray("deletedCards");
            List<PersonId> deletedCardsList = new ArrayList<>();

            for (int i = 0; i < deletedCards.length(); i++) {
                JSONObject cardObject = deletedCards.getJSONObject(i);

                int id = cardObject.getInt("id");
                int source_id = cardObject.getInt("source-id");

                ContentSource.Type type = Enum.valueOf(ContentSource.Type.class, String.valueOf(id).toUpperCase());
                deletedCardsList.add(new PersonId(type, id));
            }

            cp.remove(deletedCardsList);

            //handle merged cards
            JSONArray mergedCards = jsonRequest.getJSONArray("mergedCards");

            for (int i = 0; i < mergedCards.length(); i++) {
                JSONArray mergeObject = mergedCards.getJSONArray(i);
                List<PersonId> mergeList = new ArrayList<>();

                for (int j = 0; j < mergeObject.length(); j++) {
                    JSONObject cardObject = mergeObject.getJSONObject(i);

                    int id = cardObject.getInt("id");
                    int source_id = cardObject.getInt("source-id");

                    ContentSource.Type type = Enum.valueOf(ContentSource.Type.class, String.valueOf(id).toUpperCase());
                    mergeList.add(new PersonId(type, id));
                }

                cp.merge(new PersonIdsTuple(mergeList));
            }
        }
        catch (InterruptedException e) {
            JSONObject json = new JSONObject();
            json.put("status", ApiRequestStatus.ERROR);
            json.put("data", "Something was interrupted");
            response.getWriter().println(json.toString());

            return;
        }


        //send response

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", ApiRequestStatus.OK);

        response.getWriter().println(jsonResponse.toString());
    }

    private JSONObject getJsonData(HttpServletRequest request) {
        StringBuilder jb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        return new JSONObject(jb.toString());
    }

    private String getQueryId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie: cookies) {
            if (cookie.getName().equals(Cookies.QID))
                return cookie.getValue();
        }

        return null;
    }
}
