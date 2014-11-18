
$(document).ready(function() {
    $(".sources .column").sortable({
        connectWith: ".destinations .column",
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

    $(".destinations .column").sortable({
        connectWith: ".sources .column",
        handle: ".card-header",
        cancel: ".card-remove",
        placeholder: "card-placeholder ui-corner-all"
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