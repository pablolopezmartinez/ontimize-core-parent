package com.ontimize.gui.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;

public class MemoCellRenderer extends CellRenderer {

	public MemoCellRenderer() {
		JTextArea ta = new JTextArea();
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		ta.setOpaque(true);
		this.setJComponent(ta);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);
		if (c instanceof JTextArea) {
			JTextArea ta = (JTextArea) c;
			if (value != null) {
				ta.setText(value.toString());
			} else {
				ta.setText("");
			}
		}
		return c;
	}
}