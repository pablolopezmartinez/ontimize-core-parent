package com.ontimize.report.item;

import java.util.ResourceBundle;
import java.util.Vector;

public class SelectableMultipleItem {

	Vector itemList = null;

	public SelectableMultipleItem(Vector list, ResourceBundle res) {
		this.itemList = list;
	}

	public Vector getItemList() {
		return this.itemList;
	}

	@Override
	public String toString() {
		String sValue = "";
		for (int i = 0; i < this.itemList.size(); i++) {
			Object ite = this.itemList.get(i);
			sValue += sValue.length() == 0 ? ite.toString() : "," + ite.toString();
		}
		return sValue;
	}

	public String getText() {
		String sValue = "";
		for (int i = 0; i < this.itemList.size(); i++) {
			com.ontimize.report.item.SelectableItem ite = (com.ontimize.report.item.SelectableItem) this.itemList.get(i);
			sValue += sValue.length() == 0 ? ite.getText() : "," + ite.getText();
		}
		return sValue;

	}

}
