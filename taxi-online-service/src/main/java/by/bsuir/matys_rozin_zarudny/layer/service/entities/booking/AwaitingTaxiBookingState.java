package by.bsuir.matys_rozin_zarudny.layer.service.entities.booking;

import java.util.Date;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.Taxi;

/**
 * Represents the awaiting taxi booking state.
 *
 */
public class AwaitingTaxiBookingState implements BookingState {

    @Override
    public void cancelBooking(Booking booking) {
        booking.setState(
                Booking.getCancelledBookingState());
    }

    @Override
    public void cancelTaxi(Booking booking) {
        throw new IllegalStateException("Taxi not dispatched.");
    }

    @Override
    public void dispatchTaxi(Booking booking, Taxi taxi) {

        booking.setState(
                Booking.getTaxiDispatchedBookingState());

        if (!taxi.checkseatAvailability(booking.getNumberPassengers())) {
            throw new IllegalStateException("The taxi does not have enough seats.");
        }

        booking.setTaxi(taxi);
    }

    @Override
    public void dropOffPassenger(Booking booking, Date time) {
        throw new IllegalStateException("Passenger not picked up");
    }

    @Override
    public void pickupPassenger(Booking booking, Date time) {
        throw new IllegalStateException("Taxi not yet dispatched.");
    }

    @Override
    public String toString() {
        return "Awaiting taxi.";
    }
}
