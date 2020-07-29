package com.ontimize.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JToolBar;

import com.ontimize.module.IModuleActionToolBarListener;

/**
 * This class implements a default toolbar listener.
 * <p>
 *
 * @author Imatia Innovation
 */
public class DefaultToolBarListener implements ToolBarListener, ActionListener {

    /**
     * A reference to application. By default, null.
     */
    protected Application application = null;

    /**
     * A reference to a toolbar. By default, null.
     */
    protected JToolBar toolBar = null;

    private boolean registered = false;

    /** The listener list. */
    protected List<IModuleActionToolBarListener> moduleListenerList;

    /**
     * The class constructor. In this version, it is empty.
     */
    public DefaultToolBarListener() {
        this.moduleListenerList = new ArrayList<IModuleActionToolBarListener>();
    }

    @Override
    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public void addToolBarToListenFor(JToolBar t) {
        if (!this.registered || (this.toolBar != t)) {
            this.registered = true;
            this.toolBar = t;
            for (int i = 0; i < this.toolBar.getComponentCount(); i++) {
                if (this.toolBar.getComponentAtIndex(i) instanceof AbstractButton) {
                    ((AbstractButton) this.toolBar.getComponentAtIndex(i)).addActionListener(this);
                }
                if (this.toolBar.getComponentAtIndex(i) instanceof ApToolBarPopupButton) {
                    ApToolBarPopupButton b = (ApToolBarPopupButton) this.toolBar.getComponentAtIndex(i);
                    for (int j = 0; j < b.getPopupComponentsCount(); j++) {
                        if (b.getPopupComponentAt(j) instanceof AbstractButton) {
                            ((AbstractButton) b.getPopupComponentAt(j)).addActionListener(this);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (IModuleActionToolBarListener listener : this.moduleListenerList) {
            if (listener.actionModulePerformed(e)) {
                return;
            }
        }
        if ((this.application != null) && (this.application.getMenuListener() != null)
                && (this.application.getMenuListener() instanceof ActionListener)) {
            ((ActionListener) this.application.getMenuListener()).actionPerformed(e);
        }
    }

    @Override
    public void setInitialState() {

    }

    /**
     * Adds the listener.
     * @param listener the listener
     */
    @Override
    public void addListener(IModuleActionToolBarListener listener) {
        this.moduleListenerList.add(listener);
    }

}
