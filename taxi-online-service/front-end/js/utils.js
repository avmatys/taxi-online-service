//This script contains functions and utils for working with REST web-service

//----Functions for working with REST web-service----//

// Function for authorization
// param - data is used for storing user login and password
function authorization(authData){
    $.ajax({
        data: authData,
	    dataType: 'json',
 		contentType: "application/json; charset=utf-8",
        timeout: 10000,
        type: 'POST',
        url: 'http://localhost:8080/taxi-online-service/api/v1/auth/login/'
 
    }).done(function(data, textStatus, jqXHR) {
    	//Everything is all right. Need to save the account data in order to provide basic authorization functions
        data.data.password = authData.password;
      	Cookies.set('user-info', JSON.stringify(data.data));
        //TO-DO Redirect to page own page (passanger or driver)
        window.location.href = "order.html"
 
    }).fail(function(jqXHR, textStatus, errorThrown) {
    	//TO-DO   Add some special futures here
        alert('Некорректный логин или пароль!');
    });	
}

//-----Special utils for REST client-----//

//Util which include an authorization header in request
// param:  xhr - XMLHttpRequest
//		   data - JSON account data (get from cookie)
function makeAuthorizationHeader(xhr, data) {
    xhr.setRequestHeader("Authorization", 
        "Basic " + btoa(data.username + ":" + data.password));
}

//Util for creating JSON login and password data
function createAuthorizationJSONData(){
	var json = {
		'username': $('#username').val(), 
		'password': $('#password').val()
	}; 
	return JSON.stringify(json);
}
