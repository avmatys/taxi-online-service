/**
 * Represent taxi booking functionality
 * @type {{properties, bookTaxi}}
 * @constructor
 */
var BookingWrapper = function () {

    var properties = {
        'bookingUrl': 'http://localhost:8080/taxi-online-service/api/v1/booking/',

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

        'Error_400': 'Ошибка. Неккоретный запрос',
        'Error_401': 'Ошибка. Пользователь не авторизован',
        'Error_404': 'Ошибка. Запрашиваемый ресурс не найден',
        'Error_500': 'Ошибка на сервере. Повторите попытку позже',

        'passenger': 'PASSENGER'

    };

    /**
     * Function for booking a taxi
     */
    function bookTaxi() {
        var bookingDTO = JSON.stringify(createBookingJSONData());
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(bookingDTO, properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typePOST, beforeSend, properties.bookingUrl, bookingDone, bookingFail);
    }

    /**
     * Function for creating booking information
     * @returns BookingDTO
     */
    function createBookingJSONData() {
        var json = getRouteInformaton();
        json.passenger_username = Cookies.getJSON(properties.userCookieName).username;
        json.number_passengers = $('#numberPassengers').val();
        return json;
    }

    /**
     * Function for creating an authorization header
     * @param xhr
     */
    function beforeSend(xhr) {
        var user = Cookies.getJSON(properties.userCookieName);
        xhr.setRequestHeader("Authorization",
            "Basic " + btoa(user.username + ":" + user.password));
    }

    /**
     * Function for handling successful booking event
     * @param data - request from the server
     */
    function bookingDone(data) {
        window.location.href = properties.userProfileLink;
    }

    /**
     * Function for handling booking error event
     * @param jqXHR
     */
    function bookingFail(jqXHR) {
        console.log("Error: " + jqXHR.status + ", " + jqXHR.statusText);
        if (jqXHR.status === 400) {
            alert(properties.Error_400);
        } else if (jqXHR.status === 401) {
            alert(properties.Error_401);
        } else if (jqXHR.status === 404) {
            alert(properties.Error_404);
        } else if (jqXHR.status === 500) {
            alert(properties.Error_500);
        }
    }

    return {
        properties: properties,
        bookTaxi: bookTaxi
    }

}();
var htmlCodeWrapper = function () {
    var code = {
        'accountPage': '<i class="flaticon-avatar"></i>  Личный кабинет',
        'exit': '<i class="flaticon-logout"></i> Выход'
    }
    return {
        code: code
    }
}
jQuery(document).ready(function ($) {
    if (!!Cookies.get(BookingWrapper.properties.userCookieName)) {

        var userPage = (Cookies.getJSON(BookingWrapper.properties.userCookieName).role === BookingWrapper.properties.passenger) ? BookingWrapper.properties.userProfileLink : BookingWrapper.properties.driverProfileLink;

        var codeWrapper = htmlCodeWrapper();
        //Change links
        $('.auth>a:first').html(codeWrapper.code.accountPage).attr("href", userPage);
        $('.auth>a:last').html(codeWrapper.code.exit).attr("href", " ").click(function () {
            var logoutWrapper = LogoutWrapper();
            logoutWrapper.logout();
            return false;
        });
        //Add link to a navbar
        $('.taxi-options>a:first').attr("href", BookingWrapper.properties.orderLink);
    }
    else {
        window.location.href = BookingWrapper.properties.loginLink;
    }
    //TODO validation
    $('#form_order').submit(function (event) {
        event.preventDefault();
        BookingWrapper.bookTaxi();
    });

});

