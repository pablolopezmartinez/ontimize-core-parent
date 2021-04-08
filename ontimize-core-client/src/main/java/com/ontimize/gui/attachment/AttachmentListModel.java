package com.ontimize.gui.attachment;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractListModel;

import com.ontimize.db.EntityResult;
import com.ontimize.db.EntityResultUtils;
import com.ontimize.gui.Form;

/**
 * Model used in the attachment list
 */
public class AttachmentListModel extends AbstractListModel {

    /**
     * {@link EntityResult} containing attachment information
     */
    private EntityResult record = null;

    @Override
    public int getSize() {
        if (this.record == null) {
            return 0;
        } else {
            return this.record.calculateRecordNumber();
        }
    }

    @Override
    public Object getElementAt(int index) {
        Hashtable h = this.record.getRecordValues(index);
        return h.get(Form.ATTACHMENT_ID);
    }


    public void setAttachment(EntityResult res) {
        int end = this.getSize();
        this.record = res;
        if (this.record == null) {
            this.record = new EntityResult();
        }
        this.fireContentsChanged(this, 0, end - 1);
    }


    public Hashtable getRecord(Object o) {
        if (this.record.containsKey(Form.ATTACHMENT_ID)) {
            Vector v = (Vector) this.record.get(Form.ATTACHMENT_ID);
            int index = v.indexOf(o);
            if (index >= 0) {
                return this.record.getRecordValues(index);
            }
        }
        return null;
    }

    public Hashtable getRecord(int i) {
        if (i >= 0) {
            return this.record.getRecordValues(i);
        } else {
            return null;
        }
    }

    public int getRecordIndex(Hashtable kv) {
        return this.record.getRecordIndex(kv);
    }

    public void updateRecord(int i, Hashtable av) {
        EntityResultUtils.updateRecordValues(this.record, av, i);
    }

    public void deleteRecord(int i) {
        this.record.deleteRecord(i);
    }

    public boolean isEmpty() {
        if (this.getSize() == 0) {
            return true;
        }
        return false;
    }

}
