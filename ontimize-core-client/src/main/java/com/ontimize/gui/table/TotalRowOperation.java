package com.ontimize.gui.table;

import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;

public interface TotalRowOperation {

	public Number getOperationValue(List columnValues, Map requiredColumnValues);

	public String getOperationText();

	public JMenuItem getItem();

	public List<String> getRequiredColumns();

}
