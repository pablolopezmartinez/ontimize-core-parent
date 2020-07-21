package com.ontimize.util;

import java.awt.Shape;
import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.RootPaneContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AWTUtilities {

    private static final Logger logger = LoggerFactory.getLogger(AWTUtilities.class);

    // com.sun.awt.AWTUtilities.setWindowOpaque(this, false);
    public static void setWindowOpaque(Window w, boolean opaque) {
        try {
            Class awtUtilitites = Class.forName("com.sun.awt.AWTUtilities");
            Method method = awtUtilitites.getMethod("setWindowOpaque", new Class[] { Window.class, boolean.class });
            method.invoke(null, new Object[] { w, new Boolean(opaque) });
        } catch (Exception thr) {
            AWTUtilities.logger.trace(null, thr);
            try {
                if (w instanceof RootPaneContainer) {
                    ((RootPaneContainer) w).getRootPane().putClientProperty("Window.alpha", new Float(opaque ? 1 : 0));
                }
            } catch (Exception e) {
                AWTUtilities.logger.trace(null, e);
            }
        }
    }

    // com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.8f);
    public static void setWindowOpacity(Window w, float opacity) {
        try {
            try {
                // java 7
                Method method = w.getClass().getMethod("setOpacity", new Class[] { float.class });
                method.invoke(w, new Object[] { new Float(opacity) });
                return;
            } catch (Exception thr) {
                AWTUtilities.logger.trace(null, thr);
            }

            Class awtUtilitites = Class.forName("com.sun.awt.AWTUtilities");
            Method method = awtUtilitites.getMethod("setWindowOpacity", new Class[] { Window.class, float.class });
            method.invoke(null, new Object[] { w, new Float(opacity) });
        } catch (Exception thr) {
            AWTUtilities.logger.trace(null, thr);
            try {
                if (w instanceof RootPaneContainer) {
                    ((RootPaneContainer) w).getRootPane().putClientProperty("Window.alpha", new Float(opacity));
                }
            } catch (Exception e) {
                AWTUtilities.logger.trace(null, e);
            }
        }
    }

    // setWindowShape
    // AWTUtilities.setWindowShape(this, new RoundRectangle2D.Float(0, 0,
    // this.getWidth(), this.getHeight(), 20, 20));
    public static void setWindowShape(Window w, Shape shape) {
        try {
            Class awtUtilitites = Class.forName("com.sun.awt.AWTUtilities");
            Method method = awtUtilitites.getMethod("setWindowShape", new Class[] { Window.class, Shape.class });
            method.invoke(null, new Object[] { w, shape });
        } catch (Exception thr) {
            AWTUtilities.logger.trace(null, thr);
        }
    }

    // this.setAlwaysOnTop(true);
    public static void setAlwaysOnTop(Window w, boolean always) {
        try {
            Method method = w.getClass().getMethod("setAlwaysOnTop", new Class[] { boolean.class });
            method.invoke(null, new Object[] { Boolean.valueOf(always) });
        } catch (Exception thr) {
            AWTUtilities.logger.trace(null, thr);
        }
    }

}
