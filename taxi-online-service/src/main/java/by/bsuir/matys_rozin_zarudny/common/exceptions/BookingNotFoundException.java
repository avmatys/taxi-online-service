package by.bsuir.matys_rozin_zarudny.common.exceptions;

public class BookingNotFoundException extends NonexistentEntityException {
	public BookingNotFoundException() {
		super("Taxi not found.");
	}
}
