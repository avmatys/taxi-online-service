package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account;

import by.bsuir.matys_rozin_zarudny.layer.persistence.AccountDao;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.CompositeSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;


public class UniqueUsernameSpecification extends CompositeSpecification<Account> {

    private AccountDao accountDao;
    
    public UniqueUsernameSpecification(ValidatorResult result){
        super(result);
        this.accountDao = new AccountDao();
    }
    
    public UniqueUsernameSpecification(AccountDao accountDao, ValidatorResult result){
        super(result);
        this.accountDao = accountDao;
    }
    
    @Override
    public boolean isSatisfiedBy(Account candidate) {
        if(this.accountDao.findEntityById(candidate.getUsername()) == null){
            return true;
        }else{
            this.getErrorResult().addError("Account with username already exists.");
            return false;
        }
    }
}
