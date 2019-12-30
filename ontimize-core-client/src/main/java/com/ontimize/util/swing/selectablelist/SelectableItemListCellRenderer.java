package com.ontimize.util.swing.selectablelist;

import java.awt.Color;
import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import com.ontimize.gui.ApplicationManager;

public class SelectableItemListCellRenderer extends JCheckBox implements ListCellRenderer {

	protected Color selectedBackground = UIManager.getColor("List.selectionBackground");
	protected Color selectedForeground = UIManager.getColor("List.selectionForeground");

	protected Color notSelectedForeground = UIManager.getColor("List.foreground");

	protected Color notSelectedBackground = UIManager.getColor("List.background");

	protected ResourceBundle bundle = null;

	public SelectableItemListCellRenderer() {
		this.setBorderPaintedFlat(true);
	}

	public SelectableItemListCellRenderer(ResourceBundle resource) {
		this.setBorderPaintedFlat(true);
		this.bundle = resource;
	}

	@Override
	public String getName() {
		return "SelectableItem";
	}

	@Override
	public Component getListCellRendererComponent(JList l, Object v, int r, boolean sel, boolean foc) {

		this.selectedBackground = UIManager.getColor("List[Selected].textBackground");
		this.selectedForeground = UIManager.getColor("List[Selected].textForeground");

		if (this.selectedBackground == null) {
			this.selectedBackground = UIManager.getColor("List.selectionBackground");
		}
		if (this.selectedForeground == null) {
			this.selectedForeground = UIManager.getColor("List.selectionForeground");
		}

		this.notSelectedBackground = UIManager.getColor("\"SelectableItem\".background");
		this.notSelectedForeground = UIManager.getColor("\"SelectableItem\".foreground");

		if (this.notSelectedBackground == null) {
			this.notSelectedBackground = UIManager.getColor("List.background");
		}
		if (this.notSelectedForeground == null) {
			this.notSelectedForeground = UIManager.getColor("List.foreground");
		}

		this.setOpaque(true);
		if (sel) {
			this.setForeground(this.selectedForeground);
			this.setBackground(this.selectedBackground);
		} else {
			this.setForeground(this.notSelectedForeground);
			this.setBackground(this.notSelectedBackground);
		}

		if (v instanceof SelectableItem) {
			this.setText(ApplicationManager.getTranslation(((SelectableItem) v).toString(), this.bundle));
			// this.setText(((SelectableItem) v).toString());
			boolean bSelected = ((SelectableItem) v).isSelected();
			this.setSelected(bSelected);
		}

		return this;
	}

	public ResourceBundle getResourceBundle() {
		return this.bundle;
	}

	public void setResourceBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public Color getSelectedBackground() {
		return this.selectedBackground;
	}

	public void setSelectedBackground(Color selectedBackground) {
		this.selectedBackground = selectedBackground;
	}

	public Color getSelectedForeground() {
		return this.selectedForeground;
	}

	public void setSelectedForeground(Color selectedForeground) {
		this.selectedForeground = selectedForeground;
	}

	public Color getNotSelectedForeground() {
		return this.notSelectedForeground;
	}

	public void setNotSelectedForeground(Color notSelectedForeground) {
		this.notSelectedForeground = notSelectedForeground;
	}

	public Color getNotSelectedBackground() {
		return this.notSelectedBackground;
	}

	public void setNotSelectedBackground(Color notSelectedBackground) {
		this.notSelectedBackground = notSelectedBackground;
	}

}