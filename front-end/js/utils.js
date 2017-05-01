//This script contains functions and utils for working with REST web-service

//----Functions for working with REST web-service----//






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


//Util for calculation cost of trip
// param: distance - distance of a route in km
//        duration - approximate time im minutes
function calculateTripCost(distance) {

    //Define variables
    var pickUpCost = 3.5;
    var kilometerCost = 0.7;

    return distance * kilometerCost + pickUpCost;
}



//Function for generationg button
// param: id - id of the booking awaiting taxi
function generateButtonIntoTable(id){
    return '<td><button onclick="acceptTaxi()" id="' + id + '">Принять заказ</button></td>'
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