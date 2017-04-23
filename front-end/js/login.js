// Add event handler for submit form event
// Functions are described in file utils.js
jQuery(document).ready(function ($) {
    $('#form_login').submit(function (event) {
        event.preventDefault();
        authorization(createAuthorizationJSONData());
    });
});