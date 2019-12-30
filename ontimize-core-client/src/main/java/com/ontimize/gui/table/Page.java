package com.ontimize.gui.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * The preview page of the table contents
 */
class Page extends JPanel {

	BufferedImage im = null;

	double scale = 1.0;

	public Page(BufferedImage im) {
		this.setOpaque(true);
		this.setBackground(Color.white);
		this.im = im;
		this.setBorder(new LineBorder(Color.darkGray));
	}

	@Override
	public Dimension getPreferredSize() {
		Insets borderMargin = this.getBorder().getBorderInsets(this);
		if (this.im != null) {
			return new Dimension(borderMargin.left + borderMargin.right + (int) (this.im.getWidth() * this.scale),
					borderMargin.top + borderMargin.bottom + (int) (this.im.getHeight() * this.scale));
		} else {
			return new Dimension(borderMargin.left + borderMargin.right, borderMargin.top + borderMargin.bottom);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Insets borderMargin = this.getBorder().getBorderInsets(this);
		if (this.im != null) {
			g.drawImage(this.im, borderMargin.left, borderMargin.top, (int) (this.im.getWidth() * this.scale), (int) (this.im.getHeight() * this.scale), this);
		}
	}

	void setImage(BufferedImage im) {
		this.dispose();
		if (this.im != null) {
			this.im.flush();
		}
		this.im = im;
		// repaint();
		if (this.getParent() != null) {
			this.getParent().doLayout();
		}
	}

	void dispose() {
		if (this.im != null) {
			this.im.flush();
			this.im = null;
		}
	}
}