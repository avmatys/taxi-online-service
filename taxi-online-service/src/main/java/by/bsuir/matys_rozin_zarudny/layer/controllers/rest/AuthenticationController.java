package by.bsuir.matys_rozin_zarudny.layer.controllers.rest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import by.bsuir.matys_rozin_zarudny.common.exceptions.AccountAuthenticationFailed;
import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.HttpResponseFactory;
import by.bsuir.matys_rozin_zarudny.layer.service.AccountFacade;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;
import by.bsuir.matys_rozin_zarudny.layer.utils.datamapper.DataMapper;

/**
 * A controller class for receiving and handling all authentication related
 * transactions.
 */
@Path("/v1/auth")
@RequestScoped
public class AuthenticationController {

	private static final Logger LOGGER = Logger.getLogger(AuthenticationController.class.getName());

	@Inject
	private AccountFacade accountService;
	private final DataMapper mapper;
	private final HttpResponseFactory responseFactory;

	public AuthenticationController() {
		this.mapper = DataMapper.getInstance();
		this.responseFactory = HttpResponseFactory.getInstance();
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response login(String credentials) {
		try {
			Account ac = this.mapper.readValue(credentials, Account.class);

			ac = this.accountService.authenticate(ac.getUsername(), ac.getPassword());

			return this.responseFactory.getResponse(ac, Response.Status.OK);

		} catch (IOException ex) {

			LOGGER.log(Level.SEVERE, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.BAD_REQUEST);

		} catch (AccountAuthenticationFailed ex) {

			LOGGER.log(Level.SEVERE, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.UNAUTHORIZED);
		}
	}

	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response logout(String credentials) {
		try {
			Account ac = this.mapper.readValue(credentials, Account.class);

			this.accountService.logout(ac.getUsername(), ac.getPassword());

			return this.responseFactory.getResponse(ac, Response.Status.OK);

		} catch (IOException ex) {

			LOGGER.log(Level.SEVERE, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.BAD_REQUEST);

		} catch (AccountAuthenticationFailed ex) {

			LOGGER.log(Level.SEVERE, null, ex);
			return this.responseFactory.getResponse(ex.getMessage(), Response.Status.UNAUTHORIZED);
		}
	}
}
