/**
 * Represents user functionality
 * @type {{properties, findActiveBooking, cancelCurrentOrder, findBookingHistory, generateReport}}
 * @constructor
 */
var UserWrapper = function () {

    var properties = {
        'activeBookingUrl': 'http://localhost:8080/taxi-online-service/api/v1/booking/active/',
        'bookingRootUrl': 'http://localhost:8080/taxi-online-service/api/v1/booking/',
        'bookingCancelUrl': '/cancel/',
        'bookingHistoryUrl': 'http://localhost:8080/taxi-online-service/api/v1/booking/',

        'defaultContentType': 'application/json; charset=utf-8',
        'defaultDataType': 'JSON',
        'defaultTimeOut': 8000,
        'typePOST': 'POST',
        'typeGET': 'GET',
        'typePUT': 'PUT',

        'passenger': 'PASSENGER',
        'driver': 'DRIVER',

        'userCookieName': 'user-info',

        'loginLink': 'login.html',

        'Error_400': 'Ошибка. Неккоретный запрос',
        'Error_401': 'Ошибка. Пользователь не авторизован',
        'Error_404': 'Ошибка. Указанный ресурс не найден',

        'kilometer': 1000
    };

    /**
     * Dictionary for booking states
     * @type {{AWAITING_TAXI: string, CANCELED_BOOKING: string, COMPLETED_BOOKING: string, PASSENGER_PICKED_UP: string, TAXI_DISPATCHED: string}}
     */
    var BOOKING_STATE_DICTIONARY = {
        'AWAITING_TAXI': 'Ожидает такси',
        'CANCELED_BOOKING': 'Заказ отменен',
        'COMPLETED_BOOKING': 'Заказ завершен',
        'PASSENGER_PICKED_UP': 'Посадка выполнена',
        'TAXI_DISPATCHED': 'Такси выехало'
    }

    /**
     * Function for finding active booking for user
     */
    function findActiveBooking() {
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(null, properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typeGET, beforeSend, properties.activeBookingUrl, findActiveBookingDone, findActiveBookingFail);
    }

    /**
     * Event handler for successful finding active booking
     * @param data
     */
    function findActiveBookingDone(data) {
        if (data.status === "0") {
            $('.options__current_order>p.order_info').html("Из " + data.data.route.start_address.address + " в " + data.data.route.end_address.address + ". " + data.data.cost + " руб.");
            $('.options__current_order>p.current_order_id').html(data.data.id);
        }
    }

    /**
     * Event handler for unsuccessful finding active booking
     * @param jqXHR
     */
    function findActiveBookingFail(jqXHR) {
        if (jqXHR.status === 401) {
            alert(properties.Error_401);
        }
    }


    /**
     * Function for cancel current order
     * @param id
     */
    function cancelCurrentOrder(id) {
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typePUT, beforeSend, properties.bookingRootUrl + id + properties.bookingCancelUrl, cancelCurrentOrderDone, cancelCurrentOrderFail);
    }


    /**
     * Function for handling successful order cancel
     * @param data
     */
    function cancelCurrentOrderDone(data) {
        if (data.status === "0") {
            $('.options__current_order>p.order_info').html("Заказов не найдено");
            $('.options__current_order>p.current_order_id').html(" ");
        }
    }

    /**
     * Function for handling errors on canceling order
     * @param jqXHR
     */
    function cancelCurrentOrderFail(jqXHR) {
        if (jqXHR.status === 400) {
            alert(properties.Error_400);
        }
        if (jqXHR.status === 401) {
            alert(properties.Error_401);
        }
        if (jqXHR.status === 404) {
            alert(properties.Error_404);
        }
    }

    /**
     * Function for finding booking history
     */
    function findBookingHistory() {
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(null,properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typeGET, beforeSend, properties.bookingRootUrl, findBookingHistoryDone, findBookingHistoryFail);
    }

    /**
     * Function for handlind successful finding booking history
     * @param data
     */
    function findBookingHistoryDone(data){
        generateBookingHistoryTable(data);
    }

    /**
     * Function for handling error on finding booking history
     * @param jqXHR
     */
    function findBookingHistoryFail(jqXHR){
        if (jqXHR.status === 401) {
            alert(properties.Error_401);
        }
    }
    /**
     * Function for generating booking history table
     * @param data
     */
    function generateBookingHistoryTable(data){
        $('#history_table > tbody > tr').remove();
        var orders = data.data;

        var addOn = "";
        var role = Cookies.getJSON(properties.userCookieName).role;
        if( role === properties.driver ) {
            addOn = (orders[i].state === 'TAXI_DISPATCHED') ? generatePickUpButtonIntoTable(orders[i].id) : (orders[i].state === 'PASSENGER_PICKED_UP') ? generateDropOffButtonIntoTable(orders[i].id) : "";
        }

        for (var i = 0; i < orders.length; i++) {
            $('#history_table > tbody:last-child').append("<tr><td>" + BOOKING_STATE_DICTIONARY[orders[i].state] + "</td><td>"
                + new Date(orders[i].timestamp).toLocaleDateString() + " " + new Date(orders[i].timestamp).toLocaleTimeString() + "</td><td>"
                + orders[i].route.start_address.address + "</td><td>"
                + orders[i].route.end_address.address + "</td><td>"
                + orders[i].route.distance / properties.kilometer + " км." + "</td><td>"
                + orders[i].cost + "руб.</td>" + addOn + "</tr>");
        }
    }

    /**
     * Function for generating html code of pick up button
     * @param id
     * @returns {string}
     */
    function generatePickUpButtonIntoTable(id){
        return '<td><button onclick="pickUpPassenger()" id="pickup' + id + '">Посадка пассажира</button></td>'
    }

    /**
     * Function for generating html code of drop off button
     * @param id
     * @returns {string}
     */
    function generateDropOffButtonIntoTable(id){
        return '<td><button onclick="dropOffPassenger()" id="dropoff' + id + '">Высадка пассажира</button></td>'
    }

    /**
     * Function for generation report
     */
    function generateReport() {
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(null,properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typeGET, beforeSend, properties.bookingRootUrl, generateReportDone, findBookingHistoryFail);
    }


    /**
     * Function for generating report
     * @param data
     */
    function generateReportDone(data){
        var docDefinition = {
            header: function (currentPage, pageCount) {
                return {
                    text: 'Отчет заказов такси для пользователя ' + JSON.parse(Cookies.get('user-info')).family_name + " " + JSON.parse(Cookies.get('user-info')).common_name,
                    style: 'header'
                };
            },
            footer: function (currentPage, pageCount) {
                return {text: currentPage.toString() + ' из ' + pageCount, style: 'footer'};
            },

            pageSize: 'A4',
            pageOrientation: 'portrait',
            pageMargins: [50, 60, 30, 30],

            content: [
                {
                    table: {
                        headerRows: 1,
                        widths: ["11%", "13%", "27%", "27%", "8%", "14%"],
                        body: createReportData(data)
                    }
                }
            ],
            styles: {
                header: {
                    fontSize: 18,
                    bold: true,
                    italic: true,
                    alignment: 'center',
                    margin: [0, 20, 0, 20]
                },
                footer: {
                    fontSize: 12,
                    italic: true,
                    alignment: 'center'
                }
            }
        };
        // Download the PDF
        pdfMake.createPdf(docDefinition).download(Cookies.getJSON(properties.userCookieName).username + '_' + 'taxi_report.pdf');

    }

    /**
     * Function for creating report data
     * @param data
     * @returns {[*]}
     */
    function createReportData(data){
        var reportData = [
            [ 'Статус', 'Дата', 'Точка отправления', 'Точка назначения', 'Расст.', 'Стоимость' ],
        ];
        var orders = data.data;
        for(var i = 0 ; i < orders.length; i++) {
            reportData.push([ BOOKING_STATE_DICTIONARY[orders[i].state],
                new Date(orders[i].timestamp).toLocaleDateString(),
                orders[i].route.start_address.address,
                orders[i].route.end_address.address,
                orders[i].route.distance/1000.0 + " км.",
                orders[i].cost + "руб."
            ]);
        }
        return reportData;
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

    return {
        properties: properties,
        findActiveBooking: findActiveBooking,
        cancelCurrentOrder: cancelCurrentOrder,
        findBookingHistory:findBookingHistory,
        generateReport: generateReport
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
    if (!!Cookies.get(UserWrapper.properties.userCookieName)) {

        var codeWrapper = htmlCodeWrapper();

        var data = Cookies.getJSON(UserWrapper.properties.userCookieName);
        $('#name').text("Имя: " + data.family_name + " " + data.common_name);
        $('#login').text("Логин: " + data.username);
        $('#email').text("Электронная почта: " + data.email);
        $('#phone_number').text("Номер телефона: " + data.phone_number);

        //Load current booking
        UserWrapper.findActiveBooking();

        //Add event listener for canceling current booking
        $('#cancel_current_order').click(function () {
            if ($('.options__current_order>p.current_order_id').html() !== "") {
                UserWrapper.cancelCurrentOrder($('.options__current_order>p.current_order_id').html());
            }
        });
        //Add event listener to a table header (booking history)
        $('#history_link').click(function () {
            return UserWrapper.findBookingHistory();
        });
        //Add event listener to a table header (generate report)
        $('#order_link').click(function () {
            return UserWrapper.generateReport();
        });

        $('.auth>a:last').html(codeWrapper.code.exit).attr("href", " ").click(function () {
            var logoutWrapper = LogoutWrapper();
            logoutWrapper.logout();
            return false;
        });
    }
    else {
        window.location.href = UserWrapper.properties.loginLink;
    }
});