//Script file for checking if user is authorized to provide some special futures for him
jQuery(document).ready(function ($) {
    if (!!Cookies.get('user-info')) {
        //Have cookie
        // Define page
        var userPage = (JSON.parse(Cookies.get('user-info')).role === 'PASSENGER' )? 'user_profile.html' : 'driver_profile.html';
        //Add link to a user page
        $('.auth>a:first').html('<i class="flaticon-avatar"></i>' + ' Личный кабинет').attr("href", userPage);
        $('.auth>a:last').html('<i class="flaticon-logout"></i>' + ' Выход').attr("href", " ").click(function(){
            var logoutWrapper = LogoutWrapper();
            logoutWrapper.logout();
            return false;
        });
        //Add link to order a taxi
        $('.btn__order').click(function(){
            redirectTo('order.html');
        });
        //Add link to a navbar
        $('.taxi-options>a:first').attr("href", "order.html");
    }
    else{
        //Don't have cookie
        //Add link to login a taxi
        $('.btn__order').click(function(){
            redirectTo('login.html');
        });
        //Add link to a navbar
        $('.taxi-options>a:first').attr("href", "login.html");
    }
});