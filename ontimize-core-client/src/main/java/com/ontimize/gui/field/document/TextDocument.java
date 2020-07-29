package com.ontimize.gui.field.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;

public class TextDocument extends PlainDocument {

    private static final Logger logger = LoggerFactory.getLogger(TextDocument.class);

    protected boolean upperCase = false;

    public TextDocument() {
    }

    public TextDocument(boolean upperCase) {
        this.upperCase = upperCase;
    }

    public void setUpperCase(boolean m) {
        this.upperCase = m;
        try {
            String sContent = this.getText(0, this.getLength());
            this.remove(0, this.getLength());
            this.insertString(0, sContent, null);
        } catch (Exception e) {
            if (ApplicationManager.DEBUG) {
                TextDocument.logger.error(null, e);
            }
        }
    }

    @Override
    public void insertString(int offset, String stringValue, AttributeSet attributes) throws BadLocationException {
        if (this.upperCase) {
            super.insertString(offset, stringValue.toUpperCase(), attributes);
        } else {
            super.insertString(offset, stringValue, attributes);
        }
    }

}
