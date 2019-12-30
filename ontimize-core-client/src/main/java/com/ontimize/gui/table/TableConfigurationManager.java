package com.ontimize.gui.table;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class that manages renderers and editors for component <code>Table</code> when these ones are defined in an external file (complete path to this file must be established with
 * variable {@link Table#rendererEditorConfigurationFile}). Structure of this file should be similar to:</br>
 * <br>
 * <code> &lt?xml version="1.0" encoding="UTF-8"?&gt <br><br>
 * &ltTableConfiguration&gt <br><br>
 *
 * &nbsp&ltTableRenderers&gt <br> &nbsp&nbsp&nbsp&ltcom.ontimize.gui.table.BooleanCellRenderer rendererid="boolean" /&gt <br>
 * &nbsp&nbsp&nbsp&ltcom.ontimize.gui.table.BundleCellRenderer rendererid="bundle" /&gt <br> &nbsp&nbsp&nbsp&ltcom.ontimize.gui.table.CurrencyCellRenderer rendererid="currency"
 * /&gt <br> &nbsp&nbsp&nbsp&ltcom.ontimize.gui.table.DateCellRenderer rendererid="date" /&gt <br> &nbsp&nbsp&nbsp&ltcom.ontimize.gui.table.MemoCellRenderer rendererid="memo" /&gt
 * <br> &nbsp&nbsp&nbsp&ltcom.ontimize.gui.table.PercentCellRenderer rendererid="percent" /&gt <br> &nbsp&nbsp&nbsp&ltcom.ontimize.gui.table.RealCellRenderer rendererid="real" /&gt
 * <br> &nbsp&lt/TableRenderers&gt <br><br>
 *
 * &nbsp&ltTableEditors&gt <br> &nbsp&nbsp&nbsp&ltcom.ontimize.gui.table.BooleanCellEditor editorid="boolean" /&gt <br> &nbsp&lt/TableEditors&gt <br><br>
 *
 * &lt/TableConfiguration&gt <br> </code>
 *
 * @author Imatia Innovation SL
 * @since 5.2058EN
 */
public class TableConfigurationManager {

	private static final Logger					logger			= LoggerFactory.getLogger(TableConfigurationManager.class);

	protected Hashtable renderersConfig = new Hashtable();

	protected Hashtable editorsConfig = new Hashtable();

	protected static TableConfigurationManager tableConfigurationManager;

	/**
	 * This method is called from <code>Table</code> with file path parameter. It is a singleton and all <code>Table</code> objects share the same instance.
	 *
	 * @param filePath
	 *            Complete path to file.
	 * @param create
	 *            Condition to create or not the object when not exists.
	 * @return An instance of this class.
	 */
	public static TableConfigurationManager getTableConfigurationManager(String filePath, boolean create) {
		if ((TableConfigurationManager.tableConfigurationManager == null) && create) {
			try {
				TableConfigurationManager.tableConfigurationManager = new TableConfigurationManager(filePath);
			} catch (Exception e) {
				TableConfigurationManager.logger.error(null, e);
			}
		}
		return TableConfigurationManager.tableConfigurationManager;
	}

	public static void setTableConfigurationManager(TableConfigurationManager manager) {
		TableConfigurationManager.tableConfigurationManager = manager;
	}

	public TableConfigurationManager(InputStream inputStream) throws Exception {
		this.configure(inputStream);
	}

	public TableConfigurationManager() {}

	public TableConfigurationManager(String filePath) throws Exception {
		URL url = this.getClass().getClassLoader().getResource(filePath);
		if (url == null) {
			throw new Exception(this.getClass().getName() + ": file path specified not found.");
		}
		InputStream openStream = url.openStream();
		this.configure(openStream);
		openStream.close();
	}

	/**
	 * Parses TableRenderers and TableEditors.
	 *
	 * @param inputStream
	 *            Input stream
	 * @throws Exception
	 *             When occurs an exception parsing
	 */
	protected void configure(InputStream inputStream) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = factory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputStream);

		NodeList nodeList = null;

		nodeList = doc.getElementsByTagName("TableRenderers");
		if ((nodeList != null) && (nodeList.getLength() == 1)) {
			this.configureRenderers(nodeList);
		}
		nodeList = doc.getElementsByTagName("TableEditors");
		if ((nodeList != null) && (nodeList.getLength() == 1)) {
			this.configureEditors(nodeList);
		}
	}

	public void addElements(String xmlDefinition) throws Exception {
		ByteArrayInputStream stream = new ByteArrayInputStream(xmlDefinition.getBytes());
		this.configure(stream);
		stream.close();
	}

	/**
	 * Method called internally by {@link #configure(InputStream)} in order to parse renderers.
	 *
	 * @param inputStream
	 *            Input stream
	 * @throws Exception
	 *             When occurs an exception parsing
	 */
	protected void configureRenderers(NodeList nodeList) {
		Node rootNode = nodeList.item(0);

		NodeList childNodes = rootNode.getChildNodes();

		int index = 0;
		for (int i = 0; i < childNodes.getLength(); i++) {
			try {
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					index++;
					Node namedItem = item.getAttributes().getNamedItem("rendererid");
					if (namedItem != null) {
						String rendererId = namedItem.getNodeValue();
						if (rendererId != null) {
							TableElementConfig element = this.getElement(rendererId, item);
							if (element != null) {
								this.renderersConfig.put(rendererId, element);
							}
						} else {
							TableConfigurationManager.logger.debug(TableConfigurationManager.class + " : rendererid cannot be null in element " + index);
						}
					} else {
						TableConfigurationManager.logger.debug(TableConfigurationManager.class + " : rendererid cannot be null in element " + index);
					}
				}
			} catch (Exception e) {
				TableConfigurationManager.logger.error(null, e);
			}
		}
	}

	/**
	 * Method called internally by {@link #configure(InputStream)} in order to parse editors.
	 *
	 * @param inputStream
	 *            Input stream
	 * @throws Exception
	 *             When occurs an exception parsing
	 */
	protected void configureEditors(NodeList nodeList) {
		Node rootNode = nodeList.item(0);
		NodeList childNodes = rootNode.getChildNodes();
		int index = 0;
		for (int i = 0; i < childNodes.getLength(); i++) {
			try {
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					index++;
					Node namedItem = item.getAttributes().getNamedItem("editorid");
					if (namedItem != null) {
						String rendererId = namedItem.getNodeValue();
						if (rendererId != null) {
							TableElementConfig element = this.getElement(rendererId, item);
							if (element != null) {
								this.editorsConfig.put(rendererId, element);
							}
						} else {
							TableConfigurationManager.logger.debug(TableConfigurationManager.class + " : editorid cannot be null in element " + index);
						}
					} else {
						TableConfigurationManager.logger.debug(TableConfigurationManager.class + " : editorid cannot be null in element " + index);
					}
				}
			} catch (Exception e) {
				TableConfigurationManager.logger.error(null, e);
			}
		}
	}

	protected TableElementConfig getElement(Object id, Node item) {
		String className = item.getNodeName();
		Hashtable params = null;
		// Gets the attribute list
		NamedNodeMap attributeList = item.getAttributes();
		params = new Hashtable();
		for (int n = 0; n < attributeList.getLength(); n++) {
			Node node = attributeList.item(n);
			params.put(node.getNodeName(), node.getNodeValue());
		}
		return new TableElementConfig(id, className, params);
	}

	public TableCellRenderer getCellRenderer(Object id) {
		TableElementConfig element = (TableElementConfig) this.renderersConfig.get(id);
		if (element != null) {
			return (TableCellRenderer) element.getElement();
		}
		return null;
	}

	public TableCellEditor getCellEditor(String id, String columnName) {
		TableElementConfig element = (TableElementConfig) this.editorsConfig.get(id);
		if (element != null) {
			return (TableCellEditor) element.getElement(columnName);
		}
		return null;
	}

	/**
	 * Internal class for creating objects that manage identifier, class name and parameters for each element.
	 *
	 * @author Imatia Innovation SL
	 * @since 5.2058EN
	 */
	public static class TableElementConfig {

		protected Object elementId;

		protected String className;

		protected Hashtable parameters;

		public TableElementConfig(Object id, String className, Hashtable params) {
			this.elementId = id;
			this.className = className;
			this.parameters = params;
		}

		public Object getElementId() {
			return this.elementId;
		}

		public String getClassName() {
			return this.className;
		}

		public Hashtable getParameters() {
			return this.parameters;
		}

		/**
		 * Calls to {@link #createElement(Hashtable)} with {@link #parameters}.
		 *
		 * @return
		 */
		public Object getElement() {
			return this.createElement(this.parameters);
		}

		/**
		 * Calls to {@link #createElement(Hashtable)} with {@link #parameters}. Additionally, <code>CellEditor</code> needs the parameter <i>column</i> (to specify the column where
		 * to be applied) that is passed in this method by parameter.
		 *
		 * @param columnName
		 *            The name of column
		 * @return the object
		 */
		public Object getElement(String columnName) {
			Hashtable params = new Hashtable();
			if (this.parameters != null) {
				params.putAll(this.parameters);
			}
			params.put(CellEditor.COLUMN_PARAMETER, columnName);
			return this.createElement(params);
		}

		/**
		 * By reflection it is instanced the class indicated in {@link #className} using the constructor whose parameter is a <code>Hashtable</code>.
		 *
		 * @param params
		 *            <code>Hashtable</code> with parameters to object
		 * @return the object or null when cannot be created
		 */
		protected Object createElement(Hashtable params) {
			try {
				Class elementClass = Class.forName(this.className);
				Constructor constructor = null;
				Object[] parameters = null;
				try {
					constructor = elementClass.getConstructor(new Class[] { Hashtable.class });
					parameters = new Object[] { params };
				} catch (Exception e) {
					TableConfigurationManager.logger.trace(null, e);
					constructor = elementClass.getConstructor(new Class[0]);
					parameters = new Object[0];
				}
				return constructor.newInstance(parameters);
			} catch (Exception e) {
				TableConfigurationManager.logger.error(null, e);
				return null;
			}
		}
	}
}
