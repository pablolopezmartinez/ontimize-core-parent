package com.ontimize.report;

import java.util.ResourceBundle;

import com.ontimize.gui.i18n.Internationalization;

public class SelectableItem extends TranslatedItem implements Internationalization {

	protected boolean selected = false;

	public SelectableItem(String text, ResourceBundle res) {
		super(text, res);
	}

	public boolean isSelected() {
		return this.selected;
	}

	@Override
	public void setResourceBundle(ResourceBundle res) {
		super.setResourceBundle(res);
	}

	public void setSelected(boolean sel) {
		this.selected = sel;
	}

	@Override
	public String toString() {
		if (!this.isSelected()) {
			return this.translatedText;
		}
		return this.translatedText;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o instanceof SelectableItem) {
			if (this.text.equals(((SelectableItem) o).getText())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
