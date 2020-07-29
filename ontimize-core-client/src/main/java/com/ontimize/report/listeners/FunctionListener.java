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
import com.ontimize.report.item.SelectableFunctionItem;

public class FunctionListener extends MouseAdapter {

    private static final Logger logger = LoggerFactory.getLogger(FunctionListener.class);

    DefaultReportDialog reportDialog;

    public FunctionListener(DefaultReportDialog reportDialog) {
        this.reportDialog = reportDialog;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if ((e.getModifiers() == InputEvent.META_MASK) || (e.getX() > ReportUtils.LIST_MOUSE_X_MAX)) {
            return;
        }

        this.reportDialog.getFunctionList().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            int index = this.reportDialog.getFunctionList().locationToIndex(e.getPoint());
            if (index < 0) {
                return;
            }
            SelectableFunctionItem it = (SelectableFunctionItem) this.reportDialog.getFunctionList()
                .getModel()
                .getElementAt(index);
            boolean bWillBeSelected = !it.isSelected();

            it.setSelected(bWillBeSelected);
            Rectangle rect = this.reportDialog.getFunctionList().getCellBounds(index, index);
            this.reportDialog.getFunctionList().repaint(rect);

            this.reportDialog.updateReport();
            this.reportDialog.getFunctionList().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (Exception ex) {
            FunctionListener.logger.error(ex.getMessage(), ex);
        } finally {
            this.reportDialog.getFunctionList().setCursor(Cursor.getDefaultCursor());
        }
    }

};
