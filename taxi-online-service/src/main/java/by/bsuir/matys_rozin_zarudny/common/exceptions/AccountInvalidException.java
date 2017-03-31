package by.bsuir.matys_rozin_zarudny.common.exceptions;

import java.util.List;

public class AccountInvalidException extends Exception {
	private List<String> errors;

	public AccountInvalidException(List<String> errors) {
		super("Account validation failed.");
		this.errors = errors;
	}

	public AccountInvalidException(String message) {
		super(message);
	}

	public List<String> getErrors() {
		return errors;
	}
}
