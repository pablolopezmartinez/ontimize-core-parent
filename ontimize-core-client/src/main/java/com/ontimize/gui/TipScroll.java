package com.ontimize.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TipScroll extends JTipWindow {

	private static final Logger logger = LoggerFactory.getLogger(TipScroll.class);

	JLabel textLabel = new JLabel();

	Color bgColor = new Color(255, 255, 204);

	boolean leftAlignment = false;

	public TipScroll(Window parent) {
		this(parent, false);
	}

	public TipScroll(Window parent, boolean alignment) {
		super(parent);
		try {
			Color c = (Color) UIManager.get("ToolTip.background");
			this.getContentPane().setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue()));
		} catch (Exception e) {
			TipScroll.logger.trace(null, e);
			this.getContentPane().setBackground(this.bgColor);
		}
		this.leftAlignment = alignment;
		this.textLabel.setBorder(new EmptyBorder(0, 3, 0, 4));
		this.getContentPane().add(this.textLabel);
		((JPanel) this.getContentPane()).setBorder(new LineBorder(Color.black));
		this.pack();
	}

	public TipScroll() {
		this(false);
	}

	public TipScroll(boolean alignLeft) {
		super();
		try {
			Color c = (Color) UIManager.get("ToolTip.background");
			this.getContentPane().setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue()));
		} catch (Exception e) {
			TipScroll.logger.trace(null, e);
			this.getContentPane().setBackground(this.bgColor);
		}
		this.leftAlignment = alignLeft;
		this.textLabel.setBorder(new EmptyBorder(0, 3, 0, 4));
		this.getContentPane().add(this.textLabel);
		((JPanel) this.getContentPane()).setBorder(new LineBorder(Color.black));
		this.pack();
	}

	public void show(Component c, int x, int y, String text) {
		this.textLabel.setText(text);
		this.pack();

		if (this.leftAlignment) {
			Point p = c.getLocationOnScreen();
			int xMin = -p.x;
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			int xMax = d.width - this.getWidth();
			super.show(c, Math.min(Math.max(x, xMin), xMax), y - (this.getHeight() / 2));
		} else {
			Point p = c.getLocationOnScreen();
			int xMin = -p.x;
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			int xMax = d.width - this.getWidth();
			super.show(c, Math.min(Math.max(x - this.getWidth(), xMin), xMax), y - (this.getHeight() / 2));
		}

		((JPanel) this.getContentPane()).paintImmediately(this.getContentPane().getBounds());
	}

}
