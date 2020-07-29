package com.ontimize.util.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuButton extends JButton {

    private static final Logger logger = LoggerFactory.getLogger(MenuButton.class);

    static {
        try {
            UIManager.getDefaults().put("MenuButtonUI", "com.ontimize.util.swing.MenuButton$MenuButtonUI");
        } catch (Exception e) {
            MenuButton.logger.error(null, e);
        }
    }

    public MenuButton(String text) {
        super(text);
        this.setRolloverEnabled(true);
    }

    public MenuButton(Icon icon) {
        super(icon);
        this.setRolloverEnabled(true);
    }

    protected Dimension preferredSize = null;

    @Override
    public Dimension getPreferredSize() {
        if (this.preferredSize == null) {
            Dimension d = super.getPreferredSize();
            String text = this.getText();
            int h = 0;
            int w = 0;
            if ((text != null) && (text.length() > 0)) {
                h = this.getFontMetrics(this.getFont()).getHeight() + 2;
                w = this.getFontMetrics(this.getFont()).stringWidth(text) + 8;
            }
            Icon icon = this.getIcon();
            if (icon != null) {
                int hI = icon.getIconHeight() + 4;
                int wI = icon.getIconWidth() + 4;
                if (h < hI) {
                    h = hI;
                }
                w += wI;
            }
            if (d.width < w) {
                d.width = w;
            }
            if (d.height < h) {
                d.height = h;
            }
            this.preferredSize = d;
        }
        return this.preferredSize;
    }

    protected static final String uiClassID = "MenuButtonUI";

    @Override
    public String getUIClassID() {
        return MenuButton.uiClassID;
    }

    public static class MenuButtonBorder extends AbstractBorder {

        private final Border pressBorder = new EtchedBorder(EtchedBorder.LOWERED);

        private final Border normalBorder = new EtchedBorder(EtchedBorder.RAISED);

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            boolean bPress = false;
            boolean bOver = false;

            if (c instanceof AbstractButton) {
                AbstractButton b = (AbstractButton) c;
                ButtonModel bm = b.getModel();

                bPress = bm.isPressed();
                bOver = bm.isRollover();
            }

            if (bPress) {
                this.pressBorder.paintBorder(c, g, x, y, width, height);
            } else {
                if (bOver) {
                    this.normalBorder.paintBorder(c, g, x, y, width, height);
                }
            }
        }

    }

    public static class MenuButtonUI extends BasicButtonUI {

        protected static ButtonUI buttonUI;

        public static ComponentUI createUI(JComponent c) {
            if (MenuButtonUI.buttonUI == null) {
                MenuButtonUI.buttonUI = new MenuButtonUI();
            }
            return MenuButtonUI.buttonUI;
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.setBorder(new MenuButtonBorder());
        }

        @Override
        public void uninstallUI(JComponent c) {
            super.uninstallUI(c);
            c.setBorder(null);
        }

    }

}
