package com.ontimize.windows.systray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import snoozesoft.systray4j.SysTrayMenu;

public abstract class SystrayUtils {

    private static final Logger logger = LoggerFactory.getLogger(SystrayUtils.class);

    public interface SystrayListener extends ActionListener {

        public void iconLeftClicked();

        public void iconLeftDoubleClicked();

        @Override
        public void actionPerformed(ActionEvent e);

    }

    public static boolean isSystrayEnabled() {
        try {
            Class.forName("snoozesoft.systray4j.SysTrayMenuListener");
            return true;
        } catch (Exception e) {
            SystrayUtils.logger.trace(null, e);
            return false;
        }
    }

    public static void show(Object menu) {
        if (menu instanceof snoozesoft.systray4j.SysTrayMenu) {
            ((snoozesoft.systray4j.SysTrayMenu) menu).showIcon();
        }
    }

    public static void hide(Object menu) {
        if (menu instanceof snoozesoft.systray4j.SysTrayMenu) {
            ((snoozesoft.systray4j.SysTrayMenu) menu).hideIcon();
        }
    }

    public static void remove(Object menu) {
        if (menu instanceof snoozesoft.systray4j.SysTrayMenu) {
            SysTrayMenu.dispose();
        }
    }

    /**
     * @param icon Relative path to the resource without extension to ensure the portability
     * @param tip
     * @param menu
     * @param sListener Listener with interest in menu item and icon events
     * @return
     */
    public static Object addSystemTrayIcon(String icon, String tip, JMenu menu, final SystrayListener sListener) {
        if (!SystrayUtils.isSystrayEnabled()) {
            SystrayUtils.logger.debug("SystrayUtils not enabled: put needed files in classpath");
            return null;
        }
        URL url = SystrayUtils.class.getClassLoader()
            .getResource(icon + snoozesoft.systray4j.SysTrayMenuIcon.getExtension());
        if (url == null) {
            throw new IllegalArgumentException("icon " + icon + " not found");
        }
        snoozesoft.systray4j.SysTrayMenuIcon icono = new snoozesoft.systray4j.SysTrayMenuIcon(url);
        snoozesoft.systray4j.SysTrayMenu tMenu = new snoozesoft.systray4j.SysTrayMenu(icono, tip);
        snoozesoft.systray4j.SysTrayMenuListener l = new snoozesoft.systray4j.SysTrayMenuListener() {

            @Override
            public void iconLeftClicked(snoozesoft.systray4j.SysTrayMenuEvent e) {
                if (sListener != null) {
                    sListener.iconLeftClicked();
                }
            }

            @Override
            public void iconLeftDoubleClicked(snoozesoft.systray4j.SysTrayMenuEvent e) {
                if (sListener != null) {
                    sListener.iconLeftDoubleClicked();
                }
            }

            @Override
            public void menuItemSelected(snoozesoft.systray4j.SysTrayMenuEvent e) {
                if (sListener != null) {
                    sListener.actionPerformed(new ActionEvent(e.getSource(), 1000, e.getActionCommand()));
                }
            }
        };
        icono.addSysTrayMenuListener(l);

        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item == null) {
                tMenu.addSeparator();
                continue;
            }
            if (item instanceof JMenu) {
                snoozesoft.systray4j.SubMenu sm = new snoozesoft.systray4j.SubMenu(item.getActionCommand());
                SystrayUtils.createSubItems(sm, (JMenu) item, l);
                tMenu.addItem(sm);
            } else {
                String actionCommand = item.getActionCommand();

                if (actionCommand != null) {
                    snoozesoft.systray4j.SysTrayMenuItem it = new snoozesoft.systray4j.SysTrayMenuItem(actionCommand);
                    it.addSysTrayMenuListener(l);
                    it.setLabel(item.getText());
                    it.setActionCommand(actionCommand);
                    tMenu.addItem(it);
                }
            }

        }
        return tMenu;
    }

    private static void createSubItems(snoozesoft.systray4j.SubMenu tMenu, JMenu menu,
            snoozesoft.systray4j.SysTrayMenuListener l) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item == null) {
                tMenu.addSeparator();
                continue;
            }
            if (item instanceof JMenu) {
                snoozesoft.systray4j.SubMenu sm = new snoozesoft.systray4j.SubMenu(item.getActionCommand());
                SystrayUtils.createSubItems(sm, (JMenu) item, l);
                tMenu.addItem(sm);

            } else {
                snoozesoft.systray4j.SysTrayMenuItem it = new snoozesoft.systray4j.SysTrayMenuItem(
                        item.getActionCommand());
                it.addSysTrayMenuListener(l);
                it.setLabel(item.getText());
                tMenu.addItem(it);
                it.setActionCommand(item.getActionCommand());
            }
        }
    }

}
