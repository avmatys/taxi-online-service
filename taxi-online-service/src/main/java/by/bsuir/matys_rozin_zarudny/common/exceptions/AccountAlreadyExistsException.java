package by.bsuir.matys_rozin_zarudny.common.exceptions;

public class AccountAlreadyExistsException extends Exception {
	public AccountAlreadyExistsException(String message) {
		super("Account already exists");
	}
}
