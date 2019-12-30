package com.ontimize.gui.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.db.NullValue;
import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.i18n.Internationalization;
import com.ontimize.util.JEPUtils;
import com.ontimize.util.math.MathExpressionParser;
import com.ontimize.util.math.MathExpressionParserFactory;

/*
 * Implementation of a table model to represent the basic data types. version 1.0 01/05/2001. Added support to line numbers
 * @deprecated
 */
public class ExtendedTableModel extends AbstractTableModel {

	// Additional total operations registered
	private static final Logger logger = LoggerFactory.getLogger(ExtendedTableModel.class);
	/**
	 * @since 5.2078EN-0.4
	 */
	protected Vector additionalTotalRowOperations = new Vector();

	public Vector getTotalRowOperation() {
		return this.additionalTotalRowOperations;
	}

	public void addTotalRowOperation(TotalRowOperation totalOperation) {
		this.additionalTotalRowOperations.add(totalOperation);
	}

	public static Pattern availableCalculatedColumnNameCharacterPattern = Pattern.compile("[A-Z[a-z[0-9][_]]]");

	public static String ASTERISK = "*";

	public static String TOTAL = "table.total";

	static Hashtable pSumCellRenderer = new Hashtable();

	protected TableCellRenderer sumCurrencyCellRenderer = null;

	protected TableCellRenderer sumCellRenderer = null;

	protected Class[] columnsClass = null;

	protected Vector rowNumbers = new Vector();

	/**
	 * Hashtable with the data model values
	 */
	protected Hashtable data = new Hashtable();

	protected Vector columnNames = null;

	public Vector getColumnNames() {
		return this.columnNames;
	}

	public void setColumnNames(Vector columnNames) {
		this.columnNames = columnNames;
	}

	protected Vector columnTexts = null;

	public Vector getColumnTexts() {
		return this.columnTexts;
	}

	public void setColumnTexts(Vector columnTexts) {
		this.columnTexts = columnTexts;
	}

	protected int rowsNumber = 0;

	public int getRowsNumber() {
		return this.rowsNumber;
	}

	public void setRowsNumber(int rowsNumber) {
		this.rowsNumber = rowsNumber;
	}

	protected int columnsNumber = 0;

	protected Vector editableColumns = new Vector(0);

	protected Vector calculatedColumnsNames = new Vector(0);

	protected Vector calculatedColumnsExpressions = new Vector(0);

	// Required columns for calculated columns
	protected Vector colsReqCalc = new Vector(0);

	protected Vector parsers = new Vector(0);

	protected boolean editable = false;

	/**
	 * Name of the column with the rows number
	 */
	public static final String ROW_NUMBERS_COLUMN = "ROW_NUMBERS_COLUMN";

	/**
	 * @deprecated
	 */
	@Deprecated
	public ExtendedTableModel(Hashtable tableData, Vector columnNames, Vector columnTexts, Hashtable calculatedColumns) {
		this(tableData, columnNames, columnTexts, calculatedColumns, false, null);
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public ExtendedTableModel(Hashtable tableData, Vector columNames, Vector columnTexts, Hashtable calculatedColumns, boolean editable) {
		this(tableData, columNames, columnTexts, calculatedColumns, editable, null);
	}

	public ExtendedTableModel(Hashtable tableData, Vector columnNames, Hashtable calculatedColumns, boolean editable) {
		this(tableData, columnNames, (Vector) columnNames.clone(), calculatedColumns, editable);
	}

	public ExtendedTableModel(Hashtable tableData, Vector columnNames, Hashtable calculatedColumns, boolean editable, Vector colsReqCalc) {
		this(tableData, columnNames, (Vector) columnNames.clone(), calculatedColumns, editable, colsReqCalc);
	}

	/*
	 * Constructor: 'tableData' contains all the table data. It uses a Hashtable because this is the object that a database query returns. The keys in the Hashtable are the column
	 * names, and values are vectors with each column data. It is possible that the vector contains null values.
	 * @deprecated
	 */
	public ExtendedTableModel(Hashtable tableData, Vector columnNames, Vector columnTexts, Hashtable calculatedColumns, boolean editable, Vector colsReqCalc) {
		this.colsReqCalc = colsReqCalc;
		this.editable = editable;

		this.columnNames = new Vector(columnNames.size() + 1);
		this.columnTexts = new Vector(columnTexts.size() + 1);
		for (int i = 0; i < columnNames.size(); i++) {
			this.columnNames.add(i, columnNames.get(i));
		}
		for (int i = 0; i < columnTexts.size(); i++) {
			this.columnTexts.add(i, columnTexts.get(i));
		}
		// Adds the column name
		this.columnNames.add(0, ExtendedTableModel.ROW_NUMBERS_COLUMN);
		this.columnTexts.add(0, ExtendedTableModel.ROW_NUMBERS_COLUMN);

		// In the model, if calculated columns exist they are at the end
		if (calculatedColumns != null) {
			Enumeration enumKeys = calculatedColumns.keys();
			while (enumKeys.hasMoreElements()) {
				Object col = enumKeys.nextElement();
				Object expr = calculatedColumns.get(col);
				if ((col != null) && (expr != null)) {
					this.calculatedColumnsNames.add(this.calculatedColumnsNames.size(), col);
					this.calculatedColumnsExpressions.add(this.calculatedColumnsExpressions.size(), expr);
				}
			}

			// This is here (before create the calculated column names vector to
			// add the calculated columns as variables to the parser too
			for (int i = 0; i < this.calculatedColumnsNames.size(); i++) {
				MathExpressionParser parser = this.createParser(this.calculatedColumnsNames.get(i), this.calculatedColumnsExpressions.get(i));
				this.parsers.add(parser);
			}
		}

		this.columnsNumber = this.columnNames.size() + this.calculatedColumnsNames.size();
		Enumeration enumKeys = tableData.keys();
		while (enumKeys.hasMoreElements()) {
			Object oKey = enumKeys.nextElement();
			// Value must be a Vector
			Object oValue = tableData.get(oKey);
			if (oValue instanceof Vector) {
				this.rowsNumber = Math.max(((Vector) oValue).size(), this.rowsNumber);
				// Adds the data to the model
				if (columnNames.contains(oKey)) {
					this.data.put(oKey, oValue);
				}
			}
		}
		// If some of the columns is empty put an empty vector
		for (int i = 0; i < this.columnNames.size(); i++) {
			Object oColumn = this.columnNames.get(i);
			if (!this.data.containsKey(oColumn)) {
				Vector v = new Vector(this.rowsNumber);
				for (int j = 0; j < this.rowsNumber; j++) {
					v.add(j, null);
				}
				this.data.put(oColumn, v);
			}
		}

		this.setData(this.data);
		this.columnsClass = null;
	}

	private MathExpressionParser createParser(Object col, Object expr) {

		MathExpressionParser parser = MathExpressionParserFactory.getInstance();
		parser.setTraverse(ApplicationManager.DEBUG);
		Hashtable custom = JEPUtils.getCustomFunctions();
		Enumeration keys = custom.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			ExtendedTableModel.logger.debug("Add expression parser function: {} -> {}", key, custom.get(key));
			try {
				parser.addFunction(key, custom.get(key));
			} catch (java.lang.NoSuchMethodError e) {
				ExtendedTableModel.logger.error(null, e);
			}
		}

		for (int i = 1; i < this.columnNames.size(); i++) {
			parser.addVariable(this.columnNames.get(i).toString(), 0.0);
		}

		// TODO review
		if (this.calculatedColumnsNames != null) {
			for (int i = 0; i < this.calculatedColumnsNames.size(); i++) {
				parser.addVariable(this.calculatedColumnsNames.get(i).toString(), 0.0);
			}
		}

		parser.parseExpression(expr.toString());
		if (parser.hasError()) {
			ExtendedTableModel.logger.debug("Error in calculated column: {}. Expression: {}. Error: {}", col, expr, parser.getErrorInfo());
		}

		return parser;

	}

