package com.ontimize.gui;

/*
 * NO UTILIZAR. EN DESARROLLO
 *
 * @version 1.0
 */
public class FormContext {

    protected Form form = null;

    public FormContext(Form form) {
        this.form = form;
    }

    public Object setValue(String attr) {
        if (this.form == null) {
            return null;
        } else {
            return this.form.getDataFieldValue(attr);
        }
    }

}
