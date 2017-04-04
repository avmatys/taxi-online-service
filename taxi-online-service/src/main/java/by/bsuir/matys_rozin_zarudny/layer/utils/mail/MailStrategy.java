package by.bsuir.matys_rozin_zarudny.layer.utils.mail;

/**
 * Mail strategy interface
 */
public interface MailStrategy {

    /**
     * Send mail with specified message.
     *
     * @param subject subject of message
     * @param message message to send as part of email body.
     * @param recipient recipient to send message to.
     * @return true if mail sent else false.
     */
    public boolean sendMail(String subject, String message, String recipient);

    /**
     * Return true if valid email else false.
     *
     * @param address address to validate.
     * @return true if valid email else false.
     */
    public boolean isValidEmail(String address);
}
