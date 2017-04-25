//Script for working with google maps api

//Define variables
var map;
var markers = [];
var geocoder;
var directionsDisplay;
var directionsService;

//Initialize map
function initMap() {

    map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 53.900, lng: 27.566},
        zoom: 12
    });
    map.addListener('click', function (e) {
        placeMarkerAndPanTo(e.latLng, map);
    });

    var infoWindow = new google.maps.InfoWindow({map: map});

    // Try HTML5 geolocation.
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            var pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            infoWindow.setPosition(pos);
            infoWindow.setContent('Вы здесь!');
            map.setCenter(pos);
        }, function () {
            handleLocationError(true, infoWindow, map.getCenter());
        });
    } else {
        // Browser doesn't support Geolocation
        handleLocationError(false, infoWindow, map.getCenter());
    }
    //Create geocoder
    //This tool is used for decoding longitude and latitude into adress on map
    geocoder = new google.maps.Geocoder;

    //Create utils for route planning
    directionsService = new google.maps.DirectionsService;
    directionsDisplay = new google.maps.DirectionsRenderer({
        suppressMarkers: true
    });
    directionsDisplay.setMap(map);
}

//Error handler
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
        'Error: The Geolocation service failed.' :
        'Error: Your browser doesn\'t support geolocation.');
}
//Function for creating markers
//Also call a method for route creation
function placeMarkerAndPanTo(latLng, map) {

    if (markers.length >= 2) {
        $('.alert.warning').html('<span class="closebtn" onclick="toggle()">&times;</span><strong>Oops!</strong> Путь не может содержать более 2 точек').toggle();

    } else {
        var marker = new google.maps.Marker({
            position: latLng,
            map: map
        });
        markers.push(marker);
        geocodeLatLng();
        if (markers.length === 2) {
            createRoute();
            $('.order__results').fadeIn();
        }
    }
}

//Function for setting markers
function setMapOnAll(map) {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

//Function for clearing markers
function clearMarkers() {
    setMapOnAll(null);
}

//Function for showing markers
function showMarkers() {
    setMapOnAll(map);
}

//Function for deleting markers, route and start and end points
function deleteMarkersRouteInfo() {
    clearMarkers();
    markers = [];

    document.getElementById('startLocation').value = "";
    document.getElementById('endLocation').value = "";

    directionsDisplay.setDirections({routes: []});
    $('.order__results').fadeOut();
}

//Function for getting address from longitude and latitude
function geocodeLatLng() {
    var marker;

    if (markers.length === 1) {
        marker = markers[0];
    }
    if (markers.length === 2) {
        marker = markers[1];
    }
    //Create address according to latLng
    geocoder.geocode({'location': marker.position}, function (results, status) {

        if (status === 'OK') {
            if (results[0]) {
                //Write result
                if (markers.length === 1) {
                    document.getElementById('startLocation').value = results[0].formatted_address;
                }
                if (markers.length === 2) {
                    document.getElementById('endLocation').value = results[0].formatted_address;
                }
            } else {
                $('.alert.warning').html('<span class="closebtn"  onclick="toggle()">&times;</span><strong>Oops!</strong> Место не найдено').show();
            }
        } else {
            //TO-DO Create beautiful notification
            $('.alert.danger').html('<span class="closebtn"  onclick="toggle()">&times;</span><strong>Oops!</strong> Ошибка геокодера: ' + status).show();
        }
    });
}


//Util for creating a route from 2 points
function createRoute() {

    var start = new google.maps.LatLng(markers[0].position.lat(), markers[0].position.lng());
    var end = new google.maps.LatLng(markers[1].position.lat(), markers[1].position.lng());
    calculateRoute(start, end);

}

//Variables
var KILOMETER = 1000;
var MINUTE = 60;

//Function for calculating and displaying route and route info
function calculateRoute(start, end) {

    var request = {
        origin: start,
        destination: end,
        travelMode: 'DRIVING'
    };
    directionsService.route(request, function (response, status) {
        if (status === 'OK') {
            directionsDisplay.setDirections(response);

            //Calculate total duration and distance
            var totalDistance = 0.0;
            var totalDuration = 0.0;
            var legs = response.routes[0].legs;
            for (var i = 0; i < legs.length; i++) {
                totalDistance += legs[i].distance.value;
                totalDuration += legs[i].duration.value;
            }
            totalDistance = totalDistance / KILOMETER;
            totalDuration = totalDuration / MINUTE;

            showRouteInfo(totalDistance, totalDuration, calculateTripCost(totalDistance));
        }
    });
}

//Function for printing route information
function showRouteInfo(totalDistance, totalDuration, totalCost) {

    $('#totalDistance').find('span').text(Math.round(totalDistance) + " км.");
    $('#totalDuration').find('span').text(Math.round(totalDuration) + " мин.");
    $('#totalCost').find('span').text(Math.round(totalCost) + " руб.");
}

//Function for getting route information
function getRouteInformaton(){
    if(markers.length==2){
        var route = {
            'start_location':{
                'latitude': markers[0].position.lat(),
                'longitude': markers[0].position.lng()
            },
            'end_location':{
                'latitude': markers[1].position.lat(),
                'longitude': markers[1].position.lng()
            }
        }
        return route;
    }
}