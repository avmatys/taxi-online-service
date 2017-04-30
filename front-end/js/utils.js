//This script contains functions and utils for working with REST web-service

//----Functions for working with REST web-service----//

//Logout function
function logout() {
    if (!!Cookies.get('user-info')) {
        var user = JSON.parse(Cookies.get('user-info'));
        var data = {
            'username': user.username,
            'password': user.password
        };
        Cookies.remove('user-info');
        $.ajax({
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            timeout: 10000,
            type: 'POST',
            url: 'http://localhost:8080/taxi-online-service/api/v1/auth/logout/'

        }).done(function (data, textStatus, jqXHR) {
            //Everything is all right
            //Redirect
            window.location.href = 'index.html';
        }).fail(function (jqXHR, textStatus, errorThrown) {
            //TODO notification
            if (jqXHR.status === 400) {
                alert("Bad request");
            }
            if (jqXHR.status === 401) {
                alert("Unauthorized");
            }
        });
    }
    else {
        window.location.href = 'login.html';
    }
    //Need for <a href="" ...></a> to disable hyperlink
    return false;
}

// Function for taxi booking
// param - data is used for storing user login and passwor
function taxiBooking(data) {
    $.ajax({
        data: data,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        timeout: 10000,
        type: 'POST',
        beforeSend: function (xhr) {
            var user = JSON.parse(Cookies.get('user-info'));
            xhr.setRequestHeader("Authorization",
                "Basic " + btoa(user.username + ":" + user.password));
        },
        url: 'http://localhost:8080/taxi-online-service/api/v1/booking/'

    }).done(function (data, textStatus, jqXHR) {
        //TODO message
        window.location.href = "user_profile.html";

    }).fail(function (jqXHR, textStatus, errorThrown) {
        //TODO notification
        if (jqXHR.status === 400) {
            alert("Bad request");
        }
        if (jqXHR.status === 401) {
            alert("Unauthorized");
        }
        if (jqXHR.status === 404) {
            alert("Not Found");
        }
        if (jqXHR.status === 500) {
            alert("Internal server error");
        }
        $('.alert.danger').html('<span class="closebtn" onclick="toggle()">&times;</span><strong>Ошибочка!</strong> Нельзя заказать такси!').show();
    });
}
function beforeSend(xhr){
    var user = JSON.parse(Cookies.get('user-info'));
    xhr.setRequestHeader("Authorization",
        "Basic " + btoa(user.username + ":" + user.password));
}
// Function for find active booking for user
// param: data - user login
function findActiveBooking() {
    $.ajax({
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        timeout: 10000,
        type: 'GET',
        beforeSend: /*function (xhr) {
            var user = JSON.parse(Cookies.get('user-info'));
            xhr.setRequestHeader("Authorization",
                "Basic " + btoa(user.username + ":" + user.password));
        }*/ beforeSend,
        url: 'http://localhost:8080/taxi-online-service/api/v1/booking/active/'

    }).done(function (data, textStatus, jqXHR) {
        if (data.status === "0") {
            $('.options__current_order>p.order_info').html("Из " + data.data.route.start_address.address + " в " + data.data.route.end_address.address + ". " + data.data.cost + " руб.");
            $('.options__current_order>p.current_order_id').html(data.data.id);
        }

    }).fail(function (jqXHR, textStatus, errorThrown) {
        //TODO notification 'У вас нет заказов, сделайте заказ у нас' или вообще не надо
        if (jqXHR.status === 401) {
            alert("Unauthorized");
        }
        if (jqXHR.status === 404) {
            alert("Not Found");
        }
    });
}

