package by.bsuir.matys_rozin_zarudny.layer.service;

import javax.ejb.Local;

import by.bsuir.matys_rozin_zarudny.common.exceptions.AccountAuthenticationFailed;
import by.bsuir.matys_rozin_zarudny.common.exceptions.AccountInvalidException;
import by.bsuir.matys_rozin_zarudny.common.exceptions.EntityNotFoundException;
import by.bsuir.matys_rozin_zarudny.layer.service.entities.Account;


@Local
public interface AccountFacade {

    /**
     * Register a new account. Two user's cannot have the same username.
     *
     * @param acct account to create.
     * @throws AccountInvalidException if email invalid.
     */
    public void registerAccount(Account acct)
            throws AccountInvalidException;

    /**
     * Authenticate a user.
     *
     * @param username username of account
     * @param password password of account
     * @return account object if authentication successful else null.
     * @throws AccountAuthenticationFailed if authentication fails.
     */
    public Account authenticate(String username, String password)
            throws AccountAuthenticationFailed;

  /**
     * Logout a user. Used for taxi driver to notify they are now off duty.
     *
     * @param username username of account
     * @param password password of account
     * @throws AccountAuthenticationFailed if authentication fails.
     */
    public void logout(String username, String password) throws AccountAuthenticationFailed;
    
    /**
     * Authenticate user from base64 encoded message.
     *
     * @param base64Credentials base64 encoded credentials
     * @return account object if authentication successful else null.
     * @throws AccountAuthenticationFailed if authentication fails.
     */
    public Account authenticate(final String base64Credentials)
            throws AccountAuthenticationFailed;

    /**
     * Return account with corresponding username.
     *
     * @param username username
     * @return an account with the corresponding username. If the account does
     * not exist return null.
     */
    public Account findAccount(final String username);

    /**
     * Reset account password.
     *
     * @param username username of account to reset.
     * @throws AccountInvalidException account not found
     */
    public void resetPassword(final String username)
            throws AccountInvalidException;

    /**
     * Reset account password.
     *
     * @param code temporary authentication code.
     * @param username username to authenticate with.
     * @param newPassword new password for user.
     * @throws AccountAuthenticationFailed if username and code do not match a
     * valid, active password reset event.
     * @throws EntityNotFoundException account not found but password resets
     * exist. Indicates data integrity issues.
     */
    public void resetPassword(final String code, final String username, final String newPassword)
            throws AccountAuthenticationFailed, EntityNotFoundException;
}
