package by.bsuir.matys_rozin_zarudny.common.exceptions;

import java.util.List;

public class InvalidBookingException extends Exception {
	private List<String> errors;

	public InvalidBookingException(String message) {
		super(message);
	}

	public InvalidBookingException(List<String> errors) {
		super("Booking exception.");
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}
}
