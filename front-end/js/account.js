/**
 * Represents user functionality
 * @returns {{properties: {activeBookingUrl: string, bookingRootUrl: string, bookingCancelUrl: string, bookingHistoryUrl: string, findAwatingTaxiUrl: string, acceptBookingUrl: string, pickUpUrl: string, dropOffUrl: string, defaultContentType: string, defaultDataType: string, defaultTimeOut: number, typePOST: string, typeGET: string, typePUT: string, passenger: string, driver: string, userCookieName: string, loginLink: string, Error_400: string, Error_401: string, Error_404: string, bookingAcceptedMessage: string, bookingAcceptingErrorMessage: string, kilometer: number}, findActiveBooking: findActiveBooking, cancelCurrentOrder: cancelCurrentOrder, findBookingHistory: findBookingHistory, generateReport: generateReport, findAwaitingTaxiBookings: findAwaitingTaxiBookings}}
 * @constructor
 */
var AccountWrapper = function () {

    var properties = {
        'activeBookingUrl': 'http://localhost:8080/taxi-online-service/api/v1/booking/active/',
        'bookingRootUrl': 'http://localhost:8080/taxi-online-service/api/v1/booking/',
        'bookingCancelUrl': '/cancel/',
        'bookingHistoryUrl': 'http://localhost:8080/taxi-online-service/api/v1/booking/',
        'findAwatingTaxiUrl': 'http://localhost:8080/taxi-online-service/api/v1/booking/awaiting/',
        'acceptBookingUrl': '/accept/',
        'pickUpUrl': '/pickuppassenger/',
        'dropOffUrl': '/dropoffpassenger/',

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

        'bookingAcceptedMessage': 'Операция завершена успешно',
        'bookingAcceptingErrorMessage': 'Во время выполнения операции произошла ошибка',

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
        ajaxWrapper.ajaxRequest(null,properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typePUT, beforeSend, properties.bookingRootUrl + id + properties.bookingCancelUrl, cancelCurrentOrderDone, cancelCurrentOrderFail);
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

        var flag = role === properties.driver? 1:0;

        for (var i = 0; i < orders.length; i++) {
           if(flag===1){
                addOn = (orders[i].state === 'TAXI_DISPATCHED') ? generatePickUpButtonIntoTable(orders[i].id) : (orders[i].state === 'PASSENGER_PICKED_UP') ? generateDropOffButtonIntoTable(orders[i].id) : "";
            }
            $('#history_table > tbody:last-child').append("<tr><td>" + BOOKING_STATE_DICTIONARY[orders[i].state] + "</td><td>"
                + new Date(orders[i].timestamp).toLocaleDateString() + " " + new Date(orders[i].timestamp).toLocaleTimeString() + "</td><td>"
                + orders[i].route.start_address.address + "</td><td>"
                + orders[i].route.end_address.address + "</td><td>"
                + orders[i].route.distance / properties.kilometer + " км." + "</td><td>"
                + orders[i].cost + "руб.</td>" + addOn + "</tr>");
        }
        $('.dropoff').click(function(e){
            dropOffPassenger(e);
        });
        $('.pickup').click(function(e){
            pickUpPassenger(e);
        })
    }

    /**
     * Function for generating html code of pick up button
     * @param id
     * @returns {string}
     */
    function generatePickUpButtonIntoTable(id){
        return "<td><button class = 'pickup' id=pickup" + id + ">Посадка пассажира</button></td>";
    }

    /**
     * Function for generating html code of drop off button
     * @param id
     * @returns {string}
     */
    function generateDropOffButtonIntoTable(id){
        return "<td><button class='dropoff' id=dropoff" + id + ">Высадка пассажира</button></td>";
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

    // Function for finding awaiting taxi bookings
    function findAwaitingTaxiBookings() {
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(null,properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typeGET, beforeSend, properties.findAwatingTaxiUrl, findAwaitingTaxiDone, findBookingHistoryFail);
    }

    /**
     * Function for generating table of awaiting taxis bookings
     * @param data
     */
    function findAwaitingTaxiDone(data){
        $('#awaiting_taxi_table > tbody > tr').remove();
        var orders = data.data;
        for(var i = 0 ; i < orders.length; i++) {
            $('#awaiting_taxi_table > tbody:last-child').append("<tr><td>" + BOOKING_STATE_DICTIONARY[orders[i].state] + "</td><td>"
                + new Date(orders[i].timestamp).toLocaleDateString() + new Date(orders[i].timestamp).toLocaleTimeString() + "</td><td>"
                + orders[i].route.start_address.address + "</td><td>"
                + orders[i].route.end_address.address + "</td><td>"
                + orders[i].route.distance/properties.kilometer + " км." + "</td><td>"
                + orders[i].cost + "руб.</td>"
                + "<td><button class='acceptTaxi' id="+ orders[i].id + ">Принять заказ</button></td>" +"</tr>"
            );
        }
        $('.acceptTaxi').click(function (e) {
            acceptTaxi(e);
        });
    }

    /**
     * Function for accepting taxi
     * Handles event onclick
     * @param e
     */
    function acceptTaxi(e){
        e = e || window.event;
        e = e.target || e.srcElement;
        if (e.nodeName === 'BUTTON'){
            acceptBooking(e.id);
        }
    }

    /**
     * Function for accepting current booking
     * @param id
     */
    function acceptBooking(id) {
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(null,properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typePUT, beforeSend, properties.bookingRootUrl + id + properties.acceptBookingUrl, acceptBookingDone, acceptBookingFail);
    }

    /**
     * Function for hanfling successful accepting
     * @param data
     */
    function acceptBookingDone(data){
        alert(properties.bookingAcceptedMessage);
    }

    /**
     * Function for handling errors
     * @param jqXHR
     */
    function acceptBookingFail(jqXHR){
        alert(properties.bookingAcceptingErrorMessage);
    }

    /**
     * Function for pick up passenger
     * @param id
     */
    function pickUpPassengerTaxi(id) {
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(null,properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typePUT, beforeSend, properties.bookingRootUrl + id + properties.pickUpUrl, acceptBookingDone, acceptBookingFail);
    }

    /**
     * Function for drop off passenger
     * @param id
     */
    function dropOffPassengerTaxi(id) {
        var ajaxWrapper = AjaxWrapper();
        ajaxWrapper.ajaxRequest(null,properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typePUT, beforeSend, properties.bookingRootUrl + id + properties.dropOffUrl, acceptBookingDone, acceptBookingFail);
    }


    /**
     * Function for handling onclick event for pick up passenger
     * @param e
     */
    function pickUpPassenger(e){
        e = e || window.event;
        e = e.target || e.srcElement;
        if (e.nodeName === 'BUTTON'){
            pickUpPassengerTaxi(parseInt(e.id.replace ( /[^\d.]/g, '' ),10));
        }
    }

    /**
     * Function for handling onclick event for drop off passenger
     * @param e
     */
    function dropOffPassenger(e){
        e = e || window.event;
        e = e.target || e.srcElement;
        if (e.nodeName === 'BUTTON'){
            dropOffPassengerTaxi(parseInt(e.id.replace ( /[^\d.]/g, '' ),10));
        }
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
        generateReport: generateReport,
        findAwaitingTaxiBookings: findAwaitingTaxiBookings,
        acceptTaxi: acceptTaxi
    }
};