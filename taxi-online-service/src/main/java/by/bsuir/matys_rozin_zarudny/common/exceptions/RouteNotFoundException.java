package by.bsuir.matys_rozin_zarudny.common.exceptions;

public class RouteNotFoundException extends NonexistentEntityException {
	public RouteNotFoundException() {
		super("Route not found.");
	}
}
