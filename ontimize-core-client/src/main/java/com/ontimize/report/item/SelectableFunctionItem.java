package com.ontimize.report.item;

import java.util.Comparator;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.i18n.Internationalization;
import com.ontimize.report.DefaultReportDialog;
import com.ontimize.report.ReportUtils;
import com.ontimize.report.TranslatedItem;

public class SelectableFunctionItem extends TranslatedItem implements Internationalization, Comparator {

	private static final Logger logger = LoggerFactory.getLogger(SelectableFunctionItem.class);

	protected boolean selected = false;

	protected int operation = ReportUtils.SUM;

	protected String operationText = DefaultReportDialog.SUM_OP_KEY;

	public SelectableFunctionItem(String text, ResourceBundle res) {
		super(text, res);
		this.setOperationText();
	}

	public boolean isSelected() {
		return this.selected;
	}

	@Override
	public void setResourceBundle(ResourceBundle res) {
		super.setResourceBundle(res);
		this.setOperationText();
	}

	public void setSelected(boolean sel) {
		this.selected = sel;
	}

	public void setOperation(int op) {
		this.operation = op;
		this.setOperationText();
	}

	protected void setOperationText() {
		if (this.res != null) {
			try {
				if (this.operation == ReportUtils.SUM) {
					this.operationText = DefaultReportDialog.SUM_OP_KEY;
				} else if (this.operation == ReportUtils.MAX) {
					this.operationText = DefaultReportDialog.MAXIMUM_OP_KEY;
				} else if (this.operation == ReportUtils.MIN) {
					this.operationText = DefaultReportDialog.MINIMUM_OP_KEY;
				} else if (this.operation == ReportUtils.AVG) {
					this.operationText = DefaultReportDialog.AVERAGE_OP_KEY;
				}
				this.operationText = this.res.getString(this.operationText);

			} catch (Exception e) {
				SelectableFunctionItem.logger.debug(e.getMessage(), e);
			}
		}
	}

	public int getOperation() {
		return this.operation;
	}

	@Override
	public String toString() {
		if (!this.isSelected()) {
			return this.translatedText;
		}
		return this.translatedText + " - " + this.operationText;
	}

	public int compareTo(Object o) {
		if (!(o instanceof SelectableFunctionItem)) {
			return -1;
		} else {
			SelectableFunctionItem item = (SelectableFunctionItem) o;
			return item.translatedText.compareTo(this.translatedText);
		}
	}

	@Override
	public int compare(Object o1, Object o2) {
		SelectableFunctionItem item1 = (SelectableFunctionItem) o1;
		SelectableFunctionItem item2 = (SelectableFunctionItem) o2;
		return item2.compareTo(item1);
	}
};