	@Override
	public int getRowCount() {
		return this.rowNumbers.size();
	}

	public Hashtable getData() {
		Hashtable totalData = (Hashtable) this.data.clone();
		// Put the calculated columns
		if (this.calculatedColumnsNames != null) {
			int n = this.columnNames.size();
			for (int i = 0; i < this.calculatedColumnsNames.size(); i++) {
				Vector dataCol = new Vector();
				Object nameCol = this.calculatedColumnsNames.get(i);
				// Get the value of each row
				for (int j = 0; j < this.rowsNumber; j++) {
					dataCol.add(j, this.getValue(j, n + i));
				}
				totalData.put(nameCol, dataCol);
			}
		}
		return totalData;
	}

	protected void deleteInnerRow(int row) {
		Enumeration enumKeys = this.data.keys();
		while (enumKeys.hasMoreElements()) {
			Object oKey = enumKeys.nextElement();
			Object oValue = this.data.get(oKey);
			if (oValue instanceof Vector) {
				Vector columnData = (Vector) oValue;
				if (row < columnData.size()) {
					columnData.remove(row);
				}
			}
		}
	}

	public void deleteRows(int[] rows) {
		// Sort the values starting with the least
		Arrays.sort(rows);

		// Fire the events for the delete rows.
		if ((rows != null) && (rows.length > 0)) {

			for (int i = rows.length - 1; i >= 0; i--) {
				this.deleteRow(rows[i]);
			}
		}
	}

