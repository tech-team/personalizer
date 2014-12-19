
var deletedCards = [];
var currentRow = 1;


$(document).ready(function() {

    addRow();
    $('#add_row').click(addRow);

    $('#continue_button').click(function() {
        var mergedCards = [];

        var $persons = $(".destinations .person");

        $persons.each(function(i, person) {
            var merge = [];

            var $person = $(person);
            $person.find(".card").each(function(j, card) {
                var $card = $(card);
                merge.push({
                    "id": $card.data('id'),
                    "source_id": $card.data('source_id')
                });
            });

            mergedCards.push(merge);
        });

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: Locations.POST_FILTER_CARDS,
            dataType: "json",
            data: JSON.stringify({
                deletedCards: deletedCards,
                mergedCards: mergedCards
            })
        })
            .done(function(msg) {
                window.location.href = Locations.RESULTS;
            })
            .fail(function(error) {
                alert("Error while trying to POST_FILTER_CARDS: " + error);
            });
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

            if ($card.data('source_id') != $column.data('source_id'))
                $(ui.sender).sortable("cancel");
        }
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
    console.log("Received " + cards.length + " cards from " + source);

    $vk_column = $(".column[data-source_id='" + source.toLowerCase() + "']");

    _.each(cards, function(card) {
        var $card = $(card);
        $card.addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
            .find(".card-header")
            .addClass("ui-widget-header ui-corner-all")
            .prepend("<span class='ui-icon ui-icon-closethick card-remove'></span>");

        $card.find(".card-remove").click(function () {
            var $card = $(this).closest(".card");

            deletedCards.push({
                    "id": $card.data('id'),
                    "source_id": $card.data('source_id')
                });

            $card.remove();
        });

        $card.appendTo($vk_column);
    });
}

function addRow() {
    var row = '<tr><td class="merge-id"><h4>&nbsp;' + currentRow++ + '&nbsp;</h4></td><td class="merge-content"><div class="person thumbnail"></div></td></tr>';
    var $row = $(row);

    $row.appendTo($('.merge-table'));

    $row.find(".person").sortable({
        connectWith: ".sources .column, .destinations .person",
        handle: ".card-header",
        cancel: ".card-remove",
        placeholder: "card-placeholder ui-corner-all",
        floating: true
    });
}