package com.ontimize.gui;

import java.io.Serializable;
import java.util.Hashtable;

public class TableMultipleValue implements Serializable {

	protected Object value = null;

	protected Hashtable values = null;

	public TableMultipleValue(Object value) {
		this.value = value;
		this.values = new Hashtable();
	}

	public void put(Object key, Object value) {
		this.values.put(key, value);
	}

	public Hashtable getValues() {
		return this.values;
	}

	public Object getValue() {
		return this.value;
	}
}