	public void deleteRow(int row) {
		this.deleteInnerRow(row);
		this.updateRowsNumbers();
		this.fireTableChanged(new TableModelEvent(this, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
	}

	private void updateRowsNumbers() {
		this.rowsNumber = 0;
		Enumeration enumKeys = this.data.keys();
		while (enumKeys.hasMoreElements()) {
			Object oKey = enumKeys.nextElement();
			// Value must be a vector
			Object oValue = this.data.get(oKey);
			if (oValue instanceof Vector) {
				int previousNumber = this.rowsNumber;
				this.rowsNumber = Math.max(((Vector) oValue).size(), this.rowsNumber);
				if (previousNumber != 0) {
					if (this.rowsNumber != previousNumber) {
						// TODO translate the message
						ExtendedTableModel.logger.error("No all the vectors with the column information have the same size. Wrong key: {} ", oKey);
						if (this.rowsNumber < 100) {
							ExtendedTableModel.logger.debug(oValue.toString());
						}
					}
				}
			}
		}

		// If the new rows number is 1 then update the columns class cache:
		if (this.rowsNumber == 1) {
			this.updateColumnClasses();
		}
		if (this.rowNumbers != null) {
			this.rowNumbers.clear();
		}
		this.rowNumbers = new Vector(this.rowsNumber + 10);
		for (int i = 0; i < this.rowsNumber; i++) {
			this.rowNumbers.add(new Integer(i + 1));
		}
	}

	private void updateColumnClasses() {
		this.columnsClass = new Class[this.getColumnCount()];
		synchronized (this.columnsClass) {
			this.columnsClass[0] = RowHeadCellRenderer.class;
			for (int i = 1; i < this.columnNames.size(); i++) {
				String columnName = this.columnNames.get(i).toString();
				Object oValue = this.data.get(columnName);
				// Value must be a vector
				if (oValue == null) {
					this.columnsClass[i] = Object.class;
				} else {
					if (((Vector) oValue).size() <= 0) {
						this.columnsClass[i] = Object.class;
					} else {
						// by default
						this.columnsClass[i] = Object.class;
						for (int j = 0; j < ((Vector) oValue).size(); j++) {
							Object valueI = ((Vector) oValue).get(j);
							if (valueI != null) {
								this.columnsClass[i] = valueI.getClass();
								break;
							}
						}
					}
				}
			}
			for (int i = this.columnNames.size(); i < this.columnsClass.length; i++) {
				this.columnsClass[i] = Double.class;
			}
		}
	}

	@Override
	public int getColumnCount() {
		return this.columnsNumber;
	}

	protected void updateColumnCount() {
		this.columnsNumber = this.columnNames.size() + this.calculatedColumnsNames.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		// For the column 0 return the row numbers
		if (row < this.rowsNumber) {
			if (column == 0) {
				// Here better an int that an Integer
				return this.rowNumbers.get(row);
			}
			// Return the value for this row and column.
			return this.getValue(row, column);
		} else {
			if (row == this.rowsNumber) {
				if (column == 0) {
					return ExtendedTableModel.TOTAL;
				} else {
					return this.getValue(row, column);
				}
			} else if (row == (this.rowsNumber + 1)) {
				if (column == 0) {
					return ExtendedTableModel.ASTERISK;
				} else {
					return this.getValue(row, column);
				}
			}
			return null;
		}
	}

	protected Object getValue(int row, int column) {
		if (column < this.columnNames.size()) {
			Object oColumnData = this.data.get(this.columnNames.get(column));

			if (oColumnData == null) {
				return null;
			}
			if (oColumnData instanceof Vector) {
				Vector vColData = (Vector) oColumnData;
				if (row >= vColData.size()) {
					if (ApplicationManager.DEBUG) {
						ExtendedTableModel.logger
								.debug("Requeste value for row: " + row + " and column: " + this.columnNames.get(column) + " . Vector size is : " + vColData.size());
					}
					return null;
				} else {
					Object oValue = vColData.get(row);
					return oValue;
				}
			} else {
				return null;
			}
		} else {
			// If the column is a calculated column
			Object oColumnName = this.calculatedColumnsNames.get(column - this.columnNames.size());
			Object expression = this.calculatedColumnsExpressions.get(column - this.columnNames.size());
			boolean someNull = false;
			for (int i = 0; i < this.colsReqCalc.size(); i++) {
				String c = (String) this.colsReqCalc.get(i);
				if (ExtendedTableModel.expressionContainsColName(c, expression.toString(), ExtendedTableModel.availableCalculatedColumnNameCharacterPattern)) {
					int columnIndex = this.columnNames.indexOf(c);
					if (columnIndex >= 0) {
						Object oValue = this.getValueAt(row, columnIndex);
						if (oValue == null) {
							someNull = true;
							break;
						}
					}
				}
			}
			if (someNull) {
				return null;
			}

			MathExpressionParser parser = (MathExpressionParser) this.parsers.get(column - this.columnNames.size());

			Hashtable rowValuesForExpression = this.getRowValuesForExpression((String) this.calculatedColumnsExpressions.get(column - this.columnNames.size()), row);
			Enumeration columnKeys = rowValuesForExpression.keys();
			while (columnKeys.hasMoreElements()) {
				String col = (String) columnKeys.nextElement();
				Object oValue = rowValuesForExpression.get(col);
				if ((oValue != null) && (oValue instanceof Number)) {
					parser.addVariableAsObject(col.toString(), new Double(((Number) oValue).doubleValue()));
				} else {
					if (oValue != null) {
						parser.addVariableAsObject(col.toString(), oValue);
					} else {
						parser.addVariable(col.toString(), 0.0);
					}
				}
			}

			if (parser.hasError()) {
				if (ApplicationManager.DEBUG) {
					ExtendedTableModel.logger.debug(
							this.getClass().toString() + ". Error in calculated column: " + oColumnName + ". Expression: " + expression + ". Error: " + parser.getErrorInfo());
				}
			}

			return parser.getValueAsObject();
		}
	}

	protected Hashtable getRowValuesForExpression(String expression, int row) {
		Hashtable values = new Hashtable();

		for (int i = 0; i < this.columnsNumber; i++) {
			String col = this.getColumnName(i);
			if (ExtendedTableModel.expressionContainsColName(col, expression, ExtendedTableModel.availableCalculatedColumnNameCharacterPattern)) {
				Object oValue = this.getValueAt(row, i);
				if ((oValue != null) && (oValue instanceof Number)) {
					values.put(col, new Double(((Number) oValue).doubleValue()));
				} else if (oValue != null) {
					values.put(col, oValue);
				} else {
					values.put(col, new Double(0.0));
				}
			}
		}
		return values;
	}

	@Override
	public String getColumnName(int index) {
		try {
			if (index < this.columnNames.size()) {
				return (String) this.columnNames.get(index);
			} else {
				return (String) this.calculatedColumnsNames.get(index - this.columnNames.size());
			}
		} catch (Exception e) {
			ExtendedTableModel.logger.trace(null, e);
			return super.getColumnName(index);
		}
	}

	public String getColumnIdentifier(int index) {
		try {
			if (index < this.columnNames.size()) {
				return (String) this.columnNames.get(index);
			} else {
				return (String) this.calculatedColumnsNames.get(index - this.columnNames.size());
			}
		} catch (Exception e) {
			ExtendedTableModel.logger.trace(null, e);
			return null;
		}
	}

	/**
	 * Overwrite the method to set the appropriate renderer to the supported data types. DefaultCellRenderer is used for all the not supported data types.
	 */
	@Override
	public Class getColumnClass(int column) {
		if (this.columnsClass == null) {
			this.updateColumnClasses();
			return this.columnsClass[column];
		} else {
			try {
				return this.columnsClass[column];
			} catch (Exception e) {
				if (ApplicationManager.DEBUG) {
					ExtendedTableModel.logger.debug("Error getting column class for column index:" + column, e);
				}
				return null;
			}
		}
	}

	public void setEditableColumn(Object id) {
		if (id.equals(ExtendedTableModel.ROW_NUMBERS_COLUMN)) {
			return;
		}
		if (!this.editableColumns.contains(id)) {
			this.editableColumns.add(id);
		}
	}

	public void setEditableColumn(Object id, boolean editable) {
		if (editable) {
			this.setEditableColumn(id);
		} else {
			this.removeEditableColumn(id);
		}
	}

	public void removeEditableColumn(Object id) {
		if (this.editableColumns.contains(id)) {
			this.editableColumns.remove(id);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column < this.columnNames.size()) {
			Object columName = this.columnNames.get(column);
			if (this.editableColumns.contains(columName.toString())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public synchronized void setValueAt(Object value, int row, int column) {
		this.setValueAt(value, row, column, true);
	}

	public synchronized void setValueAt(Object value, int row, int column, boolean fireEvents) {
		if (column < this.columnNames.size()) {
			Vector vData = (Vector) this.data.get(this.getColumnIdentifier(column));
			if (vData != null) {
				if (row < this.rowsNumber) {
					vData.remove(row);
					vData.add(row, value);
				} else {
					vData.add(row, value);
				}
			} else {
				Vector v = new Vector();
				v.setSize(this.rowsNumber);
				v.setElementAt(value, row);
				this.data.put(this.columnNames.get(column), v);
			}
			if (fireEvents) {
				this.fireTableCellUpdated(row, column);
			}
		}
	}

	public Hashtable getRowData(int[] rows) {
		if (rows == null) {
			return null;
		} else {
			Hashtable hRowValues = new Hashtable();
			Enumeration c = this.data.keys();
			while (c.hasMoreElements()) {
				Object oKey = c.nextElement();

				// Data for the column with the row number are not included
				if (oKey.equals(ExtendedTableModel.ROW_NUMBERS_COLUMN)) {
					continue;
				}

				Vector vValues = (Vector) this.data.get(oKey);
				Vector vColumnValues = new Vector();
				for (int j = 0; j < rows.length; j++) {
					if (rows[j] >= vValues.size()) {
						if (ApplicationManager.DEBUG) {
							ExtendedTableModel.logger
									.debug(this.getClass().toString() + ": the row index is bigger than the max rows in the table" + rows[j] + "/" + vValues.size());
						}
						continue;
					}
					Object oRowValue = vValues.get(rows[j]);
					vColumnValues.add(oRowValue);
				}
				hRowValues.put(oKey, vColumnValues);
			}
			return hRowValues;
		}
	}

	public Hashtable getRowDataForKeys(List keys, Hashtable keysValues) {
		boolean allKeysMatch = true;
		for (int i = 0; i < this.getRowCount(); i++) {
			Iterator iKeys = keys.iterator();
			allKeysMatch = true;
			while (iKeys.hasNext()) {
				Object key = iKeys.next();
				Object keyValue = keysValues.get(key);
				Vector list = (Vector) this.data.get(key);
				if (list == null) {
					allKeysMatch = false;
					break;
				}
				Object current = list.get(i);
				if ((current == null) && (keyValue == null)) {
					continue;
				}

				if ((keyValue != null) && keyValue.equals(current)) {
					continue;
				}
				allKeysMatch = false;
				break;
			}

			if (allKeysMatch) {
				return this.getRowData(i);
			}
		}

		return new Hashtable();
	}

	public Hashtable getRowData(int row) {
		if (row < 0) {
			return null;
		} else {
			Hashtable hRowValues = new Hashtable();
			Enumeration enumKeys = this.data.keys();
			while (enumKeys.hasMoreElements()) {
				Object oKey = enumKeys.nextElement();

				// Data for the column with the row number are not included
				if (oKey.equals(ExtendedTableModel.ROW_NUMBERS_COLUMN)) {
					continue;
				}

				Vector vValues = (Vector) this.data.get(oKey);
				if (row >= vValues.size()) {
					if (ApplicationManager.DEBUG) {
						ExtendedTableModel.logger.debug(this.getClass().toString() + ": the row index is bigger than the max rows in the table." + row + "/" + vValues.size());
					}
					continue;
				}
				Object oRowValue = vValues.get(row);
				if (oRowValue != null) {
					hRowValues.put(oKey, oRowValue);
				}
			}
			return hRowValues;
		}
	}

	public Hashtable getCalculatedRowData(int rowIndex) {
		if (rowIndex < 0) {
			return null;
		} else {
			Hashtable hRowValues = new Hashtable();
			for (int i = 0; i < this.calculatedColumnsNames.size(); i++) {
				int column = this.columnNames.size() + i;
				String sKey = this.getColumnIdentifier(column);
				Object v = this.getValue(rowIndex, column);
				if (v != null) {
					hRowValues.put(sKey, v);
				}
			}
			return hRowValues;
		}
	}

	public void updateRowData(Hashtable rowData, Hashtable keysValues) {
		this.updateRowData(rowData, null, keysValues);
	}

	public void updateRowData(Hashtable rowData, List columns, Hashtable keysValues) {
		if (keysValues.size() == 0) {
			return;
		}
		ArrayList keyList = Collections.list(keysValues.keys());
		Vector vKey = (Vector) this.data.get(keyList.get(0));
		Object oKeyValue = keysValues.get(keyList.get(0));
		if ((vKey == null) || (oKeyValue == null)) {
			return;
		}
		for (int i = 0; i < vKey.size(); i++) {
			if (((oKeyValue == null) && (vKey == null)) || oKeyValue.equals(vKey.get(i))) {
				boolean keysMatch = true;
				for (int j = 1; j < keyList.size(); j++) {
					Object oKeyName = keyList.get(j);
					Vector v = (Vector) this.data.get(oKeyName);
					if (v == null) {
						return;
					}
					Object oSentValue = keysValues.get(oKeyName);
					Object oValue = v.get(i);
					if (((oSentValue == null) && (oValue != null)) || ((oSentValue != null) && (oValue == null)) || !oSentValue.equals(oValue)) {
						keysMatch = false;
						break;
					}
				}
				if (keysMatch) {
					// Is the row number i
					Enumeration enumKeys = this.data.keys();
					while (enumKeys.hasMoreElements()) {
						Object oKeyl = enumKeys.nextElement();
						Vector vData = (Vector) this.data.get(oKeyl);
						if (vData.size() <= i) {
							ExtendedTableModel.logger
									.debug(this.getClass().toString() + " -> Data vector for the column: " + oKeyl + " has not the required element number " + vData.size());
							vData.setSize(i + 1);
						}
						if ((columns != null) && !columns.contains(oKeyl)) {
							continue;
						}
						vData.setElementAt(rowData.get(oKeyl), i);
					}
					this.fireTableCellUpdated(i, TableModelEvent.ALL_COLUMNS);
					return;
				}
			}
		}
	}

	public void updateRowData(Hashtable rowData, Vector keys) {
		if (keys.size() == 0) {
			return;
		}
		Vector vKey = (Vector) this.data.get(keys.get(0));
		Object oKeyValue = rowData.get(keys.get(0));
		if ((vKey == null) || (oKeyValue == null)) {
			return;
		}
		for (int i = 0; i < vKey.size(); i++) {
			if (((oKeyValue == null) && (vKey == null)) || oKeyValue.equals(vKey.get(i))) {
				boolean keysMatch = true;
				for (int j = 1; j < keys.size(); j++) {
					Object oKeyName = keys.get(j);
					Vector v = (Vector) this.data.get(oKeyName);
					if (v == null) {
						return;
					}
					Object oSentValue = rowData.get(oKeyName);
					Object oValue = v.get(i);
					if (((oSentValue == null) && (oValue != null)) || ((oSentValue != null) && (oValue == null)) || !oSentValue.equals(oValue)) {
						keysMatch = false;
						break;
					}
				}
				if (keysMatch) {
					// It is the row number i
					Enumeration enumKeys = this.data.keys();
					while (enumKeys.hasMoreElements()) {
						Object oKeyl = enumKeys.nextElement();
						Vector vData = (Vector) this.data.get(oKeyl);
						if (vData.size() <= i) {
							ExtendedTableModel.logger
									.debug(this.getClass().toString() + " -> Data vector for the column: " + oKeyl + " has not the required element number " + vData.size());
							vData.setSize(i + 1);
						}
						vData.setElementAt(rowData.get(oKeyl), i);
					}
					this.fireTableCellUpdated(i, TableModelEvent.ALL_COLUMNS);
					return;
				}
			}
		}
	}

	public void addRow(Hashtable rowData) {
		this.addInnerRow(rowData);
		this.updateRowsNumbers();
		this.fireTableChanged(new TableModelEvent(this, this.getRowCount() - 1, this.getRowCount() - 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	protected void addInnerRow(Hashtable rowData) {
		if (rowData == null) {
			return;
		}
		// For each column search the value and add it to the data
		for (int i = 0; i < this.columnNames.size(); i++) {
			Object oColumnName = this.columnNames.get(i);
			if (!oColumnName.equals(ExtendedTableModel.ROW_NUMBERS_COLUMN)) {
				Object oVectorValue = this.data.get(this.columnNames.get(i));
				if (oVectorValue != null) {
					((Vector) oVectorValue).add(((Vector) oVectorValue).size(), rowData.get(this.columnNames.get(i)));
				} else {
					Vector aux = new Vector(this.getRowCount());
					for (int k = 0; k < this.getRowCount(); k++) {
						aux.add(null);
					}
					aux.add(aux.size(), rowData.get(this.columnNames.get(i)));
					this.data.put(this.columnNames.get(i), aux);
				}
			}
		}
	}

	/**
	 * @param rowValues
	 */
	public void addRows(Vector rowValues) {
		int oldRowNumber = this.getRowCount();
		if ((rowValues == null) || (rowValues.size() == 0)) {
			return;
		}
		for (int i = 0; i < rowValues.size(); i++) {
			Hashtable rowData = (Hashtable) rowValues.get(i);
			if (rowData != null) {
				this.addInnerRow(rowData);
			}
		}
		this.updateRowsNumbers();
		this.fireTableChanged(new TableModelEvent(this, oldRowNumber, oldRowNumber + rowValues.size(), TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	public void addRows(int[] pos, Vector rowsData) {
		this.addRows(rowsData);
	}

	public void addRow(int index, Hashtable rowData) {
		if (rowData == null) {
			return;
		}
		// For each column search the value and add it to the data
		for (int i = 0; i < this.columnNames.size(); i++) {
			Object oColumnName = this.columnNames.get(i);
			if (!oColumnName.equals(ExtendedTableModel.ROW_NUMBERS_COLUMN)) {
				Object oVectorValue = this.data.get(this.columnNames.get(i));
				if (oVectorValue != null) {
					if (index < 0) {
						index = 0;
					}
					if (index > ((Vector) oVectorValue).size()) {
						index = ((Vector) oVectorValue).size();
					}
					((Vector) oVectorValue).add(index, rowData.get(this.columnNames.get(i)));
				} else {
					Vector aux = new Vector();
					index = 0;
					aux.add(index, rowData.get(this.columnNames.get(i)));
					this.data.put(this.columnNames.get(i), aux);
				}
			}
		}
		this.updateRowsNumbers();
		this.fireTableChanged(new TableModelEvent(this, index, index, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	public void setData(Hashtable data) {
		if (data == null) {
			this.data = new Hashtable();
		} else {
			this.data = data;
		}

		this.columnsClass = null;
		this.updateRowsNumbers();
		this.fireTableChanged(new TableModelEvent(this));
	}

	public TableCellRenderer getSumCellRenderer(boolean currency, ResourceBundle bundle) {
		if (currency) {
			if (this.sumCurrencyCellRenderer == null) {
				this.sumCurrencyCellRenderer = new SumCurrencyCellRenderer();
			}
			if (this.sumCurrencyCellRenderer instanceof Internationalization) {
				((Internationalization) this.sumCurrencyCellRenderer).setResourceBundle(bundle);
				((Internationalization) this.sumCurrencyCellRenderer).setComponentLocale(bundle.getLocale());

			}
			return this.sumCurrencyCellRenderer;
		} else {
			if (this.sumCellRenderer == null) {
				this.sumCellRenderer = new SumCellRenderer();
			}
			if (this.sumCellRenderer instanceof Internationalization) {
				((Internationalization) this.sumCellRenderer).setResourceBundle(bundle);
				((Internationalization) this.sumCellRenderer).setComponentLocale(bundle.getLocale());
			}
			return this.sumCellRenderer;
		}
	}

	public void addColumn(String col) {
		this.addColumn(col, true);
	}

	public void addColumn(String col, boolean fireEvent) {
		// Add a new column if this does not exist
		ExtendedTableModel.logger.debug("Adding column: {}. Previous column count = {}", col, this.columnsNumber);
		if (!this.columnNames.contains(col)) {
			this.columnNames.add(col);
			this.columnTexts.add(col);
			Vector v = new Vector();
			for (int i = 0; i < this.rowsNumber; i++) {
				v.add(i, null);
			}
			if (this.data != null) {
				this.data.put(col, v);
			}
			this.updateColumnCount();
			this.updateColumnClasses();
			if (fireEvent) {
				this.fireTableStructureChanged();
			}
			ExtendedTableModel.logger.debug("Added column: {}. Current column count = {}", col, this.columnsNumber);
		}
	}

	public void addCalculatedColumn(String col, String expression) {
		// Add a new column if this does not exist
		if (ApplicationManager.DEBUG) {
			ExtendedTableModel.logger.debug(this.getClass().getName() + " Adding calculated column: " + col + ". Previous column count = " + this.columnsNumber);
		}

		if (!this.calculatedColumnsNames.contains(col)) {
			this.calculatedColumnsNames.add(col);
			this.calculatedColumnsExpressions.add(expression);

			MathExpressionParser parser = this.createParser(col, expression);
			this.parsers.add(parser);

			Vector v = new Vector();
			for (int i = 0; i < this.rowsNumber; i++) {
				v.add(i, null);
			}
			if (this.data != null) {
				this.data.put(col, v);
			}
			this.updateColumnCount();
			this.updateColumnClasses();
			this.fireTableStructureChanged();
			if (ApplicationManager.DEBUG) {
				ExtendedTableModel.logger.debug(this.getClass().getName() + " Added calculated column: " + col + ". Current column count = " + this.columnsNumber);
			}
		}
	}

	public void deleteColumn(String col) {
		this.deleteColumn(col, true);
	}

	public void deleteColumn(String col, boolean fireEvent) {
		if (ApplicationManager.DEBUG) {
			ExtendedTableModel.logger.debug(this.getClass().getName() + " Removing column: " + col + ". Previous column count = " + this.columnsNumber);
		}
		if (this.columnNames.contains(col)) {
			this.columnNames.remove(col);
			this.columnTexts.remove(col);
			this.updateColumnCount();
			this.updateColumnClasses();
			if (fireEvent) {
				this.fireTableStructureChanged();
			}
			if (ApplicationManager.DEBUG) {
				ExtendedTableModel.logger.debug(this.getClass().getName() + " Removed column: " + col + ". Current column count = " + this.columnsNumber);
			}
		}
	}

	public void deleteCalculatedColumn(String col) {
		if (ApplicationManager.DEBUG) {
			ExtendedTableModel.logger.debug(this.getClass().getName() + " Removing calculated column: " + col + ". Previous column count = " + this.columnsNumber);
		}
		int index = this.calculatedColumnsNames.indexOf(col);
		if (index >= 0) {
			this.calculatedColumnsNames.remove(index);
			this.calculatedColumnsExpressions.remove(index);
			this.parsers.remove(index);

			this.updateColumnCount();
			this.updateColumnClasses();
			this.fireTableStructureChanged();
			if (ApplicationManager.DEBUG) {
				ExtendedTableModel.logger.debug(this.getClass().getName() + " Removed calculated column: " + col + ". Current column count = " + this.columnsNumber);
			}
		}
	}

	public Hashtable getCalculatedColumns() {
		Hashtable hC = new Hashtable();
		for (int i = 0; i < this.calculatedColumnsNames.size(); i++) {
			hC.put(this.calculatedColumnsNames.get(i), this.calculatedColumnsExpressions.get(i));
		}
		return hC;
	}

	public Vector getCalculatedColumnsName() {
		return this.calculatedColumnsNames;
	}

	public Vector getRequiredColumnsToCalculatedColumns() {
		return this.colsReqCalc;
	}

	public String getCalculatedColumnExpression(String col) {
		if (col == null) {
			return null;
		}
		for (int i = 0; i < this.calculatedColumnsNames.size(); i++) {
			if (col.equals(this.calculatedColumnsNames.get(i))) {
				return (String) this.calculatedColumnsExpressions.get(i);
			}
		}
		return null;
	}

	public void setCalculatedColumnExpression(String col, String expression) {
		if (this.calculatedColumnsNames != null) {
			int index = this.calculatedColumnsNames.indexOf(col);
			if (index >= 0) {
				this.calculatedColumnsExpressions.setElementAt(expression, index);
			}
			MathExpressionParser parser = this.createParser(col, expression);
			this.parsers.setElementAt(parser, index);
		}
	}

	public Object getCalculatedValue(int column, Hashtable rowValues) {
		Object oColumnName = this.calculatedColumnsNames.get(column - this.columnNames.size());
		Object expression = this.calculatedColumnsExpressions.get(column - this.columnNames.size());
		boolean someNull = false;
		for (int i = 0; i < this.colsReqCalc.size(); i++) {
			String c = (String) this.colsReqCalc.get(i);
			if (ExtendedTableModel.expressionContainsColName(c, expression.toString(), ExtendedTableModel.availableCalculatedColumnNameCharacterPattern)) {
				Object oValue = rowValues.get(c);
				if (oValue == null) {
					someNull = true;
					break;
				}
			}
		}
		if (someNull) {
			return null;
		}

		MathExpressionParser parser = (MathExpressionParser) this.parsers.get(column - this.columnNames.size());
		// Adds the column values

		Vector allColumns = new Vector(this.columnNames);
		if (this.calculatedColumnsNames != null) {
			allColumns.addAll(this.calculatedColumnsNames);
		}
		for (int i = 1; i < allColumns.size(); i++) {
			Object col = allColumns.get(i);
			Object oValue = rowValues.get(col);
			if ((oValue != null) && (oValue instanceof Number)) {
				parser.addVariableAsObject(col.toString(), new Double(((Number) oValue).doubleValue()));
			} else {
				if ((oValue != null) && !(oValue instanceof NullValue)) {
					parser.addVariableAsObject(col.toString(), oValue);
				} else {
					parser.addVariable(col.toString(), 0.0);
				}
			}
		}
		if (parser.hasError()) {
			if (ApplicationManager.DEBUG) {
				ExtendedTableModel.logger
						.debug(this.getClass().toString() + ". Error in calculated column: " + oColumnName + ". Expression: " + expression + ". Error: " + parser.getErrorInfo());
			}
		}
		Object valueAsObject = parser.getValueAsObject();
		if ((valueAsObject != null) && (valueAsObject instanceof Number)) {
			if (Double.isNaN(((Number) valueAsObject).doubleValue())) {
				return null;
			}
		}
		return valueAsObject;
	}

	public static boolean expressionContainsColName(String colName, String expression, Pattern validCharactersInColumnName) {
		int index = expression.indexOf(colName);
		while (index >= 0) {
			// If the expression contains the column then can be this columns
			// exactly or some similar
			// e.g Expression = 2*TEST2 contains the text TEST but not the
			// TEST columns (contains TEST2 but not TEST)
			char previousChar = index > 0 ? expression.charAt(index - 1) : '(';
			char nextChar = (index + colName.length()) < expression.length() ? expression.charAt(index + colName.length()) : ')';

			Matcher matcherPrev = validCharactersInColumnName.matcher("" + previousChar);
			Matcher matcherNext = validCharactersInColumnName.matcher("" + nextChar);
			if (matcherPrev.find() || matcherNext.find()) {
				expression = expression.substring(index + colName.length());
			} else {
				return true;
			}
			index = expression.indexOf(colName);
		}
		return false;
	}

	public static final String SUM_OPERATION = "SUM";
	public static final String AVG_OPERATION = "AVG";
	public static final String MAX_OPERATION = "MAX";
	public static final String MIN_OPERATION = "MIN";
	public static final String CONCAT_OPERATION = "CONCAT";

	public Object getColumnOperation(String columnIdentifier, String operation) {
		if (ExtendedTableModel.SUM_OPERATION.equalsIgnoreCase(operation)) {
			return this.getColumnSumAverage(columnIdentifier, false);
		} else if (ExtendedTableModel.MAX_OPERATION.equalsIgnoreCase(operation)) {
			return this.getColumnMaximumMinimum(columnIdentifier, true);
		} else if (ExtendedTableModel.MIN_OPERATION.equalsIgnoreCase(operation)) {
			return this.getColumnMaximumMinimum(columnIdentifier, false);
		} else if (ExtendedTableModel.AVG_OPERATION.equalsIgnoreCase(operation)) {
			return this.getColumnSumAverage(columnIdentifier, true);
		} else if (ExtendedTableModel.CONCAT_OPERATION.equalsIgnoreCase(operation)) {
			return this.getColumnConcat(columnIdentifier);
		} else {
			return this.getTotalRowOperation(columnIdentifier, operation);
		}

		// return getColumnSumAverage(columnIdentifier,false);
	}

	protected Object getTotalRowOperation(String columnIdentifier, String operation) {
		for (int j = 0; j < this.additionalTotalRowOperations.size(); j++) {
			TotalRowOperation totalRowOperation = (TotalRowOperation) this.additionalTotalRowOperations.get(j);
			if (operation.equalsIgnoreCase(totalRowOperation.getOperationText())) {
				// find column index
				int colIndex = this.getColumnIndex(columnIdentifier);
				if (colIndex < 0) {
					ExtendedTableModel.logger.debug(ExtendedTableModel.class.getName() + ":" + columnIdentifier + " column name doesn't exist in table model");
					return null;
				}
				// get column values
				List listValues = new ArrayList();
				Map<String, List> requiredColumnValues = new HashMap<String, List>();

				List<String> columns = totalRowOperation.getRequiredColumns();
				int[] columnIndexes = null;
				if ((columns != null) && (columns.size() > 0)) {
					columnIndexes = new int[columns.size()];
					for (int i = 0; i < columns.size(); i++) {
						int currentIndex = this.getColumnIndex(columns.get(i));
						if (currentIndex < 0) {
							ExtendedTableModel.logger.debug(ExtendedTableModel.class.getName() + ":" + columns.get(i) + " column name doesn't exist in table model");
							return null;
						}
						columnIndexes[i] = currentIndex;
						requiredColumnValues.put(columns.get(i), new ArrayList<Number>());
					}
				} else {
					columnIndexes = new int[0];
				}

				for (int i = 0; i < this.rowsNumber; i++) {
					Object oValue = this.getValue(i, colIndex);
					if (oValue == null) {
						continue;
					}
					if (!(oValue instanceof Number)) {
						ExtendedTableModel.logger.debug(ExtendedTableModel.class.getName() + ":" + columnIdentifier + "in row " + i + " isn't a NUMBER instance.");
						return null;
					}
					listValues.add(oValue);

					for (int k = 0; k < columnIndexes.length; k++) {
						oValue = this.getValue(i, columnIndexes[k]);
						if (oValue == null) {
							continue;
						}
						if (!(oValue instanceof Number)) {
							ExtendedTableModel.logger.debug(ExtendedTableModel.class.getName() + ":" + columns.get(k) + "in row " + i + " isn't a NUMBER instance.");
							return null;
						}
						requiredColumnValues.get(columns.get(k)).add(i, oValue);
					}
				}

				return totalRowOperation.getOperationValue(listValues, requiredColumnValues);
			}
		}
		return null;
	}

	public int getColumnIndex(Object col) {
		if (this.calculatedColumnsNames.contains(col)) {
			return this.columnNames.size() + this.calculatedColumnsNames.indexOf(col);
		} else {
			return this.columnNames.indexOf(col);
		}
	}

	/**
	 * Sums all the values for a specified column.
	 *
	 * @param columnIdentifier
	 * @return the sum of the values, or null in case the column does not exist
	 */

	protected Object getColumnSumAverage(Object columnIdentifier, boolean average) {
		int colIndex = this.getColumnIndex(columnIdentifier);
		if (colIndex < 0) {
			ExtendedTableModel.logger.debug(ExtendedTableModel.class.getName() + ":" + columnIdentifier + " column name doesn't exist in table model");
			return null;
		}

		double total = 0.0;
		int count = 0;
		for (int i = 0; i < this.rowsNumber; i++) {
			Object oValue = this.getValue(i, colIndex);
			if (oValue == null) {
				continue;
			}
			if (!(oValue instanceof Number)) {
				ExtendedTableModel.logger.debug(ExtendedTableModel.class.getName() + ":" + columnIdentifier + "in row " + i + " isn't a Number instance.");
				return null;
			}
			total = total + ((Number) oValue).doubleValue();
			count++;
		}

		if (average) {
			if (count == 0) {
				return null;
			}
			return new Double(total / count);
		} else {
			return new Double(total);
		}
	}

	/**
	 * Gets the maximum or minimum value for a specified column.
	 *
	 * @param max
	 *            . true if return value is maximum.
	 */
	protected Number getColumnMaximumMinimum(Object columnIdentifier, boolean max) {
		int colIndex = this.getColumnIndex(columnIdentifier);
		if (colIndex < 0) {
			ExtendedTableModel.logger.debug(ExtendedTableModel.class.getName() + ":" + columnIdentifier + " column name doesn't exist in table model");
			return null;
		}

		List listValues = new ArrayList();

		for (int i = 0; i < this.rowsNumber; i++) {
			Object oValue = this.getValue(i, colIndex);
			if (oValue == null) {
				continue;
			}
			if (!(oValue instanceof Number)) {
				ExtendedTableModel.logger.debug(ExtendedTableModel.class.getName() + ":" + columnIdentifier + "in row " + i + " isn't a NUMBER instance.");
				return null;
			}
			listValues.add(oValue);
		}
		if (listValues.size() == 0) {
			return null;
		}
		Object o = null;
		if (max) {
			o = Collections.max(listValues);
		} else {
			o = Collections.min(listValues);
		}
		return (Number) o;
	}

	/**
	 * Gets the maximum or minimum value for a specified column.
	 *
	 * @param max
	 *            . true if return value is maximum.
	 */
	protected String getColumnConcat(Object columnIdentifier) {
		int colIndex = this.getColumnIndex(columnIdentifier);
		if (colIndex < 0) {
			ExtendedTableModel.logger.debug(ExtendedTableModel.class.getName() + ":" + columnIdentifier + " column name doesn't exist in table model");
			return null;
		}

		StringBuilder buffer = new StringBuilder();

		for (int i = 0; i < this.rowsNumber; i++) {
			Object oValue = this.getValue(i, colIndex);
			if (oValue == null) {
				continue;
			}
			if (buffer.length() > 0) {
				buffer.append(";");
			}
			buffer.append(oValue.toString());
		}

		return buffer.toString();
	}
}
