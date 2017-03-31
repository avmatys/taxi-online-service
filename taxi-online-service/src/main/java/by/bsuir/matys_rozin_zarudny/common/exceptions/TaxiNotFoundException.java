package by.bsuir.matys_rozin_zarudny.common.exceptions;

public class TaxiNotFoundException extends NonexistentEntityException {
	public TaxiNotFoundException() {
		super("Taxi not found.");
	}
}
