package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account;

import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.CompositeSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;

/**
 * Password constraint specification.
 */
public class PasswordConstrantSpecification extends CompositeSpecification<Account> {

    public PasswordConstrantSpecification(ValidatorResult result){
        super(result);
    }
    
    @Override
    public boolean isSatisfiedBy(Account candidate) {
        if (candidate.getPassword().length() < 6){
            this.getErrorResult().addError("Password must be more than 6 characters.");
            return false;
        }else{
            return true;
        }
    }    
}