// Function for find active booking for user
// param: id - id of the booking
function cancelCurrentOrder(id) {
    $.ajax({
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        timeout: 10000,
        type: 'PUT',
        beforeSend: function (xhr) {
            var user = JSON.parse(Cookies.get('user-info'));
            xhr.setRequestHeader("Authorization",
                "Basic " + btoa(user.username + ":" + user.password));
        },
        url: 'http://localhost:8080/taxi-online-service/api/v1/booking/' + id + '/cancel/'

    }).done(function (data, textStatus, jqXHR) {
        if (data.status === "0") {
            $('.options__current_order>p.order_info').html("Заказов не найдено");
            $('.options__current_order>p.current_order_id').html(" ");
        }

    }).fail(function (jqXHR, textStatus, errorThrown) {
        //TODO notification 'Упс, при отмене произошла ошибка'
        if (jqXHR.status === 400) {
            alert("Bad request");
        }
        if (jqXHR.status === 401) {
            alert("Unauthorized");
        }
        if (jqXHR.status === 404) {
            alert("Not Found");
        }
    });
}
//Define dictionary of booking states
var BOOKING_STATE_DICTIONARY = {
    'AWAITING_TAXI':'Ожидает такси',
    'CANCELED_BOOKING': 'Заказ отменен',
    'COMPLETED_BOOKING':'Заказ завершен',
    'PASSENGER_PICKED_UP':'Посадка выполнена',
    'TAXI_DISPATCHED': 'Такси выехало'
}
// Function for finding booking history
function findBookingHistory() {
    $.ajax({
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        timeout: 10000,
        type: 'GET',
        beforeSend: function (xhr) {
            var user = JSON.parse(Cookies.get('user-info'));
            xhr.setRequestHeader("Authorization",
                "Basic " + btoa(user.username + ":" + user.password));
        },
        url: 'http://localhost:8080/taxi-online-service/api/v1/booking/'

    }).done(function (data, textStatus, jqXHR) {
        //Сlear table
        $('#history_table > tbody > tr').remove();
        if(JSON.parse(Cookies.get('user-info')).role === "PASSENGER") {
            var orders = data.data;
            for (var i = 0; i < orders.length; i++) {
                $('#history_table > tbody:last-child').append("<tr><td>" + BOOKING_STATE_DICTIONARY[orders[i].state] + "</td><td>"
                    + new Date(orders[i].timestamp).toLocaleDateString() + "</td><td>"
                    + orders[i].route.start_address.address + "</td><td>"
                    + orders[i].route.end_address.address + "</td><td>"
                    + orders[i].route.distance / 1000.0 + " км." + "</td><td>"
                    + orders[i].cost + "руб." + "</td></tr>");
            }

        }
        else{
            var orders = data.data;
            for (var i = 0; i < orders.length; i++) {
                var button = (orders[i].state==='TAXI_DISPATCHED')?generatePickUpButtonIntoTable(orders[i].id):(orders[i].state==='PASSENGER_PICKED_UP')?generateDropOffButtonIntoTable(orders[i].id):"";

                $('#history_table > tbody:last-child').append("<tr><td>" + BOOKING_STATE_DICTIONARY[orders[i].state] + "</td><td>"
                    + new Date(orders[i].timestamp).toLocaleDateString() + " " + new Date(orders[i].timestamp).toLocaleTimeString() + "</td><td>"
                    + orders[i].route.start_address.address + "</td><td>"
                    + orders[i].route.end_address.address + "</td><td>"
                    + orders[i].route.distance / 1000.0 + " км." + "</td><td>"
                    + orders[i].cost + "руб. </td>" + button +"</tr>");
            }
        }


    }).fail(function (jqXHR, textStatus, errorThrown) {
        //TODO notification 'Упс, загрузке произошла ошибка'
        if (jqXHR.status === 401) {
            alert("Unauthorized");
        }
    });
    //Need for <a href="" ...></a> to disable hyperlink
    //return false;
}

