/*Common js functions*/
jQuery(document).ready(function ($) {
    $('.logo').click(function(){
        window.location="index.html";
    });

    //SVG Fallback
    if(!Modernizr.svg) {
        $("img[src*='svg']").attr("src", function() {
            return $(this).attr("src").replace(".svg", ".png");
        });
    }

    //Chrome Smooth Scroll
    try {
        $.browserSelector();
        if($("html").hasClass("chrome")) {
            $.smoothScroll();
        }
    } catch(err) {

    }

    $("img, a").on("dragstart", function(event) { event.preventDefault(); });


});

function redirectTo(link){
	window.location = ''+link;
}

function toggle(){
	$(this).parent().toggle();
}
