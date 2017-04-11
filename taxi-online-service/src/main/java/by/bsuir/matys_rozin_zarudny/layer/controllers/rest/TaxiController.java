package by.bsuir.matys_rozin_zarudny.layer.controllers.rest;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import by.bsuir.matys_rozin_zarudny.common.exceptions.TaxiNotFoundException;
import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.HttpResponseFactory;
import by.bsuir.matys_rozin_zarudny.layer.service.LocationTrackingService;
import by.bsuir.matys_rozin_zarudny.layer.service.TaxiFacade;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Location;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.taxi.Taxi;
import by.bsuir.matys_rozin_zarudny.layer.utils.datamapper.DataMapper;

/**
 * Controller class for taxis.
 */
@Path("/v1/taxi")
@RequestScoped
public class TaxiController {

	private static final Logger LOGGER = Logger.getLogger(TaxiController.class.getName());

	@Inject
	private LocationTrackingService locationTrackingService;
	@Inject
	private TaxiFacade taxiService;
	private final HttpResponseFactory responseFactory;
	private final DataMapper mapper;

	
	public TaxiController() {
		this.responseFactory = HttpResponseFactory.getInstance();
		this.mapper = DataMapper.getInstance();
	}

	/**
	 * Update location of taxi associated with the provided id.
	 *
	 * @param id
	 *            id of the taxi.
	 * @param message
	 *            the json update message.
	 * @return update confirmation.
	 */
	@POST
	@Path("/{id}/location")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("driver")
	public Response updateTaxiLocation(@PathParam("id") Long id, String message) {
		try {

			Location location = this.mapper.readValue(message, Location.class);

			// drivers should only be able to update there own taxi.
			// event time is based on current server time.
			this.locationTrackingService.updateLocation(id, location.getLatitude(), location.getLongitude(),
					new Date().getTime());

			LOGGER.log(Level.INFO, "updateTaxiLocation - Taxi Id: {0} Lat: {1} Lng:{2}",
					new Object[] { id, location.getLatitude(), location.getLongitude() });

			return this.responseFactory.getResponse("Taxi location updated.", Response.Status.OK);

		} catch (TaxiNotFoundException ex) {

			LOGGER.log(Level.INFO, null, ex);

			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.NOT_FOUND);

		} catch (IOException | IllegalArgumentException ex) {

			LOGGER.log(Level.WARNING, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.BAD_REQUEST);
		}
	}

	/**
	 * Find taxi by id.
	 *
	 * @param id
	 *            taxi id.
	 * @return taxi with specified id else 404.
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "driver", "passenger" })
	public Response getTaxi(@PathParam("id") Long id) {

		Taxi taxi = this.taxiService.findTaxi(id);

		if (taxi != null) {
			return this.responseFactory.getResponse(taxi, Response.Status.OK);
		} else {
			LOGGER.log(Level.INFO, "getTaxi - Taxi not found");
			return this.responseFactory.getResponse("Taxi not found.", Response.Status.NOT_FOUND);
		}
	}
}