// Function for finding booking history
function generateReport() {
    $.ajax({
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        timeout: 10000,
        type: 'GET',
        beforeSend: function (xhr) {
            var user = JSON.parse(Cookies.get('user-info'));
            xhr.setRequestHeader("Authorization",
                "Basic " + btoa(user.username + ":" + user.password));
        },
        url: 'http://localhost:8080/taxi-online-service/api/v1/booking/'

    }).done(function (data, textStatus, jqXHR) {
        //Сreate report
        var docDefinition = {
            header: function(currentPage, pageCount) {
                return { text: 'Отчет заказов такси для пользователя ' + JSON.parse(Cookies.get('user-info')).family_name + " " + JSON.parse(Cookies.get('user-info')).common_name, style: 'header' };
            },
            footer: function(currentPage, pageCount) { return {text: currentPage.toString() + ' из ' + pageCount, style: 'footer'};
            },

            pageSize:'A4',
            pageOrientation:'portrait',
            pageMargins:[50,60,30,30],

            content: [
                {
                    table: {
                        headerRows: 1,
                        widths: [ "11%", "13%", "27%", "27%", "8%", "14%"],
                        body: createReportData(data)
                    }
                }
            ],
            styles: {
                header: {
                    fontSize: 18,
                    bold: true,
                    italic: true,
                    alignment:'center',
                    margin: [0, 20, 0,20]
                },
                footer: {
                    fontSize: 12,
                    italic: true,
                    alignment: 'center'
                }
            }
        };
        // Download the PDF
        pdfMake.createPdf(docDefinition).download(JSON.parse(Cookies.get('user-info')).username+'_'+'taxi_report.pdf');

    }).fail(function (jqXHR, textStatus, errorThrown) {
        //TODO notification 'Упс, загрузке произошла ошибка'
        if (jqXHR.status === 401) {
            alert("Unauthorized");
        }
    });
    //Need for <a href="" ...></a> to disable hyperlink
    //return false;
}

// Function for finding awaiting taxi bookings
function findAwaitingTaxiBookings() {
    $.ajax({
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        timeout: 10000,
        type: 'GET',
        beforeSend: function (xhr) {
            var user = JSON.parse(Cookies.get('user-info'));
            xhr.setRequestHeader("Authorization",
                "Basic " + btoa(user.username + ":" + user.password));
        },
        url: 'http://localhost:8080/taxi-online-service/api/v1/booking/awaiting/'

    }).done(function (data, textStatus, jqXHR) {
        //Сlear table
        $('#awaiting_taxi_table > tbody > tr').remove();
        var orders = data.data;
        for(var i = 0 ; i < orders.length; i++) {
            $('#awaiting_taxi_table > tbody:last-child').append("<tr><td>" + BOOKING_STATE_DICTIONARY[orders[i].state] + "</td><td>"
                + new Date(orders[i].timestamp).toLocaleTimeString() + "</td><td>"
                + orders[i].route.start_address.address + "</td><td>"
                + orders[i].route.end_address.address + "</td><td>"
                + orders[i].route.distance/1000.0 + " км." + "</td><td>"
                + orders[i].cost + "руб.</td>"+generateButtonIntoTable(orders[i].id) +"</tr>"

                );
        }



    }).fail(function (jqXHR, textStatus, errorThrown) {
        //TODO notification 'Упс, загрузке произошла ошибка'
        if (jqXHR.status === 401) {

        }
    });
    //Need for <a href="" ...></a> to disable hyperlink
    //return false;
}

// Function for accepting taxi
// param: id - id of booking
function acceptBooking(id) {
    $.ajax({
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        type: 'PUT',
        beforeSend: function (xhr) {
            var user = JSON.parse(Cookies.get('user-info'));
            xhr.setRequestHeader("Authorization",
                "Basic " + btoa(user.username + ":" + user.password));
        },
        url: 'http://localhost:8080/taxi-online-service/api/v1/booking/'+ id + '/accept/'

    }).done(function (data, textStatus, jqXHR) {
        alert("done")
    }).fail(function (jqXHR, textStatus, errorThrown) {
       alert("fail")
    });
    //Need for <a href="" ...></a> to disable hyperlink
    //return false;
}

