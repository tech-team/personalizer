var SEARCHING_PAGE = '/searching';

$(document).ready(function() {
    $('#start_search_button').click(function() {
        var search_id = Math.random(); //will be obtained from server-side (unique for each search request)
        navigate(SEARCHING_PAGE, {search_id: search_id});

        return false;
    });
});

function navigate(page, params) {
    var qps = "";
    _.each(_.pairs(params), function(pair) {
        qps += pair[0] + "=" + pair[1] + "&";
    });

    window.location.href = page;
    window.location.search = qps;
}