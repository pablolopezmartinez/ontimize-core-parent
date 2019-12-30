package com.ontimize.report.listeners;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.ontimize.report.DefaultReportDialog;
import com.ontimize.report.item.SelectableDynamicItem;

public class DynamicPopupListener extends MouseAdapter {

	DefaultReportDialog reportDialog;

	public DynamicPopupListener(DefaultReportDialog reportDialog) {
		this.reportDialog = reportDialog;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if ((e.getClickCount() == 1) && (e.getModifiers() == InputEvent.META_MASK)) {
			// Checks the selected item
			int x = e.getX();
			int y = e.getY();
			int index = this.reportDialog.getPrintingColumnList().locationToIndex(e.getPoint());
			if (index >= 0) {
				this.reportDialog.setCurrentDynamicItem((SelectableDynamicItem) this.reportDialog.getPrintingColumnList().getModel().getElementAt(index));
				if (this.reportDialog.getCurrentDynamicItem() == null) {
					return;
				}
				if (!this.reportDialog.getSelectedPrintingColumns().contains(this.reportDialog.getCurrentDynamicItem().getText())) {
					return;
				}
				boolean d = this.reportDialog.getCurrentDynamicItem().isDynamic();
				if (d) {
					this.reportDialog.getMultilineMenu().setSelected(true);
				} else {
					this.reportDialog.getSimpleLineMenu().setSelected(true);
				}
				this.reportDialog.getMultilinePopup().setLabel(this.reportDialog.getCurrentDynamicItem().toString());
				this.reportDialog.getMultilinePopup().show(this.reportDialog.getPrintingColumnList(), x, y);
			}
		}
		DefaultReportDialog.checkListStatusButtons(this.reportDialog.getPrintingColumnList(), this.reportDialog.getAllUpButton(), this.reportDialog.getUpButton(),
				this.reportDialog.getDownButton(), this.reportDialog.getAllDownButton());
	}
}
