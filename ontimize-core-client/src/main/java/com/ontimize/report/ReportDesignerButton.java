package com.ontimize.report;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;

public class ReportDesignerButton extends JButton {

	static MouseAdapter borderHandler = new MouseAdapter() {

		@Override
		public void mouseEntered(MouseEvent e) {
			if (((AbstractButton) e.getSource()).isEnabled()) {
				((AbstractButton) e.getSource()).setBorderPainted(true);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!((AbstractButton) e.getSource()).isSelected()) {
				((AbstractButton) e.getSource()).setBorderPainted(false);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	};

	public ReportDesignerButton(Icon i) {
		super(i);
		this.setMargin(new Insets(2, 2, 2, 2));
		this.setBorderPainted(false);
		this.addMouseListener(ReportDesignerButton.borderHandler);
	}

	public ReportDesignerButton(String i) {
		super(i);
		this.setMargin(new Insets(2, 2, 2, 2));
		this.setBorderPainted(false);
		this.addMouseListener(ReportDesignerButton.borderHandler);

	}
}