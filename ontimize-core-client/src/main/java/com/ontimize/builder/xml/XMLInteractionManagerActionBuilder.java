package com.ontimize.builder.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ontimize.builder.InteractionManagerActionBuilder;
import com.ontimize.gui.BasicInteractionManagerAction;
import com.ontimize.gui.InteractionManager;
import com.ontimize.gui.InteractionManagerAction;
import com.ontimize.gui.InteractionManagerAction.Listener;
import com.ontimize.gui.InteractionManagerAction.ListenerItem;
import com.ontimize.gui.InteractionManagerAction.Mode;
import com.ontimize.gui.InteractionManagerAction.ModeAction;

/**
 * <p>
 * Creates a {@link InteractionManagerAction} object model with a set of actions
 * to manage the parent form state with the given XML from the
 * {@link InteractionManager}.
 * <p>
 * Implementation of {@link InteractionManagerActionBuilder}
 *
 * @author Imatia Innovation S.L.
 * @since Ontimize 5.2059EN
 */
public class XMLInteractionManagerActionBuilder extends com.ontimize.xml.XMLInterpreter implements com.ontimize.builder.InteractionManagerActionBuilder {
	static final Logger logger = LoggerFactory.getLogger(XMLInteractionManagerActionBuilder.class);

	// Mode & Actions

	public static final String MODE_KEY = "Mode";
	public static final String MODE_ID_KEY = "id";
	public static final String ACTION_ATTR_KEY = "attr";
	public static final String ACTION_VALUE_KEY = "value";
	public static final String ACTION_CLASS_KEY = "class";

	// Listeners

	public static final String LISTENER_KEY = "Listener";
	public static final String LISTENER_ID_KEY = "id";
	public static final String LISTENER_ATTR_KEY = "attr";
	public static final String LISTENER_CLASS_KEY = "class";

	private static final String MSG_WARNING = " WARNING -> ";

	private static final String MSG_GET_STREAM_RESOURCE_NULL = "Resource parameter not found";
	private static final String MSG_GET_STREAM_RESOURCE_EMPTY = "Resource parameter is empty";
	private static final String MSG_GET_STREAM_FILE_NULL = "Resource is not a existing file";
	private static final String MSG_GET_STREAM_FILE_INVALID = "Resource is not a valid file";
	private static final String MSG_GET_STREAM_STREAM_NULL = "Resource is not valid";
	private static final String MSG_GET_STREAM_NULL = "Stream resource is not valid";

	private static final String MSG_CREATE_MANAGER_NAME_NULL = "Manager: Class name not found or is empty.";
	private static final String MSG_CREATE_MANAGER_CLASS_NULL = "Manager: Class not found in classpath ";
	private static final String MSG_CREATE_MANAGER_CLASS_INVALID = "Manager: Class is not a instanceof com.ontimize.gui.InteractionManagerAction interface. ";

