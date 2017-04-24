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

    $('#form_login').submit(function (event) {
        event.preventDefault();
        authorization(JSON.stringify(createAuthorizationJSONData()));
    });
});