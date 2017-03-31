package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.booking;

import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.BookingDto;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.CompositeSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;

/**
 *  Validate passenger specification for a booking dto.
 * 
 */
public class ValidatePassengerNumberSpecification extends CompositeSpecification<BookingDto>{

    
    public ValidatePassengerNumberSpecification(ValidatorResult result){
        super(result);
    }
    
    @Override
    public boolean isSatisfiedBy(BookingDto candidate) {

        if(candidate == null){
            throw new IllegalArgumentException("Candidate cannot be null.");
        }
        
        if(candidate.getNumberPassengers() <= 0){
            this.getErrorResult().addError("Passenger count must be more than 0.");
            return false;
        }

        return true;
    }
}