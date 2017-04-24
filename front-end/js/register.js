// Add event handler for submit form event
// Functions are described in file utils.js
jQuery(document).ready(function ($) {
    //Change links
    var link = "login.html"
    if (!!Cookies.get('user-info')) {
        //Have cookie
        link="order.html";
    }
    $('.taxi-options>a:first').attr("href", link);

    //TODO validation
    jQuery(document).ready(function ($) {
        $('#form_register').submit(function (event) {
            event.preventDefault();
            registration(JSON.stringify(createRegistrationJSONData()));
        });
    });
});