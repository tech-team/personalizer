//NB: Locations and ApiRequestStatus objects
// should be same as in java/server/FrontendServlet.java

var Locations = {
    //static
    INDEX: "/",
    REQUEST: "/request",
    FILTER: "/filter",
    RESULTS: "/results",

    //AJAX
    START_SEARCH: "/api/start_search",
    GET_CARDS: "/api/get_cards",
    FILTER_CARDS: "/api/filter_cards"
};

var ApiRequestStatus = {
    OK: "OK",
    ERROR: "ERROR",
    WAIT: "WAIT",
    FINISHED: "FINISHED"
};

function navigate(page, params) {
    var qps = "";
    _.each(_.pairs(params), function(pair) {
        qps += pair[0] + "=" + pair[1] + "&";
    });

    window.location.href = page + "?" + qps;
}