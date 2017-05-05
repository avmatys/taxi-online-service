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

    var userWrapper = AccountWrapper();

    if (!Cookies.get(userWrapper.properties.userCookieName)) {
        window.location.href = userWrapper.properties.loginLink;
    } else {

        var codeWrapper = htmlCodeWrapper();

        var data = Cookies.getJSON(userWrapper.properties.userCookieName);
        $('#name').text("Имя: " + data.family_name + " " + data.common_name);
        $('#login').text("Логин: " + data.username);
        $('#email').text("Электронная почта: " + data.email);
        $('#phone_number').text("Номер телефона: " + data.phone_number);

        //Add event listener to a table header (booking history)
        $('#history_link').click(function () {
            return userWrapper.findBookingHistory();
        });
        //Add event listener to a table header (generate report)
        $('#order_link').click(function () {
            return userWrapper.generateReport();
        });
        //Add event listener to a table header (generate report)
        $('#current_orders').click(function () {
            return userWrapper.findAwaitingTaxiBookings();
        });
        $('.auth>a:last').html(codeWrapper.code.exit).attr("href", " ").click(function () {
            var logoutWrapper = LogoutWrapper();
            logoutWrapper.logout();
            return false;
        });


    }


});