
$(document).ready(function() {
    $('#start_search_button').click(function() {
        $.ajax({
            type: "GET",
            url: Locations.START_SEARCH,
            dataType: "json",
            data: {
                name: "John",
                location: "Boston"
            }
        })
            .done(function(msg) {
                alert("qid received: " + msg.qid);
                navigate(Locations.FILTER, {qid: msg.qid});
            })
            .fail(function(error) {
                alert("Error while trying to receive qid: " + error);
            });

        return false;
    });
});