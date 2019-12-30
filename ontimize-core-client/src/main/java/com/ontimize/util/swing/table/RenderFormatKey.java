package com.ontimize.util.swing.table;

import java.text.NumberFormat;

import com.ontimize.util.swing.table.PivotTableUtils.NumberRenderer;

public class RenderFormatKey {

	protected String key;
	protected NumberFormat format;
	protected Class renderClass;

	public RenderFormatKey(String key, NumberFormat format, Class renderClass) {
		this.key = key;
		this.format = format;
		this.renderClass = renderClass;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public NumberFormat getFormat() {
		return this.format;
	}

	public void setFormat(NumberFormat format) {
		this.format = format;
	}

	public Class getRenderer() {
		return this.renderClass;
	}

	public void setRenderer(NumberRenderer renderer) {
		this.renderClass = this.renderClass;
	}

}