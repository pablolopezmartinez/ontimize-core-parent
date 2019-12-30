package com.ontimize.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.sql.Types;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.BorderManager;
import com.ontimize.gui.ColorConstants;
import com.ontimize.gui.images.ImageManager;
import com.ontimize.util.templates.ITemplateField;

public class ParseUtils {

	private static final Logger logger = LoggerFactory.getLogger(ParseUtils.class);

	public static boolean getBoolean(String s, boolean defaultValue) {
		return ParseTools.getBoolean(s, defaultValue);
	}

	public static int getInteger(String s, int defaultValue) {
		return ParseTools.getInteger(s, defaultValue);
	}

	public static long getLong(String s, long defaultValue) {
		return ParseTools.getLong(s, defaultValue);
	}

	public static String getString(String s, String defaultValue) {
		return ParseTools.getString(s, defaultValue);
	}

	public static double getDouble(String s, double defaultValue) {
		return ParseTools.getDouble(s, defaultValue);
	}

	public static float getFloat(String s, float defaultValue) {
		return ParseTools.getFloat(s,defaultValue);
	}

	public static Image getImage(String path, Image defaultValue) {
		try {
			ImageIcon icon = ImageManager.getIcon(path);
			return icon == null ? defaultValue : icon.getImage();
		} catch (Exception e) {
			ParseUtils.logger.trace(null, e);
			return defaultValue;
		}
	}

	public static ImageIcon getImageIcon(String path, ImageIcon defaultValue) {
		try {
			ImageIcon icon = ImageManager.getIcon(path);
			return icon == null ? defaultValue : icon;
		} catch (Exception e) {
			ParseUtils.logger.trace(null, e);
			return defaultValue;
		}
	}

	public static Border getBorder(String borderName, Border defaultBorder) {
		if (borderName == null) {
			return defaultBorder;
		}
		if ("no".equals(borderName)) {
			return BorderManager.getBorder(BorderManager.EMPTY_BORDER_KEY);
		} else if ("yes".equals(borderName)) {
			return defaultBorder;
		} else if ("".equals(borderName)) {
			return defaultBorder;
		} else {
			Border border = BorderManager.getBorder(borderName);
			if (border != null) {
				return border;
			}
		}
		return defaultBorder;
	}

	public static Font getFont(String string, Font defaultFont) {
		if ((string == null) || "".equals(string)) {
			return defaultFont;
		}
		return Font.decode(string);
	}

	public static Color getColor(String string, Color defaultColor) {
		if ((string == null) || "".equals(string)) {
			return defaultColor;
		}
		try {
			return ColorConstants.parseColor(string);
		} catch (Exception e) {
			ParseUtils.logger.error(null, e);
			return defaultColor;
		}
	}

	public static Paint getPaint(String string, Paint defaultPaint) {
		if ((string == null) || "".equals(string)) {
			return defaultPaint;
		}
		Paint paint = null;
		try {
			paint = ColorConstants.paintNameToPaint(string);
		} catch (Exception e) {
			if (ApplicationManager.DEBUG) {
				ParseUtils.logger.error(null, e);
			} else {
				ParseUtils.logger.trace(null, e);
			}
			try {
				paint = ColorConstants.parseColor(string);
			} catch (Exception e1) {
				ParseUtils.logger.trace(null, e1);
				paint = defaultPaint;
			}
		}
		return paint;
	}

	public static Insets getMargin(String string, Insets defaultMargin) {
		if (string != null) {
			try {
				return ApplicationManager.parseInsets(string);
			} catch (Exception e) {
				ParseUtils.logger.trace(null, e);
				return defaultMargin;
			}
		} else {
			return defaultMargin;
		}
	}

	public static ImageIcon getPressedImageIcon(String pressedIconPath, String iconPath, ImageIcon defaultValue) {
		if (pressedIconPath != null) {
			ImageIcon icon = null;
			if (!pressedIconPath.equals("yes")) {
				icon = ImageManager.getIcon(pressedIconPath);
			}
			if (icon != null) {
				return icon;
			} else if (iconPath != null) {
				int index = iconPath.lastIndexOf(".");
				if (index == -1) {
					return ParseUtils.getImageIcon(iconPath + "_pressed", defaultValue);
				} else {
					return ParseUtils.getImageIcon(iconPath.substring(0, index) + "_pressed" + iconPath.substring(index, iconPath.length()), defaultValue);
				}
			}
		}
		return defaultValue;
	}