// Function for pick up passenger
// param: id - id of booking
function pickUpPassengerTaxi(id) {
    $.ajax({
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        type: 'PUT',
        beforeSend: function (xhr) {
            var user = JSON.parse(Cookies.get('user-info'));
            xhr.setRequestHeader("Authorization",
                "Basic " + btoa(user.username + ":" + user.password));
        },
        url: 'http://localhost:8080/taxi-online-service/api/v1/booking/'+ id + '/pickuppassenger/'

    }).done(function (data, textStatus, jqXHR) {
        alert("done")
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert("fail")
    });
    //Need for <a href="" ...></a> to disable hyperlink
    //return false;
}

// Function for pick up passenger
// param: id - id of booking
function dropOffPassengerTaxi(id) {
    $.ajax({
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        type: 'PUT',
        beforeSend: function (xhr) {
            var user = JSON.parse(Cookies.get('user-info'));
            xhr.setRequestHeader("Authorization",
                "Basic " + btoa(user.username + ":" + user.password));
        },
        url: 'http://localhost:8080/taxi-online-service/api/v1/booking/'+ id + '/dropoffpassenger/'

    }).done(function (data, textStatus, jqXHR) {
        alert("done")
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert("fail")
    });
    //Need for <a href="" ...></a> to disable hyperlink
    //return false;
}


//-----Special utils for REST client-----//

//Util which include an authorization header in request
// param:  xhr - XMLHttpRequest
//		   data - JSON account data (get from cookie)
function createAuthorizationHeader(xhr, data) {
    xhr.setRequestHeader("Authorization",
        "Basic " + btoa(data.username + ":" + data.password));
}



// Util for creating JSON booking data
function createTaxiBookingJSONData() {
    var json = getRouteInformaton();
    json.passenger_username = JSON.parse(Cookies.get('user-info')).username;
    json.number_passengers = $('#numberPassengers').val();
    return json;
}



//Util for calculation cost of trip
// param: distance - distance of a route in km
//        duration - approximate time im minutes
function calculateTripCost(distance) {

    //Define variables
    var pickUpCost = 3.5;
    var kilometerCost = 0.7;

    return distance * kilometerCost + pickUpCost;
}

//Util for creating data for report
// param: data - input data from server
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

//Function for generationg button
// param: id - id of the booking awaiting taxi
function generateButtonIntoTable(id){
    return '<td><button onclick="acceptTaxi()" id="' + id + '">Принять заказ</button></td>'
}

//Function for generationg button
// param: id - id of the booking awaiting taxi
function generatePickUpButtonIntoTable(id){
    return '<td><button onclick="pickUpPassenger()" id="pickup' + id + '">Посадка пассажира</button></td>'
}

//Function for generationg button
// param: id - id of the booking awaiting taxi
function generateDropOffButtonIntoTable(id){
    return '<td><button onclick="dropOffPassenger()" id="dropoff' + id + '">Высадка пассажира</button></td>'
}

//Function for accepting booking
// param: e - event onclick href
function acceptTaxi(e){
    e = e || window.event;
    e = e.target || e.srcElement;
    if (e.nodeName === 'BUTTON'){
        acceptBooking(e.id);
    }
}

//Function for accepting booking
// param: e - event onclick href
function pickUpPassenger(e){
    e = e || window.event;
    e = e.target || e.srcElement;
    if (e.nodeName === 'BUTTON'){
        pickUpPassengerTaxi(parseInt(e.id.replace ( /[^\d.]/g, '' ),10));
    }
}
//Function for accepting booking
// param: e - event onclick href
function dropOffPassenger(e){
    e = e || window.event;
    e = e.target || e.srcElement;
    if (e.nodeName === 'BUTTON'){
        dropOffPassengerTaxi(parseInt(e.id.replace ( /[^\d.]/g, '' ),10));
    }
}