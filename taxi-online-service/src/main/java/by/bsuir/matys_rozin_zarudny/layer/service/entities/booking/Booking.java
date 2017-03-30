package by.bsuir.matys_rozin_zarudny.layer.service.entities.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import by.bsuir.matys_rozin_zarudny.layer.persistence.data.mappers.booking.JpaBookingStateDataConverter;
import by.bsuir.matys_rozin_zarudny.layer.persistence.data.mappers.booking.JsonBookingStateDataConverter;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Route;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.Taxi;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Represents Taxi booking.
 *
 */
@Entity
@Table(name = "BOOKING")
@NamedQueries({
    @NamedQuery(
            name = "Booking.findBookingsInState",
            query = "SELECT b FROM Booking b WHERE b.state = :state"
    ),
    @NamedQuery(
            name = "Booking.findBookingsForPassenger",
            query = "SELECT b FROM Booking b WHERE b.passenger.username = :username"
    ),
    @NamedQuery(
            name = "Booking.findBookingsForDriver",
            query = "SELECT b FROM Booking b WHERE b.taxi.account.username = :username"
    ),
    @NamedQuery(
            name = "Booking.findBookingsforUserInState",
            query = "SELECT b FROM Booking b WHERE b.passenger.username = :username AND NOT (b.state = :state) AND NOT (b.state = :state2)"
    )
})
public class Booking implements Serializable {

    @Transient
    private static final long serialVersionUID = -1373406783231928690L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "NUMBER_PASSENGERS")
    private int numberPassengers;

    @Column(name = "COST")
    private double cost;

    @ManyToOne
    @JoinColumn(name = "PASSENGER_USERNAME")
    private Account passenger;

    @ManyToOne
    @JoinColumn(name = "TAXI_ID")
    private Taxi taxi;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROUTE_ID")
    private Route route;

    @Column(name = "BOOKING_STATE")
    @Convert(converter = JpaBookingStateDataConverter.class)
    private BookingState state;

    // booking states
    @JsonIgnore
    @Transient
    private static BookingState awaitingTaxiBookingState = new AwaitingTaxiBookingState();
    @JsonIgnore
    @Transient
    private static BookingState cancelledBookingState = new CancelledBookingState();
    @JsonIgnore
    @Transient
    private static BookingState taxiDispatchedBookingState = new TaxiDispatchedBookingState();
    @JsonIgnore
    @Transient
    private static BookingState passengerPickedUpBookingState = new PassengerPickedUpBookingState();
    @JsonIgnore
    @Transient
    private static BookingState completedTaxiBookingState = new CompletedBookingState();

    public Booking() {
        // Empty constructor required by JPA.
    }

    public Booking(Account passenger, Route route, int numberPassengers) {
        this.passenger = passenger;
        this.route = route;
        this.numberPassengers = numberPassengers;
        this.timestamp = new Date();
        this.state = Booking.getAwaitingTaxiBookingState();
        this.cost = this.estimateCost(0.3);
    }

    public Booking(Account passenger, Route route, int numberPassengers, Taxi taxi) {
        this.passenger = passenger;
        this.route = route;
        this.numberPassengers = numberPassengers;
        this.timestamp = new Date();
        this.taxi = taxi;
        this.state = Booking.getTaxiDispatchedBookingState();
        this.cost = this.calculateCost();
    }

    public void cancelBooking() {
        try {
            this.state.cancelBooking(this);
        } catch (IllegalStateException ex) {
            throw ex;
        }
    }

    public void cancelTaxi() {

        try {
            this.state.cancelTaxi(this);
        } catch (IllegalStateException ex) {
            throw ex;
        }
    }

    public void dispatchTaxi(Taxi taxi) {

        try {
            this.state.dispatchTaxi(this, taxi);
        } catch (IllegalStateException ex) {
            throw ex;
        }
    }

    public void dropOffPassenger(Date time) {

        try {
            this.state.dropOffPassenger(this, time);
        } catch (IllegalStateException ex) {
            throw ex;
        }
    }

    public void pickupPassenger(Date time) {
        try {
            this.state.pickupPassenger(this, time);
        } catch (IllegalStateException ex) {
            throw ex;
        }
    }


    public final double calculateCost() {
        return this.roundToTwoDecimalPlaces(this.estimateCost(this.taxi.getCostPerKilometer()));
    }

    /**
     * Estimate cost for taxi using set
	 *
     */
    public final double estimateCost(double scalar) {
        return this.roundToTwoDecimalPlaces(scalar * this.route.getDistance() * this.route.getTimeInMinutes());
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getNumberPassengers() {
        return numberPassengers;
    }

    public void setNumberPassengers(int numberPassengers) {
        this.numberPassengers = numberPassengers;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
        this.cost = this.calculateCost();
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Account getPassenger() {
        return passenger;
    }

    public void setPassenger(Account passenger) {
        this.passenger = passenger;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonSerialize(using = JsonBookingStateDataConverter.class)
    public BookingState getState() {
        return state;
    }
  
    @JsonIgnore
    public boolean isActive(){
        return !(this.getState() instanceof CompletedBookingState 
                || this.getState() instanceof CancelledBookingState);
    }

    /**
     * Round to two decimal places.
     *
     */
    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    
    public void setState(BookingState state) {
        this.state = state;
    }

    public static BookingState getCancelledBookingState() {
        return Booking.cancelledBookingState;
    }

    public static final BookingState getAwaitingTaxiBookingState() {
        return Booking.awaitingTaxiBookingState;
    }

    public static final BookingState getTaxiDispatchedBookingState() {
        return Booking.taxiDispatchedBookingState;
    }

    public static BookingState getPassengerPickedUpBookingState() {
        return Booking.passengerPickedUpBookingState;
    }

    public static BookingState getCompletedTaxiBookingState() {
        return Booking.completedTaxiBookingState;
    }
}
