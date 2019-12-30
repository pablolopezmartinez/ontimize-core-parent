package com.ontimize.gui;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.util.ClassHandler;
import com.ontimize.util.ParseUtils;

/**
 * <p>
 * Handles the actions to change the form status in the available modes.
 * <p>
 * Default implementation of {@link InteractionManagerAction} handler.
 *
 * @author Imatia Innovation S.L.
 * @since Ontimize 5.2059EN
 *
 * @see InteractionManagerAction
 */
public class BasicInteractionManagerAction implements InteractionManagerAction {

	private static final Logger logger = LoggerFactory.getLogger(BasicInteractionManagerAction.class);

	/**
	 * <p>
	 * Handles the action to change the form status in a mode.
	 * <p>
	 * Default implementation of {@link Mode}
	 *
	 * @author Imatia Innovation S.L.
	 * @since Ontimize 5.2059EN
	 */
	public static class BasicMode implements Mode {

		private static final String MSG_ID_NULL = "Mode: Mode id is null or empty";

		protected String id = null;
		protected List actions = new ArrayList();

		public BasicMode(String id) throws IllegalArgumentException {
			if ((id == null) || (id.length() == 0)) {
				throw new IllegalArgumentException(BasicMode.MSG_ID_NULL);
			}
			this.id = id;
		}

		@Override
		public Object getId() {
			return this.id;
		}

		@Override
		public List getActionList() {
			return this.actions;
		}

