package com.ontimize.gui.field.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Symbols for mask: </br>
 * '*' : any character </br>
 * '#' : alphabetic characters </br>
 * 'A' : Capital alphabetic characters </br>
 * 'a' : Lower case characters </br>
 * '0' : Numeric characters </br>
 * '%' : Numeric character or Capital alphabetic character </br>
 * A distinct character in a position of mask indicates that only will be allowed this character in
 * that position.
 */
public class MaskDocument extends PlainDocument {

    private static final Logger logger = LoggerFactory.getLogger(MaskDocument.class);

    protected String mask = null;

    protected boolean exactMatch = false;

    public MaskDocument(String masc) {
        this(masc, false);
    }

    public MaskDocument(String masc, boolean exactMatch) {
        super();
        this.mask = masc;
        this.exactMatch = exactMatch;
    }

    public void setExactMatch(boolean exact) {
        this.exactMatch = exact;
    }

    public String getValue() throws Exception {
        if (this.getLength() == 0) {
            return "";
        }
        String text = this.getText(0, this.getLength());
        return text;
    }

    protected boolean isFixedChar(char c) {
        switch (c) {
            case '*':
                return false;
            case '#':
                return false;
            case 'A':
                return false;
            case 'a':
                return false;
            case '0':
                return false;
            case '%':
                return false;
            default:
                return true;
        }
    }

    public void setValue(String stringValue, boolean forze) throws Exception {
        this.remove(0, this.getLength());
        if (forze) {
            super.insertString(0, stringValue, null);
        } else {
            this.insertString(0, stringValue, null);
        }
    }

    public boolean isRight() {
        try {
            if (this.exactMatch) {
                if (this.getLength() < this.mask.length()) {
                    return false;
                } else {
                    return this.compareToMask(this.getText(0, this.getLength()));
                }
            } else {
                return this.compareToMask(this.getText(0, this.getLength()));
            }
        } catch (Exception e) {
            MaskDocument.logger.error(null, e);
            return false;
        }
    }

    protected boolean compareToMask(String s) {
        if (s == null) {
            return true;
        }
        if (s.length() > this.mask.length()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            boolean res = this.isCharacterValidInOffset(s.charAt(i), i);
            if (!res) {
                return false;
            }
        }
        return true;
    }

    protected void removeLast(int length, int offset) throws Exception {
        if (length > offset) {
            this.remove(offset, 1);
        }
    }

    @Override
    public void insertString(int offset, String stringValue, AttributeSet attributes) {
        if (stringValue.length() > 1) {
            for (int i = 0; i < stringValue.length(); i++) {
                Character character = new Character(stringValue.charAt(i));
                this.insertString(offset + i, character.toString(), attributes);
            }
            return;
        } else if (stringValue.length() == 1) {
            if (this.mask == null) {
                try {
                    super.insertString(offset, stringValue, attributes);
                    MaskDocument.logger.debug("MaskDocument: The mask is null");
                } catch (BadLocationException e) {
                    MaskDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
                }
            } else {
                if (offset >= this.mask.length()) {
                    return;
                } else {
                    // Checks if the character is allowed in this position
                    // depending on the mask
                    char maskCharacter = this.mask.charAt(offset);
                    try {
                        int length = this.getLength();
                        switch (maskCharacter) {
                            case '*':
                                insertAsteriskString(offset, stringValue, attributes, length);
                                break;
                            case '#':
                                insertHashString(offset, stringValue, attributes, length);
                                break;
                            case 'A':
                                insertUpperAString(offset, stringValue, attributes, length);
                                break;
                            case 'a':
                                insertLowerAString(offset, stringValue, attributes, length);
                                break;
                            case '0':
                                insertDigitString(offset, stringValue, attributes, length);
                                break;
                            case '%':
                                // digits
                                insertAnyString(offset, stringValue, attributes, length);
                                break;
                            default:
                                insertOtherString(offset, stringValue, attributes);
                                break;
                        }
                    } catch (Exception ex) {
                        MaskDocument.logger.debug(this.getClass().toString() + ": " + ex.getMessage(), ex);
                    }
                }
            }
        }
    }

