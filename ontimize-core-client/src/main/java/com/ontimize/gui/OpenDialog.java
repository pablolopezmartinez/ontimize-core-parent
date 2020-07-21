package com.ontimize.gui;

import java.awt.Frame;

/**
 * Interface that defines methods to implement all objects that need a reference to the frame for
 * opening a modal dialog. It is necessary because objects could not know that container included
 * them. It is applied in form components that display a modal dialog.
 *
 * @author Imatia Innovation
 */
public interface OpenDialog {

    /**
     * This method is called from Form for each component that implements this interface. It fixes the
     * parent frame for element. This frame is needed to open the modal dialog.
     * @param parentFrame the frame
     */
    public void setParentFrame(Frame parentFrame);

}
