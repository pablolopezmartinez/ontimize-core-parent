package com.ontimize.gui;

import java.awt.Window;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.db.Entity;
import com.ontimize.db.EntityResult;
import com.ontimize.gui.field.DataComponent;
import com.ontimize.gui.field.ReferenceFieldAttribute;
import com.ontimize.gui.table.Table;
import com.ontimize.locator.EntityReferenceLocator;

public abstract class BaseDetailForm extends JPanel implements IDetailForm {

    private static final Logger logger = LoggerFactory.getLogger(BaseDetailForm.class);

    protected Hashtable tableKeys = null;

    protected Vector fieldsKey = null;

    protected int vectorIndex = 0;

    protected Form form = null;

    protected Hashtable parentkeys = null;

    protected Table table = null;

    protected String title = null;

    protected Hashtable data = new Hashtable();

    protected Hashtable codValues = null;

    protected Hashtable reverseCodValues = null;

    @Override
    public void setComponentLocale(Locale l) {
        this.form.setComponentLocale(l);
    }

    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.form.setResourceBundle(resourceBundle);
    }

    @Override
    public Vector getTextsToTranslate() {
        Vector v = this.form.getTextsToTranslate();
        return v;
    }

    @Override
    public Form getForm() {
        return this.form;
    }

    @Override
    public Table getTable() {
        return this.table;
    }

    protected void initCodValues(Hashtable codValues) {
        if (codValues == null) {
            return;
        }
        this.codValues = codValues;
        this.reverseCodValues = new Hashtable();
        Enumeration enumeration = this.codValues.keys();
        while (enumeration.hasMoreElements()) {
            Object current = enumeration.nextElement();
            this.reverseCodValues.put(this.codValues.get(current), current);
        }
    }

    @Override
    public String getFormFieldName(Object name) {
        if (name == null) {
            return null;
        }
        return this.getFormFieldName(name.toString());
    }

    protected String getFormFieldName(String name) {
        if (this.codValues == null) {
            return name;
        }
        if (this.codValues.containsKey(name)) {
            return (String) this.codValues.get(name);
        }
        return name;
    }

    public String getTableFieldName(String name) {
        if (this.reverseCodValues == null) {
            return name;
        }
        if (this.reverseCodValues.containsKey(name)) {
            return (String) this.reverseCodValues.get(name);
        }
        return name;
    }

    @Override
    public String getTableFieldName(Object name) {
        if (name == null) {
            return null;
        }
        return this.getTableFieldName(name.toString());
    }

    @Override
    public Hashtable valuesToForm(Hashtable values) {
        if (values != null) {
            Hashtable clone = new Hashtable();
            Enumeration enumeration = values.keys();
            while (enumeration.hasMoreElements()) {
                Object current = enumeration.nextElement();
                clone.put(this.getFormFieldName(current.toString()), values.get(current));
            }
            return clone;
        }
        return null;
    }

    @Override
    public Hashtable valuesToTable(Hashtable values) {
        Hashtable clone = new Hashtable();
        Enumeration enumeration = values.keys();
        while (enumeration.hasMoreElements()) {
            Object current = enumeration.nextElement();
            clone.put(this.getTableFieldName(current.toString()), values.get(current));
        }
        return clone;
    }

    protected Vector listToForm(Vector list) {
        Vector current = new Vector();
        for (int i = 0; i < list.size(); i++) {
            current.add(this.getFormFieldName(list.get(i).toString()));
        }
        return current;
    }

    protected void updateFieldsParentkeys() {
        // Fill form fields used as parent keys
        if ((this.parentkeys != null) && !this.parentkeys.isEmpty()) {
            Enumeration enumOtherParentKeys = this.parentkeys.keys();
            while (enumOtherParentKeys.hasMoreElements()) {
                Object oParentkey = enumOtherParentKeys.nextElement();
                this.form.setDataFieldValue(oParentkey, this.parentkeys.get(oParentkey));
                DataComponent comp = this.form.getDataFieldReference(oParentkey.toString());
                if (comp != null) {
                    comp.setModifiable(false);
                }
            }
        }
    }

    @Override
    public void setQueryInsertMode() {
        this.updateFieldsParentkeys();
        this.form.getInteractionManager().setQueryInsertMode();
    }

    @Override
    public void setUpdateMode() {
        this.updateFieldsParentkeys();
        this.form.getInteractionManager().setUpdateMode();
    }

    @Override
    public void setInsertMode() {
        this.updateFieldsParentkeys();
        this.form.getInteractionManager().setInsertMode();
    }

    @Override
    public void setQueryMode() {
        this.updateFieldsParentkeys();
        this.form.getInteractionManager().setQueryMode();
    }

    @Override
    public void setAttributeToFix(Object attribute, Object value) {
        if (attribute == null) {
            return;
        }
        String formAttr = this.getFormFieldName(attribute.toString());
        this.form.setDataFieldValue(formAttr, value);
        DataComponent comp = this.form.getDataFieldReference(formAttr);
        if (comp != null) {
            comp.setModifiable(false);
        }
    }

    @Override
    public void resetParentkeys(List parentKeys) {
        if (parentKeys != null) {
            for (int i = 0; i < parentKeys.size(); i++) {
                String formAttr = this.getFormFieldName(parentKeys.get(i));
                DataComponent comp = this.form.getDataFieldReference(formAttr);
                if (comp != null) {
                    comp.setModifiable(true);
                    comp.deleteData();
                }
            }
        }
    }

    @Override
    public void setParentKeyValues(Hashtable parentKeyValues) {
        this.parentkeys = this.valuesToForm(parentKeyValues);
        this.updateFieldsParentkeys();
    }

    /**
     * This method sets the keys in the table records.<br>
     * This keys are used to query the record values
     * @param tableKeys
     * @param index
     */
    @Override
    public void setKeys(Hashtable tableKeys, int index) {
        this.tableKeys = this.valuesToForm(tableKeys);
        // Reset the index of the selected element
        this.vectorIndex = 0;

        // If there are more than one record
        int recordNumber = 0;
        if (tableKeys.isEmpty()) {
            this.form.disableButtons();
            this.form.disableDataFields();
        } else {
            Enumeration enumTableKeys = this.tableKeys.keys();
            Vector vKeys = (Vector) this.tableKeys.get(enumTableKeys.nextElement());
            recordNumber = vKeys.size();
        }

        if (index < recordNumber) {
            this.vectorIndex = index;
        }
        if (!tableKeys.isEmpty()) {
            if (!(this.form instanceof FormExt)) {
                if (this.vectorIndex >= 0) {
                    this.data = this.query(this.vectorIndex);
                    this.form.updateDataFields(this.data);
                    if (recordNumber > 1) {
                        this.form.startButton.setEnabled(true);
                        this.form.previousButton.setEnabled(true);
                        this.form.nextButton.setEnabled(true);
                        this.form.endButton.setEnabled(true);
                        if (this.vectorIndex == 0) {
                            this.form.startButton.setEnabled(false);
                            this.form.previousButton.setEnabled(false);
                        } else if (this.vectorIndex >= (recordNumber - 1)) {
                            this.form.nextButton.setEnabled(false);
                            this.form.endButton.setEnabled(false);
                        }
                    }
                } else {
                    this.form.updateDataFields(new Hashtable());
                }
            } else {
                ((FormExt) this.form).updateDataFields(this.tableKeys, this.vectorIndex);
            }
        } else {
            this.form.updateDataFields(new Hashtable());
        }
        if (recordNumber == 0) {
            this.setQueryInsertMode();
        }
    }

    protected EntityResult query(int index) {
        EntityResult res = null;
        try {
            Hashtable hKeysValues = new Hashtable();
            if (index >= 0) {
                // parent key values are used in the query too
                // Parentkey;
                if (this.parentkeys != null) {
                    // Other parent keys
                    Enumeration enumOtherKeys = this.parentkeys.keys();
                    while (enumOtherKeys.hasMoreElements()) {
                        Object oParentkeyElement = enumOtherKeys.nextElement();
                        if (this.parentkeys.get(oParentkeyElement) == null) {

                            if (BaseDetailForm.logger.isDebugEnabled()) {
                                Window w = SwingUtilities.getWindowAncestor(this);
                                MessageDialog.showErrorMessage(w,
                                        "DEBUG: DetailForm: parentkey " + oParentkeyElement
                                                + " is NULL. It won't be included in the query. Check the xml that contains the table configuration and ensure that the parentkey has value there.");
                            }
                        } else {
                            hKeysValues.put(oParentkeyElement, this.parentkeys.get(oParentkeyElement));
                        }
                    }
                }
                Vector vTableKeys = this.table.getKeys();
                for (int i = 0; i < vTableKeys.size(); i++) {
                    Object oKeyField = vTableKeys.get(i);
                    Vector vKeyValues = (Vector) this.tableKeys.get(oKeyField);
                    if (vKeyValues.size() <= index) {
                        if (BaseDetailForm.logger.isDebugEnabled()) {
                            Window window = SwingUtilities.getWindowAncestor(this);
                            MessageDialog.showErrorMessage(window,
                                    "DEBUG: DetailForm: Hashtable with the detail form keys contains less elements for the key "
                                            + oKeyField + " than the selected index " + index);
                        }
                        return new EntityResult();
                    }

                    if (vKeyValues.get(index) == null) {
                        if (BaseDetailForm.logger.isDebugEnabled()) {
                            Window window = SwingUtilities.getWindowAncestor(this);
                            MessageDialog.showErrorMessage(window,
                                    "DEBUG: DetailForm:  Hashtable with the detail form keys contains a NULL value for the key: "
                                            + oKeyField + " in the selected index: " + index);
                        }
                    }
                    hKeysValues.put(oKeyField, vKeyValues.get(index));
                }

            } else {
                return new EntityResult();
            }
            EntityReferenceLocator referenceLocator = this.form.getFormManager().getReferenceLocator();
            Entity entity = referenceLocator.getEntityReference(this.form.getEntityName());
            Vector vAttributeList = (Vector) this.form.getDataFieldAttributeList().clone();
            // If key is not include then add it to the query fields, but it can
            // be
            // an ReferenceFieldAttribute
            for (int i = 0; i < this.fieldsKey.size(); i++) {
                boolean containsKey = false;
                for (int j = 0; j < vAttributeList.size(); j++) {
                    Object oAttribute = vAttributeList.get(j);
                    if (oAttribute.equals(this.fieldsKey.get(i))) {
                        containsKey = true;
                        break;
                    } else if (oAttribute instanceof ReferenceFieldAttribute) {
                        if (((ReferenceFieldAttribute) oAttribute).getAttr() != null) {
                            if (((ReferenceFieldAttribute) oAttribute).getAttr().equals(this.fieldsKey.get(i))) {
                                containsKey = true;
                                break;
                            }
                        }
                    }
                }
                if (!containsKey) {
                    vAttributeList.add(this.fieldsKey.get(i));
                }
            }
            res = entity.query(hKeysValues, vAttributeList, referenceLocator.getSessionId());
            // For each key get the value and add it to the data
            return res;
        } catch (Exception e) {
            BaseDetailForm.logger
                .error("DetailForm: Error in query. Check the parameters, the xml and the entity configuration", e);
            if (ApplicationManager.DEBUG) {
                BaseDetailForm.logger.error(null, e);
            }
            return new EntityResult();
        }
    }

    @Override
    public void free() {
        this.tableKeys = null;
        this.fieldsKey = null;
        this.parentkeys = null;
        this.table = null;
        this.data = null;
        this.codValues = null;
        this.reverseCodValues = null;
        FreeableUtils.freeComponent(form);
        this.form = null;
    }

}
