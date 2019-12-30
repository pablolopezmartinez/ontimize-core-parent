package com.ontimize.report.item;

import java.util.ResourceBundle;

import com.ontimize.gui.ApplicationManager;

public class PredefinedFunctionItem extends SelectableFunctionItem {

	protected boolean selected = false;

	public PredefinedFunctionItem(String text, ResourceBundle res) {
		super(text, res);
		this.operationText = "";
		this.translatedText = ApplicationManager.getTranslation(text, res);
	}

	@Override
	public String toString() {
		return this.translatedText;
	}
}
