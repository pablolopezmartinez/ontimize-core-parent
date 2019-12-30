package com.ontimize.gui.field.document;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.table.DateCellEditor;

/**
 * Document for managing hour and dates in same field. It is useful for table editors that map columns with timestamp values.
 *
 * @see {@link DateCellEditor}
 *
 * @author Imatia Innovation SL
 * @since 5.2059EN
 */
public class HourDateDocument extends DateDocument {

	private static final Logger			logger					= LoggerFactory.getLogger(HourDateDocument.class);

	/**
	 * Default pattern for hours HH:mm
	 */
	public static final String HH_mm = "HH:mm";

	public static final char charDateHourSeparator = ' ';

	protected String sDateHourSeparator = new String(new char[] { HourDateDocument.charDateHourSeparator });

	/**
	 * String with hour pattern
	 */
	protected String patternHour;

	/**
	 * String with hour and date patterns separated by <code>charDateHourSeparator</code>. Order of these patterns will be assigned according to variable <code>hourFirst</code>. By
	 * default, hour is showed at first.
	 */
	protected String patternHourDate;

	/**
	 * Date that stores the document for each moment.
	 */
	protected Date currentDocumentDate;

	/**
	 * Instance of date formatter
	 */
	protected static SimpleDateFormat dfHourDate;

	/**
	 * It assigns a pattern to the document without date, only with hour.
	 */
	protected boolean onlyHour;

	/**
	 * Hour will be showed at first.
	 */
	protected boolean hourFirst;

	static {
		if (HourDateDocument.dfHourDate == null) {
			HourDateDocument.dfHourDate = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM, DateDocument.defaultLocale);
		}
	}

	/**
	 * Constructor that creates a date document with hour and date, when hour is showed at first.
	 */
	public HourDateDocument() {
		this(false, true);
	}

	public HourDateDocument(boolean onlyHour, boolean hourFirst) {
		this.onlyHour = onlyHour;
		this.hourFirst = hourFirst;
	}

	@Override
	public void insertString(int offset, String sValue, AttributeSet attributes) throws BadLocationException {
		// Date introduced reaches the date pattern length
		if (offset >= this.patternHourDate.length()) {
			return;
		}
		// We are doing i.e. copy-paste where date is longer than pattern, it
		// must be truncated
		if (sValue.length() > this.patternHourDate.length()) {
			sValue = sValue.substring(0, this.patternHourDate.length());
		}
		char patternChar = this.patternHourDate.charAt(offset);
		switch (patternChar) {
		// '/'
		case '/':
			sValue = "/";
			break;
		// ':'
		case HourDocument.SEPARATOR:
			sValue = HourDocument.SEPARATOR_STR;
			break;
		case ' ':
			sValue = this.sDateHourSeparator;
			break;
		default:
			break;
		}
		this.insertStringWithoutCheck(offset, sValue, attributes);
	}

	/**
	 * Builds pattern for date and hour. Moreover, it concatenates both patterns.
	 */
	@Override
	protected void buildPattern() {
		// builds pattern for date (i.e. dd/mm/yyyy)
		super.buildPattern();
		// builds hour pattern
		this.buildHourPattern();
		// Concatenates both previous patterns
		this.buildHourDatePattern();
	}

	/**
	 * Concatenates both patterns.
	 */
	protected void buildHourDatePattern() {
		if (this.onlyHour) {
			this.patternHourDate = this.patternHour;
		} else {
			if (this.hourFirst) {
				this.patternHourDate = this.patternHour + " " + this.datePattern;
			} else {
				this.patternHourDate = this.datePattern + " " + this.patternHour;
			}

		}
		HourDateDocument.dfHourDate.applyPattern(this.patternHourDate);
	}

	/**
	 * Applies specified pattern to date formatter.
	 *
	 * @param patternHourDate
	 *            The pattern to be applied.
	 */
	public void setPattern(String patternHourDate) {
		this.patternHourDate = patternHourDate;
		HourDateDocument.dfHourDate.applyPattern(patternHourDate);
	}

	/**
	 * Builds hour pattern.
	 */
	protected void buildHourPattern() {
		String sPattern = HourDateDocument.dfHourDate.toPattern();
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
			if (index_a >= 0) {
				if (index_a < index_h) {
					this.patternHour = HourDocument.a_kk_mm_ss;
				} else {
					this.patternHour = HourDocument.hh_mm_ss_a;
				}
			}
		} else {
			this.patternHour = HourDateDocument.HH_mm;
		}
	}

	/**
	 * Pattern is valid when parsing returns a correct date.
	 */
	@Override
	public boolean isValid() {
		boolean bValid = false;
		try {
			this.currentDocumentDate = HourDateDocument.dfHourDate.parse(this.getText(0, this.getLength()));
			if (this.onlyHour) {
				return this.isValidHour(this.getText(0, this.patternHour.length()));
			}
			if (this.hourFirst) {
				bValid = super.isValid(this.getText(this.patternHour.length() + 1, this.getLength() - this.patternHour.length()));
				if (bValid) {
					bValid = this.isValidHour(this.getText(0, this.patternHour.length()));
				}
			} else {
				bValid = super.isValid(this.getText(0, this.datePattern.length()));
				if (bValid) {
					bValid = this.isValidHour(this.getText(this.datePattern.length() + 1, this.getLength() - this.datePattern.length()));
				}
			}
		} catch (Exception e) {
			HourDateDocument.logger.trace(null, e);
			return false;
		}
		return bValid;
	}

	/**
	 * Checks whether hour is valid.
	 *
	 * @param sHour
	 *            <code>String</code> with hour
	 * @return True when hour is well formed according to the hour pattern
	 */
	public boolean isValidHour(String sHour) {

		if (sHour.length() < this.patternHour.length()) {
			return false;
		} else {
			try {
				if (this.patternHour.startsWith("K") || this.patternHour.startsWith("k") || this.patternHour.startsWith("H") || this.patternHour.startsWith("h")) {
					if (sHour.length() < 7) {
						this.patternHour = this.patternHour.substring(0, 5);
					}
				}
				DateFormatSymbols symbols = new DateFormatSymbols();
				symbols.setLocalPatternChars("GyMdkHmsSEDFwWahKz");
				DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, this.locale);
				((SimpleDateFormat) df).setDateFormatSymbols(symbols);
				((SimpleDateFormat) df).applyPattern(this.patternHour);
				Date hour = df.parse(sHour);
				df.format(hour);
				this.currentTimestamp = new Timestamp(this.currentDocumentDate.getTime());
				return true;
			} catch (ParseException e) {
				return false;
			}
		}
	}

	@Override
	public String getText(int offset, int length) throws BadLocationException {
		String sCurrentText = null;
		try {
			sCurrentText = super.getText(offset, length);
		} catch (Exception e) {
			if (DateDocument.DEBUG) {
				try {
					HourDateDocument.logger.debug(this.getClass().getName() + ": Unparseable date: " + this.getText(0, this.getLength()), e);
				} catch (BadLocationException e1) {
					HourDateDocument.logger.trace(null, e1);
				}
			}
		}
		return sCurrentText;
	}

	@Override
	public void setValue(Date value) {
		try {
			String stringDate = HourDateDocument.dfHourDate.format(value);
			this.remove(0, this.getLength());
			this.insertString(0, stringDate, null);
		} catch (Exception e) {
			if (ApplicationManager.DEBUG) {
				HourDateDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
			}
		}
	}

	@Override
	public Timestamp getTimestampValue() {
		// format();
		return this.currentTimestamp;
	}

}
