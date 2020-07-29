package com.ontimize.ols;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.ols.compatibility.ICompatible;
import com.ontimize.ols.control.LCMC;

public class WindowLError extends JWindow {

    private static final Logger logger = LoggerFactory.getLogger(WindowLError.class);

    private static WindowLError wle = null;

    private final JPanel panel = new JPanel();

    private static ICompatible icompatible = null;

    static {
        if (ApplicationManager.jvmVersionHigherThan_1_4_0()) {
            try {
                Class c = Class.forName("com.ontimize.ols.compatibility.Compatible");
                WindowLError.icompatible = (ICompatible) c.newInstance();
                LLog.log("Compatibility initialized windowlerror");
            } catch (Exception ex) {
                if (LLog.DEBUG) {
                    WindowLError.logger.error(null, ex);
                }
            }
        }
    }

    public static void putRightBottom(Component c, Component cp) {

        int x = -1;
        int y = -1;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        if (cp == null) {
            x = (int) d.getWidth() - c.getWidth() - 3;
            y = (int) d.getHeight() - c.getHeight() - 53;
        } else {
            x = (cp.getX() + cp.getWidth()) - c.getWidth() - 8;
            y = (cp.getY() + cp.getHeight()) - c.getHeight() - 27;
        }

        if (WindowDWarning.isVisibleDW()) {
            x = WindowDWarning.getWDWX() - 170;
            y = WindowDWarning.getWDWY();
        }

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        // if(x>d.width) x = 0;
        // if(y>d.height) y = 0;
        c.setLocation(x, y);
    }

    private class ListenerLErrorWindow extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            WindowLError.this.panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,
                    Color.yellow.brighter(), Color.yellow.darker()));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            WindowLError.this.panel.setBorder(
                    BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red.brighter(), Color.red.darker()));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            LCMC.showLCMC(ApplicationManager.getApplication().getFrame(),
                    ApplicationManager.getApplication().getResourceBundle());
        }

    }

    private final ListenerLErrorWindow llew = new ListenerLErrorWindow();

    public WindowLError(Frame f) {
        super(f);
        this.setSize(170, 65);
        this.panel.setBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red.brighter(), Color.red.darker()));
        this.panel.setToolTipText("License Error");
        this.panel.setLayout(new FlowLayout());
        this.panel
            .add(new JLabel(ApplicationManager.getIcon("com/ontimize/ols/resource/images/licenseWarning-48x48.gif")));
        this.panel.setBackground(Color.gray.brighter());
        this.panel.add(new JLabel(ApplicationManager.getTranslation("WindowLicenseError.licenseError",
                ApplicationManager.getApplication().getResourceBundle())));
        this.getContentPane().add(this.panel, "Center");
        this.panel.addMouseListener(this.llew);
        if (WindowLError.icompatible != null) {
            WindowLError.icompatible.setFocusable(this, false);
        }
        WindowLError.putRightBottom(this, null);
    }

    public static void setVWLE(boolean b) {
        if ((WindowLError.wle == null) && (ApplicationManager.getApplication() != null)) {
            WindowLError.wle = new WindowLError(ApplicationManager.getApplication().getFrame());
        }
        if (WindowLError.wle != null) {
            WindowLError.wle.setVisible(b);
        }
    }

    public static boolean isVisibleWindowLError() {
        if (WindowLError.wle == null) {
            return false;
        }
        return WindowLError.wle.isVisible();
    }

    public static void placeWindowLError() {
        if (WindowLError.wle == null) {
            return;
        }
        if (!WindowLError.wle.isVisible()) {
            return;
        }
        if (ApplicationManager.getApplication() == null) {
            return;
        }
        WindowLError.putRightBottom(WindowLError.wle, ApplicationManager.getApplication().getFrame());
    }

}
