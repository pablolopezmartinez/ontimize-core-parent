package com.ontimize.report.listeners;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.report.DefaultReportDialog;
import com.ontimize.report.ReportUtils;
import com.ontimize.report.item.SelectableDateGroupItem;
import com.ontimize.report.item.SelectableItem;

public class DateGroupListener extends MouseAdapter {

	private static final Logger logger = LoggerFactory.getLogger(DateGroupListener.class);

	DefaultReportDialog reportDialog;

	public DateGroupListener(DefaultReportDialog reportDialog) {
		this.reportDialog = reportDialog;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if ((e.getModifiers() == InputEvent.META_MASK) || (e.getX() > ReportUtils.LIST_MOUSE_X_MAX)) {
			return;
		}

		this.reportDialog.getGroupList().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			int index = this.reportDialog.getGroupList().locationToIndex(e.getPoint());
			if ((index < 0) || !(this.reportDialog.getGroupList().getModel().getElementAt(index) instanceof SelectableDateGroupItem)) {
				return;
			}
			SelectableItem it = (SelectableItem) this.reportDialog.getGroupList().getModel().getElementAt(index);
			boolean bWillBeSelected = !it.isSelected();

			it.setSelected(bWillBeSelected);
			Rectangle rect = this.reportDialog.getGroupList().getCellBounds(index, index);
			this.reportDialog.getGroupList().repaint(rect);

			this.reportDialog.updateReport();
			this.reportDialog.getGroupList().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} catch (Exception ex) {
			DateGroupListener.logger.error(ex.getMessage(), ex);
		} finally {
			this.reportDialog.getGroupList().setCursor(Cursor.getDefaultCursor());
		}
	}
};
