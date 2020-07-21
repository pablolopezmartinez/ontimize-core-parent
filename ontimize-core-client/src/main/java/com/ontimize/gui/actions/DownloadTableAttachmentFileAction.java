package com.ontimize.gui.actions;

import java.awt.event.ActionEvent;
import java.util.Hashtable;

import com.ontimize.db.EntityResult;
import com.ontimize.gui.Form;
import com.ontimize.gui.table.Table;

public class DownloadTableAttachmentFileAction extends DownloadAttachmentFileAction {

    protected Table table;

    public DownloadTableAttachmentFileAction(String entity, boolean openFile, String sName, boolean bWait,
            String uriSound, boolean askOpen, boolean temporal, Table table) {
        super(entity, openFile, sName, bWait, uriSound, askOpen, temporal);
        this.table = table;
    }

    @Override
    protected String getProposedFileName(ActionEvent e) {
        final Form f = this.getForm(e);
        if ((this.fileFieldName != null) && (this.table != null) && (this.table.getSelectedRowsNumber() > 0)) {
            EntityResult erSelected = new EntityResult(this.table.getSelectedRowData());
            Hashtable recordValues = erSelected.getRecordValues(0);
            return recordValues.get(this.fileFieldName).toString().trim();
        } else {
            return null;
        }
    }

    @Override
    protected Hashtable getAttachmentValuesKeys(Form f) throws Exception {
        // TODO Auto-generated method stub
        Hashtable keys = super.getAttachmentValuesKeys(f);
        if ((this.table != null) && (this.table.getSelectedRowsNumber() > 0)) {

            EntityResult erSelected = new EntityResult(this.table.getSelectedRowData());
            for (int i = 0; i < erSelected.calculateRecordNumber(); i++) {
                Hashtable currentDoc = erSelected.getRecordValues(i);

                for (Object key : this.table.getKeys()) {
                    if (currentDoc.containsKey(key)) {
                        keys.put(key, currentDoc.get(key));
                    }
                }
            }
        }
        return keys;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
    }

}
