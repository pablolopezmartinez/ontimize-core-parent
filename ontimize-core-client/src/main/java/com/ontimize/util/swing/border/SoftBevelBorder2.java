package com.ontimize.util.swing.border;

import java.awt.Color;
import java.awt.Component;

import javax.swing.border.SoftBevelBorder;

public class SoftBevelBorder2 extends SoftBevelBorder {

    public SoftBevelBorder2(int type) {
        super(type);
    }

    @Override
    public Color getHighlightInnerColor(Component c) {
        Color highlight = this.getHighlightInnerColor();
        return highlight != null ? highlight : c.getBackground();
    }

    @Override
    public Color getShadowInnerColor(Component c) {
        Color shadow = this.getShadowInnerColor();
        return shadow != null ? shadow : c.getBackground();
    }

    @Override
    public Color getShadowOuterColor(Component c) {
        Color shadow = this.getShadowInnerColor();
        return shadow != null ? shadow : c.getBackground().darker().darker();
    }

    @Override
    public Color getHighlightOuterColor(Component c) {
        Color highlight = this.getHighlightInnerColor();
        return highlight != null ? highlight : c.getBackground().brighter().brighter();
    }

}
