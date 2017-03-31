package by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator;

import java.util.List;

/**
 * Interface to represent validator results.
 * 
 */
public interface ValidatorResult {

	public void addError(String error);

	public List<String> getErrors();

	public boolean isFailedBusinessRules();
}