    protected void insertOtherString(int offset, String stringValue, AttributeSet attributes)
            throws BadLocationException {
        int length;
        Character auxCharacter = new Character(this.mask.charAt(offset));
        super.insertString(offset, auxCharacter.toString(), attributes);
        // Now insert the new character but overwrite
        length = this.getLength();
        try {
            if ((length > (offset + 1)) && ((offset + 1) < this.mask.length())) {
                this.remove(offset + 1, 1);
            }
            this.insertString(offset + 1, stringValue, attributes);
        } catch (BadLocationException e) {
            MaskDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
        }
    }

    protected void insertAnyString(int offset, String stringValue, AttributeSet attributes, int length)
            throws Exception {
        if (!Character.isDigit(stringValue.charAt(0))) {
            // If the character is not a digit then checks
            // if it is a letter
            if (!Character.isLetter(stringValue.charAt(0))) {
                return;
            } else {
                try {
                    this.removeLast(length, offset);
                    super.insertString(offset, stringValue.toUpperCase(), attributes);
                } catch (BadLocationException e) {
                    MaskDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
                }
            }
        } else {
            try {
                this.removeLast(length, offset);
                super.insertString(offset, stringValue, attributes);
            } catch (BadLocationException e) {
                MaskDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
            }
        }
    }

    protected void insertDigitString(int offset, String stringValue, AttributeSet attributes, int length)
            throws Exception {
        // Digits
        if (!Character.isDigit(stringValue.charAt(0))) {
            return;
        } else {
            try {
                this.removeLast(length, offset);
                super.insertString(offset, stringValue, attributes);
            } catch (BadLocationException e) {
                MaskDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
            }
        }
    }

    protected void insertLowerAString(int offset, String stringValue, AttributeSet attributes, int length)
            throws Exception {
        // Lower case alphabetic characters
        if (!Character.isLetter(stringValue.charAt(0))) {
            return;
        } else {
            try {
                this.removeLast(length, offset);
                super.insertString(offset, stringValue.toLowerCase(), attributes);
            } catch (BadLocationException e) {
                MaskDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
            }
        }
    }

    protected void insertUpperAString(int offset, String stringValue, AttributeSet attributes, int length)
            throws Exception {
        // Upper case alphabetic characters
        if (!Character.isLetter(stringValue.charAt(0))) {
            return;
        } else {
            try {
                this.removeLast(length, offset);
                super.insertString(offset, stringValue.toUpperCase(), attributes);
            } catch (BadLocationException e) {
                MaskDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
            }
        }
    }

    protected void insertAsteriskString(int offset, String stringValue, AttributeSet attributes, int length)
            throws Exception {
        // All characters are allowed
        try {
            this.removeLast(length, offset);
            super.insertString(offset, stringValue, attributes);
        } catch (BadLocationException e) {
            MaskDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
        }
    }

    protected void insertHashString(int offset, String stringValue, AttributeSet attributes, int length)
            throws Exception {
        // Alphabetic characters
        if (!Character.isLetter(stringValue.charAt(0))) {
            return;
        } else {
            try {
                this.removeLast(length, offset);
                super.insertString(offset, stringValue, attributes);
            } catch (BadLocationException e) {
                MaskDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
            }
        }
    }

    private boolean isCharacterValidInOffset(char c, int offset) {
        char maskCharacter = this.mask.charAt(offset);
        boolean res = false;
        switch (maskCharacter) {
            case '*':
                res = true;
                break;
            case '#':
                // Alphabetic characters
                res = Character.isLetter(c);
                break;
            case 'A':
                // Upper case alphabetic characters
                res = Character.isLetter(c) && Character.isUpperCase(c);
                break;
            case 'a':
                // Lower case alphabetic characters
                res = Character.isLetter(c) && Character.isLowerCase(c);
                break;
            case '0':
                // digits
                res = Character.isDigit(c);
                break;
            case '%':
                // digits
                res = Character.isDigit(c) || Character.isLetter(c);
                break;
            default:
                res = c == maskCharacter;
                break;
        }
        return res;
    }

}
