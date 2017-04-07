package by.bsuir.matys_rozin_zarudny.layer.security;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;

/**
 * Security context wrapper class account. 
 *
 */
public class AccountSecurityContext implements SecurityContext {

    private final Account account;
    private final String requireUri;

    public AccountSecurityContext(Account account, String requireUri) {
        this.account = account;
        this.requireUri = requireUri;
    }

    /**
     * Returns a java.security.Principal object containing the name of the
     * current authenticated user. If the user has not been authenticated, the
     * method returns null.
     */
    @Override
    public Principal getUserPrincipal() {
        return new Principal() {

            @Override
            public String getName() {
                return account.getUsername();
            }
        };
    }

    /**
     * Returns a boolean value if the user user is included
     * in the specified logical "role".
     */
    @Override
    public boolean isUserInRole(String role) {
        return this.account.hasRole(role);
    }

    /**
     * Returns a boolean indicating whether this request was made using a secure
     * channel, such as HTTPS.
     */
    @Override
    public boolean isSecure() {
        return this.requireUri.startsWith("https");
    }

    /**
     * Returns the string value of the authentication scheme used to protect the
     * resource.
     */
    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
