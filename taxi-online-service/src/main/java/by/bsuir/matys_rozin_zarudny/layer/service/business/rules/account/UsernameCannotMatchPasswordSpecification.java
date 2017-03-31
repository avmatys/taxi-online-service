package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account;

import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.CompositeSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;


public class UsernameCannotMatchPasswordSpecification extends CompositeSpecification<Account>{

    public UsernameCannotMatchPasswordSpecification(ValidatorResult result){
        super(result);
    }
    
    @Override
    public boolean isSatisfiedBy(Account candidate) {
        if(candidate.getPassword().equals(candidate.getUsername())){
            this.getErrorResult().addError("Password and username cannot match.");
            return false;
        }else{
            return true;
        }
    }
}
