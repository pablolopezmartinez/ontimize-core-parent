package com.ontimize.report.item;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import com.ontimize.gui.images.ImageManager;

public class SelectableConfigItemsListCellRenderer extends JPanel implements ListCellRenderer {

	protected JCheckBox checkbox;
	protected JButton configButton;

	public SelectableConfigItemsListCellRenderer() {
		this.checkbox = new JCheckBox();
		this.checkbox.setBorderPaintedFlat(true);
		this.configButton = new JButton(ImageManager.getIcon(ImageManager.GEAR));
		this.configButton.setBorderPainted(false);
		this.configButton.setContentAreaFilled(false);
		this.setLayout(new GridBagLayout());
		this.add(this.checkbox, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.configButton, new GridBagConstraints(1, 0, 1, 1, 0, 1, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	public int getButtonWidth() {
		return this.configButton.getPreferredSize().width;
	}

	@Override
	public String getName() {
		return "SelectableItem";
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (this.checkbox != null) {
			this.checkbox.setForeground(fg);
		}
		if (this.configButton != null) {
			this.configButton.setForeground(fg);
		}
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (this.checkbox != null) {
			this.checkbox.setBackground(bg);
		}
		if (this.configButton != null) {
			this.configButton.setBackground(bg);
		}
	}

	@Override
	public Component getListCellRendererComponent(JList l, Object v, int r, boolean sel, boolean foc) {

		Color selectedBackground = UIManager.getColor("List[Selected].textBackground");
		Color selectedForeground = UIManager.getColor("List[Selected].textForeground");

		if (selectedBackground == null) {
			selectedBackground = UIManager.getColor("List.selectionBackground");
		}
		if (selectedForeground == null) {
			selectedForeground = UIManager.getColor("List.selectionForeground");
		}

		Color notSelectedBackground = UIManager.getColor("\"SelectableItem\".background");
		Color notSelectedForeground = UIManager.getColor("\"SelectableItem\".foreground");

		if (notSelectedBackground == null) {
			notSelectedBackground = UIManager.getColor("List.background");
		}
		if (notSelectedForeground == null) {
			notSelectedForeground = UIManager.getColor("List.foreground");
		}

		this.setOpaque(true);
		if (sel) {
			this.setForeground(selectedForeground);
			this.setBackground(selectedBackground);
		} else {
			this.setForeground(notSelectedForeground);
			this.setBackground(notSelectedBackground);
		}

		if (v instanceof SelectableDateGroupItem) {
			this.checkbox.setText(((SelectableDateGroupItem) v).toString());
			boolean bSelected = ((SelectableDateGroupItem) v).isSelected();
			this.checkbox.setSelected(bSelected);
			return this;
		}

		if (v instanceof com.ontimize.report.item.SelectableItem) {
			this.checkbox.setText(((com.ontimize.report.item.SelectableItem) v).toString());
			boolean bSelected = ((com.ontimize.report.item.SelectableItem) v).isSelected();
			this.checkbox.setSelected(bSelected);
		}

		if (v instanceof SelectableFunctionItem) {
			this.checkbox.setText(((SelectableFunctionItem) v).toString());
			boolean bSelected = ((SelectableFunctionItem) v).isSelected();
			this.checkbox.setSelected(bSelected);
		}
		if (v instanceof SelectableMultipleItem) {
			this.checkbox.setText(((SelectableMultipleItem) v).toString());
			this.checkbox.setSelected(true);
		}
		if (v instanceof PredefinedFunctionItem) {
			this.checkbox.setText(((PredefinedFunctionItem) v).toString());
			boolean bSelected = ((PredefinedFunctionItem) v).isSelected();
			this.checkbox.setSelected(bSelected);
		}
		return this;
	}
}
