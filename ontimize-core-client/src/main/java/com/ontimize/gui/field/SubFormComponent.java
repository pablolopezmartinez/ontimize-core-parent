package com.ontimize.gui.field;

import com.ontimize.gui.Form;
import com.ontimize.gui.manager.IFormManager;

public interface SubFormComponent {

	public void setFormManager(IFormManager manager);

	/**
	 * Get an instance of the subform component
	 *
	 * @return
	 */
	public Form getForm();
}
