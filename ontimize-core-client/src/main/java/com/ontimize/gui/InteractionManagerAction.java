package com.ontimize.gui;

import java.util.List;

/**
 * <p>
 * Actions model to change the form status in the available modes.
 *
 * @author Imatia Innovation S.L.
 * @since Ontimize 5.2059EN
 */
public interface InteractionManagerAction {

    /**
     * <p>
     * {@link InteractionManager} form mode wrapper with a {@link ModeAction} list model.
     *
     * @author Imatia Innovation S.L.
     * @since Ontimize 5.2059EN
     * @see ModeAction
     */
    public static interface Mode {

        public Object getId();

        public List getActionList();

        public void addAction(ModeAction action);

    };

    /**
     * <p>
     * Action to perform in a object of the form.
     *
     * @author Imatia Innovation S.L.
     * @since Ontimize 5.2059EN
     * @see Mode
     */
    public static interface ModeAction {

        public String getName();

        public String getAttr();

        public Object getValue();

        public Class getValueClass();

        public String getValueClassName();

    };

    /**
     * <p>
     * Handles the listener list.
     *
     * @author Imatia Innovation S.L.
     * @since Ontimize 5.2059EN
     * @see ListenerItem
     */
    public static interface Listener {

        public ListenerItem getListenerItem(Object id);

        public List getListenerList();

        public void add(ListenerItem item);

        public void setListener(Form form);

        public ListenerItem remove(Object id);

        public void clear();

    }

    /**
     * <p>
     * Listener to add to a form element.
     *
     * @author Imatia Innovation S.L.
     * @since Ontimize 5.2059EN
     * @see Listener
     */
    public static interface ListenerItem {

        public Object getId();

        public String getAttr();

        public Class getListenerClass();

        public String getListenerClassName();

        public Object getListenerClassInstance();

        public String getListenerType();

    }

    public Mode getMode(Object modeId);

    public List getActionList(Object modeId);

    public boolean isEmpty();

    public void add(Mode mode);

    public Mode removeMode(Object modeId);

    public void clear();

    /**
     * <p>
     * Execute the available actions for the given <code>mode</code> in the given form.
     * @param form Destination form.
     * @param mode Form mode.
     */
    public void setMode(Form form, int modeId);

    /**
     * <p>
     * Execute the available actions for the given <code>mode</code> in the given form.
     * @param form Destination form.
     * @param mode Form mode.
     */
    public void setMode(Form form, String modeId);

    // Listener

    /**
     * <p>
     * Return the available listener handler.
     * @return {@link Listener} handler.
     */
    public Listener getListener();

    /**
     * <p>
     * Set the {@link InteractionManagerAction} listener handler.
     * @param listener {@link Listener} handler.
     */
    public void setListener(Listener listener);

}
