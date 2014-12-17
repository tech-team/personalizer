
$(document).ready(function() {
    $('#continue_button').click(function() {
        //TODO: send AJAX request to Locations.POST_FILTER_CARDS
        window.location.href = Locations.RESULTS;
    });

    sendPersonListRequest();

    //TODO: think to remember:
    //all the addClass things should be carried out on new cards as well

    $(".sources .column").sortable({
        connectWith: ".destinations .person",
        handle: ".card-header",
        cancel: ".card-remove",
        placeholder: "card-placeholder ui-corner-all",

        receive: function(e, ui) {
            //TODO: test is it possible to place that item that column
            $(ui.sender).sortable("cancel");
        },

//                stop: function()
//                {
//                    $(this).sortable('cancel');
//                }
    });

    $(".destinations .person").sortable({
        connectWith: ".sources .column, .destinations .person",
        handle: ".card-header",
        cancel: ".card-remove",
        placeholder: "card-placeholder ui-corner-all",
        floating: true
    });

    $(".card")
        .addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
        .find(".card-header")
        .addClass("ui-widget-header ui-corner-all")
        .prepend("<span class='ui-icon ui-icon-closethick card-remove'></span>");

    $(".card-remove").click(function () {
        $(this).closest(".card").remove();
    });
});

function sendPersonListRequest() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: Locations.GET_PERSON_LIST
    })
    .done(function(msg) {
            console.log("msg received, status: " + msg.status);

            switch (msg.status) {
                case ApiRequestStatus.OK:
                    console.log("person list received: " + msg.data);
                    handlePersonList(msg.source, msg.data);
                    sendPersonListRequestDelayed();
                    break;
                case ApiRequestStatus.FINISHED:
                    break;
                case ApiRequestStatus.WAIT:
                    sendPersonListRequestDelayed();
                    break;
                case ApiRequestStatus.ERROR:
                    break;
            }
    })
    .fail(function(error) {
            alert("Error while trying to receive card: " + error);
    });
}

function sendPersonListRequestDelayed() {
    setTimeout(sendPersonListRequest, 500);
}

function handlePersonList(source, cards) {
    alert("Recieved " + cards.length + " cards from " + source);
}