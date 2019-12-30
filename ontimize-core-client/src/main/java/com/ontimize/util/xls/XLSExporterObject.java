package com.ontimize.util.xls;

import java.util.Hashtable;

import javax.swing.table.TableCellRenderer;

public class XLSExporterObject {

	public XLSExporterObject(String columnName, TableCellRenderer rendererColumn, Hashtable properties) {
		this.columnName = columnName;
		this.rendererColumn = rendererColumn;
		this.properties = properties;
	}

	public XLSExporterObject(String columnName, TableCellRenderer rendererColumn) {
		this(columnName, rendererColumn, null);
	}

	protected String columnName;

	protected TableCellRenderer rendererColumn;

	protected Hashtable properties;

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public TableCellRenderer getRendererColumn() {
		return this.rendererColumn;
	}

	public void setRendererColumn(TableCellRenderer rendererColumn) {
		this.rendererColumn = rendererColumn;
	}

	public Hashtable getProperties() {
		return this.properties;
	}

	public void setProperties(Hashtable properties) {
		this.properties = properties;
	}

}
