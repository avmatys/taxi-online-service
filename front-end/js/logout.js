/**
 * Represents logout functionality
 * @returns {{logout: logout}}
 * @constructor
 */
var LogoutWrapper = function(){

    var properties = {
        'logoutUrl': 'http://localhost:8080/taxi-online-service/api/v1/auth/logout/',

        'defaultContentType': 'application/json; charset=utf-8',
        'defaultDataType': 'JSON',
        'defaultTimeOut': 8000,
        'typePOST': 'POST',

        'userCookieName': 'user-info',

        'indexLink': 'index.html',

        'Error_400': 'Ошибка. Неккоретный запрос',
        'Error_401': 'Ошибка. Пользователь не авторизован',

    };

    /**
     * Function is used for logout client from system
     */
    function logout() {
        if (!!Cookies.get(properties.userCookieName)) {
            var user = Cookies.getJSON(properties.userCookieName);
            var data = {
                'username': user.username,
                'password': user.password
            };
            Cookies.remove(properties.userCookieName);
            var ajaxWrapper = AjaxWrapper();
            ajaxWrapper.ajaxRequest(JSON.stringify(data), properties.defaultDataType, properties.defaultContentType, properties.defaultTimeOut, properties.typePOST, function(){}, properties.logoutUrl, logoutDone, logoutFail);
        }
        else {
            window.location.href = properties.indexLink;
        }
    }

    /**
     * Function is used for handling successful logout
     */
    function logoutDone(){
        window.location.href = properties.indexLink;
    }
    /**
     * Function is used for handing errors on logout
     */
    function logoutFail(){
        if (jqXHR.status === 400) {
            alert(properties.Error_400);
        }
        if (jqXHR.status === 401) {
            alert(properties.Error_401);
        }
    }
    return{
        logout:logout
    }
}