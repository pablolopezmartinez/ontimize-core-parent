package com.ontimize.gui.button;

import javax.swing.Icon;

/**
 * This class implements a basic rollover button implementation.
 * <p>
 *
 * @author Imatia Innovation
 */
public class RolloverButton extends com.ontimize.util.swing.RolloverButton {

	/**
	 * Class constructor. Only calls to <code>super()</code>.
	 */
	public RolloverButton() {
		super();

	}

	/**
	 * Class constructor. Only calls to <code>super()</code> with parameter.
	 * <p>
	 *
	 * @param text
	 *            the text for button
	 */
	public RolloverButton(String text) {
		super(text);

	}

	/**
	 * Class constructor. Only calls to <code>super()</code> with parameter.
	 * <p>
	 *
	 * @param icon
	 *            the icon for button
	 */
	public RolloverButton(Icon icon) {
		super(icon);

	}
}
