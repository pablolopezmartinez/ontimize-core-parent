package com.ontimize.gui.field.html.dialogs;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.ontimize.gui.field.html.dialogs.panels.ListAttributesPanel;
import com.ontimize.gui.field.html.utils.I18n;
import com.ontimize.gui.images.ImageManager;

public class ListDialog extends OptionDialog {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final I18n i18n = I18n.getInstance();

    public static final int UNORDERED = ListAttributesPanel.UL_LIST;

    public static final int ORDERED = ListAttributesPanel.OL_LIST;

    public static Icon icon = ImageManager.getIcon(ImageManager.LIST_ORDERED);

    public static String title = "HTMLShef.list_properties";

    public static String desc = "HTMLShef.list_properties_desc";

    protected ListAttributesPanel listAttrPanel;

    public ListDialog(Frame parent) {
        super(parent, ListDialog.title, ListDialog.desc, ListDialog.icon);
        this.init();
    }

    public ListDialog(Dialog parent) {
        super(parent, ListDialog.title, ListDialog.desc, ListDialog.icon);
        this.init();
    }

    protected void init() {
        this.listAttrPanel = new ListAttributesPanel();
        this.setContentPane(this.listAttrPanel);
        this.pack();
        this.setSize(220, this.getHeight());
        this.setResizable(false);
    }

    public void setListType(int t) {
        this.listAttrPanel.setListType(t);
    }

    public int getListType() {
        return this.listAttrPanel.getListType();
    }

    public void setListAttributes(Map attr) {
        this.listAttrPanel.setAttributes(attr);
    }

    public Map getListAttributes() {
        return this.listAttrPanel.getAttributes();
    }

    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        super.setResourceBundle(resourceBundle);
        if (this.listAttrPanel != null) {
            this.listAttrPanel.setResourceBundle(resourceBundle);
        }
    }

}
