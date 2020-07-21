package com.ontimize.printing;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;

public class Image implements ReportElement {

    private static final Logger logger = LoggerFactory.getLogger(Image.class);

    byte[] imageBytes = null;

    public Image(byte[] bytes) {
        this.imageBytes = bytes;
    }

    @Override
    public void insert(ReportFrame reportFrame, boolean multipage) throws Exception {
    }

    @Override
    public void insert(ReportFrame reportFrame, String templateElementIdentifier, boolean multiplePage)
            throws Exception {
        HTMLDocument htmlDocument = reportFrame.getCurrentPage().getHTMLDocument();
        Element element = htmlDocument.getElement(templateElementIdentifier);
        int height = 100;
        int width = 100;
        AttributeSet attributesSet = element.getAttributes();
        Enumeration enumNames = attributesSet.getAttributeNames();
        Object oHeight = null;
        Object oWidth = null;
        while (enumNames.hasMoreElements()) {
            Object oName = enumNames.nextElement();
            if (oName.toString().equalsIgnoreCase("height")) {
                oHeight = attributesSet.getAttribute(oName);
            }
            if (oName.toString().equalsIgnoreCase("width")) {
                oWidth = attributesSet.getAttribute(oName);
            }
            if (ApplicationManager.DEBUG) {
                Image.logger.debug(oName.toString() + " : " + attributesSet.getAttribute(oName));
            }
        }
        if (oHeight != null) {
            try {
                height = Integer.parseInt(oHeight.toString());
            } catch (Exception e) {
                Image.logger.trace(null, e);
            }
        }
        if (oWidth != null) {
            try {
                width = Integer.parseInt(oWidth.toString());
            } catch (Exception e) {
                Image.logger.trace(null, e);
            }
        }
        if (element != null) {
            // We have to provide an URL
            File fTemp = new File("imag_0001.gif");
            FileOutputStream FOSout = new FileOutputStream(fTemp);
            FOSout.write(this.imageBytes);
            FOSout.flush();
            htmlDocument.insertAfterStart(element, "<IMAGE src='" + fTemp.toURL() + "' height='"
                    + Integer.toString(height) + "' width='" + Integer.toString(width) + "'></IMAGE>");
            FOSout.close();
            // Now, we delete disk image because image should be loaded in
            // document.
            fTemp.deleteOnExit();
        } else {
            if (ApplicationManager.DEBUG) {
                Image.logger.debug("Identifier not encountered: " + templateElementIdentifier + " in document.");
            }
        }
    };

}
