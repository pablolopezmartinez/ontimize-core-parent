package com.ontimize.gui.container;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.field.DataComponent;

public class BackgroundImageGroup extends JImageContainer implements DataComponentGroup {

    private static final Logger logger = LoggerFactory.getLogger(BackgroundImageGroup.class);

    protected Hashtable dataComponent = new Hashtable();

    protected Object attribute = null;

    public BackgroundImageGroup(Hashtable parameters) {
        // Parent constructor execute the initialization
        super(parameters);
        Object attr = parameters.get("attr");
        if (attr == null) {
            BackgroundImageGroup.logger.debug(this.getClass().toString() + " 'attr' parameter is required");
        } else {
            this.attribute = attr;
        }

    }

    @Override
    public void add(Component comp, Object constraints) {
        if (comp instanceof DataComponent) {
            this.dataComponent.put(((DataComponent) comp).getAttribute(), comp);
        }
        super.add(comp, constraints);
    }

    @Override
    public Object getAttribute() {
        return this.attribute;
    }

    @Override
    public Hashtable getGroupValue() {
        Hashtable hValue = new Hashtable();
        Enumeration c = this.dataComponent.keys();
        while (c.hasMoreElements()) {
            Object oKey = c.nextElement();
            Object pDataComponent = this.dataComponent.get(oKey);
            hValue.put(oKey, ((DataComponent) pDataComponent).getValue());
        }
        return hValue;
    }

    @Override
    public void setAllModificable(boolean modif) {
        Enumeration c = this.dataComponent.keys();
        while (c.hasMoreElements()) {
            Object oKey = c.nextElement();
            Object oDataComponent = this.dataComponent.get(oKey);
            if (oDataComponent instanceof DataComponent) {
                ((DataComponent) oDataComponent).setModifiable(modif);
            }
        }
    }

    @Override
    public void setAllEnabled(boolean en) {
        Enumeration c = this.dataComponent.keys();
        while (c.hasMoreElements()) {
            Object oKey = c.nextElement();
            Object oDataComponent = this.dataComponent.get(oKey);
            if (oDataComponent instanceof DataComponent) {
                ((DataComponent) oDataComponent).setEnabled(en);
            }
        }
    }

    @Override
    public void setGroupValue(Hashtable value) {
        Enumeration c = this.dataComponent.keys();
        while (c.hasMoreElements()) {
            Object oKey = c.nextElement();
            Object oDataComponent = this.dataComponent.get(oKey);
            if (oDataComponent instanceof DataComponent) {
                ((DataComponent) oDataComponent).setValue(value.get(((DataComponent) oDataComponent).getAttribute()));
            }
        }
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public void initPermissions() {
    }

    @Override
    public boolean isRestricted() {
        return false;
    }

    @Override
    public Vector getAttributes() {
        Vector v = new Vector();
        Enumeration enumKeys = this.dataComponent.keys();
        while (enumKeys.hasMoreElements()) {
            v.add(enumKeys.nextElement());
        }
        return v;
    }

}
