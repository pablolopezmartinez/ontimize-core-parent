package com.ontimize.gui.table;

import com.ontimize.gui.Form;
import com.ontimize.gui.IBackgroundFormBuilder;

public class DetailFormBuilder implements IBackgroundFormBuilder {

    protected Table table;

    protected Form form;

    public DetailFormBuilder(Table table) {
        this.table = table;
    }

    @Override
    public String getFormName() {
        return this.table.getFormName();
    }

    @Override
    public synchronized Form getForm() {
        if (this.form == null) {
            String sFormName = this.table.getFormName();
            this.form = this.table.getParentForm().getFormManager().getFormCopyInEDTh(sFormName);
        }
        return this.form;
    }

}
