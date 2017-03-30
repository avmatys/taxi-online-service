package by.bsuir.matys_rozin_zarudny.layer.service.entities.booking;

import java.util.Date;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.Taxi;

import java.io.Serializable;

/**
 * Interface representing a booking state.
 *
 */
public interface BookingState extends Serializable {

    public void cancelBooking(Booking booking);

    public void cancelTaxi(Booking booking);

    public void dispatchTaxi(Booking booking, Taxi taxi);

    /**
     * Drop off passenger.
     * @throws IllegalArgumentException Invalid time. Start time cannot be
     * before booking creation timestamp.
     */
    public void dropOffPassenger(Booking booking, Date time);

    /**
     * Pickup passenger.
     * @throws IllegalArgumentException Invalid time. Start time cannot be
     * before booking creation timestamp.
     */
    public void pickupPassenger(Booking booking, Date time);
}
