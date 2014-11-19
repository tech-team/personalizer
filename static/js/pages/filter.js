
$(document).ready(function() {
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