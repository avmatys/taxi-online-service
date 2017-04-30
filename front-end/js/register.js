/**
 * Represent registrtation functionality
 * @type {{register, properties}}
 * @constructor
 */
var RegistrationWrapper = function () {

    var properties = {
        'registerUrl': 'http://localhost:8080/taxi-online-service/api/v1/account/register/',

        'defaultContentType': 'application/json; charset=utf-8',
        'defaultDataType': 'JSON',
        'defaultTimeOut': 8000,
        'typePOST': 'POST',

        'userCookieName': 'user-info',

        'indexLink': 'index.html',
        'orderLink': 'order.html',
        'loginLink': 'login.html',
        'userProfileLink': 'user_profile.html',
        'driverProfileLink': 'driver_profile.html',
        'registrationError': 'Ошибка регистрации. Данный логин занят'
    };

    /**
     * Function for register feature
     */
    function register(){
        var accountInformation = JSON.stringify(createRegistrationJSONData());
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(accountInformation, properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typePOST, function () {
        }, properties.registerUrl, registrationDone, registrationFail);
    }
    /**
     * Function for handling successful registration
     * @param data - account information
     */
    function registrationDone(data) {
        createUserCookie(data);
        var role = data.data.role;
        var redirectPage = (role === "PASSENGER") ? properties.userProfileLink : (role === "DRIVER") ? properties.driverProfileLink : properties.indexLink;
        window.location.href = redirectPage;
    }

    /**
     * Function for handling registration failure
     * @param jqXHR
     */
    function registrationFail(jqXHR) {
        console.log("Error: " + jqXHR.status + ", " + jqXHR.statusText);
        alert(properties.registrationError);
    }

    /**
     * Function for saving account data into cookie
     * @param data - account information
     */
    function createUserCookie(data) {
        data.data.password = $('#password').val();
        Cookies.set(properties.userCookieName, JSON.stringify(data.data));
    }

    /**
     * Function for creating account login and password
     * @returns account properties
     */
    function createRegistrationJSONData() {
        return json = {
            'username': $('#username').val(),
            'password': $('#password').val(),
            'common_name': $('#commonName').val(),
            'family_name': $('#familyName').val(),
            'email': $('#email').val(),
            'phone_number': $('#phone').val(),
            'role': 'PASSENGER'
        };
    }

    return {
        register:register,
        properties:properties
    }
}();


jQuery(document).ready(function ($) {
    //Change links
    var link = RegistrationWrapper.properties.loginLink;
    if (!!Cookies.get(RegistrationWrapper.properties.userCookieName)) {
        link=RegistrationWrapper.properties.orderLink;
    }
    $('.taxi-options>a:first').attr("href", link);

    //TODO validation
    jQuery(document).ready(function ($) {
        $('#form_register').submit(function (event) {
            event.preventDefault();
            RegistrationWrapper.register();
        });
    });
});