package com.ontimize.util.templates;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.Form;

/*
 * Class to implements the action to open a template with the form data. 'fieldData' : List with all the fields to get the data separated by ;. 'imageData' : Attributes of the
 * images to use in the templates separated by ;. 'tableData' : Table data used to create the template separated by ;. 'templatePath' : Template path 'generator' : Manager to use
 * in the template creation process, default value is 'word'.
 */

public class OpenTemplateAction extends AbstractAction {

	private static final Logger	logger			= LoggerFactory.getLogger(OpenTemplateAction.class);

	public static final String FIELD_DATA = "fieldData";

	public static final String IMAGE_DATA = "imageData";

	public static final String TABLE_DATA = "tableData";

	public static final String TEMPLATE_PATH = "templatePath";

	public static final String GENERATOR = "generator";

	protected Form currentForm = null;

	protected Vector fieldDataList;
	protected Vector imageDataList;
	protected Vector tableDataList;

	protected String templateURL;

	public OpenTemplateAction(Form f, Hashtable parameters) {
		this.currentForm = f;
		this.init(parameters);
	}

	public OpenTemplateAction(Form f, Hashtable parameters, String name) {
		super(name);
		this.currentForm = f;
		this.init(parameters);
	}

	public OpenTemplateAction(Form f, Hashtable parameters, String name, Icon icon) {
		super(name, icon);
		this.currentForm = f;
		this.init(parameters);
	}

	protected void init(Hashtable parameters) {
		if (parameters.containsKey(OpenTemplateAction.TEMPLATE_PATH)) {
			this.templateURL = (String) parameters.get(OpenTemplateAction.TEMPLATE_PATH);
		}

		if (parameters.containsKey(OpenTemplateAction.FIELD_DATA)) {
			this.fieldDataList = new Vector();
			StringTokenizer temp = new StringTokenizer((String) parameters.get(OpenTemplateAction.FIELD_DATA), ";");
			while (temp.hasMoreTokens()) {
				this.fieldDataList.add(temp.nextToken());
			}
		}

		if (parameters.containsKey(OpenTemplateAction.IMAGE_DATA)) {
			this.imageDataList = new Vector();
			StringTokenizer temp = new StringTokenizer((String) parameters.get(OpenTemplateAction.IMAGE_DATA), ";");
			while (temp.hasMoreTokens()) {
				this.imageDataList.add(temp.nextToken());
			}
		}

		if (parameters.containsKey(OpenTemplateAction.TABLE_DATA)) {
			this.tableDataList = new Vector();
			StringTokenizer temp = new StringTokenizer((String) parameters.get(OpenTemplateAction.TABLE_DATA), ";");
			while (temp.hasMoreTokens()) {
				this.tableDataList.add(temp.nextToken());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			TemplateGenerator generator = this.getTemplateGenerator();
			generator.fillDocument(this.templateURL, this.createFieldData(), this.createTableData(), this.createImageData());
		} catch (Exception ex) {
			OpenTemplateAction.logger.error(null, ex);
		}
	}

	protected TemplateGenerator getTemplateGenerator() {
		return TemplateGeneratorFactory.templateGeneratorInstance(TemplateGeneratorFactory.WORD);
	}

	protected Hashtable createFieldData() {
		Hashtable data = new Hashtable();
		if ((this.fieldDataList == null) || (this.fieldDataList.size() == 0)) {
			if (AbstractTemplateGenerator.DEBUG) {
				OpenTemplateAction.logger.debug("Warning " + this.getClass() + ":" + OpenTemplateAction.FIELD_DATA + " doesn't set parameter value. All form fields be used");
			}
			data = this.currentForm.getDataFieldText();
		} else {
			data = this.currentForm.getDataFieldText(this.fieldDataList);
		}

		if (data.isEmpty()) {
			return data;
		}
		Enumeration keys = data.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = data.get(key);
			Vector v = new Vector();
			v.add(value);
			data.put(key, v);
		}
		return data;
	}

	protected Hashtable createTableData() {
		return new Hashtable();
	}

	protected Hashtable createImageData() {
		return new Hashtable();
	}
}
