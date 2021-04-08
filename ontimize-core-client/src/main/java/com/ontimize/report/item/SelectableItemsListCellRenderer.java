package com.ontimize.report.item;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

public class SelectableItemsListCellRenderer extends JCheckBox implements ListCellRenderer {

    public SelectableItemsListCellRenderer() {
        this.setBorderPaintedFlat(true);
    }

    @Override
    public String getName() {
        return "SelectableItem";
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
            this.setText(((SelectableDateGroupItem) v).toString());
            boolean bSelected = ((SelectableDateGroupItem) v).isSelected();
            this.setSelected(bSelected);
            return this;
        }

        if (v instanceof com.ontimize.report.item.SelectableItem) {
            this.setText(((com.ontimize.report.item.SelectableItem) v).toString());
            boolean bSelected = ((com.ontimize.report.item.SelectableItem) v).isSelected();
            this.setSelected(bSelected);
        }

        if (v instanceof SelectableFunctionItem) {
            this.setText(((SelectableFunctionItem) v).toString());
            boolean bSelected = ((SelectableFunctionItem) v).isSelected();
            this.setSelected(bSelected);
        }
        if (v instanceof SelectableMultipleItem) {
            this.setText(((SelectableMultipleItem) v).toString());
            this.setSelected(true);
        }
        if (v instanceof PredefinedFunctionItem) {
            this.setText(((PredefinedFunctionItem) v).toString());
            boolean bSelected = ((PredefinedFunctionItem) v).isSelected();
            this.setSelected(bSelected);
        }
        return this;
    }

}