		@Override
		public void addAction(ModeAction action) {
			if (action != null) {
				this.actions.add(action);
			}
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Mode) {
				Mode other = (Mode) o;

				Object thisId = this.getId();
				Object otherId = other.getId();
				return (thisId != null) && (otherId != null) && thisId.equals(otherId);
			} else {
				return super.equals(o);
			}
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append(this.id);
			s.append(", ");
			s.append(this.actions);
			return s.toString();
		}
	}

	/**
	 * <p>
	 * Stores a action to change the form fields behaviour.
	 * <p>
	 * Default implementation of {@link ModeAction}
	 *
	 * @author Imatia Innovation S.L.
	 * @since Ontimize 5.2059EN
	 */
	public static class BasicModeAction implements ModeAction {

		public static final String DEFAULT_ACTIONS_LOCATION_SYSTEM_PROPERTY_KEY = "com.ontimize.gui.BasicInteractionManagerAction.BasicModeAction.DEFAULT_ACTIONS";

		public static final String DEFAULT_ACTIONS_LOCATION = "com/ontimize/gui/default_actions.properties";

		protected static Properties DEFAULT_ACTIONS = new Properties();
		static {
			String s = System.getProperty(BasicModeAction.DEFAULT_ACTIONS_LOCATION_SYSTEM_PROPERTY_KEY);
			if ((s == null) || (s.length() == 0)) {
				s = BasicModeAction.DEFAULT_ACTIONS_LOCATION;
			}

			InputStream is = null;
			try {
				is = BasicModeAction.class.getClassLoader().getResourceAsStream(s);
				if (is != null) {
					BasicModeAction.DEFAULT_ACTIONS.load(is);
				}
			} catch (Exception e) {
				BasicInteractionManagerAction.logger.error(null, e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (Exception ex) {
						BasicInteractionManagerAction.logger.error(null, ex);
					}
				}
			}
		}

		private static final String MSG_ACTION_NULL = "Mode > Action: Name not found or is empty.";
		private static final String MSG_ATTR_NULL = "Mode > Action: Attr not found or is empty.";
		private static final String MSG_CLASS_DEFAULT = "Mode > Action: Searching a default class for action ";
		private static final String MSG_CLASS_NULL = "Mode > Action: Class not found or is empty.";

		protected String name = null;
		protected String attr = null;
		protected Object value = null;
		protected Class valueClass = null;
		protected String valueClassName = null;

		public BasicModeAction(String name, String attr, String className, String value) throws IllegalArgumentException {
			if ((name == null) || (name.length() == 0)) {
				throw new IllegalArgumentException(BasicModeAction.MSG_ACTION_NULL);
			}

			if ((attr == null) || (attr.length() == 0)) {
				throw new IllegalArgumentException(BasicModeAction.MSG_ATTR_NULL);
			}

			this.name = name;
			this.attr = attr;

			// Compute class and value.
			this.valueClassName = BasicModeAction.getDefaultClass(className, name);
			this.valueClass = ClassHandler.getClassObject(this.valueClassName, true);
			this.value = ClassHandler.getValueObject(this.valueClassName, value);
		}

		/**
		 * <p>
		 * If no class is provided, check the default class for the given action
		 *
		 * @param clazz
		 *            Class name for the parameter value.
		 * @param action
		 *            Method to call
		 */
		protected static String getDefaultClass(String clazz, String action) throws IllegalArgumentException {
			String defaultClazz = clazz;
			if ((defaultClazz == null) || (defaultClazz.length() == 0)) {

				if (ApplicationManager.DEBUG) {
					BasicInteractionManagerAction.logger.debug(BasicModeAction.MSG_CLASS_DEFAULT + action);
				}

				// Check if the action has a default class.
				defaultClazz = BasicModeAction.DEFAULT_ACTIONS.getProperty(action.toLowerCase());
				if ((defaultClazz == null) || (defaultClazz.length() == 0)) {
					throw new IllegalArgumentException(BasicModeAction.MSG_CLASS_NULL);
				}
			}
			return defaultClazz;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String getAttr() {
			return this.attr;
		}

		@Override
		public Object getValue() {
			return this.value;
		}

		@Override
		public Class getValueClass() {
			return this.valueClass;
		}

		@Override
		public String getValueClassName() {
			return this.valueClassName;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof ModeAction) {
				ModeAction other = (ModeAction) o;

				String thisName = this.getName();
				String thisAttr = this.getAttr();

				String otherName = other.getName();
				String otherAttr = other.getAttr();

				return (thisName != null) && (otherName != null) && thisName.equalsIgnoreCase(otherName) && (thisAttr != null) && (otherAttr != null) && thisAttr
						.equalsIgnoreCase(otherAttr);
			} else {
				return super.equals(o);
			}
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append("{ ");
			s.append(this.name);
			s.append(", ");
			s.append(this.attr);
			s.append(", ");
			s.append(this.value);
			s.append(", ");
			s.append(this.valueClass);
			s.append(", ");
			s.append(this.valueClassName);
			s.append(" }");
			return s.toString();
		}
	}

	public static class BasicListener implements Listener {

		protected static final String ADD_PREFFIX = "add";

		private static final String MSG_ELEMENT_NULL_1 = " Element ";
		private static final String MSG_ELEMENT_NULL_2 = " not found in current form ";

		private static final String MSG_METHOD_NULL_1 = " Method ";
		private static final String MSG_METHOD_NULL_2 = " not found for element ";
		private static final String MSG_METHOD_NULL_3 = " in the current form ";

		private static final String MSG_METHOD_INVALID_1 = " Method ";
		private static final String MSG_METHOD_INVALID_2 = " with invalid argument ";
		private static final String MSG_METHOD_INVALID_3 = " called in element ";
		private static final String MSG_METHOD_INVALID_4 = " in the current form ";

		protected Map listeners = new Hashtable();

		public BasicListener() {}

		@Override
		public ListenerItem getListenerItem(Object id) {
			return (ListenerItem) this.listeners.get(id);
		}

		@Override
		public List getListenerList() {
			Collection c = this.listeners.values();
			return new ArrayList(c);
		}

		@Override
		public void add(ListenerItem item) {
			Object id = item != null ? item.getId() : null;
			if (id != null) {
				this.listeners.put(id, item);
			}
		}

		@Override
		public void setListener(Form form) {
			if (form == null) {
				return;
			}

			String formName = form.getResourceFileName();

			List l = this.getListenerList();
			for (int i = 0, size = l != null ? l.size() : 0; i < size; i++) {
				ListenerItem item = (ListenerItem) l.get(i);

				String attr = item.getAttr();
				String type = item.getListenerType();
				Class clazz = item.getListenerClass();
				Object instance = item.getListenerClassInstance();

				String typeWithAdd = BasicListener.ADD_PREFFIX + ParseUtils.getCamelCase(type);
				try {
					Object reference = form.getElementReference(attr);
					if (reference == null) {
						throw new IllegalArgumentException(BasicListener.MSG_ELEMENT_NULL_1 + attr + BasicListener.MSG_ELEMENT_NULL_2 + formName);
					}

					// Test the valid parameter.

					Method method = ClassHandler.getMethodObject(reference, typeWithAdd);
					if (method == null) {
						throw new IllegalArgumentException(
								BasicListener.MSG_METHOD_NULL_1 + typeWithAdd + BasicListener.MSG_METHOD_NULL_2 + attr + BasicListener.MSG_METHOD_NULL_3 + formName);
					}

					method.invoke(reference, new Object[] { instance });
				} catch (InvocationTargetException e) {
					BasicInteractionManagerAction.error(
							new Object[] { BasicListener.MSG_METHOD_INVALID_1, typeWithAdd, BasicListener.MSG_METHOD_INVALID_2, clazz, BasicListener.MSG_METHOD_INVALID_3, attr, BasicListener.MSG_METHOD_INVALID_4, formName, e });
				} catch (Exception ex) {
					BasicInteractionManagerAction.error(ex);
				}
			}
		}

		@Override
		public ListenerItem remove(Object id) {
			return id != null ? (ListenerItem) this.listeners.remove(id) : null;
		}

		@Override
		public void clear() {
			this.listeners.clear();
		}

		@Override
		public String toString() {
			return this.listeners.toString();
		}
	}

	public static class BasicListenerItem implements ListenerItem {

		private static final String MSG_ID_NULL = "Listener > Item: Id not found or is empty.";
		private static final String MSG_ATTR_NULL = "Listener > Item: Attr not found or is empty.";
		private static final String MSG_CLASS_NAME_NULL = "Listener > Item: Class name not found or is empty.";
		private static final String MSG_TYPE_NULL = "Listener > Item: Listener type not found or is empty.";

		protected Object id = null;
		protected String attr = null;
		protected Class clazz = null;
		protected String clazzName = null;
		protected Object clazzInstance = null;
		protected String type = null;

		public BasicListenerItem(Object id, String attr, String className, String type) throws IllegalArgumentException {
			if (id == null) {
				throw new IllegalArgumentException(BasicListenerItem.MSG_ID_NULL);
			}
			this.id = id;

			if ((attr == null) || (attr.length() == 0)) {
				throw new IllegalArgumentException(BasicListenerItem.MSG_ATTR_NULL);
			}
			this.attr = attr;

			// Compute class name.
			if ((className == null) || (className.length() == 0)) {
				throw new IllegalArgumentException(BasicListenerItem.MSG_CLASS_NAME_NULL);
			}
			this.clazz = ClassHandler.getClassObject(className);
			this.clazzName = className;
			this.clazzInstance = ClassHandler.getClassInstance(className);

			if ((type == null) || (type.length() == 0)) {
				throw new IllegalArgumentException(BasicListenerItem.MSG_TYPE_NULL);
			}
			this.type = type;
		}

		@Override
		public Object getId() {
			return this.id;
		}

		@Override
		public String getAttr() {
			return this.attr;
		}

		@Override
		public Class getListenerClass() {
			return this.clazz;
		}

		@Override
		public String getListenerClassName() {
			return this.clazzName;
		}

		@Override
		public Object getListenerClassInstance() {
			return this.clazzInstance;
		}

		@Override
		public String getListenerType() {
			return this.type;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof ListenerItem) {
				ListenerItem other = (ListenerItem) o;

				Object thisId = this.getId();
				Object otherId = other.getId();

				return (thisId != null) && (otherId != null) && thisId.equals(otherId);
			} else {
				return super.equals(o);
			}
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();

			s.append("{ ");
			s.append(this.id);
			s.append(", ");
			s.append(this.attr);
			s.append(", ");
			s.append(this.clazz);
			s.append(", ");
			s.append(this.clazzName);
			s.append(", ");
			s.append(this.type);
			s.append("} ");
			return s.toString();
		}
	}

	public static final String DEFAULT_MODES_LOCATION_SYSTEM_PROPERTY_KEY = "com.ontimize.gui.BasicInteractionManagerAction.DEFAULT_MODES";

	public static final String DEFAULT_MODES_LOCATION = "com/ontimize/gui/default_modes.properties";

	protected static Properties DEFAULT_MODES = new Properties();
	static {
		String location = System.getProperty(BasicInteractionManagerAction.DEFAULT_MODES_LOCATION_SYSTEM_PROPERTY_KEY);
		if ((location == null) || (location.length() == 0)) {
			location = BasicInteractionManagerAction.DEFAULT_MODES_LOCATION;
		}

		InputStream is = null;
		try {
			is = BasicInteractionManagerAction.class.getClassLoader().getResourceAsStream(location);
			if (is != null) {
				BasicInteractionManagerAction.DEFAULT_MODES.load(is);
			}
		} catch (Exception e) {
			BasicInteractionManagerAction.logger.error(null, e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception ex) {
					BasicInteractionManagerAction.logger.error(null, ex);
				}
			}
		}
	}

	protected static Object convertMode(Object modeId) {
		Object iModeId = modeId;
		if (iModeId instanceof String) {
			iModeId = ((String) iModeId).toLowerCase();
		}

		Object convertedId = iModeId != null ? BasicInteractionManagerAction.DEFAULT_MODES.get(iModeId) : null;
		return convertedId != null ? convertedId : iModeId;
	}

	protected static void error(Throwable t) {
		BasicInteractionManagerAction.error(t != null ? new Object[] { t.getMessage() } : null);
	}

	protected static void error(Object[] message) {
		StringBuilder s = new StringBuilder();
		s.append(BasicInteractionManagerAction.class.getName());
		s.append(BasicInteractionManagerAction.MSG_ERROR);

		for (int i = 0, size = message != null ? message.length : 0; i < size; i++) {
			Object current = message[i];
			if (current != null) {
				s.append(current);
			}
		}
		BasicInteractionManagerAction.logger.error(s.toString());
	}

	protected static final String SET_PREFFIX = "set";

	private static final String MSG_ERROR = " ERROR -> ";
	private static final String MSG_ELEMENT_NULL_1 = " Element ";
	private static final String MSG_ELEMENT_NULL_2 = " not found in current form ";

	private static final String MSG_METHOD_NULL_1 = " Method ";
	private static final String MSG_METHOD_NULL_2 = " not found for element ";
	private static final String MSG_METHOD_NULL_3 = " in the current form ";

	private static final String MSG_METHOD_INVALID_1 = " Method ";
	private static final String MSG_METHOD_INVALID_2 = " with invalid argument ";
	private static final String MSG_METHOD_INVALID_3 = " called in element ";
	private static final String MSG_METHOD_INVALID_4 = " in the current form ";

	/**
	 * <p>
	 * Stores the current modes.
	 */
	protected Map modes = new Hashtable();

	protected Listener listener = null;

	public BasicInteractionManagerAction() {}

	@Override
	public void add(Mode mode) {
		if (mode != null) {
			Object id = mode.getId();
			Object convertedId = BasicInteractionManagerAction.convertMode(id);
			if (convertedId != null) {
				this.modes.put(id, mode);
			}
		}
	}

	@Override
	public Mode getMode(Object modeId) {
		Object convertedId = BasicInteractionManagerAction.convertMode(modeId);
		Object o = convertedId != null ? this.modes.get(convertedId) : null;
		return (o != null) && (o instanceof Mode) ? (Mode) o : null;
	}

	@Override
	public List getActionList(Object modeId) {
		Mode mode = this.getMode(modeId);
		return mode != null ? mode.getActionList() : null;
	}

	@Override
	public boolean isEmpty() {
		return this.modes.isEmpty();
	}

	@Override
	public Mode removeMode(Object modeId) {
		Object convertedId = BasicInteractionManagerAction.convertMode(modeId);
		return convertedId != null ? (Mode) this.modes.remove(convertedId) : null;
	}

	@Override
	public void clear() {
		this.modes.clear();
	}

	@Override
	public void setMode(Form form, int modeId) {
		this.setMode(form, Integer.toString(modeId));
	}

	@Override
	public void setMode(Form form, String modeId) {
		if (form == null) {
			return;
		}
		String formName = form.getResourceFileName();

		List actions = this.getActionList(modeId);
		for (int i = 0, size = actions != null ? actions.size() : 0; i < size; i++) {
			ModeAction action = (ModeAction) actions.get(i);

			String name = action.getName();
			String attr = action.getAttr();
			Class clazz = action.getValueClass();
			Object value = action.getValue();
			String nameWithSet = BasicInteractionManagerAction.SET_PREFFIX + ParseUtils.getCamelCase(name);
			try {

				// 1. Search element in the given form
				Object reference = form.getElementReference(attr);
				if (reference == null) {
					throw new IllegalArgumentException(BasicInteractionManagerAction.MSG_ELEMENT_NULL_1 + attr + BasicInteractionManagerAction.MSG_ELEMENT_NULL_2 + formName);
				}

				// 2.(1) Check method with "set" and the given name.
				Method method = ClassHandler.getMethodObject(reference, nameWithSet, new Class[] { clazz });

				// 2.(2) Check method with the given name.
				if (method == null) {
					method = ClassHandler.getMethodObject(reference, name, new Class[] { clazz });
				}

				// 3. Invoke the method
				if (method == null) {
					throw new IllegalArgumentException(
							BasicInteractionManagerAction.MSG_METHOD_NULL_1 + nameWithSet + BasicInteractionManagerAction.MSG_METHOD_NULL_2 + attr + BasicInteractionManagerAction.MSG_METHOD_NULL_3 + formName);
				}
				method.invoke(reference, new Object[] { value });
			} catch (InvocationTargetException e) {
				BasicInteractionManagerAction.error(
						new Object[] { BasicInteractionManagerAction.MSG_METHOD_INVALID_1, nameWithSet, BasicInteractionManagerAction.MSG_METHOD_INVALID_2, value, BasicInteractionManagerAction.MSG_METHOD_INVALID_3, attr, BasicInteractionManagerAction.MSG_METHOD_INVALID_4, formName, e });
			} catch (Exception ex) {
				BasicInteractionManagerAction.error(ex);
			}
		}
	}

	// Listeners

	@Override
	public Listener getListener() {
		return this.listener;
	}

	@Override
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();

		Collection cValues = this.modes.values();
		Iterator iValues = cValues.iterator();

		while (iValues.hasNext()) {
			Object o = iValues.next();
			if (s.length() > 0) {
				s.append(", ");
			}
			s.append(o);
		}
		return s.toString();
	}
}
