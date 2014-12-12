
$(document).ready(function() {
    sendCardRequest();

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

function sendCardRequest() {
    $.ajax({
        type: "GET",
        url: Locations.GET_CARDS,
        dataType: "json",
        //data: {
        //    name: "John",
        //    location: "Boston"
        //}
    })
    .done(function(msg) {
            console.log("msg received, status: " + msg.status);

            switch (msg.status) {
                case ApiRequestStatus.OK:
                    console.log("card received: " + msg.data);
                    sendCardRequestDelayed();
                    break;
                case ApiRequestStatus.FINISHED:
                    if (msg.data)
                        console.log("card received: " + msg.data);
                    break;
                case ApiRequestStatus.WAIT:
                    sendCardRequestDelayed();
                    break;
                case ApiRequestStatus.ERROR:
                    break;
            }
    })
    .fail(function(error) {
            alert("Error while trying to receive card: " + error);
    });
}

function sendCardRequestDelayed() {
    setTimeout(sendCardRequest, 500);
}