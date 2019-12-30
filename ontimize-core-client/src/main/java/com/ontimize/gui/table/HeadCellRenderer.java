package com.ontimize.gui.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.FontSelector;

public class HeadCellRenderer extends CellRenderer {

	private static final Logger logger = LoggerFactory.getLogger(HeadCellRenderer.class);

	protected static class EText extends JTextArea {

		private Icon icon = null;

		private int maxLinesNumber = 2;

		public EText() {
			super();
			this.setLineWrap(true);
			this.setWrapStyleWord(true);
			this.setOpaque(true);
		}

		public void setLineMaxNumber(int n) {
			this.maxLinesNumber = n;
		}

		public void setIcon(Icon i) {
			this.icon = i;
			this.repaint();
		}

		public Icon getIcon() {
			return this.icon;
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension d = super.getPreferredSize();
			if (this.icon != null) {
				d.width = d.width + this.icon.getIconWidth() + 2;
				d.height = Math.max(d.height, this.icon.getIconHeight() + 4);
			} else {
				d.height = Math.min(d.height, (this.maxLinesNumber * this.getFontMetrics(this.getFont()).getHeight()) + 2);
			}
			return d;
		}

		@Override
		protected void paintComponent(Graphics g) {
			if (this.icon != null) {
				g.translate(this.icon.getIconWidth() + 1, 0);
			}
			super.paintComponent(g);
			if (this.icon != null) {
				g.translate(-(this.icon.getIconWidth() + 1), 0);
				this.icon.paintIcon(this, g, 1, (this.getHeight() / 2) - (this.icon.getIconHeight() / 2));

			}
		}
	}

	public HeadCellRenderer() {
		super.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public HeadCellRenderer(boolean multiline) {
		super.setHorizontalAlignment(SwingConstants.CENTER);
		if (multiline) {
			this.setJComponent(new EText());
		}
	}

	public void setMaxLinesNumber(int n) {
		if (this.component instanceof EText) {
			((EText) this.component).setLineMaxNumber(n);
		}
	}

	@Override
	public void setTipWhenNeeded(JTable table, Object value, int column) {
		// TIP
		try {
			this.component.setToolTipText(null);
			if (table != null) {
				if ((this.component instanceof JLabel) && (column >= 0)) {
					// TableColumn tc =
					// table.getColumn(table.getColumnName(column));
					TableColumn tc = table.getColumnModel().getColumn(column);
					if (tc.getWidth() < (this.component.getPreferredSize().width + 4)) {
						this.component.setToolTipText(this.getText());
					}
				}
			}
		} catch (Exception e) {
			HeadCellRenderer.logger.error(null, e);
		}

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (table != null) {
			c.setFont(table.getFont());
			JTableHeader header = table.getTableHeader();
			if (header != null) {
				c.setForeground(header.getForeground());
				c.setBackground(header.getBackground());
				c.setFont(header.getFont());
			}
		}

		if ((value != null) && value.equals(ExtendedTableModel.ROW_NUMBERS_COLUMN)) {
			if (c instanceof JTextComponent) {
				((JTextComponent) c).setText("");
			} else if (c instanceof JLabel) {
				((JLabel) c).setText("");
			}
		}

		((JComponent) c).setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		this.setTipWhenNeeded(table, value, column);
		return c;
	}

	@Override
	public void updateUI() {
		super.updateUI();
		this.setFont((Font) UIManager.get(FontSelector.table));
		if (this.component != null) {
			this.component.setFont((Font) UIManager.get(FontSelector.table));
		}
	}

}
