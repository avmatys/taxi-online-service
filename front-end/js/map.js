var map;
var markers = [];
function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: 53.900, lng: 27.566},
		zoom: 12
	});
	map.addListener('click', function(e) {
		placeMarkerAndPanTo(e.latLng, map);
	});


var infoWindow = new google.maps.InfoWindow({map: map});

        // Try HTML5 geolocation.
        if (navigator.geolocation) {
        	navigator.geolocation.getCurrentPosition(function(position) {
        		var pos = {
        			lat: position.coords.latitude,
        			lng: position.coords.longitude
        		};

        		infoWindow.setPosition(pos);
        		infoWindow.setContent('Вы здесь!');
        		map.setCenter(pos);
        	}, function() {
        		handleLocationError(true, infoWindow, map.getCenter());
        	});
        } else {
          // Browser doesn't support Geolocation
          handleLocationError(false, infoWindow, map.getCenter());
        }
      }


      function handleLocationError(browserHasGeolocation, infoWindow, pos) {
      	infoWindow.setPosition(pos);
      	infoWindow.setContent(browserHasGeolocation ?
      		'Error: The Geolocation service failed.' :
      		'Error: Your browser doesn\'t support geolocation.');
      }
      function placeMarkerAndPanTo(latLng, map) {
      	var marker = new google.maps.Marker({
      		position: latLng,
      		map: map
      	});
      	markers.push(marker);
      	console.log(marker.getPosition());
      	//map.panTo(latLng);
      }

      function setMapOnAll(map) {
        for (var i = 0; i < markers.length; i++) {
          markers[i].setMap(map);
        }
      }

      function clearMarkers() {
        setMapOnAll(null);
      }


      function showMarkers() {
        setMapOnAll(map);
      }

    
      function deleteMarkers() {
        clearMarkers();
        markers = [];
      }