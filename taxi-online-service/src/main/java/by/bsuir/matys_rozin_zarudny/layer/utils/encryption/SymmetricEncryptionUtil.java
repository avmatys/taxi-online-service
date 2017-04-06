
package by.bsuir.matys_rozin_zarudny.layer.utils.encryption;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Symmetric encryption utility class.
 */
public class SymmetricEncryptionUtil {

	// class logger
	private static final Logger LOGGER = Logger.getLogger(SymmetricEncryptionUtil.class.getName());

	private static final String STRING_ENCODING = "UTF8";

	private SymmetricEncryptionUtil() {
		// private as utility class.
	}

	/**
	 * Load keystore key from file path and return key.
	 * 
	 * @param keystorePath
	 *            path to keystore.
	 * @param keystorePassword
	 *            keystore password.
	 * @param keyName
	 *            name of key.
	 * @param keyPassword
	 *            password of key.
	 * @param keystoreType
	 *            the type of keystore.
	 * @return Symmetric key in keystore with specified key name and password.
	 *         Null if not found or an exception has been thrown.
	 */
	public static Key loadKeyFromKeyStore(String keystorePath, String keystorePassword, String keyName,
			String keyPassword, String keystoreType) {

		try {

			KeyStore ks = KeyStore.getInstance(keystoreType);
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(keystorePath);
			ks.load(is, keystorePassword.toCharArray());
			return ks.getKey(keyName, keyPassword.toCharArray());

		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException ex) {
			LOGGER.log(Level.SEVERE, null, ex);

		} catch (FileNotFoundException ex) {
			LOGGER.log(Level.SEVERE, null, ex);

		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, null, ex);

		}

		return null;
	}

	/**
	 * Load keystore from file path and return symmetric key.
	 * 
	 * @param keystorePath
	 *            path to keystore.
	 * @param keystorePassword
	 *            keystore password.
	 * @param keystoreType
	 *            the type of keystore.
	 * @return Symmetric key in keystore with specified key name and password.
	 *         Null if not found or an exception has been thrown.
	 */
	public static KeyStore loadKeyStore(String keystorePath, String keystorePassword, String keystoreType) {

		try {

			KeyStore ks = KeyStore.getInstance(keystoreType);
			InputStream is = new FileInputStream(keystorePath);
			ks.load(is, keystorePassword.toCharArray());
			return ks;

		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException ex) {
			LOGGER.log(Level.SEVERE, null, ex);

		} catch (FileNotFoundException ex) {
			LOGGER.log(Level.SEVERE, null, ex);

		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, null, ex);

		}

		return null;
	}

	/**
	 * Encrypt plaintext using an asymmetric encryption protocol and return the
	 * ciphertext.
	 *
	 * @param plaintext
	 *            plaintext to encrypt.
	 * @param cipher
	 *            cipher to use.
	 * @param key
	 *            symmetric key.
	 * @return the encrypted plaintext encoded in base 64.
	 */
	public static String encrypt(String plaintext, String cipher, Key key) {

		byte[] ciphertext;

		try {
			// get cipher instance
			Cipher ecipher = Cipher.getInstance(cipher.toString());

			// set encryption mode.
			ecipher.init(Cipher.ENCRYPT_MODE, key);

			// encrypt
			ciphertext = ecipher.doFinal(plaintext.getBytes(SymmetricEncryptionUtil.STRING_ENCODING));

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException
				| IllegalBlockSizeException | BadPaddingException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
			return null;
		}

		return Base64.getEncoder().encodeToString(ciphertext);
	}

	/**
	 * Decrypt ciphertext using an asymmetric encryption protocol and the
	 * provided ciphertext and returns the plaintext.
	 *
	 * @param ciphertext
	 *            ciphertext encoded in base64 to decrypt.
	 * @param cipher
	 *            cipher to use.
	 * @param key
	 *            symmetric key.
	 * @return the decrypted plaintext.
	 */
	public static String decrypt(String ciphertext, String cipher, Key key) {

		byte[] plaintext;

		try {
			Cipher ecipher = Cipher.getInstance(cipher);

			// set encryption mode.
			ecipher.init(Cipher.DECRYPT_MODE, key);

			// decrypt
			plaintext = ecipher.doFinal(Base64.getDecoder().decode(ciphertext));

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException ex) {

			LOGGER.log(Level.SEVERE, null, ex);
			return null;
		}

		return new String(plaintext, Charset.forName(SymmetricEncryptionUtil.STRING_ENCODING));
	}
}
