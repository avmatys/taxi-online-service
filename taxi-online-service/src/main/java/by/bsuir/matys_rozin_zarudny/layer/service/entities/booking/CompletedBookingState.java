package by.bsuir.matys_rozin_zarudny.layer.service.entities.booking;

import java.util.Date;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.Taxi;

/**
 * Represents completed booking state.
 *
 */
public class CompletedBookingState implements BookingState {

    private IllegalStateException e = new IllegalStateException("Booking completed.");

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
        return "Completed booking state.";
    }
}
