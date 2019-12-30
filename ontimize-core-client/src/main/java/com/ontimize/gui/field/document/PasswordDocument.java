package com.ontimize.gui.field.document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordDocument extends PlainDocument {

	private static final Logger logger = LoggerFactory.getLogger(PasswordDocument.class);

	protected char echoChar = '*';

	protected int maxLength = -1;

	protected StringBuilder		contain			= new StringBuilder("");

	protected boolean patternMatches = true;

	public PasswordDocument() {
		super();
	}

	public PasswordDocument(char maskCharacter) {
		this(maskCharacter, -1);
	}

	public PasswordDocument(char maskCharacter, int limit) {
		super();
		this.echoChar = maskCharacter;
		this.maxLength = limit;
	}

	@Override
	public void insertString(int offset, String stringValue, AttributeSet attributes) {
		try {
			StringBuilder sbMaskString = new StringBuilder(stringValue.length());
			for (int i = 0; i < stringValue.length(); i++) {
				sbMaskString.append(this.echoChar);
			}

			if ((this.maxLength < 0) || ((this.getLength() + sbMaskString.length()) <= this.maxLength)) {
				this.contain.insert(offset, stringValue);
				super.insertString(offset, sbMaskString.toString(), attributes);
			}
		} catch (Exception e) {
			PasswordDocument.logger.debug("Exception Password Document:", e);
		}
	}

	@Override
	public void remove(int offset, int length) {
		// Delete
		try {
			this.contain.delete(offset, offset + length);
			super.remove(offset, length);
		} catch (Exception e) {
			PasswordDocument.logger.debug("Exception PasswordDocument (Remove):", e);

		}
	}

	public String getContain() {
		return this.contain.toString();
	}

	public void setPatternMatches(boolean b) {
		this.patternMatches = b;
	}

	public boolean isPatternMatches() {
		return this.patternMatches;
	}

	public boolean checkPattern(String password, String patternString) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(password.toString());

		if (matcher.matches()) {
			this.setPatternMatches(true);
			return true;
		} else {
			this.setPatternMatches(false);
			return false;
		}

	}
}