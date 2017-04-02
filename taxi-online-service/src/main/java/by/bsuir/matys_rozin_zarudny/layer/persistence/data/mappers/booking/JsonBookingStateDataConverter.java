package by.bsuir.matys_rozin_zarudny.layer.persistence.data.mappers.booking;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.BookingState;

import java.io.IOException;

/**
 * JSON serializer for Booking state class.
 */
public class JsonBookingStateDataConverter extends JsonSerializer<BookingState> {

    @Override
    public void serialize(BookingState t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeString(new JpaBookingStateDataConverter().convertToDatabaseColumn(t));
    }

}
