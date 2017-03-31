package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.account;

import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.CompositeSpecification;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;
import by.bsuir.matys_rozin_zarudny.layer.utils.mail.MailStrategy;
import by.bsuir.matys_rozin_zarudny.layer.utils.mail.SmtpMailStrategy;

public class ValidEmailSpecification extends CompositeSpecification<Account> {

    private MailStrategy mailStrategy;

    public ValidEmailSpecification(ValidatorResult result) {
        super(result);
        this.mailStrategy = new SmtpMailStrategy();
    }

    public ValidEmailSpecification(ValidatorResult result, MailStrategy mailStrategy) {
        super(result);
        this.mailStrategy = mailStrategy;
    }

    @Override
    public boolean isSatisfiedBy(Account candidate) {
        if (this.mailStrategy.isValidEmail(candidate.getEmail())) {
            return true;
        } else {
            this.getErrorResult().addError("Invalid email address.");
            return false;
        }
    }
}
