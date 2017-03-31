package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.booking;

import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.BookingDto;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.CompositeSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;

/**
 *  Validate username specification for a booking dto.
 * 
 */
public class ValidUsernameSpecification extends CompositeSpecification<BookingDto>{

    
    public ValidUsernameSpecification(ValidatorResult result){
        super(result);
    }
    
    @Override
    public boolean isSatisfiedBy(BookingDto candidate) {

        if(candidate == null){
            throw new IllegalArgumentException("Candidate cannot be null.");
        }

        return true;
    }
}