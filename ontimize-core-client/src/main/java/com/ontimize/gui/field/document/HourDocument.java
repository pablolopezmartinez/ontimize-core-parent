package com.ontimize.gui.field.document;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.i18n.Internationalization;

/**
 * This document implements the model for managing dates in a JTextField
 *
 * @version 1.0 22/01/2002
 */
public class HourDocument extends PlainDocument implements Internationalization {

	private static final Logger	LOGGER			= LoggerFactory.getLogger(HourDocument.class);

	public static final String KK_mm_ss = "KK:mm:ss";

	public static final String kk_mm_ss_a = "kk:mm:ss aa";

	public static final String a_kk_mm_ss = "aa kk:mm:ss";

	public static final String HH_mm_ss = "HH:mm:ss";

	public static final String hh_mm_ss_a = "hh:mm:ss aa";

	public static final String a_hh_mm_ss = "aa hh:mm:ss";

	public static final char SEPARATOR = ':';

	public static final String SEPARATOR_STR = ":";

	protected Locale locale = Locale.getDefault();

	protected SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM, this.locale);

	protected String patternHour;

	protected Date hour = new Date();

	protected Timestamp hourTimestamp = new Timestamp(this.hour.getTime());

	public HourDocument() {
		// Uses the locale to set the pattern
		DateFormatSymbols symbols = new DateFormatSymbols();
		symbols.setLocalPatternChars("GyMdkHmsSEDFwWahKz");
		this.dateFormat.setDateFormatSymbols(symbols);
		this.buildPattern();
	}

	public Date getHour() {
		return this.hour;
	}

	/**
	 * Characters used in date format: k,K,h,H,m,s, a (format am-pm) Hour patterns could be:<br>
	 * - HH:mm:ss<br>
	 * - HH:mm:ss<br>
	 * - hh:mm:ss a<br>
	 * - kk:mm:ss a<br>
	 * - a hh:mm:ss<br>
	 * - a kk:mm:ss<br>
	 * - KK:mm:ss<br>
	 */
	protected void buildPattern() {
		String sPattern = this.dateFormat.toPattern();
		int index_K = sPattern.indexOf('K');
		int index_k = sPattern.indexOf('k');
		int index_h = sPattern.indexOf('h');
		int index_a = sPattern.indexOf('a');

		if (index_K >= 0) {
			this.patternHour = HourDocument.KK_mm_ss;
		} else if (index_k >= 0) {
			if (index_a < index_k) {
				this.patternHour = HourDocument.a_kk_mm_ss;
			} else {
				this.patternHour = HourDocument.kk_mm_ss_a;
			}
		} else if (index_h >= 0) {
			if (index_a < index_h) {
				this.patternHour = HourDocument.a_kk_mm_ss;
			} else {
				this.patternHour = HourDocument.hh_mm_ss_a;
			}
		} else {
			this.patternHour = HourDocument.HH_mm_ss;
		}
		this.dateFormat.applyPattern(this.patternHour);
	}

	@Override
	public void setResourceBundle(ResourceBundle res) {}

	@Override
	public Vector getTextsToTranslate() {
		Vector v = new Vector();
		return v;
	}

	@Override
	public void setComponentLocale(Locale l) {
		this.locale = l;
		int length = 0;
		try {
			length = this.getLength();
			if (length > 0) {
				this.hour = this.dateFormat.parse(this.getText(0, length));
			}
			DateFormatSymbols symbols = new DateFormatSymbols();
			symbols.setLocalPatternChars("GyMdkHmsSEDFwWahKz");
			DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, l);
			this.dateFormat.setDateFormatSymbols(symbols);
			if (df instanceof SimpleDateFormat) {
				this.dateFormat = (SimpleDateFormat) df;
				this.buildPattern();
				this.dateFormat.applyPattern(this.patternHour);
				this.remove(0, length);
				if (length > 0) {
					this.insertString(0, this.dateFormat.format(this.hour), null);
				}
			}
		} catch (Exception e) {
			HourDocument.LOGGER.trace(null, e);
			try {
				this.remove(0, length);
			} catch (Exception e2) {
				HourDocument.LOGGER.trace(null, e2);
			}
		}
	}

	@Override
	public void insertString(int offset, String stringValue, AttributeSet attributes) throws BadLocationException {
		// Only numeric values are allowed
		if (offset >= this.patternHour.length()) {
			return;
		}
		int stringValueLength = stringValue.length();
		if (stringValueLength != 1) {
			for (int i = 0; i < stringValueLength; i++) {
				this.insertString(i, stringValue.substring(i, i + 1), attributes);
			}
			return;
		} else {
			if (stringValueLength == 1) {
				if (offset == this.patternHour.indexOf(" ")) {
					super.insertString(offset, " ", attributes);
					this.insertString(offset + 1, stringValue, attributes);
					this.isValid();
					return;
				}
				if (offset == this.patternHour.indexOf('a')) {
					if (stringValue.equalsIgnoreCase("p")) {
						super.insertString(offset, "PM", attributes);
						this.isValid();
						return;
					} else if (stringValue.equalsIgnoreCase("a")) {
						super.insertString(offset, "AM", attributes);
						this.isValid();
						return;
					}
				}
				char character = stringValue.charAt(0);
				if ((character == HourDocument.SEPARATOR) && (this.patternHour.charAt(offset) == HourDocument.SEPARATOR)) {
					super.insertString(offset, stringValue, attributes);
					return;
				}
				if (Character.isDigit(character)) {
					// If it is the separator position then add it
					if (this.patternHour.charAt(offset) == HourDocument.SEPARATOR) {
						String currentString = this.getText(0, this.getLength());
						if (offset < currentString.length()) {
							if (currentString.charAt(offset) != HourDocument.SEPARATOR) {
								stringValue = HourDocument.SEPARATOR_STR + stringValue;
								super.insertString(offset, stringValue, attributes);
								this.isValid();
								return;
							} else {
								stringValue = HourDocument.SEPARATOR_STR + stringValue;
								// Insert the separator in the next character
								super.insertString(offset, stringValue, attributes);
								this.isValid();
								return;
							}
						} else {
							stringValue = HourDocument.SEPARATOR_STR + stringValue;
							super.insertString(offset, stringValue, attributes);
							this.isValid();
							return;
						}
					} else {
						// If the character in 'offset' position is not/
						// SEPARATOR_STR
						super.insertString(offset, stringValue, attributes);
						this.isValid();
						return;
					}
				}
			}
		}
	}

	public SimpleDateFormat getFormat() {
		return this.dateFormat;
	}

	public void format() {
		String sPattern = this.patternHour;
		try {
			if (this.patternHour.startsWith("K") || this.patternHour.startsWith("k") || this.patternHour.startsWith("H") || this.patternHour.startsWith("h")) {
				if (this.getLength() < 7) {
					this.patternHour = this.patternHour.substring(0, 5);
				}
			}
			String sHour = this.getText(0, this.getLength());
			this.dateFormat.applyPattern(this.patternHour);
			this.hour = this.dateFormat.parse(sHour);
			this.dateFormat.applyPattern(sPattern);
			this.patternHour = sPattern;
			String sNewDate = this.dateFormat.format(this.hour);
			this.remove(0, this.getLength());
			this.insertStringWithoutCheck(0, sNewDate, null);
		} catch (Exception excepcion) {
			try {
				super.remove(0, this.getLength());
			} catch (Exception e) {
				HourDocument.LOGGER.trace(null, e);
			}
			if (ApplicationManager.DEBUG) {
				HourDocument.LOGGER.trace(null, excepcion);
			} else {
				HourDocument.LOGGER.trace(null, excepcion);
			}
		} finally {
			this.patternHour = sPattern;
			this.dateFormat.applyPattern(sPattern);
		}

	}

	public boolean isValid() {
		if (this.getLength() < this.patternHour.length()) {
			return false;
		} else {
			try {
				String sPattern = this.patternHour;
				String sHour = this.getText(0, this.getLength());
				try {
					if (this.patternHour.startsWith("K") || this.patternHour.startsWith("k") || this.patternHour.startsWith("H") || this.patternHour.startsWith("h")) {
						if (this.getLength() < 7) {
							this.patternHour = this.patternHour.substring(0, 5);
						}
					}
					this.dateFormat.applyPattern(this.patternHour);
					this.hour = this.dateFormat.parse(sHour);
					this.dateFormat.applyPattern(sPattern);
					this.patternHour = sPattern;
					this.dateFormat.format(this.hour);
					this.hourTimestamp = new Timestamp(this.hour.getTime());
					return true;
				} catch (ParseException e) {
					this.patternHour = sPattern;
					this.dateFormat.applyPattern(sPattern);
					return false;
				}
			} catch (BadLocationException e) {
				HourDocument.LOGGER.trace(null, e);
				return false;
			}
		}
	}

	public String getSampleHour() {
		return this.patternHour;
	}

	protected void insertStringWithoutCheck(int offset, String stringValue, AttributeSet attributes) throws BadLocationException {
		super.insertString(offset, stringValue, attributes);
	}

}
