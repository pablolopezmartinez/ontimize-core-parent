package com.ontimize.util.swing.icon;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class CompoundIcon extends ImageIcon {

	protected Icon principal = null;

	protected Icon add = null;

	public CompoundIcon(ImageIcon principal, ImageIcon a) {
		super(principal.getImage());
		this.add = a;
		this.principal = principal;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		super.paintIcon(c, g, x, y);
		this.add.paintIcon(c, g, (this.getIconWidth() - this.add.getIconWidth()) + x, y);
	}
}
