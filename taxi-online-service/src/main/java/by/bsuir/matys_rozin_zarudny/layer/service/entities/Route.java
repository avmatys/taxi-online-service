package by.bsuir.matys_rozin_zarudny.layer.service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Represents a Taxi route.
 *
 */
@Entity
@Table(name = "ROUTE")
public class Route implements Serializable {
    
    @Transient
    private static final long serialVersionUID = -1765861964246272848L;
    
    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JoinColumn(name = 
    	    /**
    	     * Return time in minutes.
    	     *
    	     * @return time in minutes.
    	     */"START_LOCATION")
    @ManyToOne(cascade = CascadeType.ALL)
    private Address startAddress;

    @JoinColumn(name = "END_LOCATION")
    @ManyToOne(cascade = CascadeType.ALL)
    private Address endAddress;

    @Column(name = "DISTANCE")
    private double distance;

    @Column(name = "ESTIMATED_TRAVEL_TIME")
    private double estimateTravelTime;

    @Transient
    private List<Location> path;

    public Route() {
        // Empty constructor required by JPA.
    }

    public Route(Address startAddress, Address endAddress,
            double distance, List<Location> path, double estimateTravelTime) {

        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.distance = distance;
        this.estimateTravelTime = estimateTravelTime;
        this.path = path;
    }

    public Address getStartAddress() {
        return this.startAddress;
    }

    public void setStartLocation(Address startAddress) {
        this.startAddress = startAddress;
    }

    public Address getEndAddress() {
        return this.endAddress;
    }

    public void setEndAddress(Address endAddress) {
        this.endAddress = endAddress;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<Location> getPath() {
        return this.path;
    }

    public void setRoute(List<Location> path) {
        this.path = path;
    }

    public double getEstimateTravelTime() {
        return this.estimateTravelTime;
    }

    public void setEstimateTravelTime(double estimateTravelTime) {
        this.estimateTravelTime = estimateTravelTime;
    }

    @JsonIgnore
    public double getTimeInMinutes() {
        return this.estimateTravelTime / 60;
    }
}
