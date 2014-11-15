
$(document).ready(function() {
    $(".sources .column").sortable({
        connectWith: ".destinations .column",
        handle: ".portlet-header",
        cancel: ".portlet-toggle",
        placeholder: "portlet-placeholder ui-corner-all",

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
        handle: ".portlet-header",
        cancel: ".portlet-toggle",
        placeholder: "portlet-placeholder ui-corner-all"
    });

    $(".portlet")
        .addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
        .find(".portlet-header")
        .addClass("ui-widget-header ui-corner-all")
        .prepend("<span class='ui-icon ui-icon-minusthick portlet-toggle'></span>");

    $(".portlet-toggle").click(function () {
        var icon = $(this);
        icon.toggleClass("ui-icon-minusthick ui-icon-plusthick");
        icon.closest(".portlet").find(".portlet-content").toggle();
    });
});