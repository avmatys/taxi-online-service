//This script contains functions and utils for working with REST web-service

//----Functions for working with REST web-service----//







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
