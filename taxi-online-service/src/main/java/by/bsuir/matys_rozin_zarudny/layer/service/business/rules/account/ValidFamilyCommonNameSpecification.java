package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account;

import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.CompositeSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;


public class ValidFamilyCommonNameSpecification extends CompositeSpecification<Account>{

    
    public ValidFamilyCommonNameSpecification(ValidatorResult result){
        super(result);
    }
    
    @Override
    public boolean isSatisfiedBy(Account candidate) {

        if(candidate.getCommonName() == null || candidate.getFamilyName() == null){
            throw new IllegalArgumentException("Family and common name cannot be null.");
        }
        
        if (candidate.getCommonName().equals("") || candidate.getFamilyName().equals("")) {
            this.getErrorResult().addError("family and common name cannot be empty.");
            return false;
        }else{
            return true;
        }
    }
}
