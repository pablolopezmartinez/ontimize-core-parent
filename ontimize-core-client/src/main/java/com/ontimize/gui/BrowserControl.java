package com.ontimize.gui;

import java.lang.reflect.Method;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserControl {

    private static final Logger logger = LoggerFactory.getLogger(BrowserControl.class);

    /**
     * Shows a url in the default browser or open the default mail client to send an e-mail.
     *
     * @since 5.2059EN <br>
     *        This method detects by reflection whether exists class <code>java.awt.Desktop</code> is
     *        present and try to open URL/open mail client with method <i>browse</i>/<i>mail</i> of
     *        class <code>java.awtDesktop</code>. In other case try to open url with runtime object.
     * @param url The URL of the file or the e-mail address
     */
    public static void displayURL(String url) {
        try {
            // with java6 we use Desktop
            Class classObject = BrowserControl.class.getClassLoader().loadClass("java.awt.Desktop");
            Method method = classObject.getMethod("getDesktop", null);
            Object currentDesktop = method.invoke(null, null);
            Method browseMethod = null;
            if ((url != null) && (url.toLowerCase().indexOf("mailto:") >= 0)) {
                browseMethod = classObject.getMethod("mail", new Class[] { URI.class });
            } else {
                browseMethod = classObject.getMethod("browse", new Class[] { URI.class });
            }
            browseMethod.invoke(currentDesktop, new Object[] { new URI(url) });
        } catch (Exception e) {
            // In other case we use Runtime
            String cmd = null;
            try {
                boolean windows = BrowserControl.isWindowsPlatform();
                if (windows) {
                    if ((url != null) && (url.toLowerCase().indexOf("mailto:") >= 0)) {
                        cmd = BrowserControl.WIN_PATH + " " + BrowserControl.WIN_FLAG + " " + url;
                    } else {
                        cmd = BrowserControl.WIN_PATH + " " + BrowserControl.WIN_FLAG + " " + url;
                        // cmd = WIN_PATH + " " + WIN_FLAG + " " +
                        // WIN_NEW_WINDOW_START + url + WIN_NEW_WINDOW_END;
                    }
                    Process p = Runtime.getRuntime().exec(cmd);
                } else {
                    // Under Unix, Netscape has to be running for the "-remote"
                    // command to work. So, we try sending the command and
                    // check for an exit value. If the exit command is 0,
                    // it worked, otherwise we need to start the browser.
                    // cmd = 'netscape -remote
                    // openURL(http://www.javaworld.com)'
                    cmd = BrowserControl.UNIX_PATH + " " + BrowserControl.UNIX_FLAG + "(" + url + ")";
                    Process p = Runtime.getRuntime().exec(cmd);

                    try {
                        // wait for exit code -- if it's 0, command worked,
                        // otherwise we need to start the browser up.
                        int exitCode = p.waitFor();

                        if (exitCode != 0) {
                            // Command failed, start up the browser

                            // cmd = 'netscape http://www.javaworld.com'
                            cmd = BrowserControl.UNIX_PATH + " " + url;
                            p = Runtime.getRuntime().exec(cmd);
                        }
                    } catch (InterruptedException x) {
                        BrowserControl.logger.error("Error bringing up browser, cmd='" + cmd + "'");
                        BrowserControl.logger.debug("Caught: " + x, e);
                    }
                }
            } catch (Exception x) {
                // couldn't exec browser
                BrowserControl.logger.debug("Could not invoke browser, command=" + cmd);
                BrowserControl.logger.debug("Caught: " + x, x);
            }
        }

    }

    /**
     * Try to determine whether this application is running under Windows or some other platform by
     * examing the "os.name" property.
     * @return true if this application is running under a Windows OS
     */
    public static boolean isWindowsPlatform() {
        String os = System.getProperty("os.name");

        if ((os != null) && os.startsWith(BrowserControl.WIN_ID)) {
            return true;
        } else {
            return false;
        }
    }

    // Used to identify the windows platform.
    private static final String WIN_ID = "Windows";

    // The default system browser under windows.
    private static final String WIN_PATH = "rundll32";

    // The flag to display a url.
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";

    private static final String WIN_NEW_WINDOW_START = "javascript:location.href='";

    private static final String WIN_NEW_WINDOW_END = "'";

    // The default browser under unix.
    private static final String UNIX_PATH = "netscape";

    // The flag to display a url.
    private static final String UNIX_FLAG = "-remote openURL";

}
