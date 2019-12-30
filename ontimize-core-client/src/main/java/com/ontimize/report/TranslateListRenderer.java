package com.ontimize.report;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import com.ontimize.gui.ApplicationManager;

/**
 * ListCellRenderer used to translate the texts.
 */

public class TranslateListRenderer extends DefaultListCellRenderer {

	ResourceBundle bundle = null;

	public TranslateListRenderer(ResourceBundle bundle) {
		this.setOpaque(true);
		this.bundle = bundle;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (comp instanceof JLabel) {
			if (value != null) {
				this.setText(ApplicationManager.getTranslation(value.toString(), this.bundle));
			}
		}
		return comp;
	}
}