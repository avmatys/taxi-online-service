package by.bsuir.matys_rozin_zarudny.layer.persistence.data.mappers.booking;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.AwaitingTaxiBookingState;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.BookingState;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.BookingStates;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.CancelledBookingState;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.CompletedBookingState;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.PassengerPickedUpBookingState;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.TaxiDispatchedBookingState;

/**
 * Mapping between database entity and state object.
 */
@Converter
public class JpaBookingStateDataConverter implements AttributeConverter<BookingState, String> {

    @Override
    public String convertToDatabaseColumn(BookingState attribute) {

        // use java reflection here instead?
        if (attribute instanceof AwaitingTaxiBookingState) {
            return BookingStates.AWAITING_TAXI.toString();

        } else if (attribute instanceof CancelledBookingState) {
            return BookingStates.CANCELED_BOOKING.toString();

        } else if (attribute instanceof CompletedBookingState) {
            return BookingStates.COMPLETED_BOOKING.toString();

        } else if (attribute instanceof PassengerPickedUpBookingState) {
            return BookingStates.PASSENGER_PICKED_UP.toString();

        } else if (attribute instanceof TaxiDispatchedBookingState) {
            return BookingStates.TAXI_DISPATCHED.toString();
        }

        throw new IllegalArgumentException("Cannot convert object to data.");
    }

    @Override
    public BookingState convertToEntityAttribute(String dbData) {

        // use java reflection here instead?
        if (dbData.equals(BookingStates.AWAITING_TAXI.toString())) {
            return new AwaitingTaxiBookingState();

        } else if (dbData.equals(BookingStates.CANCELED_BOOKING.toString())) {
            return new CancelledBookingState();

        } else if (dbData.equals(BookingStates.COMPLETED_BOOKING.toString())) {
            return new CompletedBookingState();

        } else if (dbData.equals(BookingStates.TAXI_DISPATCHED.toString())) {
            return new TaxiDispatchedBookingState();

        } else if (dbData.equals(BookingStates.PASSENGER_PICKED_UP.toString())) {
            return new PassengerPickedUpBookingState();
        }

        throw new IllegalArgumentException("Cannot convert database entity to object.");

    }
}
