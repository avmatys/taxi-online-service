package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator;

/**
 * Entity validator interface.
 * 
 */
public abstract class Validator<T> {

	private ValidatorResult result;

	public Validator(ValidatorResult result) {
		this.result = result;
	}

	public abstract boolean validate(T entity);

	public ValidatorResult getValidatorResult() {
		return this.result;
	}
}
