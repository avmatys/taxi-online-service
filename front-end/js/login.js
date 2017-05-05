/**
 * Represent authorization functionality
 * @type {{login, properties}}
 * @constructor
 */
var AuthorizationWrapper = function () {

    var properties = {
        'loginUrl': 'http://localhost:8080/taxi-online-service/api/v1/auth/login/',

        'defaultContentType': 'application/json; charset=utf-8',
        'defaultDataType': 'JSON',
        'defaultTimeOut': 8000,

        'userCookieName': 'user-info',

        'indexLink': 'index.html',
        'orderLink': 'order.html',
        'loginLink': 'login.html',
        'userProfileLink': 'user_profile.html',
        'driverProfileLink': 'driver_profile.html',
        'authorizationError': 'Ошибка авторизации. Проверьте правильность введенных данных'
    };

    /**
     * Function for login feature
     */
    function login(){
        var accountInformation = JSON.stringify(createAuthorizationJSONData());
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(accountInformation, properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, "POST", function () {
        }, properties.loginUrl, authorizationDone, authorizationFail);
    }

    /**
     * Function for handling successful authorization
     * @param data - account information
     */
    function authorizationDone(data) {
        createUserCookie(data);
        var role = data.data.role;
        var redirectPage = (role === "PASSENGER") ? properties.userProfileLink : (role === "DRIVER") ? properties.driverProfileLink : properties.indexLink;
        window.location.href = redirectPage;
    }

    /**
     * Function for handling authorization failure
     * @param jqXHR
     */
    function authorizationFail(jqXHR) {
        console.log("Error: " + jqXHR.status + ", " + jqXHR.statusText);
        alert(properties.authorizationError);
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
     * @returns {{
     *             username: account login ,
     *             password: account password
     *          }}
     */
    function createAuthorizationJSONData() {
        return {
            'username': $('#username').val(),
            'password': $('#password').val()
        }
    }

    return {
        login:login,
        properties:properties
    }
}();


jQuery(document).ready(function ($) {

    //Create link according cookie data
    var link = AuthorizationWrapper.properties.loginLink;
    if (!!Cookies.get(AuthorizationWrapper.properties.userCookieName)) {
        link = AuthorizationWrapper.properties.orderLink;
    }
    $('.taxi-options>a:first').attr("href", link);

    //TODO validation
    $('#form_login').submit(function (event) {
        event.preventDefault();
        AuthorizationWrapper.login();
    });
});
