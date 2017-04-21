package by.bsuir.matys_rozin_zarudny.layer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.inject.Inject;

import by.bsuir.matys_rozin_zarudny.common.exceptions.AccountAuthenticationFailed;
import by.bsuir.matys_rozin_zarudny.common.exceptions.BookingNotFoundException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.IllegalBookingStateException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.InvalidBookingException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.InvalidGoogleApiResponseException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.RouteNotFoundException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.TaxiNotFoundException;
import by.bsuir.matys_rozin_zarudny.layer.persistence.BookingDao;
import by.bsuir.matys_rozin_zarudny.layer.persistence.RouteDao;
import by.bsuir.matys_rozin_zarudny.layer.persistence.TaxiDao;
import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.BookingDto;
import by.bsuir.matys_rozin_zarudny.layer.service.business.rules.validator.BookingValidator;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Route;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.Booking;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.BookingStates;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.events.EventTypes;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.Taxi;
import by.bsuir.matys_rozin_zarudny.layer.utils.gcm.GcmClient;

/**
 * A service class implementing the booking facade.
 */
@Stateless
public class BookingService implements BookingFacade {

	@Inject
	private BookingDao bookingDao;
	@Inject
	private AccountFacade accountService;
	@Inject
	private RouteDao routeDao;
	@Inject
	private TaxiDao taxiDao;
	@Inject
	private GoogleDistanceMatrixFacade googleDistanceMatrixFacade;
	@Inject
	private TaxiFacade taxiService;

	/**
	 * Constructor for dependency injection/testing.
	 * 
	 * @param bookingDao
	 *            booking dao.
	 * @param accountService
	 *            account service.
	 * @param routeDao
	 *            route dao.
	 * @param taxiDao
	 *            taxi dao.
	 * @param googleDistanceMatrixFacade
	 *            Google distance matrix service.
	 */
	public BookingService(BookingDao bookingDao, AccountFacade accountService, RouteDao routeDao, TaxiDao taxiDao,
			GoogleDistanceMatrixFacade googleDistanceMatrixFacade, TaxiService taxiService) {
		this.bookingDao = bookingDao;
		this.accountService = accountService;
		this.routeDao = routeDao;
		this.taxiDao = taxiDao;
		this.googleDistanceMatrixFacade = googleDistanceMatrixFacade;
		this.taxiService = taxiService;
	}

	public BookingService() {
		// Intentionally left blank for dependency injection.
	}

	@Override
	public Booking findBooking(Long id) {
		return this.bookingDao.findEntityById(id);
	}

	/**
	 * Return booking if booking id matches the authenticated user, else null.
	 *
	 * @param id
	 *            id of booking.
	 * @param username
	 *            username of authenticated user.
	 * @return return booking if booking id matches the authenticated user, else
	 *         null.
	 */
	@Override
	public Booking findBookingForUser(Long id, String username) {
		Booking booking = this.findBooking(id);

		if (booking != null && booking.getPassenger().getUsername().equals(username)) {
			return booking;
		} else {
			return null;
		}
	}

	/**
	 * Book a taxi.
	 *
	 * @param bookingDto
	 *            booking data transfer object. Requires: 
	 *             - username username of passenger.
	 *             - numberPassengers number of passengers. 
	 *             - pickup location 
	 *             - destination location
	 * @throws AccountAuthenticationFailed
	 *             if account authentication fails.
	 * @throws RouteNotFoundException
	 *             route not found.
	 * @throws InvalidGoogleApiResponseException
	 *             invalid response from Google (Invalid JSON as there API has
	 *             changed).
	 * @throws InvalidBookingException
	 *             invalid booking e.g. user has active bookings.
	 */
	@Override
	public Booking makeBooking(final BookingDto bookingDto) throws AccountAuthenticationFailed, RouteNotFoundException,
			InvalidGoogleApiResponseException, InvalidBookingException {

		try {
			BookingValidator validator = new BookingValidator();

			if (validator.validate(bookingDto)) {

				Account passenger = this.accountService.findAccount(bookingDto.getPassengerUsername());

				if (passenger == null) {
					throw new AccountAuthenticationFailed();
				}

				if (this.incompleteBookings(passenger.getUsername())) {
					validator.getValidatorResult().addError("A user can only have one active booking.");
					throw new InvalidBookingException(validator.getValidatorResult().getErrors());
				}

				Route route = this.googleDistanceMatrixFacade.getRouteInfo(bookingDto.getStartLocation(),
						bookingDto.getEndLocation());

				if (route == null) {
					throw new RouteNotFoundException();
				}

				Booking booking = new Booking(passenger, route, bookingDto.getNumberPassengers());

				this.bookingDao.persistEntity(booking);

				return booking;

			} else {
				List<String> errors = validator.getValidatorResult().getErrors();

				throw new InvalidBookingException(errors);
			}

		} catch (InvalidGoogleApiResponseException ex) {
			throw ex;
		}
	}

