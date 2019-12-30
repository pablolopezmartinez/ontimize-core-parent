package com.ontimize.util.webstart;

import java.net.URL;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;

public abstract class WebStartUtilities {

	private static final Logger	logger				= LoggerFactory.getLogger(WebStartUtilities.class);

	private static boolean webstartApplication = false;

	private static boolean checked = false;

	public WebStartUtilities() {}

	public static boolean isOffline() {
		if (!WebStartUtilities.webstartApplication) {
			return false;
		}

		// If there is web start then use the services
		try {
			javax.jnlp.BasicService bs = (javax.jnlp.BasicService) javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
			boolean offline = bs.isOffline();
			return offline;
		} catch (Exception e) {
			WebStartUtilities.logger.trace(null, e);
			return false;
		}
	}

	private static void check() {
		try {
			String[] names = javax.jnlp.ServiceManager.getServiceNames();
			if (names == null) {
				throw new Exception("Webstart Services don't exist");
			} else {
				if (ApplicationManager.DEBUG) {
					WebStartUtilities.logger.debug(" WebStart Application: Services: ");
					for (int i = 0; i < names.length; i++) {
						WebStartUtilities.logger.debug(names[i] + " ");
					}
				}
			}
			WebStartUtilities.webstartApplication = true;
		} catch (Exception e) {
			WebStartUtilities.logger.debug("WebStartUtilities: The application is NOT using WebStart.", e);
			WebStartUtilities.webstartApplication = false;
		} finally {
			WebStartUtilities.checked = true;
		}
	}

	static {
		try {
			Class.forName("javax.jnlp.ServiceManager");
			WebStartUtilities.check();
		} catch (Exception e) {
			WebStartUtilities.logger.info("Webstart classes haven't been found");
			WebStartUtilities.logger.trace("", e);
			WebStartUtilities.webstartApplication = false;
			WebStartUtilities.checked = true;
		}
	}

	public static boolean isWebStartApplication() {
		if (!WebStartUtilities.checked) {
			WebStartUtilities.check();
		}
		return WebStartUtilities.webstartApplication;
	}

	public static void openBrowser(String url) throws Exception {
		if (!WebStartUtilities.webstartApplication) {
			throw new Exception("WEBSTART doesn't enable");
		}

		// If there is web start then use the services
		WebStartUtilities.logger.debug("Open browser using javax.jnlp.BasicService: " + url);
			javax.jnlp.BasicService bs = (javax.jnlp.BasicService) javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
			bs.showDocument(new URL(url));
	}

	public static boolean checkJarDownloaded(String jarNameAsInJNLP) {
		if (!WebStartUtilities.webstartApplication) {
			return true;
		}

		// If there is web start then use the services
		try {
			javax.jnlp.BasicService bs = (javax.jnlp.BasicService) javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
			URL codebase = bs.getCodeBase();
			javax.jnlp.DownloadService ds = (javax.jnlp.DownloadService) javax.jnlp.ServiceManager.lookup("javax.jnlp.DownloadService");
			boolean cached = ds.isResourceCached(new URL(codebase, jarNameAsInJNLP), null);
			if (!cached) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			WebStartUtilities.logger.error(null, e);
			return true;
		}
	}

	public static void downloadJar(final String jarNameAsInJNLP, final WebStartDownloadListener listener) {
		if (!WebStartUtilities.webstartApplication) {
			return;
		}
		Thread downloadThread = new Thread() {

			@Override
			public void run() {
				// If there is web start then use the services
				try {
					javax.jnlp.BasicService bs = (javax.jnlp.BasicService) javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
					URL codebase = bs.getCodeBase();
					javax.jnlp.DownloadService ds = (javax.jnlp.DownloadService) javax.jnlp.ServiceManager.lookup("javax.jnlp.DownloadService");
					URL url = new URL(codebase, jarNameAsInJNLP);
					boolean cached = ds.isResourceCached(url, null);
					if (cached) {
						if (listener != null) {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									listener.downloadCompleted(new WebStartDownloadEvent(WebStartUtilities.class, jarNameAsInJNLP));
								}
							});
						}
						return;
					} else {
						// There is not cache, then download it
						javax.jnlp.DownloadServiceListener dsl = ds.getDefaultProgressWindow();
						ds.loadResource(url, null, dsl);
						if (listener != null) {
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									listener.downloadCompleted(new WebStartDownloadEvent(WebStartUtilities.class, jarNameAsInJNLP));
								}
							});
						}
					}
				} catch (Exception e) {
					WebStartUtilities.logger.error(null, e);
					if (listener != null) {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								listener.downloadFailed(new WebStartDownloadEvent(WebStartUtilities.class, jarNameAsInJNLP));
							}
						});
					}
					return;
				}
			}
		};
		downloadThread.start();
	}
}
