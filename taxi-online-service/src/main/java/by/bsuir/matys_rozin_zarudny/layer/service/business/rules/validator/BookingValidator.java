package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator;

import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.BookingDto;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.booking.ValidUsernameSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.booking.ValidatePassengerNumberSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.booking.ValidateStartEndLocationSpecification;


public class BookingValidator extends Validator<BookingDto> {
    
    public BookingValidator(){
        super(new ValidatorContext());
    }
    
    @Override
    public boolean validate(BookingDto booking){
        ValidatorResult result = this.getValidatorResult();
        
        return new ValidUsernameSpecification(result)
                .and(new ValidateStartEndLocationSpecification(result))
                .and(new ValidatePassengerNumberSpecification(result))
                .isSatisfiedBy(booking);
    }
}
