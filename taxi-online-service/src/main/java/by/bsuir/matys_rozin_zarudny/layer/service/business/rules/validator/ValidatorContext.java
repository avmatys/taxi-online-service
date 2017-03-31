package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements validator result specification.
 * 
 */
public class ValidatorContext implements ValidatorResult {

	private final List<String> errors = new ArrayList<>();

	@Override
	public void addError(String error) {
		this.errors.add(error);
	}

	@Override
	public List<String> getErrors() {
		return this.errors;
	}

	@Override
	public boolean isFailedBusinessRules() {
		return errors.isEmpty();
	}
}