	/**
	 * Return true if incomplete bookings for user else false.
	 *
	 * @param username
	 *            username to check.
	 * @return true if incomplete bookings for user else false
	 */
	private boolean incompleteBookings(String username) {
		return !this.bookingDao.findInCompletedBookingsForUser(username).isEmpty();
	}

	/**
	 * Update booking.
	 *
	 * @param booking
	 *            booking to update.
	 */
	@Override
	public void updateBooking(Booking booking) {
		this.bookingDao.update(booking);
	}

	
	@Override
	public List<Booking> findBookingsInAwaitingTaxiDispatchState() {
		return this.bookingDao.findBookingInState(Booking.getAwaitingTaxiBookingState());
	}

	/**
	 * A collections of bookings. If passenger display booking history. If
	 * driver display job history. If passenger has multiple roles combine
	 * collections.
	 *
	 * @param username
	 *            username.
	 * @return return booking history.
	 */
	@Override
	public List<Booking> findBookingHistory(String username) {

		List<Booking> bookingHistory = new ArrayList<>();

		if (username == null) {
			throw new IllegalArgumentException("Username cannot be nill.");
		}

		Account account = this.accountService.findAccount(username);
		if (account != null) {

			// append as a passenger can have multiple roles.
			if (account.hasRole("passenger")) {
				bookingHistory.addAll(this.bookingDao.findBookingsForPassenger(username));
			}
			if (account.hasRole("driver")) {
				bookingHistory.addAll(this.bookingDao.findBookingsForDriver(username));
			}

			return bookingHistory;
		}

		return bookingHistory;
	}

	/**
	 * Accept a taxi booking.
	 *
	 * @param username
	 *            username of taxi driver.
	 * @param bookingId
	 *            booking id.
	 * @throws TaxiNotFoundException
	 *             taxi not found.
	 * @throws BookingNotFoundException
	 *             booking not found.
	 * @throws IllegalBookingStateException
	 *             if booking in an illegal state.
	 */
	@Override
	public synchronized void acceptBooking(String username, long bookingId)
			throws TaxiNotFoundException, BookingNotFoundException, IllegalBookingStateException {

		Taxi taxi = this.taxiDao.findTaxiForDriver(username);
		Booking booking = this.findBooking(bookingId);

		if (taxi == null)
			throw new TaxiNotFoundException();
		if (booking == null)
			throw new BookingNotFoundException();

		try {
			booking.dispatchTaxi(taxi);

			taxi.acceptJob();
			this.taxiDao.update(taxi);
			this.bookingDao.update(booking);
		} catch (IllegalStateException ex) {
			throw new IllegalBookingStateException(ex.getMessage());
		}
	}

	/**
	 * Pink up passenger.
	 *
	 * @param username
	 *            username or driver accepting the booking
	 * @param bookingId
	 *            id of booking to update.
	 * @param timestamp
	 *            timestamp of update.
	 * @throws BookingNotFoundException
	 *             booking not found.
	 * @throws TaxiNotFoundException
	 *             taxi for user not found.
	 * @throws IllegalBookingStateException
	 *             if booking in an illegal state.
	 */
	@Override
	public void pickUpPassenger(String username, long bookingId, long timestamp)
			throws BookingNotFoundException, TaxiNotFoundException, IllegalBookingStateException {

		Booking booking = this.findBooking(bookingId);
		Taxi taxi = this.taxiDao.findTaxiForDriver(username);

		if (taxi == null) {
			throw new TaxiNotFoundException();
		}

		if (booking == null) {
			throw new BookingNotFoundException();
		}

		try {
			booking.pickupPassenger(new Date(timestamp));
			this.bookingDao.update(booking);
		} catch (IllegalStateException ex) {
			throw new IllegalBookingStateException(ex.getMessage());
		}
	}

