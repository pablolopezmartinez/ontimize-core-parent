package com.ontimize.util.swing;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JComboBox;

import com.ontimize.gui.i18n.Internationalization;
import com.ontimize.util.swing.list.I18nListCellRenderer;

public class TranslateComboBox extends JComboBox implements Internationalization {

	protected ResourceBundle bundle = null;

	public TranslateComboBox(ResourceBundle bundle) {
		this.init(bundle);
	}

	public TranslateComboBox(ResourceBundle bundle, Vector v) {
		super(v);
		this.init(bundle);
	}

	private void init(ResourceBundle bundle) {
		this.bundle = bundle;
		if (this.bundle != null) {
			super.setRenderer(new I18nListCellRenderer(this.bundle));
		}
	}

	/**
	 * getTextsToTranslate
	 *
	 * @return Vector
	 */
	@Override
	public Vector getTextsToTranslate() {
		return null;
	}

	/**
	 * setLocaleComponente
	 *
	 * @param locale
	 *            Locale
	 */
	@Override
	public void setComponentLocale(Locale locale) {}

	/**
	 * setResourceBundle
	 *
	 * @param resourceBundle
	 *            ResourceBundle
	 */
	@Override
	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.bundle = resourceBundle;
		super.setRenderer(new I18nListCellRenderer(this.bundle));
	}
}
