package com.ontimize.util.swing;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.ontimize.gui.ApplicationManager;

public class TranslateTableCellRenderer extends DefaultTableCellRenderer {

	ResourceBundle bundle = null;

	public TranslateTableCellRenderer(ResourceBundle bundle) {
		this.bundle = bundle;

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (comp instanceof JLabel) {
			this.setText(ApplicationManager.getTranslation(value.toString(), this.bundle));
		}
		return comp;
	}
}
