package by.bsuir.matys_rozin_zarudny.layer.service.business.rules;

import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.ValidatorResult;

/**
 * Composite specification class.
 * 
 */
public abstract class CompositeSpecification<T> implements Specification<T>  {

    private ValidatorResult result;
    
    public CompositeSpecification(ValidatorResult result){
        this.result = result;
    }
    
    @Override
    public abstract boolean isSatisfiedBy(T candidate);

    @Override
    public Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(result, this, other);
    }
    
    @Override
    public ValidatorResult getErrorResult() {
        return this.result;
    }
}
