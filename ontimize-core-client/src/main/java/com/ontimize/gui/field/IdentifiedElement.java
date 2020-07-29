package com.ontimize.gui.field;

import com.ontimize.gui.SecureElement;

public interface IdentifiedElement extends SecureElement {

    /**
     * Gets the object attribute. This attribute allows to identify the object
     */
    public Object getAttribute();

}
