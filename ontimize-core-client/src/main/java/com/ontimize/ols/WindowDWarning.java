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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.ols.compatibility.ICompatible;

public class WindowDWarning extends JWindow {

    private static final Logger logger = LoggerFactory.getLogger(WindowDWarning.class);

    private static WindowDWarning wdw = null;

    private final JPanel panel = new JPanel();

    private static ICompatible icompatible = null;

    static {
        if (ApplicationManager.jvmVersionHigherThan_1_4_0()) {
            try {
                Class c = Class.forName("com.ontimize.ols.compatibility.Compatible");
                WindowDWarning.icompatible = (ICompatible) c.newInstance();
                LLog.log("Compatibility initialized windowDWarning");
            } catch (Exception ex) {
                if (LLog.DEBUG) {
                    WindowDWarning.logger.error(null, ex);
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

    private final ListenerWD lwd = new ListenerWD();

    private class ListenerWD extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            WindowDWarning.this.panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,
                    Color.yellow.brighter(), Color.yellow.darker()));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            WindowDWarning.this.panel.setBorder(
                    BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red.brighter(), Color.red.darker()));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            JOptionPane.showMessageDialog(ApplicationManager.getApplication().getFrame(),
                    ApplicationManager.getTranslation("WindowDWarning.warningMessage",
                            ApplicationManager.getApplication().getResourceBundle()),
                    ApplicationManager.getTranslation("WindowDWarning.warningMessageTitle",
                            ApplicationManager.getApplication().getResourceBundle()),
                    JOptionPane.WARNING_MESSAGE,
                    ApplicationManager.getIcon("com/ontimize/ols/resource/images/ontimize48x48.gif")

            );
        }

    }

    public WindowDWarning(Frame f) {
        super(f);
        this.setSize(180, 65);
        this.panel.setBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red.brighter(), Color.red.darker()));
        this.panel.setToolTipText("License Message");
        this.panel.setLayout(new FlowLayout());
        this.panel.add(new JLabel(
                ApplicationManager.getIcon("com/ontimize/ols/resource/images/license_information-48x48.png")));
        this.panel.setBackground(Color.gray.brighter());
        // panel.add(new
        // JLabel("<html><body>Licencia de desarrollo<BR>para uso no comercial</body></html>"));
        this.panel.add(new JLabel(ApplicationManager.getTranslation("WindowDWarning.comercialWarning",
                ApplicationManager.getApplication().getResourceBundle())));
        this.getContentPane().add(this.panel, "Center");
        this.panel.addMouseListener(this.lwd);
        if (WindowDWarning.icompatible != null) {
            WindowDWarning.icompatible.setFocusable(this, false);
        }
        WindowDWarning.putRightBottom(this, null);
    }

    public static void setVDW(boolean b) {
        if ((WindowDWarning.wdw == null) && (ApplicationManager.getApplication() != null)) {
            WindowDWarning.wdw = new WindowDWarning(ApplicationManager.getApplication().getFrame());
        }
        if (WindowDWarning.wdw != null) {
            WindowDWarning.wdw.setVisible(b);
        }
    }

    public static boolean isVisibleDW() {
        if (WindowDWarning.wdw == null) {
            return false;
        }
        return WindowDWarning.wdw.isVisible();
    }

    public static int getWDWX() {
        if (WindowDWarning.wdw == null) {
            return -1;
        }
        return WindowDWarning.wdw.getX();
    }

    public static int getWDWY() {
        if (WindowDWarning.wdw == null) {
            return -1;
        }
        return WindowDWarning.wdw.getY();
    }

    public static void placeDW() {
        if (WindowDWarning.wdw == null) {
            return;
        }
        if (!WindowDWarning.wdw.isVisible()) {
            return;
        }
        if (ApplicationManager.getApplication() == null) {
            return;
        }
        WindowDWarning.putRightBottom(WindowDWarning.wdw, ApplicationManager.getApplication().getFrame());
    }

    public static void main(String[] args) {
        WindowDWarning wdw = new WindowDWarning(null);
        wdw.setVisible(true);
    }

}
