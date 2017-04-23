//Script is used for creating a booking DTO and sending to a server

//Util for calculation cost of trip
function calculateTripCost(distance, duration){

    var pickUpCost = 2.5;
    var kilometerCost = 0.45;
    var minuteCost = 0.07;

    var costDistance = distance * kilometerCost;
    var costDuration = duration * minuteCost;

    return costDistance + costDuration/10 + pickUpCost;
}