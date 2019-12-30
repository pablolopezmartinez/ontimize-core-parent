package com.ontimize.util.mail;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMailManager extends AbstractMailManager {

	private static final Logger logger = LoggerFactory.getLogger(DefaultMailManager.class);

	public DefaultMailManager(String mailserver) throws IllegalArgumentException {
		this(mailserver, false);
	}

	public DefaultMailManager(String mailserver, String userN) throws IllegalArgumentException {
		this(mailserver, userN, false);
	}

	public DefaultMailManager(String mailserver, String userN, String password) throws IllegalArgumentException {
		this(mailserver, userN, password, false);
	}

	public DefaultMailManager(String mailserver, boolean useAuth) throws IllegalArgumentException {

		if (mailserver == null) {
			throw new IllegalArgumentException("mailserver can not be null");
		}
		this.setMailServer(mailserver.toString());

		this.setAuthenticator(new AbstractMailManager.PopupAuthenticator(null));
		this.setUseAuth(useAuth);
	}

	public DefaultMailManager(String mailserver, String userN, boolean useAuth) throws IllegalArgumentException {

		if (mailserver == null) {
			throw new IllegalArgumentException("mailserver can not be null");
		}
		this.setMailServer(mailserver.toString());

		if (userN == null) {
			throw new IllegalArgumentException("username can not be null");
		}
		this.setUserName(userN.toString());
		this.setAuthenticator(new AbstractMailManager.PopupAuthenticator(null));
		this.setUseAuth(useAuth);
	}

	public DefaultMailManager(String mailserver, String userN, String password, boolean useAuth) throws IllegalArgumentException {

		if (mailserver == null) {
			throw new IllegalArgumentException("mailserver can not be null");
		}
		this.setMailServer(mailserver.toString());

		if (userN == null) {
			throw new IllegalArgumentException("username can not be null");
		}
		this.setUserName(userN.toString());

		if (password == null) {
			throw new IllegalArgumentException("password can not be null");
		}

		this.setAuthenticator(new AbstractMailManager.SilentAuthenticator(this.getUserName(), password));
		this.setUseAuth(useAuth);
	}

	/**
	 * <Table BORDER=1 CELLPADDING=3 CELLSPACING=1 RULES=ROWS FRAME=BOX>
	 * <tr>
	 * <td><b>attribute</td>
	 * <td><b>values</td>
	 * <td><b>default</td>
	 * <td><b>required</td>
	 * <td><b>meaning</td>
	 * </tr>
	 * <tr>
	 * <td>mailserver</td>
	 * <td><i></td>
	 * <td></td>
	 * <td>yes</td>
	 * <td>Establishes the mail server to be used</code>.</td>
	 * </tr>
	 * <tr>
	 * <td>username</td>
	 * <td><i></i></td>
	 * <td></td>
	 * <td>no</td>
	 * <td>Established the user name to be used</td>
	 * </tr>
	 * <tr>
	 * <td>password</td>
	 * <td><i></i></td>
	 * <td></td>
	 * <td>no</td>
	 * <td>Established the password to be used</td>
	 * </tr>
	 * <tr>
	 * <td>auth</td>
	 * <td><i>yes/no</i></td>
	 * <td>no</td>
	 * <td>no</td>
	 * <td>Established condition if the mail server needs authentication</td>
	 * </tr>
	 * <tr>
	 * <td>mail.smtp.timeout</td>
	 * <td><i></i></td>
	 * <td></td>
	 * <td>no</td>
	 * <td>Established time to be waited for smtp mail server.</td>
	 * </tr>
	 * </TABLE>
	 *
	 * @param prop
	 * @throws IllegalArgumentException
	 */
	public DefaultMailManager(Properties prop) throws IllegalArgumentException {

		if (prop == null) {
			throw new IllegalArgumentException("Properties can not be null");
		}

		Object mailserver = prop.get("mailserver");
		if (mailserver == null) {
			throw new IllegalArgumentException("mailserver can not be null");
		}
		this.setMailServer(mailserver.toString());

		Object userN = prop.get("username");
		if (userN == null) {
			DefaultMailManager.logger.debug("username is null. PopupAuthenticator. ");
			this.setAuthenticator(new AbstractMailManager.PopupAuthenticator(null));
		} else {
			this.setUserName(userN.toString());

			Object passw = prop.get("password");
			if (passw == null) {
				DefaultMailManager.logger.debug("password es null");
				this.setAuthenticator(new AbstractMailManager.PopupAuthenticator(null));
			} else {
				this.setAuthenticator(new AbstractMailManager.SilentAuthenticator(this.getUserName(), passw.toString()));
			}
		}

		Object auth = prop.get("auth");
		if (auth != null) {
			if (auth.toString().equalsIgnoreCase("yes") || auth.toString().equalsIgnoreCase("true")) {
				this.setUseAuth(true);
			}
		}
		// We have the configuration to send mails

		Object timeoutsmtp = prop.get("mail.smtp.timeout");
		if (timeoutsmtp != null) {
			try {
				this.timeoutSMTP = Integer.parseInt(timeoutsmtp.toString());
			} catch (Exception e) {
				DefaultMailManager.logger.error("Error in parameter: 'mail.smtp.timeout' " + e.getMessage(), e);
			}

		}

		// Since 5.2070EN-0.1 (used in gmail accounts)
		Object port = prop.get("mail.smtp.port");
		if (port != null) {
			this.setPort(Integer.parseInt(port.toString()));
		}

		// Since 5.2070EN-0.1 (used in gmail accounts)
		Object starttls = prop.get("mail.smtp.starttls.enable");
		if (starttls != null) {
			if (starttls.toString().equalsIgnoreCase("yes") || starttls.toString().equalsIgnoreCase("true")) {
				this.setStarttls(starttls.toString());
			}
		}

		// Since 5.2070EN-0.1 (used in gmail accounts)
		Object socketfactoryclass = prop.get("mail.smtp.socketFactory.class");
		if (socketfactoryclass != null) {
			if (socketfactoryclass.toString().equalsIgnoreCase("yes") || socketfactoryclass.toString().equalsIgnoreCase("true")) {
				this.setSocketFactoryClass(socketfactoryclass.toString());
			}
		}

		// Since 5.2070EN-0.1 (used in gmail accounts)
		Object socketfactoryfallback = prop.get("mail.smtp.socketFactory.fallback");
		if (socketfactoryfallback != null) {
			this.setSocketFactoryFallback(socketfactoryfallback.toString());
		}

	}

}