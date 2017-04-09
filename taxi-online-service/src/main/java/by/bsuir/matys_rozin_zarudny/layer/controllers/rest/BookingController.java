package by.bsuir.matys_rozin_zarudny.layer.controllers.rest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import by.bsuir.matys_rozin_zarudny.common.exceptions.AccountAuthenticationFailed;
import by.bsuir.matys_rozin_zarudny.common.exceptions.BookingNotFoundException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.EntityNotFoundException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.IllegalBookingStateException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.InvalidBookingException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.InvalidGoogleApiResponseException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.InvalidLocationException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.RouteNotFoundException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.TaxiNotFoundException;
import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.BookingDto;
import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.HttpListResponse;
import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.HttpResponseFactory;
import by.bsuir.matys_rozin_zarudny.layer.service.BookingFacade;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.booking.Booking;
import by.bsuir.matys_rozin_zarudny.layer.utils.datamapper.DataMapper;

/**
 * A controller class for receiving and handling all booking related
 * transactions.
 */
@Path("/v1/booking")
@RequestScoped
public class BookingController {

	private static final Logger LOGGER = Logger.getLogger(BookingController.class.getName());

	@Inject
	private BookingFacade bookingService;
	private final HttpResponseFactory responseFactory;
	private final DataMapper mapper;

	public BookingController() {
		this.responseFactory = HttpResponseFactory.getInstance();
		this.mapper = DataMapper.getInstance();
	}

