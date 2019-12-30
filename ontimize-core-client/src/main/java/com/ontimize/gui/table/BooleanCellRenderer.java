package com.ontimize.gui.table;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import com.ontimize.gui.images.ImageManager;
import com.ontimize.util.swing.image.BooleanImage;

/**
 * Renderer used to show the boolean data in tables
 *
 * @version 1.0 01/04/2001
 */
public class BooleanCellRenderer extends CellRenderer implements TableCellRenderer {

	protected static ImageIcon selectIcon = null;

	protected static ImageIcon deselectIcon = null;

	public static boolean USE_CHECKBOX = false;

	public BooleanCellRenderer() {
		super();
		if (((BooleanCellRenderer.selectIcon == null) || (BooleanCellRenderer.deselectIcon == null)) && !BooleanCellRenderer.USE_CHECKBOX) {
			ImageIcon selectIcon = ImageManager.getIcon(ImageManager.CHECK_SELECTED);
			ImageIcon unselecIcon = ImageManager.getIcon(ImageManager.CHECK_UNSELECTED);
			if ((selectIcon == null) || (unselecIcon == null)) {
				BooleanCellRenderer.USE_CHECKBOX = true;
			} else {
				BooleanCellRenderer.selectIcon = selectIcon;
				BooleanCellRenderer.deselectIcon = unselecIcon;
			}
		}
		if (BooleanCellRenderer.USE_CHECKBOX) {
			JCheckBox checkBox = new JCheckBox();
			checkBox.setOpaque(true);
			checkBox.setBorderPaintedFlat(true);
			checkBox.setHorizontalAlignment(SwingConstants.CENTER);
			this.setJComponent(checkBox);
		} else {
			JCheckBox bt = new JCheckBox();
			bt.setSelectedIcon(BooleanCellRenderer.selectIcon);
			bt.setRolloverEnabled(false);
			bt.setIcon(BooleanCellRenderer.deselectIcon);
			bt.setHorizontalAlignment(SwingConstants.CENTER);
			bt.setBorder(CellRenderer.emptyBorder);
			this.setJComponent(bt);
		}

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus, int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);

		if (value instanceof Boolean) {
			if (((Boolean) value).booleanValue()) {
				((AbstractButton) c).setSelected(true);
			} else {
				((AbstractButton) c).setSelected(false);
			}
		} else if (value instanceof Number) {
			if (((Number) value).intValue() > 0) {
				((AbstractButton) c).setSelected(true);
			} else {
				((AbstractButton) c).setSelected(false);
			}
		} else if (value instanceof String) {
			if (value.equals("S")) {
				((AbstractButton) c).setSelected(true);
			} else {
				((AbstractButton) c).setSelected(false);
			}
		} else if (value instanceof BooleanImage) {
			BooleanImage bImage = (BooleanImage) value;
			if (bImage.getValue()) {
				((AbstractButton) c).setSelected(true);
			} else {
				((AbstractButton) c).setSelected(false);
			}
		} else {
			((AbstractButton) c).setSelected(false);
		}

		if (!hasFocus) {
			((AbstractButton) c).setBorderPainted(false);
		} else {
			((AbstractButton) c).setBorderPainted(true);
		}
		return c;
	}

	@Override
	public void setToolTipText(String text) {
		if (this.component != null) {
			this.component.setToolTipText(text);
		}
	}

}
