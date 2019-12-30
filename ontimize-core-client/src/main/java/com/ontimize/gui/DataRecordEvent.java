package com.ontimize.gui;

import java.util.EventObject;
import java.util.Hashtable;

import com.ontimize.db.EntityResult;

public class DataRecordEvent extends EventObject {

	public static final int UPDATE = 0;

	public static final int DELETE = 1;

	public static final int INSERT = 2;

	protected int type = 0;

	protected EntityResult rs = null;

	protected Hashtable keysValues = null;

	protected Hashtable attributesValues = null;

	public DataRecordEvent(Object source, int type, Hashtable kv, Hashtable av, EntityResult rs) {
		super(source);
		this.type = type;
		this.keysValues = kv;
		this.attributesValues = av;
		this.rs = rs;
	}

	public Hashtable getKeysValues() {
		return this.keysValues;
	}

	public Hashtable getAttributesValues() {
		return this.attributesValues;
	}

	public EntityResult getResult() {
		return this.rs;
	}

	public int getType() {
		return this.type;
	}
}
