package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account;

import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.CompositeSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;


public class ValidPhoneNumberSpecification extends CompositeSpecification<Account> {

    public ValidPhoneNumberSpecification(ValidatorResult result) {
        super(result);
    }

    @Override
    public boolean isSatisfiedBy(Account candidate) {

        if (candidate == null) {
            throw new IllegalArgumentException("Candidate cannot be null.");
        }

        if (candidate.getPhoneNumber() == null) {
            this.getErrorResult().addError("Phone number must be provided.");
            return false;
        }

        if (!candidate.getPhoneNumber().matches("\\d{11}")) {
            this.getErrorResult().addError("Phone number invalid.");
            return false;
        }
        
        return true;
    }

}
