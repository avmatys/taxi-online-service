package by.bsuir.matys_rozin_zarudny.layer.utils.mail;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import by.bsuir.matys_rozin_zarudny.configuration.ConfigService;
import by.bsuir.matys_rozin_zarudny.layer.utils.encryption.EncryptedProperties;

/**
 * SMTP Mail Strategy
 */
public class SmtpMailStrategy implements MailStrategy {

	private static final Logger LOGGER = Logger.getLogger(SmtpMailStrategy.class.getName());

	private final EncryptedProperties mailProperties;

	public SmtpMailStrategy() {
		mailProperties = new EncryptedProperties(ConfigService.getConfig("application.properties"));
	}

	public SmtpMailStrategy(EncryptedProperties mailProperties) {
		this.mailProperties = mailProperties;
	}

	/**
	 * Return shared mail session objects to provide access to mail transport
	 * services.
	 */
	private Session getMailSession() {
		return Session.getInstance(mailProperties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailProperties.getKey("mail.smtp.username"),
						mailProperties.getKey("mail.smtp.password"));
			}
		});
	}

	/**
	 * Send mail with specified message.
	 *
	 * @param subject
	 *            subject of message
	 * @param message
	 *            message to send as part of email body.
	 * @param recipient
	 *            recipient to send message to.
	 * @return true if mail sent else false.
	 */
	@Override
	public boolean sendMail(String subject, String message, String recipient) {

		boolean mailSent = false;

		try {
			Message mailMessage = new MimeMessage(this.getMailSession());

			mailMessage.setFrom(new InternetAddress(mailProperties.getKey("mail.smtp.from.address")));

			mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

			mailMessage.setSubject(subject);
			mailMessage.setText(message);

			Transport.send(mailMessage);

			mailSent = true;

		} catch (MessagingException ex) {
			LOGGER.log(Level.SEVERE, ex.toString());
		}

		return mailSent;
	}

	/**
	 * Return true if valid email else false.
	 */
	@Override
	public boolean isValidEmail(String address) {
		try {
			InternetAddress email = new InternetAddress(address);
			email.validate();
			return true;
		} catch (AddressException ex) {
			return false;
		}
	}
}
