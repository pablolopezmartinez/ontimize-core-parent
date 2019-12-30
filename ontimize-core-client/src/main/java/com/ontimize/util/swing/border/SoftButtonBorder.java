package com.ontimize.util.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class SoftButtonBorder extends SoftBevelBorder implements javax.swing.plaf.UIResource {

	public SoftButtonBorder() {
		super(BevelBorder.RAISED);

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

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		boolean isPressed = false;

		if (c instanceof AbstractButton) {
			AbstractButton b = (AbstractButton) c;
			ButtonModel model = b.getModel();

			isPressed = (model.isPressed() && model.isArmed()) || model.isSelected();

			if (isPressed) {
				this.bevelType = BevelBorder.LOWERED;
			} else {
				this.bevelType = BevelBorder.RAISED;
			}
			super.paintBorder(c, g, x, y, width, height);
		}

	}
}