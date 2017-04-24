//Script is used for creating a booking DTO and sending to a server,checking auth and calculating a cost of trip
jQuery(document).ready(function ($) {
    if (!!Cookies.get('user-info')) {
        //Have cookie
        // Define page
        var userPage = Cookies.get('user-info').role === 'PASSENGER' ? 'user_profile.html' : 'driver_profile.html';
        //Add link to a user page
        $('.auth>a:first').html('<i class="flaticon-avatar"></i>' + ' Личный кабинет').attr("href", userPage);
        $('.auth>a:last').html('<i class="flaticon-logout"></i>' + ' Выход').attr("href", " ").click(function () {
            logout();
        });
        //Add link to a navbar
        $('.taxi-options>a:first').attr("href", "order.html");
    }
    else {
        redirectTo("login.html")
    }
});