	protected InputStream getStream(String resource) throws IOException {
		if (resource == null) {
			throw new IllegalArgumentException(XMLInteractionManagerActionBuilder.MSG_GET_STREAM_RESOURCE_NULL);
		}
		if (resource.length() == 0) {
			throw new IllegalArgumentException(XMLInteractionManagerActionBuilder.MSG_GET_STREAM_RESOURCE_EMPTY);
		}

		try {
			File file = new File(resource);
			if (!file.exists()) {
				throw new IllegalArgumentException(XMLInteractionManagerActionBuilder.MSG_GET_STREAM_FILE_NULL);
			}
			if (!file.isFile()) {
				throw new IllegalArgumentException(XMLInteractionManagerActionBuilder.MSG_GET_STREAM_FILE_INVALID);
			}

			InputStream is = new FileInputStream(file);
			return is;
		} catch (Exception ex) {
			XMLInteractionManagerActionBuilder.logger.error(ex.getMessage(), ex);
		}

		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
			if (is == null) {
				throw new IllegalArgumentException(XMLInteractionManagerActionBuilder.MSG_GET_STREAM_STREAM_NULL);
			}
			return is;
		} catch (Exception ex) {
			XMLInteractionManagerActionBuilder.logger.error(ex.getMessage(), ex);
		}
		return null;
	}

	protected static void warning(Exception ex) {
		XMLInteractionManagerActionBuilder.warning(ex != null ? ex.getMessage() : null);
	}

	protected static void warning(String message) {
		if ((message != null) && (message.length() > 0)) {
			StringBuilder s = new StringBuilder();
			s.append(XMLInteractionManagerActionBuilder.class.getName());
			s.append(XMLInteractionManagerActionBuilder.MSG_WARNING);
			s.append(message);
			XMLInteractionManagerActionBuilder.logger.warn(s.toString());
		}
	}

	@Override
	public InteractionManagerAction buildAction(String resource) {
		InteractionManagerAction imAction = null;
		InputStream is = null;
		try {
			is = this.getStream(resource);
			imAction = this.buildAction(is);
		} catch (Exception ex) {
			XMLInteractionManagerActionBuilder.warning(ex);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception ex) {
					XMLInteractionManagerActionBuilder.logger.trace(null, ex);
				}
			}
		}
		return imAction;
	}

	@Override
	public InteractionManagerAction buildAction(InputStream resource) {
		try {
			if (resource == null) {
				throw new IllegalArgumentException(XMLInteractionManagerActionBuilder.MSG_GET_STREAM_NULL);
			}

			// Check the root node
			Document document = this.getDocumentModel(resource);
			Element root = document.getDocumentElement();

			InteractionManagerAction action = this.createManager(root);
			if (action == null) {
				return null;
			}

			// Check the mode list.
			NodeList childrenNL = root.getChildNodes();
			for (int i = 0, size = childrenNL != null ? childrenNL.getLength() : 0; i < size; i++) {

				if (childrenNL != null) {
					Node childNode = childrenNL.item(i);
					this.checkNodeActionList(childNode, action);
					this.checkNodeListenerList(childNode, action);
				}


			}
			return action;
		} catch (Exception ex) {
			XMLInteractionManagerActionBuilder.warning(ex);
		}
		return null;
	}

	protected void checkNodeActionList(Node childNode, InteractionManagerAction action) {
		InteractionManagerAction.Mode modeObj = this.createMode(childNode);
		if (modeObj != null) {
			NodeList actionNL = childNode.getChildNodes();
			for (int j = 0, actionSize = actionNL != null ? actionNL.getLength() : 0; j < actionSize; j++) {
				if (actionNL != null) {
					Node actionNode = actionNL.item(j);
					ModeAction actionObj = this.createAction(actionNode);
					modeObj.addAction(actionObj);
				}
			}
			action.add(modeObj);
		}
	}

	protected void checkNodeListenerList(Node childNode, InteractionManagerAction action) {
		Listener listObj = this.createListener(childNode);
		if (listObj != null) {
			NodeList listNL = childNode.getChildNodes();
			for (int j = 0, listSize = listNL != null ? listNL.getLength() : 0; j < listSize; j++) {
				if (listNL != null) {
					Node listNode = listNL.item(j);
					ListenerItem listItem = this.createListenerItem(listNode);
					listObj.add(listItem);
				}
			}

			action.setListener(listObj);
		}
	}

	protected InteractionManagerAction createManager(Node node) {
		if ((node == null) || (node.getNodeType() == Node.TEXT_NODE)) {
			return null;
		}

		String name = node.getNodeName();
		try {
			if ((name == null) || (name.length() == 0)) {
				throw new IllegalArgumentException(XMLInteractionManagerActionBuilder.MSG_CREATE_MANAGER_NAME_NULL);
			}

			Class c = Class.forName(name);
			Object o = c.newInstance();
			InteractionManagerAction ima = (InteractionManagerAction) o;
			return ima;
		} catch (ClassNotFoundException cnfe) {
			XMLInteractionManagerActionBuilder.logger.trace(null, cnfe);
			XMLInteractionManagerActionBuilder.warning(XMLInteractionManagerActionBuilder.MSG_CREATE_MANAGER_CLASS_NULL + name);
		} catch (ClassCastException cce) {
			XMLInteractionManagerActionBuilder.logger.trace(null, cce);
			XMLInteractionManagerActionBuilder.warning(XMLInteractionManagerActionBuilder.MSG_CREATE_MANAGER_CLASS_INVALID + name);
		} catch (Exception ex) {
			XMLInteractionManagerActionBuilder.logger.trace(null, ex);
			XMLInteractionManagerActionBuilder.warning(ex);
		}
		return null;
	}

	protected Mode createMode(Node node) {
		if ((node == null) || (node.getNodeType() != Node.ELEMENT_NODE)) {
			return null;
		}

		String name = node.getNodeName();
		if ((name == null) || !name.equalsIgnoreCase(XMLInteractionManagerActionBuilder.MODE_KEY)) {
			return null;
		}

		try {
			NamedNodeMap nnm = node.getAttributes();
			Node idNode = nnm != null ? nnm.getNamedItem(XMLInteractionManagerActionBuilder.MODE_ID_KEY) : null;

			String idValue = idNode != null ? idNode.getNodeValue() : null;
			Mode mode = new BasicInteractionManagerAction.BasicMode(idValue);
			return mode;
		} catch (Exception ex) {
			XMLInteractionManagerActionBuilder.warning(ex);
		}
		return null;
	}

	protected ModeAction createAction(Node node) {
		if ((node == null) || (node.getNodeType() != Node.ELEMENT_NODE)) {
			return null;
		}

		try {
			NamedNodeMap nnm = node.getAttributes();
			Node attrNode = nnm != null ? nnm.getNamedItem(XMLInteractionManagerActionBuilder.ACTION_ATTR_KEY) : null;
			Node valueNode = nnm != null ? nnm.getNamedItem(XMLInteractionManagerActionBuilder.ACTION_VALUE_KEY) : null;
			Node classNode = nnm != null ? nnm.getNamedItem(XMLInteractionManagerActionBuilder.ACTION_CLASS_KEY) : null;

			String nameValue = node.getNodeName();
			String attrValue = attrNode != null ? attrNode.getNodeValue() : null;
			String valueValue = valueNode != null ? valueNode.getNodeValue() : null;
			String classValue = classNode != null ? classNode.getNodeValue() : null;

			ModeAction action = new BasicInteractionManagerAction.BasicModeAction(nameValue, attrValue, classValue, valueValue);
			return action;
		} catch (Exception ex) {
			XMLInteractionManagerActionBuilder.warning(ex);
		}
		return null;
	}

	protected Listener createListener(Node node) {
		if ((node == null) || (node.getNodeType() != Node.ELEMENT_NODE)) {
			return null;
		}

		String name = node.getNodeName();
		if ((name == null) || !name.equalsIgnoreCase(XMLInteractionManagerActionBuilder.LISTENER_KEY)) {
			return null;
		}

		Listener l = new BasicInteractionManagerAction.BasicListener();
		return l;
	}

	protected ListenerItem createListenerItem(Node node) {
		if ((node == null) || (node.getNodeType() != Node.ELEMENT_NODE)) {
			return null;
		}

		try {
			NamedNodeMap nnm = node.getAttributes();
			Node idNode = nnm != null ? nnm.getNamedItem(XMLInteractionManagerActionBuilder.LISTENER_ID_KEY) : null;
			Node attrNode = nnm != null ? nnm.getNamedItem(XMLInteractionManagerActionBuilder.LISTENER_ATTR_KEY) : null;
			Node classNode = nnm != null ? nnm.getNamedItem(XMLInteractionManagerActionBuilder.LISTENER_CLASS_KEY) : null;

			String nameValue = node.getNodeName();
			String idValue = idNode != null ? idNode.getNodeValue() : null;
			String attrValue = attrNode != null ? attrNode.getNodeValue() : null;
			String classValue = classNode != null ? classNode.getNodeValue() : null;

			ListenerItem li = new BasicInteractionManagerAction.BasicListenerItem(idValue, attrValue, classValue, nameValue);
			return li;
		} catch (Exception ex) {
			XMLInteractionManagerActionBuilder.warning(ex);
		}
		return null;
	}
}
