//Script for adding functionality to driver page
jQuery(document).ready(function ($) {
    if (!!Cookies.get('user-info')) {
        var data = JSON.parse(Cookies.get('user-info'));
        $('#name').text("Имя: "+data.family_name + " " + data.common_name);
        $('#login').text("Логин: "+data.username);
        $('#email').text("Электронная почта: "+data.email);
        $('#phone_number').text("Номер телефона: "+data.phone_number);
    }
    else{
        window.location.href = 'login.html';
    }

    //Add event listener to a table header (booking history)
    $('#history_link').click(function(){
        return findBookingHistory();
    });
    //Add event listener to a table header (generate report)
    $('#order_link').click(function(){
        return generateReport();
    });
    //Add event listener to a table header (generate report)
    $('#current_orders').click(function(){
        return findAwaitingTaxiBookings();
    });

});