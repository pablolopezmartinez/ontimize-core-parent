package com.ontimize.gui.field.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class LimitedTextDocument extends TextDocument {

    private int maxLength = 0;

    private boolean activateLimit = true;

    public LimitedTextDocument(int maximumLength) {
        this.maxLength = maximumLength;
    }

    public LimitedTextDocument(int maximumLength, boolean mayusculas) {
        super(mayusculas);
        this.maxLength = maximumLength;
    }

    @Override
    public void insertString(int offset, String stringValue, AttributeSet attributes) throws BadLocationException {
        if (this.activateLimit) {
            if (this.getLength() > this.maxLength) {
                return;
            }
            if ((this.getLength() + stringValue.length()) <= this.maxLength) {
                if (this.upperCase) {
                    super.insertString(offset, stringValue.toUpperCase(), attributes);
                } else {
                    super.insertString(offset, stringValue, attributes);
                }
            } else {
                super.insertString(offset, stringValue.substring(0, this.maxLength - this.getLength()), attributes);
                return;
            }
        } else {
            if (this.upperCase) {
                super.insertString(offset, stringValue.toUpperCase(), attributes);
            } else {
                super.insertString(offset, stringValue, attributes);
            }
        }
    }

    public void setActivatedLimited(boolean enabled) {
        this.activateLimit = enabled;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(int l) {
        this.maxLength = l;
    }

}
