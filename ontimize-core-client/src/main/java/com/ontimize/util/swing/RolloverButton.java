package com.ontimize.util.swing;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.images.ImageManager;
import com.ontimize.util.swing.border.SoftButtonBorder;

public class RolloverButton extends JButton {
	private static final Logger logger = LoggerFactory.getLogger(RolloverButton.class);

	public static boolean createRolloverIcon = false;

	public static class RolloverListener extends MouseAdapter {

		JButton b = null;

		public RolloverListener(JButton b) {
			this.b = b;
			b.addMouseListener(this);
			b.setBorderPainted(false);
			b.setDefaultCapable(false);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (this.b.isEnabled()) {
				this.b.setBorderPainted(true);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (this.b.isEnabled()) {
				this.b.setBorderPainted(false);
			}
		}
	}

	public RolloverButton() {
		super();
		if (!RolloverButton.createRolloverIcon) {
			RolloverListener rL = new RolloverListener(this);
			RolloverButton.logger.trace("RolloverListener register : {} " + rL);
		}
	}

	public RolloverButton(String text) {
		super(text);
		if (!RolloverButton.createRolloverIcon) {
			RolloverListener rL = new RolloverListener(this);
			RolloverButton.logger.trace("RolloverListener register : {} " + rL);
		}
	}

	public RolloverButton(Icon icon) {
		super(icon);
		if (!RolloverButton.createRolloverIcon) {
			RolloverListener rL = new RolloverListener(this);
			RolloverButton.logger.trace("RolloverListener register : {} " + rL);
		}
	}

	public RolloverButton(Action a) {
		super(a);
		if (!RolloverButton.createRolloverIcon) {
			RolloverListener rL = new RolloverListener(this);
			RolloverButton.logger.trace("RolloverListener register : {} " + rL);
		}
	};

	public RolloverButton(String text, Icon icon) {
		super(text, icon);
		if (!RolloverButton.createRolloverIcon) {
			RolloverListener rL = new RolloverListener(this);
			RolloverButton.logger.trace("RolloverListener register : {} " + rL);
		}
	};

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		if (!b) {
			this.setBorderPainted(false);
		}
	}

	@Override
	public void repaint() {
		super.repaint();
		Container c = this.getParent();
		if (c != null) {
			c.repaint();
		}
	}

	@Override
	public void updateUI() {
		super.updateUI();
		if (this.getBorder() instanceof CompoundBorder) {
			Border b = ((CompoundBorder) this.getBorder()).getOutsideBorder();
			if (b instanceof javax.swing.plaf.basic.BasicBorders.ButtonBorder) {
				Border be = new SoftButtonBorder();
				CompoundBorder bn = new CompoundBorder(be, ((CompoundBorder) this.getBorder()).getInsideBorder());
				this.setBorder(bn);
			}
		}
	}

	@Override
	public void setIcon(Icon defaultIcon) {
		super.setIcon(defaultIcon);
		if (RolloverButton.createRolloverIcon && (defaultIcon instanceof ImageIcon)) {
			ImageIcon rollOverIcon = ImageManager.transparent((ImageIcon) defaultIcon, 0.5f);
			this.setRolloverIcon(rollOverIcon);
			this.setBorderPainted(false);
			this.setContentAreaFilled(false);
		}
	}

	@Override
	public void setOpaque(boolean isOpaque) {
		super.setOpaque(isOpaque);
	}
}