	public static ImageIcon getDisabledImageIcon(String disabledIconPath, String iconPath, ImageIcon defaultValue) {
		if (disabledIconPath != null) {
			ImageIcon icon = null;
			if (!disabledIconPath.equals("yes")) {
				icon = ImageManager.getIcon(disabledIconPath);
			}
			if (icon != null) {
				return icon;
			}
			if (iconPath != null) {
				int index = iconPath.lastIndexOf(".");
				if (index == -1) {
					return ParseUtils.getImageIcon(iconPath + "_disabled", defaultValue);
				} else {
					return ParseUtils.getImageIcon(iconPath.substring(0, index) + "_disabled" + iconPath.substring(index, iconPath.length()), defaultValue);
				}
			}
		}
		return defaultValue;
	}

	public static ImageIcon getRolloverImageIcon(String rolloverIconPath, String iconPath, ImageIcon defaultValue) {
		if (rolloverIconPath != null) {
			ImageIcon icon = null;
			if (!rolloverIconPath.equals("yes")) {
				icon = ImageManager.getIcon(rolloverIconPath);
			}
			if (icon != null) {
				return icon;
			}
			if (iconPath != null) {
				int index = iconPath.lastIndexOf(".");
				if (index == -1) {
					return ParseUtils.getImageIcon(iconPath + "_rollover", defaultValue);
				} else {
					return ParseUtils.getImageIcon(iconPath.substring(0, index) + "_rollover" + iconPath.substring(index, iconPath.length()), defaultValue);
				}
			}
		}
		return defaultValue;
	}

	public static String getCamelCase(String[] tokens) {
		StringBuilder buffer = new StringBuilder();

		for (int i = 0, size = tokens != null ? tokens.length : 0; i < size; i++) {
			String token = tokens[i];
			String ccToken = ParseUtils.getCamelCase(token);
			if ((ccToken != null) && (ccToken.length() > 0)) {
				buffer.append(ccToken);
			}
		}
		String s = buffer.toString();
		return s.toString();
	}

	public static String getCamelCase(String token) {
		return ParseTools.getCamelCase(token);
	}


	public static int getTemplateDataType(String templateType, int defaultTemplateType) {
		if (ITemplateField.DATA_TYPE_FIELD_ATTR.equalsIgnoreCase(templateType)) {
			return ITemplateField.DATA_TYPE_FIELD;
		}
		if (ITemplateField.DATA_TYPE_IMAGE_ATTR.equalsIgnoreCase(templateType)) {
			return ITemplateField.DATA_TYPE_IMAGE;
		}
		if (ITemplateField.DATA_TYPE_TABLE_ATTR.equalsIgnoreCase(templateType)) {
			return ITemplateField.DATA_TYPE_TABLE;
		}
		return defaultTemplateType;
	}

	public static String throwableToString(Throwable e, int lines) {
		return ApplicationManager.printStackTrace(e, lines).toString();
	}
	
	/**
	 * 
	 * @param object
	 * @param classType
	 * 
	 * @return
	 * @use ParseTools.getValueForClassType(object, classType);
	 */
	@Deprecated
	public static Object getValueForClassType(Object object, int classType) {
		return ParseTools.getValueForClassType(object, classType);
	}
	
	/**
	 * 
	 * @param typeName
	 * @return
	 * @use ParseTools.getSQLType(typeName);
	 */
	@Deprecated
	public static int getSQLType(String typeName) {
		return ParseTools.getSQLType(typeName);
	}
	
	/**
	 * 
	 * @param parseType
	 * @param defaultType
	 * @return
	 * @use ParseTools.getSQLType(parseType, defaultType);
	 */
	@Deprecated
	public static int getSQLType(int parseType, int defaultType) {
		return ParseTools.getSQLType(parseType, defaultType);
	}
	/**
	 * 
	 * @param typeName
	 * @param defaultValue
	 * @return
	 * @use ParseTools.getTypeForName(typeName, defaultValue);
	 */
	@Deprecated
	public static int getTypeForName(String typeName, int defaultValue) {
		return ParseTools.getTypeForName(typeName, defaultValue);
	}
	
	/**
	 * @param typeName
	 * @return
	 * @use ParseTools.getIntTypeForName(typeName);
	 */
	@Deprecated
	public static int getIntTypeForName(String typeName) {
		return ParseTools.getIntTypeForName(typeName);
	}
	
	/**
	 * 
	 * @param classType
	 * @return
	 * @use ParseTools.getClassType(classType);
	 */
	@Deprecated
	public static Class getClassType(int classType) {
		return ParseTools.getClassType(classType);
	}

	
	/**
	 * 
	 * @param calendarField
	 * @return
	 * @use ParseTools.getCalendarField(calendarField);
	 */
	@Deprecated
	public static int getCalendarField(String calendarField) {
		return ParseTools.getCalendarField(calendarField);
	}
	
	
	public static Object getValueForSQLType(Object object, int sqlType) {
		return ParseTools.getValueForSQLType(object, sqlType);
	}
}
