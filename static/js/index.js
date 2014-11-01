var pages = {
    request_page: '#request_page',
    handle_page: '#handle_page',
    results_page: '#results_page'
};

currentSection = pages.request_page;

function navigate(section) {
    window.location.hash = section;
}

$(document).ready(function() {
    window.location.hash = pages.request_page;

    $('#start_search_button').click(function() {
        var $progress_dialog = $('#progress_dialog');

        $progress_dialog.modal('show');
        setTimeout(function() {
            $progress_dialog.modal('hide');
            navigate(pages.handle_page);
        }, 1000);

        return false;
    });

    $('#continue_button').click(function() {
        var $progress_dialog = $('#progress_dialog');

        $progress_dialog.modal('show');
        setTimeout(function() {
            $progress_dialog.modal('hide');
            navigate(pages.results_page);
        }, 1000);

        return false;
    });

    $(window).on('hashchange', function() {
        var animationTime = 350;

        $(currentSection).fadeOut(animationTime, function() {
            currentSection = window.location.hash;
            $(currentSection).fadeIn(animationTime);

            window.location.hash = currentSection;
        });
    });
});

