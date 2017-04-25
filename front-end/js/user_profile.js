//Script for adding functionality to user page
jQuery(document).ready(function ($) {
    if (!!Cookies.get('user-info')) {
        var data = JSON.parse(Cookies.get('user-info'));
        $('#name').text("Имя: "+data.family_name + " " + data.common_name);
        $('#login').text("Логин: "+data.username);
        $('#email').text("Электронная почта: "+data.email);
        $('#phone_number').text("Номер телефона: "+data.phone_number);

        //Load current booking
        findActiveBooking();

        //Add event listener for canceling current booking
        $('#cancel_current_order').click(function() {
            if( $('.options__current_order>p.current_order_id').html()!=="") {
                cancelCurrentOrder($('.options__current_order>p.current_order_id').html());
            }
            else{
                //TODO notification
                alert("У вас нет заказа");
            }
        });
        //Add event listener to a table header (booking history)
        $('#history_link').click(function(){
            return findBookingHistory();
        });

    }
    else{
        window.location.href = 'login.html';
    }
});