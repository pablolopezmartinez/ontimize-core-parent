package com.ontimize.util.swing.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.ontimize.gui.i18n.Internationalization;

public class GroupableTableHeaderUI extends BasicTableHeaderUI implements Internationalization {

	protected Vector painted = new Vector();

	@Override
	public void paint(Graphics g, JComponent c) {
		this.painted.clear();

		Rectangle clipBounds = g.getClipBounds();
		if (this.header.getColumnModel() == null) {
			return;
		}
		((GroupableTableHeader) this.header).setColumnMargin();
		int column = 0;
		Dimension size = this.header.getSize();
		Rectangle cellRect = new Rectangle(0, 0, size.width, size.height);
		Hashtable h = new Hashtable();
		int columnMargin = 0;
		Enumeration enumeration = this.header.getColumnModel().getColumns();
		while (enumeration.hasMoreElements()) {
			cellRect.height = size.height;
			cellRect.y = 0;
			TableColumn aColumn = (TableColumn) enumeration.nextElement();
			Enumeration cGroups = ((GroupableTableHeader) this.header).getColumnGroups(aColumn);
			if (cGroups != null) {
				int groupHeight = 0;
				while (cGroups.hasMoreElements()) {
					GroupableColumnGroup cGroup = (GroupableColumnGroup) cGroups.nextElement();
					Rectangle groupRect = (Rectangle) h.get(cGroup);
					if (groupRect == null) {
						groupRect = new Rectangle(cellRect);
						Dimension d = cGroup.getSize(this.header.getTable());
						groupRect.width = d.width;
						groupRect.height = d.height;
						h.put(cGroup, groupRect);
					}
					if (!this.isPainted(cGroup)) {
						Rectangle rBg = new Rectangle(groupRect.x + 1, groupRect.y, groupRect.width, cellRect.height);
						this.paintBackgroundCell(g, rBg, column, this.getRenderer(cGroup, aColumn));
						this.paintGroupCell(g, groupRect, cGroup, this.getRenderer(cGroup, aColumn));
					}
					groupHeight += groupRect.height;
					cellRect.height = size.height - groupHeight;
					cellRect.y = groupHeight;
				}
			} else {
				cellRect.width = aColumn.getWidth() + columnMargin;
				this.paintBackgroundCell(g, cellRect, column, this.getColumnHeaderRenderer(aColumn, this.header));
			}
			cellRect.width = aColumn.getWidth() + columnMargin;
			if (cellRect.intersects(clipBounds)) {
				this.paintCell(g, cellRect, column);
			}
			cellRect.x += cellRect.width;
			column++;
		}
	}

	private TableCellRenderer getColumnHeaderRenderer(TableColumn column, JTableHeader header) {
		TableCellRenderer renderer = column.getHeaderRenderer();
		if (renderer == null) {
			renderer = header.getDefaultRenderer();
		}
		return renderer;
	}

	private TableCellRenderer getRenderer(GroupableColumnGroup group, TableColumn column) {
		TableCellRenderer renderer = group.getHeaderRenderer();
		if (renderer instanceof IGroupableTableHeaderCellRenderer) {
			return renderer;
		} else {
			renderer = column.getHeaderRenderer();
			if (renderer == null) {
				renderer = this.header.getDefaultRenderer();
			}
			if (renderer instanceof IGroupableTableHeaderCellRenderer) {
				return renderer;
			}
			return group.getHeaderRenderer();
		}

	}

	private void paintCell(Graphics g, Rectangle cellRect, int columnIndex) {
		TableColumn aColumn = this.header.getColumnModel().getColumn(columnIndex);
		TableCellRenderer renderer = this.getColumnHeaderRenderer(aColumn, this.header);
		Component component = renderer.getTableCellRendererComponent(this.header.getTable(), aColumn.getHeaderValue(), false, false, -1, columnIndex);
		this.rendererPane.paintComponent(g, component, this.header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
	}

	private void paintGroupCell(Graphics g, Rectangle cellRect, GroupableColumnGroup cGroup, TableCellRenderer renderer) {
		this.painted.add(cGroup);
		Component c = null;
		if (renderer instanceof IGroupableTableHeaderCellRenderer) {
			c = ((IGroupableTableHeaderCellRenderer) renderer).getTableHeaderCellRendererComponent(this.header.getTable(), cGroup.getHeaderValue(), false, false, -1, 0);
		} else {
			c = renderer.getTableCellRendererComponent(this.header.getTable(), cGroup.getHeaderValue(), false, false, -1, 0);
		}

		this.rendererPane.paintComponent(g, c, this.header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
	}

	private void paintBackgroundCell(Graphics g, Rectangle cellRect, int columnIndex, TableCellRenderer renderer) {
		if (renderer instanceof IGroupableTableHeaderCellRenderer) {
			Component c = ((IGroupableTableHeaderCellRenderer) renderer).getTableHeaderBackgroundCellRendererComponent(this.header.getTable(), null, false, false, -1, columnIndex);
			this.rendererPane.paintComponent(g, c, this.header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
		}
	}

	private boolean isPainted(GroupableColumnGroup group) {
		return this.painted.contains(group);
	}

	private int getHeaderHeight() {
		int height = 0;
		TableColumnModel columnModel = this.header.getColumnModel();
		for (int column = 0; column < columnModel.getColumnCount(); column++) {
			TableColumn aColumn = columnModel.getColumn(column);
			TableCellRenderer renderer = this.getColumnHeaderRenderer(aColumn, this.header);
			if (renderer == null) {
				return 19;
			}

			Component comp = renderer.getTableCellRendererComponent(this.header.getTable(), aColumn.getHeaderValue(), false, false, -1, column);
			int cHeight = comp.getPreferredSize().height;
			Enumeration e = ((GroupableTableHeader) this.header).getColumnGroups(aColumn);
			if (e != null) {
				while (e.hasMoreElements()) {
					GroupableColumnGroup cGroup = (GroupableColumnGroup) e.nextElement();
					cHeight += cGroup.getSize(this.header.getTable()).height;
				}
			}
			height = Math.max(height, cHeight);
		}
		return height;
	}

	private Dimension createHeaderSize(long width) {
		TableColumnModel columnModel = this.header.getColumnModel();
		width += columnModel.getColumnMargin() * columnModel.getColumnCount();
		if (width > Integer.MAX_VALUE) {
			width = Integer.MAX_VALUE;
		}
		return new Dimension((int) width, this.getHeaderHeight());
	}

	@Override
	public Dimension getPreferredSize(JComponent c) {
		long width = 0;
		Enumeration enumeration = this.header.getColumnModel().getColumns();
		while (enumeration.hasMoreElements()) {
			TableColumn aColumn = (TableColumn) enumeration.nextElement();
			width = width + aColumn.getPreferredWidth();
		}
		return this.createHeaderSize(width);
	}

	@Override
	public Vector getTextsToTranslate() {
		return null;
	}

	@Override
	public void setComponentLocale(Locale locale) {

	}

	@Override
	public void setResourceBundle(ResourceBundle resourcebundle) {
		Enumeration enumeration = this.header.getColumnModel().getColumns();
		while (enumeration.hasMoreElements()) {
			TableColumn aColumn = (TableColumn) enumeration.nextElement();
			Enumeration cGroups = ((GroupableTableHeader) this.header).getColumnGroups(aColumn);
			if (cGroups != null) {
				while (cGroups.hasMoreElements()) {
					GroupableColumnGroup cGroup = (GroupableColumnGroup) cGroups.nextElement();
					cGroup.setResourceBundle(resourcebundle);
				}
			}
		}
	}
}
