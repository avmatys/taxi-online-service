package by.bsuir.matys_rozin_zarudny.layer.persistence.dto;

import java.io.Serializable;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.Location;

/**
 * Booking data transfer object to reduce data coupling on methods. 
 * @look http://martinfowler.com/eaaCatalog/dataTransferObject.html
 */
public class BookingDto implements Serializable {

    private long id;
    private String passengerUsername;
    private Location startLocation;
    private Location endLocation;
    private int numberPassengers;

  
    public Location getStartLocation() {
        return startLocation;
    }
  
    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public String getPassengerUsername() {
        return passengerUsername;
    }

    public void setPassengerUsername(String passengeUsername) {
        this.passengerUsername = passengeUsername;
    }
    
    public int getNumberPassengers() {
        return numberPassengers;
    }

    public void setNumberPassengers(int numberPassengers) {
        this.numberPassengers = numberPassengers;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
