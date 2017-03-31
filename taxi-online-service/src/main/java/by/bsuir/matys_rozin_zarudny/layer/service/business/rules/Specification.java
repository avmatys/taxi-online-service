package by.bsuir.matys_rozin_zarudny.layer.service.business.rules;

import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;

/**
 * Business rule specification interface.
 */
public interface Specification<T> {
    
    /**
     * Return true if conditions satisfied else false.
     */
    public boolean isSatisfiedBy(T candidate);
    
    /**
     * Chain to conditions by the logical AND operator. 
     */
    public Specification<T> and(Specification<T> other);
    
    /**
     * Return error results from failed business rules.
     */
    public ValidatorResult getErrorResult();
}
