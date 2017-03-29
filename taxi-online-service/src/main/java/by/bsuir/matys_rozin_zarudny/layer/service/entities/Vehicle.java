package by.bsuir.matys_rozin_zarudny.layer.service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Abstract vehicle class
 *
 */
@Entity
@Table(name = "VEHICLE")
public class Vehicle implements Serializable {
    
    @Transient
    private static final long serialVersionUID = 7483538230786350749L;

    @Id
    @Column(name = "NUMBER_PLATE")
    private String numberplate;

    // number of seats including driver
    @Column(name = "NUMBER_SEATS")
    private int numberSeats;

    @JoinColumn(name = "VEHICLE_TYPE_ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private VehicleType vehicleType;

    public Vehicle() {
        // Empty constructor required by JPA.
    }

    public Vehicle(String numberplate, int numberSeats, VehicleType vehicleType) {
        this.numberplate = numberplate;
        this.numberSeats = numberSeats;
        this.vehicleType = vehicleType;
    }

    @JsonIgnore
    public double getCostPerKilometer() {
        return this.vehicleType.getCostPerKilometer();
    }
    
    public String getNumberplate() {
        return numberplate;
    }

    public void setNumberplate(String numberplate) {
        this.numberplate = numberplate;
    }

    public int getNumberSeats() {
        return numberSeats;
    }

    public void setNumberSeats(int numberSeats) {
        this.numberSeats = numberSeats;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
