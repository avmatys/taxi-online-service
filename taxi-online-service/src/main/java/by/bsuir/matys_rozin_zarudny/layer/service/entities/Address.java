package by.bsuir.matys_rozin_zarudny.layer.service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
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
 * Represent an address. Address has a location and address name.
 *
 */
@Entity
@Table(name = "ADDRESS")
public class Address implements Serializable {
    
    @Transient
    private static final long serialVersionUID = 1833058791696309083L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LOCATION_ID")
    private Location location;

    @Column(name = "STREET_ADDRESS")
    private String streetAddress;

    public Address() {
        // Needed by JPA.
    }


    public Address(String streetAddress, Location location) {
        this.streetAddress = streetAddress;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getAddress() {
        return streetAddress;
    }

    public void setAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
