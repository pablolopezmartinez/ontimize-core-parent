package com.ontimize.gui;

/**
 * This interface defines a method to resource liberation. This method must be invoked in order to
 * make the object available to the garbage collector.
 *
 * @author Imatia Innovation
 */

public interface Freeable {

    /**
     * Makes the object to release the resources.
     * @throws Exception
     */
    public void free();

}
