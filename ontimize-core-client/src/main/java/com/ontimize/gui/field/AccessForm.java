package com.ontimize.gui.field;

import com.ontimize.gui.Form;

/**
 * Interface to define the method to set the parent form for a component.
 * <p>
 *
 * @author Imatia Innovation
 */
public interface AccessForm {

    /**
     * Method declaration to set the form that contains the component. This method must be called
     * automatically during the form building.
     * <p>
     * @param form the form reference
     */
    public void setParentForm(Form form);

}
