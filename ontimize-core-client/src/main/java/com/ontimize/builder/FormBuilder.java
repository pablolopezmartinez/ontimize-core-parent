package com.ontimize.builder;

import java.awt.Container;
import java.io.InputStream;

import com.ontimize.gui.Form;

public interface FormBuilder {

    /**
     * Creates a {@link Form} with the elements specified in the file.
     * @param container Container of the Form
     * @param uriFile Path of the file with the form definition ( typically XML)
     * @return
     */
    public Form buildForm(Container container, String uriFile);

    /**
     * Creates a {@link Form} with the elements specified in the file.
     * @param parentContainer Container of the Form
     * @param input InputStream
     * @return
     */
    public Form buildForm(Container parentContainer, InputStream input);

}
