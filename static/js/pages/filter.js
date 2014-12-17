
var deletedCards = [];

$(document).ready(function() {

    $('#continue_button').click(function() {
        var mergedCards = [];

        var $persons = $(".destinations .person");

        $persons.each(function($person) {
            var merge = [];

            $person.each(function($card) {
                merge.append({
                    "id": $card.data('id'),
                    "source-id": $card.data('source-id')
                });
            });

            mergedCards.append(merge);
        });

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: Locations.POST_FILTER_CARDS,
            dataType: "json",
            data: {
                deletedCards: deletedCards,
                mergedCards: mergedCards
            }
        })
            .done(function(msg) {
                window.location.href = Locations.RESULTS;
            })
            .fail(function(error) {
                alert("Error while trying to POST_FILTER_CARDS: " + error);
            })
;        )
    });

    sendPersonListRequest();

    $(".sources .column").sortable({
        connectWith: ".destinations .person",
        handle: ".card-header",
        cancel: ".card-remove",
        placeholder: "card-placeholder ui-corner-all",

        receive: function(e, ui) {
            $card = ui.item;
            $column = $(this);

            if ($card.data('source-id') != $column.data('source-id'))
                $(ui.sender).sortable("cancel");
        }
    });

    $(".destinations .person").sortable({
        connectWith: ".sources .column, .destinations .person",
        handle: ".card-header",
        cancel: ".card-remove",
        placeholder: "card-placeholder ui-corner-all",
        floating: true
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

    $vk_column = $(".column[data-source-id='" + source.toLowerCase() + "']");

    _.each(cards, function(card) {
        var $card = $(card);
        $card.addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
            .find(".card-header")
            .addClass("ui-widget-header ui-corner-all")
            .prepend("<span class='ui-icon ui-icon-closethick card-remove'></span>");

        $card.find(".card-remove").click(function () {
            var $card = $(this).closest(".card");

            deletedCards.append({
                    "id": $card.data('id'),
                    "source-id": $card.data('source-id')
                });

            $card.remove();
        });

        $card.appendTo($vk_column);
    });
}