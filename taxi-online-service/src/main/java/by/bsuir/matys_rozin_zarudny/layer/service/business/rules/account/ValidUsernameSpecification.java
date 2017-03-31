package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account;

import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.CompositeSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;


public class ValidUsernameSpecification extends CompositeSpecification<Account>{

    
    public ValidUsernameSpecification(ValidatorResult result){
        super(result);
    }
    
    @Override
    public boolean isSatisfiedBy(Account candidate) {

        if(candidate == null){
            throw new IllegalArgumentException("Candidate cannot be null.");
        }
        
        if (candidate.getUsername() == null) {
            this.getErrorResult().addError("Username cannot be null.");
        }

        if(java.util.regex.Pattern.matches("^[a-zA-Z].{5,16}$", candidate.getUsername())){
            return true;
        }else{
            this.getErrorResult().addError("Username must be at least 6 characters.");
            return false;
        }
    }
}
