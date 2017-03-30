package by.bsuir.matys_rozin_zarudny.layer.service.entities.booking;

import java.util.Date;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.Taxi;

/**
 * Represents the canceled taxi booking state.
 *
 */
public class CancelledBookingState implements BookingState {

    private IllegalStateException e = new IllegalStateException("Booking already canceled.");

    @Override
    public void cancelBooking(Booking booking) {
        throw e;
    }

    @Override
    public void cancelTaxi(Booking booking) {
        throw e;
    }

    @Override
    public void dispatchTaxi(Booking booking, Taxi taxi) {
        throw e;
    }

    @Override
    public void dropOffPassenger(Booking booking, Date time) {
        throw e;
    }

    @Override
    public void pickupPassenger(Booking booking, Date time) {
        throw e;
    }

    @Override
    public String toString() {
        return "Booking canceled state.";
    }
}
