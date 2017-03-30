package by.bsuir.matys_rozin_zarudny.layer.service.entities.events;

import java.io.Serializable;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.Location;

/**
 * Represents a taxi location change event.
 *
 */
public class LocationEvent extends Event implements Serializable {

    private long taxiId;
    private Location location;

    public LocationEvent() {
        // Empty constructor required by JPA.
    }

    public LocationEvent(long taxiId, Location location) {
        super();
        this.location = location;
        this.taxiId = taxiId;
    }
    
    public LocationEvent(long taxiId, Location location, long timestamp) {
        super(timestamp);
        this.location = location;
        this.taxiId = taxiId;
    }

    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }

    public long getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(long taxiId) {
        this.taxiId = taxiId;
    }
}
