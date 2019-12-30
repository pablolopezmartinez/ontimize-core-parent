package com.ontimize.gui.table;

import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;

public interface GroupOperation {

	public Number getOperationValue(List columnValues, List rowIndexes, Map requiredColsValues);

	public int getOperationId();

	public String getOperationText();

	public String getHeaderText();

	public JMenuItem getItem();

	public List<String> getRequiredColumns();
}