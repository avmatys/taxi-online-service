package by.bsuir.matys_rozin_zarudny.layer.service.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Represent a vehicle type.
 *
 */
@Entity
@Table(name = "VEHICLE_TYPE")
public class VehicleType implements Serializable {
    
    @Transient
    private static final long serialVersionUID = 6556730065381319372L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MANUFACTURER")
    private String manufacturer;

    @Column(name = "model")
    private String model;

    @Column(name = "COST_PER_KILOMETER")
    private double costPerKilometer;

    public VehicleType() {
        // Empty constructor required by JPA.
    }

    public VehicleType(String name, String manufacturer, String model, double costPerKilometer) {
        this.name = name;
        this.costPerKilometer = costPerKilometer;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCostPerKilometer() {
        return this.costPerKilometer;
    }

   
    public void setCostPerKilometer(int costPerKilometer) {
        this.costPerKilometer = costPerKilometer;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
