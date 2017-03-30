package by.bsuir.matys_rozin_zarudny.layer.service.entities.booking;

/**
 * Represents possible booking states for data mapping.
 *
 */
public enum BookingStates {

    AWAITING_TAXI("AWAITING_TAXI"),
    CANCELED_BOOKING("CANCELED_BOOKING"),
    COMPLETED_BOOKING("COMPLETED_BOOKING"),
    PASSENGER_PICKED_UP("PASSENGER_PICKED_UP"),
    TAXI_DISPATCHED("TAXI_DISPATCHED");

    private final String description;

    BookingStates(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
