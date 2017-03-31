package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator;

import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account.PasswordConstrantSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account.UniqueUsernameSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account.UsernameCannotMatchPasswordSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account.ValidEmailSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account.ValidFamilyCommonNameSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account.ValidPhoneNumberSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account.ValidUsernameSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;

/**
 * Account validator contains all buseiness rules for account
 * 
 */
public class AccountValidator extends Validator<Account> {
    
    public AccountValidator(){
        super(new ValidatorContext());
    }
    
    @Override
    public boolean validate(Account account){
        ValidatorResult result = this.getValidatorResult();
        
        return new PasswordConstrantSpecification(result)
                .and(new UsernameCannotMatchPasswordSpecification(result))
                .and(new ValidUsernameSpecification(result))
                .and(new ValidEmailSpecification(result))
                .and(new UniqueUsernameSpecification(result))
                .and(new ValidPhoneNumberSpecification(result))
                .and(new ValidFamilyCommonNameSpecification(result))
                .isSatisfiedBy(account);
    }
}
