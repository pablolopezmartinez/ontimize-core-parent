package com.ontimize.util.swing.selectablelist;

public class SelectableItem implements Comparable {

	protected boolean selected = false;

	protected String text = null;

	public SelectableItem(String text) {
		this.text = text;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setSelected(boolean sel) {
		this.selected = sel;
	}

	@Override
	public String toString() {
		if (!this.isSelected()) {
			return this.text;
		}
		return this.text;
	}

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof SelectableItem)) {
			return -1;
		} else {
			SelectableItem item = (SelectableItem) o;
			return item.text.compareTo(this.text);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public int compare(Object o1, Object o2) {
		SelectableItem item1 = (SelectableItem) o1;
		SelectableItem item2 = (SelectableItem) o2;
		return item2.compareTo(item1);
	}

}