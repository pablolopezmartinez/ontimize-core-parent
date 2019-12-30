package com.ontimize.util.swing.popuplist;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractListModel;

import com.ontimize.db.EntityResult;

public class PopupListModel extends AbstractListModel {

	EntityResult record = null;

	Object key = null;

	public PopupListModel() {
		this(null);
	}

	public PopupListModel(Object key) {
		this.key = key;
	}

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
		// return h.get(key);
		return h;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public void setDataModel(EntityResult res) {
		int end = this.getSize();
		this.record = res;
		if (this.record == null) {
			this.record = new EntityResult();
		}
		this.fireContentsChanged(this, 0, end - 1);
	}

	public Hashtable getRegistry(Object o) {
		if (this.record.containsKey(this.key)) {
			Vector v = (Vector) this.record.get(this.key);
			int index = v.indexOf(o);
			if (index >= 0) {
				return this.record.getRecordValues(index);
			}
		}
		return null;
	}

}
