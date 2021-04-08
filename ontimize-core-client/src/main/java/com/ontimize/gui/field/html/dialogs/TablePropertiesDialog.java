package com.ontimize.gui.field.html.dialogs;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.ontimize.gui.field.html.dialogs.panels.CellAttributesPanel;
import com.ontimize.gui.field.html.dialogs.panels.RowAttributesPanel;
import com.ontimize.gui.field.html.dialogs.panels.TableAttributesPanel;
import com.ontimize.gui.field.html.utils.I18n;
import com.ontimize.gui.images.ImageManager;

public class TablePropertiesDialog extends OptionDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final I18n i18n = I18n.getInstance();

    public static Icon icon = ImageManager.getIcon(ImageManager.TABLE_VIEW);

    public static String title = "HTMLShef.table_properties";

    public static String desc = "HTMLShef.table_properties_desc";

    protected TableAttributesPanel tableProps = new TableAttributesPanel();

    protected RowAttributesPanel rowProps = new RowAttributesPanel();

    protected CellAttributesPanel cellProps = new CellAttributesPanel();

    protected JTabbedPane tabs;

    protected TitledBorder tablePropertiesBorder;

    public TablePropertiesDialog(Frame parent) {
        super(parent, TablePropertiesDialog.title, TablePropertiesDialog.desc, TablePropertiesDialog.icon);
        this.init();
    }

    public TablePropertiesDialog(Dialog parent) {
        super(parent, TablePropertiesDialog.title, TablePropertiesDialog.desc, TablePropertiesDialog.icon);
        this.init();
    }

    protected void init() {
        Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
        this.tablePropertiesBorder = BorderFactory
            .createTitledBorder(TablePropertiesDialog.i18n.str("HTMLShef.table_properties"));

        this.tableProps.setBorder(BorderFactory.createCompoundBorder(emptyBorder, this.tablePropertiesBorder));
        this.rowProps.setBorder(emptyBorder);
        this.cellProps.setBorder(emptyBorder);

        this.tabs = new JTabbedPane();
        this.tabs.addTab(TablePropertiesDialog.i18n.str("HTMLShef.table"), this.tableProps);
        this.tabs.addTab(TablePropertiesDialog.i18n.str("HTMLShef.row"), this.rowProps);
        this.tabs.addTab(TablePropertiesDialog.i18n.str("HTMLShef.cell"), this.cellProps);

        this.setContentPane(this.tabs);
        this.setSize(440, 375);
        this.setResizable(false);
    }

    public void setTableAttributes(Map at) {
        this.tableProps.setAttributes(at);
    }

    public void setRowAttributes(Map at) {
        this.rowProps.setAttributes(at);
    }

    public void setCellAttributes(Map at) {
        this.cellProps.setAttributes(at);
    }

    public Map getTableAttributes() {
        return this.tableProps.getAttributes();
    }

    public Map getRowAttribures() {
        return this.rowProps.getAttributes();
    }

    public Map getCellAttributes() {
        return this.cellProps.getAttributes();
    }

    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        // TODO Auto-generated method stub
        super.setResourceBundle(resourceBundle);

        if (this.tablePropertiesBorder != null) {
            this.tablePropertiesBorder.setTitle(TablePropertiesDialog.i18n.str("HTMLShef.table_properties"));
        }
        if (this.tabs != null) {
            this.tabs.setTitleAt(0, TablePropertiesDialog.i18n.str("HTMLShef.table"));
            this.tabs.setTitleAt(1, TablePropertiesDialog.i18n.str("HTMLShef.row"));
            this.tabs.setTitleAt(2, TablePropertiesDialog.i18n.str("HTMLShef.cell"));
        }

        if (this.tableProps != null) {
            this.tableProps.setResourceBundle(resourceBundle);
        }
        if (this.rowProps != null) {
            this.rowProps.setResourceBundle(resourceBundle);
        }
        if (this.cellProps != null) {
            this.cellProps.setResourceBundle(resourceBundle);
        }
    }

}
