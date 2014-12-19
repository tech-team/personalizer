
$(document).ready(function() {
    sendPersonListRequest();
});


function sendPersonListRequest() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: Locations.GET_RESULT_CARDS
    })
        .done(function(msg) {
            console.log("msg received, status: " + msg.status);

            switch (msg.status) {
                case ApiRequestStatus.OK:
                    console.log("Results list received: " + msg.data);
                    handlePersonList(msg.source, msg.data);
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

function handlePersonList(source, persons) {
    alert("Received " + persons.length + " persons from " + source);

    $column = $(".column .results");
    $persons = $(".persons");

    _.each(persons, function(person) {
        $person = $('<tr><td class="merge-id"><h4>&nbsp;1&nbsp;</h4></td><td class="merge-content"><div class="person thumbnail"></div></td></tr>');

        _.each(person, function(card) {
            var $card = $(card);

            $card.addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                .find(".card-header")
                .addClass("ui-widget-header ui-corner-all")
                .prepend("<span class='ui-icon ui-icon-closethick card-remove'></span>");

            $card.find(".card-remove").click(function () {
                var $card = $(this).closest(".card");

                $card.remove();
            });

            $card.appendTo($person);
        });

        $person.appendTo($persons);
    });
}