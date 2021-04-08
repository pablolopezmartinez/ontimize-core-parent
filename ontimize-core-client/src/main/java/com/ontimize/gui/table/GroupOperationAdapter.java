package com.ontimize.gui.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;

import com.ontimize.gui.ApplicationManager;

public abstract class GroupOperationAdapter implements GroupOperation {

    protected Table table;

    protected int operationId;

    protected String operationText;

    protected String headerText;

    /**
     * Item should be JMenuItem, JMenu...
     */
    protected JMenuItem item;

    public GroupOperationAdapter(Table sourceTable, int operationId, String operationText, String headerText) {
        this.table = sourceTable;
        this.operationId = operationId;
        this.operationText = operationText;
        this.headerText = headerText;
    }

    @Override
    public abstract Number getOperationValue(List columnValues, List rowIndexes, Map requiredColsValues);

    @Override
    public int getOperationId() {
        return this.operationId;
    }

    @Override
    public String getOperationText() {
        return ApplicationManager.getTranslation(this.operationText, this.table.getResourceBundle());
    }

    @Override
    public String getHeaderText() {
        return ApplicationManager.getTranslation(this.headerText, this.table.getResourceBundle());
    }

    @Override
    public JMenuItem getItem() {
        if (this.item == null) {
            this.item = new JMenuItem(
                    ApplicationManager.getTranslation(this.getOperationText(), this.table.getResourceBundle()));
            this.item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int col = GroupOperationAdapter.this.table.getJTable()
                        .convertColumnIndexToModel(GroupOperationAdapter.this.table.getColPress());
                    ((TableSorter) GroupOperationAdapter.this.table.getJTable().getModel())
                        .setGroupedColumnFunction(col, GroupOperationAdapter.this.getOperationId());
                }
            });
        }

        return this.item;
    }

    @Override
    public abstract List<String> getRequiredColumns();

}
