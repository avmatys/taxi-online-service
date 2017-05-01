/*Common js functions*/

$(function() {
    $('.logo').click(function () {
        window.location = "index.html";
    });
    $('.dropdown-toggle').click(function () {
        if ($(this).hasClass('active')) {
            $(this).css('transform', 'rotate(-45deg)').removeClass('active');
        } else {
            $(this).css('transform', 'rotate(45deg)').addClass('active');
        }
    });
    $(document).on('click', 'a[href*=#]', function(event){
        event.preventDefault();

        $('html, body').animate({
            scrollTop: $( $.attr(this, 'href') ).offset().top
        }, 750);
    });

    //SVG Fallback
    if (!Modernizr.svg) {
        $("img[src*='svg']").attr("src", function () {
            return $(this).attr("src").replace(".svg", ".png");
        });
    }

    //Chrome Smooth Scroll
    try {
        $.browserSelector();
        if ($("html").hasClass("chrome")) {
            $.smoothScroll();
        }
    } catch (err) {

    }

    $("img, a").on("dragstart", function (event) {
        event.preventDefault();
    });

});

function redirectTo(link){
	window.location = ''+link;
}

function showHide(){
	$(this).parent().toggle();
}
