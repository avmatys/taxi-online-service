package by.bsuir.matys_rozin_zarudny.layer.service.entities.events;

/**
 * Event type constants.
 * 
 */
public enum EventTypes {
	
    BOOKING_EVENT("bookingEvent");
    
    private final String description;
    
    EventTypes(String description){
        this.description = description;
    }
    
    @Override
    public String toString(){
        return this.description;
    }
}