	/**
	 * Drop of passenger.
	 *
	 * @param bookingId
	 *            id of booking to update.
	 * @param timestamp
	 *            timestamp of update.
	 * @throws BookingNotFoundException
	 *             booking not found.
	 * @throws TaxiNotFoundException
	 *             taxi for user not found.
	 * @throws IllegalBookingStateException
	 *             if booking in an illegal state.
	 */
	@Override
	public void dropOffPassenger(String username, long bookingId, long timestamp)
			throws BookingNotFoundException, TaxiNotFoundException, IllegalBookingStateException {

		Booking booking = this.findBooking(bookingId);

		Taxi taxi = this.taxiDao.findTaxiForDriver(username);

		if (taxi == null) {
			throw new TaxiNotFoundException();
		}

		if (booking == null) {
			throw new BookingNotFoundException();
		}

		try {
			booking.dropOffPassenger(new Date(timestamp));
			taxi.goOnDuty();
			this.taxiDao.update(taxi);

			this.bookingDao.update(booking);
		} catch (IllegalStateException ex) {
			throw new IllegalBookingStateException(ex.getMessage());
		}
	}

	/**
	 * Cancel a booking.
	 *
	 * @param username
	 *            username of person requesting cancellation of booking.
	 * @param bookingId
	 *            booking to cancel.
	 * @throws BookingNotFoundException
	 *             booking not found.
	 * @throws AccountAuthenticationFailed
	 *             user does not have permission to cancel booking.
	 * @throws IllegalBookingStateException
	 *             if booking in an illegal state.
	 */
	@Override
	public void cancelBooking(String username, long bookingId)
			throws BookingNotFoundException, AccountAuthenticationFailed, IllegalBookingStateException {

		Booking booking = this.findBooking(bookingId);

		if (booking == null) {
			throw new BookingNotFoundException();
		}

		// the passenger can only cancel their own booking.
		if (!booking.getPassenger().getUsername().equals(username)) {
			throw new AccountAuthenticationFailed();
		}

		if (booking.getTaxi() != null) {
			Taxi taxi = booking.getTaxi();
			taxi.goOnDuty();
			this.taxiDao.update(taxi);
		}

		try {
			booking.cancelBooking();
		} catch (IllegalStateException ex) {
			throw new IllegalBookingStateException(ex.getMessage());
		}

		this.bookingDao.update(booking);
	}

	/**
	 * Return most recent active booking for a user.
	 * 
	 * @param username
	 *            username of user.
	 * @return active booking for a user.
	 * @throws IllegalArgumentException
	 *             if username is null;
	 */
	@Override
	public Booking checkActiveBooking(String username) {

		if (username == null) {
			throw new IllegalArgumentException("Username cannot be null.");
		}

		List<Booking> bookings = this.bookingDao.findBookingsForPassenger(username);

		if (bookings != null && bookings.size() > 0) {
			// return most recent booking
			Booking recentBooking = bookings.get(0);

			for (Booking b : bookings) {
				if (b.isActive() && !recentBooking.getTimestamp().after(b.getTimestamp())) {
					recentBooking = b;
				}
			}

			return !recentBooking.isActive() ? null : recentBooking;
		}

		return null;
	}

	/**
	 * Allocate taxi to current waiting bookings.
	 */
	@Override
	public void allocateTaxi() {

		List<Booking> bookings = this.findBookingsInAwaitingTaxiDispatchState();

		if (!bookings.isEmpty()) {
			Random random = new Random();
			int randomBooking = random.nextInt(bookings.size());

			this.allocateTaxi(bookings.get(randomBooking));
		}
	}

	@Override
	public void allocateTaxi(Booking booking) {
		List<Taxi> taxis = this.taxiDao.findAllOnDuty();

		if (taxis.size() >= 1) {
			Random random = new Random();
			int allocatedTaxi = random.nextInt(taxis.size());

			Taxi taxi = taxis.get(allocatedTaxi);
		}

	}
}
