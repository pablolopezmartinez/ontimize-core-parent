package com.ontimize.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class BorderManager {

    public static final String EMPTY_BORDER_KEY = "emptyborder";

    public static final String DEFAULT_TABLE_BORDER_KEY = "tableborder";

    public static final String DEFAULT_IMAGE_BORDER_KEY = "imageborder";

    public static final String DEFAULT_TABLE_RENDER_BORDER_KEY = "tablerenderborder";

    public static final String DEFAULT_DATA_FIELD_REQUIRED_BORDER = "defaultdatafieldrequiredborder";

    private static Hashtable repository = new Hashtable();

    static {
        BorderManager.putBorder(BorderManager.DEFAULT_TABLE_RENDER_BORDER_KEY,
                BorderFactory.createEmptyBorder(0, 5, 0, 0));
        BorderManager.putBorder(BorderManager.EMPTY_BORDER_KEY, BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BorderManager.putBorder(BorderManager.DEFAULT_IMAGE_BORDER_KEY, new EtchedBorder(EtchedBorder.LOWERED));
        BorderManager.putBorder(BorderManager.DEFAULT_DATA_FIELD_REQUIRED_BORDER,
                new LineBorder(new Color(0x70c1dc), 1) {

                    // Border insets have to be changed in order to compatibility with
                    // xp border style
                    // Maybe good check OS
                    @Override
                    public Insets getBorderInsets(Component c) {
                        return this.getBorderInsets(c, new Insets(0, 0, 0, 0));
                    }

                    @Override
                    public Insets getBorderInsets(Component c, Insets insets) {
                        Insets margin = null;
                        //
                        // Ideally we'd have an interface defined for classes which
                        // support margins (to avoid this hackery), but we've
                        // decided against it for simplicity
                        //
                        if (c instanceof AbstractButton) {
                            margin = ((AbstractButton) c).getMargin();
                        } else if (c instanceof JToolBar) {
                            margin = ((JToolBar) c).getMargin();
                        } else if (c instanceof JTextComponent) {
                            margin = ((JTextComponent) c).getMargin();
                        }
                        insets.top = (margin != null ? margin.top : 0) + this.thickness;
                        insets.left = (margin != null ? margin.left : 0) + this.thickness;
                        insets.bottom = (margin != null ? margin.bottom : 0) + this.thickness;
                        insets.right = (margin != null ? margin.right : 0) + this.thickness;

                        return insets;
                    }
                });
    }

    public static Border getBorder(String borderName) {
        return (Border) BorderManager.repository.get(borderName);
    }

    public static void putBorder(String borderName, Border border) {
        BorderManager.repository.put(borderName, border);
    }

}
