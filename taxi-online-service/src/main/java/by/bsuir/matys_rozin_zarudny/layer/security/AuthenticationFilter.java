package by.bsuir.matys_rozin_zarudny.layer.security;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import by.bsuir.matys_rozin_zarudny.common.exceptions.AccountAuthenticationFailed;
import by.bsuir.matys_rozin_zarudny.layer.service.AccountFacade;
import by.bsuir.matys_rozin_zarudny.layer.service.AccountService;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;
import by.bsuir.matys_rozin_zarudny.layer.utils.http.HttpHeader;

/**
 * Authenticate a user using roles.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());

    private final AccountFacade accountService = new AccountService();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString(HttpHeader.AUTHORIZATION.toString());
        String requestUri = requestContext.getUriInfo().getAbsolutePath().toString();

        Account account = null;

        try {
            if (authHeader == null) {
                throw new AccountAuthenticationFailed();
            }

            account = this.accountService.authenticate(authHeader);
            requestContext.setSecurityContext(new AccountSecurityContext(account, requestUri));
        } catch (AccountAuthenticationFailed ex) {
            LOGGER.log(Level.FINEST, "User not authenticated");
        }
    }
}
