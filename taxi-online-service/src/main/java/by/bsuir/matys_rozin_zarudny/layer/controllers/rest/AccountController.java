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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import by.bsuir.matys_rozin_zarudny.common.exceptions.AccountInvalidException;
import by.bsuir.matys_rozin_zarudny.layer.persistence.dto.HttpResponseFactory;
import by.bsuir.matys_rozin_zarudny.layer.service.AccountFacade;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;
import by.bsuir.matys_rozin_zarudny.layer.utils.datamapper.DataMapper;

/**
 * A controller class for receiving and handling all account event related
 * transactions.
 */
@Path("/v1/account")
@RequestScoped
public class AccountController {
    
    private static final Logger LOGGER = Logger.getLogger(AccountController.class.getName());
    
    @Inject
    private AccountFacade accountService;
    private final DataMapper mapper;
    private final HttpResponseFactory responseFactory;
    
    public AccountController() {
        this.mapper = DataMapper.getInstance();
        this.responseFactory = HttpResponseFactory.getInstance();
    }

    /**
     * Register new account.
     *
     * @param account JSON representation of an account.
     * @return account object if successful else an appropriate error message: -
     * IOException - invalid JSON - AccountAlreadyExistsException - account with
     * username already exists. AccountInvalidException - invalid account
     * details.
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response registerAccount(String account) {
        try {
            Account ac = this.mapper.readValue(account, Account.class);
            
            this.accountService.registerAccount(ac);
            
            return this.responseFactory.getResponse(
                    ac, Response.Status.OK);
            
        } catch (IOException ex) {
            
            LOGGER.log(Level.WARNING, null, ex);
            return this.responseFactory.getResponse(
                    ex.getMessage(), Response.Status.BAD_REQUEST);
            
        } catch (AccountInvalidException ex) {
            LOGGER.log(Level.WARNING, null, ex);
            return this.responseFactory.getResponse(
                    ex.getErrors().get(0), Response.Status.BAD_REQUEST);
            
        }
    }

    /**
     * Reset account password.
     *
     * @param username inferred from path parameter.
     * @return account reset confirmation.
     */
    @POST
    @Path("/{username}/reset")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response resetAccount(@PathParam("username") String username) {
        
        try {
            this.accountService.resetPassword(username);
            
            LOGGER.log(Level.INFO, "resetAccount - resetting password {0}", username);
            
            return this.responseFactory.getResponse(
                    "Password reset sent.", Response.Status.OK);
            
        } catch (AccountInvalidException ex) {
            
            LOGGER.log(Level.WARNING, null, ex);
            return this.responseFactory.getResponse(
                    ex.getMessage(), Response.Status.NOT_FOUND);
        }
    }

   
}