	/**
	 * Create a new taxi booking.
	 *
	 * @param securityContext
	 *            injected by request scope
	 * @param message
	 *            JSON representation of a booking data access object.
	 * @return booking object with estimated cost, distance, and travel time. A
	 *         driver will not have been notified at this time.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("passenger")
	public Response makeBooking(@Context SecurityContext securityContext, String message) {

		BookingDto bookingDto = null;
		Booking booking = null;

		try {
			if (securityContext != null) {
				String username = securityContext.getUserPrincipal().getName();

				bookingDto = this.mapper.readValue(message, BookingDto.class);

				if (username != null) {
					/*
					 * Security flow - don't allow people to book taxis for
					 * other user's. Set username from security context.
					 */
					bookingDto.setPassengerUsername(username);
					booking = this.bookingService.makeBooking(bookingDto);
				}
			} else {
				throw new AccountAuthenticationFailed();
			}

			return this.responseFactory.getResponse(booking, Response.Status.OK);

		} catch (AccountAuthenticationFailed ex) {

			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.UNAUTHORIZED);
		} catch (InvalidLocationException | RouteNotFoundException ex) {

			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.NOT_FOUND);

		} catch (IOException | IllegalArgumentException ex) {

			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.BAD_REQUEST);

		} catch (InvalidGoogleApiResponseException ex) {

			LOGGER.log(Level.SEVERE, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);

		} catch (InvalidBookingException ex) {

			LOGGER.log(Level.INFO, null, ex);
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getErrors().get(0), Response.Status.NOT_FOUND);
		}
	}

	/**
	 * Return booking history based on user roles.
	 *
	 * @param securityContext
	 * @return booking history based on user roles.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "passenger", "driver" })
	public Response bookingHistory(@Context SecurityContext securityContext) {

		if (securityContext != null) {

			return Response.status(Response.Status.OK)
					.entity(new HttpListResponse<>(
							this.bookingService.findBookingHistory(securityContext.getUserPrincipal().getName()), "0")
									.toString())
					.build();
		}

		LOGGER.log(Level.INFO, "bookingHistory - user not authenticated");
		return this.responseFactory.getResponse("User not authenticated", Response.Status.UNAUTHORIZED);
	}

	/**
	 * Return a collection of bookings in awaiting taxi dispatch state.
	 *
	 * @return a collection of bookings in awaiting taxi dispatch state.
	 */
	@GET
	@Path("/awaiting")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("driver")
	public Response findBookingsAwaitingTaxiDispatch() {
		return Response.status(Response.Status.UNAUTHORIZED).entity(
				new HttpListResponse<>(this.bookingService.findBookingsInAwaitingTaxiDispatchState(), "0").toString())
				.build();
	}

	/**
	 * Find a booking.
	 *
	 * @param securityContext
	 *            user's security context injected by container.
	 * @param id
	 *            id of booking.
	 * @return the booking object. If the user is authenticated to view the
	 *         booking.
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("passenger")
	public Response findBooking(@Context SecurityContext securityContext, @PathParam("id") long id) {

		try {
			if (securityContext != null) {

				Booking booking = this.bookingService.findBookingForUser(id,
						securityContext.getUserPrincipal().getName());

				if (booking == null) {
					throw new EntityNotFoundException();
				}

				return this.responseFactory.getResponse(booking, Response.Status.OK);

			} else {
				throw new AccountAuthenticationFailed();
			}

		} catch (AccountAuthenticationFailed ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.UNAUTHORIZED);
		} catch (EntityNotFoundException ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.NOT_FOUND);
		}
	}

	/**
	 * Accept a taxi booking,
	 *
	 * @param securityContext
	 *            user's security context injected by container.
	 * @param bookingId
	 *            of booking to accept.
	 * @return the booking object. If the user is authenticated to view the
	 *         booking.
	 */
	@PUT
	@Path("/{id}/accept")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("driver")
	public Response acceptBooking(@Context SecurityContext securityContext, @PathParam("id") long bookingId) {

		try {
			if (securityContext != null) {

				this.bookingService.acceptBooking(securityContext.getUserPrincipal().getName(), bookingId);

				return this.responseFactory.getResponse("Booking updated.", Response.Status.OK);

			} else {
				throw new AccountAuthenticationFailed();
			}

		} catch (AccountAuthenticationFailed ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.UNAUTHORIZED);
		} catch (TaxiNotFoundException | BookingNotFoundException ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.NOT_FOUND);
		} catch (IllegalBookingStateException ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
		}
	}

	/**
	 * Drop of a passenger.
	 *
	 * @param securityContext
	 *            user's security context injected by container.
	 * @param bookingId
	 *            of booking to accept.
	 * @return the booking object. If the user is authenticated to view the
	 *         booking.
	 */
	@PUT
	@Path("/{id}/dropoffpassenger")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("driver")
	public Response dropOffPassenger(@Context SecurityContext securityContext, @PathParam("id") long bookingId) {

		try {
			if (securityContext != null) {

				this.bookingService.dropOffPassenger(securityContext.getUserPrincipal().getName(), bookingId,
						System.currentTimeMillis());

				return this.responseFactory.getResponse("Booking updated.", Response.Status.OK);

			} else {
				throw new AccountAuthenticationFailed();
			}

		} catch (AccountAuthenticationFailed ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.UNAUTHORIZED);
		} catch (TaxiNotFoundException | BookingNotFoundException ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.NOT_FOUND);
		} catch (IllegalBookingStateException ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
		}
	}

	/**
	 * Drop of a passenger.
	 *
	 * @param securityContext
	 *            user's security context injected by container.
	 * @param bookingId
	 *            of booking to accept.
	 * @return the booking object. If the user is authenticated to view the
	 *         booking.
	 */
	@PUT
	@Path("/{id}/pickuppassenger")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("driver")
	public Response pickupPassenger(@Context SecurityContext securityContext, @PathParam("id") long bookingId) {

		try {
			if (securityContext != null) {

				this.bookingService.pickUpPassenger(securityContext.getUserPrincipal().getName(), bookingId,
						System.currentTimeMillis());

				return this.responseFactory.getResponse("Booking updated.", Response.Status.OK);

			} else {
				throw new AccountAuthenticationFailed();
			}

		} catch (AccountAuthenticationFailed ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.UNAUTHORIZED);
		} catch (TaxiNotFoundException | BookingNotFoundException ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.NOT_FOUND);
		} catch (IllegalBookingStateException ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
		}
	}

	/**
	 * Cancel a booking.
	 *
	 * @param securityContext
	 *            user's security context injected by container.
	 * @param bookingId
	 *            of booking to accept.
	 * @return the booking object. If the user is authenticated to view the
	 *         booking.
	 */
	@PUT
	@Path("/{id}/cancel")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("passenger")
	public Response cancelBooking(@Context SecurityContext securityContext, @PathParam("id") long bookingId) {

		try {
			if (securityContext != null) {

				this.bookingService.cancelBooking(securityContext.getUserPrincipal().getName(), bookingId);

				return this.responseFactory.getResponse("Booking Cancelled.", Response.Status.OK);

			} else {
				throw new AccountAuthenticationFailed();
			}

		} catch (IllegalBookingStateException ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
		} catch (AccountAuthenticationFailed ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.UNAUTHORIZED);
		} catch (BookingNotFoundException ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.NOT_FOUND);
		}
	}

	/**
	 * Check if active booking for user and return.
	 * 
	 * @param securityContext
	 *            user's security context injected by container.
	 * @return the most recent active booking.
	 */
	@GET
	@Path("/active")
	@RolesAllowed({ "passenger", "driver" })
	public Response activeBookings(@Context SecurityContext securityContext) {

		Booking booking = null;

		try {

			if (securityContext != null) {

				booking = this.bookingService.checkActiveBooking(securityContext.getUserPrincipal().getName());

				if (booking == null) {
					return this.responseFactory.getResponse("No active bookings.", Response.Status.NOT_FOUND);
				}

				return this.responseFactory.getResponse(booking, Response.Status.OK);

			} else {
				throw new AccountAuthenticationFailed();
			}

		} catch (AccountAuthenticationFailed ex) {
			LOGGER.log(Level.INFO, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.UNAUTHORIZED);
		}
	}
}
