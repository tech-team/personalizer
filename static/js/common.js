//NB: Locations and ApiRequestStatus objects
// should be same as in java/server/FrontendServlet.java

var Locations = {
    //static
    INDEX: "/",
    REQUEST: "/request",
    FILTER: "/filter",
    RESULTS: "/results",

    //AJAX
    GET_CARDS: "/api/get_cards",
    FILTER_CARDS: "/api/filter_cards"
};

var ApiRequestStatus = {
    OK: "OK",
    ERROR: "ERROR",
    WAIT: "WAIT",
    FINISHED: "FINISHED"
};