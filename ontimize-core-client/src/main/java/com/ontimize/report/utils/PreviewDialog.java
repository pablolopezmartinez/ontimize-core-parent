package com.ontimize.report.utils;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentListener;
import java.awt.print.PageFormat;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.table.TableModel;

import com.ontimize.gui.container.EJDialog;

public abstract class PreviewDialog extends EJDialog {

    public PreviewDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public PreviewDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    protected Object emptyReport = null;

    protected boolean allowAddComponentListeners = true;

    public synchronized void setAllowAddComponentListeners(boolean b) {
        this.allowAddComponentListeners = b;
    }

    @Override
    public void addComponentListener(ComponentListener l) {
        if (this.allowAddComponentListeners) {
            super.addComponentListener(l);
        }
    }

    public Action createDefaultCloseAction() {
        return new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
    };

    public abstract void setReport(TableModel m, String xMLTemplate, URL reportBase) throws Exception;

    public abstract void setReport(TableModel m, URL template, URL base, String[] order, boolean[] asc,
            ReportProcessor r, PageFormat pf) throws Exception;

    public abstract void print(boolean showPrintDialog);

    public abstract Object getReport();

}
