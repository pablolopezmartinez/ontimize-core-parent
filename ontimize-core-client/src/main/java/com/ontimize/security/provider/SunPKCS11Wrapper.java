package com.ontimize.security.provider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.security.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;

public class SunPKCS11Wrapper implements SecurityProvider {

	private static final Logger	logger						= LoggerFactory.getLogger(SunPKCS11Wrapper.class);

	public static final String PREFERENCE_PKCS11_MODULE = "pkcs11_module";

	public static final String DLL_LIBRARY = "name = SmartCard  library = UsrPkcs11.dll";

	public static final String DLL_LIBRARY_PATTERN = "name = SmartCard  library = {0}";

	public static final String PKCS_PROVIDER_CLASS_NAME = "sun.security.pkcs11.SunPKCS11";

	/**
	 * Indicates the name and library used to install pkcs provider. This variable may be change when user want to log in with <code>OpenDNIe</code> or another pcks provider. It is
	 * system dependent (.dll file in Windows or .so file in Linux and UNIX) so, user should check before using. By default it is initialized to {@link #DLL_LIBRARY}.
	 */
	public static String pkcsConfigFile = SunPKCS11Wrapper.DLL_LIBRARY;

	public static String pkcsProviderClassName = SunPKCS11Wrapper.PKCS_PROVIDER_CLASS_NAME;

	public SunPKCS11Wrapper() {

	}

	@Override
	public Provider getProvider() {
		try {
			Class c = Class.forName(SunPKCS11Wrapper.pkcsProviderClassName);
			Constructor constructor = c.getConstructor(new Class[] { InputStream.class });
			ByteArrayInputStream bais = new ByteArrayInputStream(SunPKCS11Wrapper.pkcsConfigFile.getBytes());
			Object[] obj = new Object[] { bais };
			Provider prov = (Provider) constructor.newInstance(obj);
			return prov;
		} catch (Exception ex) {
			if (ApplicationManager.DEBUG) {
				SunPKCS11Wrapper.logger.error(null, ex);
			}
			return null;
		}
	}

}
