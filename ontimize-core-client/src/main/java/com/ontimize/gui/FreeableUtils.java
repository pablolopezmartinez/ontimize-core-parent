package com.ontimize.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.ItemSelectable;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.field.IdentifiedElement;
import com.ontimize.security.ClientSecurityManager;

public final class FreeableUtils {

    private static final Logger logger = LoggerFactory.getLogger(FreeableUtils.class);

    private FreeableUtils() {
        super();
    }

    private static void freeChildren(Container parent, String prefix) {
        if (parent == null) {
            return;
        }
        for (Component comp : parent.getComponents()) {
            try {
                FreeableUtils.logger.trace("{}Remove {} from {}", prefix, FreeableUtils.getComponentName(comp),
                        FreeableUtils.getComponentName(parent));
                parent.remove(comp);
            } catch (Exception err) {
                FreeableUtils.logger.trace("{}ERROR REMOVING  {} from {}", prefix, FreeableUtils.getComponentName(comp),
                        FreeableUtils.getComponentName(parent));
                FreeableUtils.logger.trace(null, err);
            }
            FreeableUtils.freeComponent(comp, prefix);
        }
    }

    private static String getComponentName(Component cmp) {
        StringBuilder name = new StringBuilder(cmp.getClass().getName());
        if (cmp instanceof IdentifiedElement) {
            name.append("[").append(((IdentifiedElement) cmp).getAttribute()).append("]");
        }
        return name.toString();
    }

    private static void freeComponent(Component comp, String prefix) {
        if (comp instanceof Container) {
            FreeableUtils.freeChildren((Container) comp, prefix + "   ");
        }
        if (comp instanceof Freeable) {
            try {
                ((Freeable) comp).free();
            } catch (Exception err) {
                FreeableUtils.logger.trace(null, err);
            }
        }
        FreeableUtils.freeListeners(comp);
        if (comp instanceof Window) {
            ((Window) comp).dispose();
        }
        ClientSecurityManager.unregisterSecuredElement(comp);
    }

    public static void freeComponent(Component comp) {
        FreeableUtils.freeComponent(comp, "");
    }

    public static void freeComponent(Component... components) {
        if (components == null) {
            return;
        }
        for (Component comp : components) {
            FreeableUtils.freeComponent(comp);
        }
    }

    public static void deepFreeListeners(Component component) {
        FreeableUtils.freeListeners(component);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                FreeableUtils.deepFreeListeners(child);
            }
        }
    }

    public static void freeListeners(Component component) {
        if (component == null) {
            return;
        }
        for (MouseListener listener : component.getListeners(MouseListener.class)) {
            component.removeMouseListener(listener);
            FreeableUtils.freeObject(listener);
        }
        for (MouseMotionListener listener : component.getListeners(MouseMotionListener.class)) {
            component.removeMouseMotionListener(listener);
            FreeableUtils.freeObject(listener);
        }
        for (MouseWheelListener listener : component.getListeners(MouseWheelListener.class)) {
            component.removeMouseWheelListener(listener);
            FreeableUtils.freeObject(listener);
        }
        for (ComponentListener listener : component.getListeners(ComponentListener.class)) {
            component.removeComponentListener(listener);
            FreeableUtils.freeObject(listener);
        }
        for (FocusListener listener : component.getListeners(FocusListener.class)) {
            component.removeFocusListener(listener);
            FreeableUtils.freeObject(listener);
        }
        for (HierarchyBoundsListener listener : component.getListeners(HierarchyBoundsListener.class)) {
            component.removeHierarchyBoundsListener(listener);
            FreeableUtils.freeObject(listener);
        }
        for (HierarchyListener listener : component.getListeners(HierarchyListener.class)) {
            component.removeHierarchyListener(listener);
            FreeableUtils.freeObject(listener);
        }
        for (InputMethodListener listener : component.getListeners(InputMethodListener.class)) {
            component.removeInputMethodListener(listener);
            FreeableUtils.freeObject(listener);
        }
        for (KeyListener listener : component.getListeners(KeyListener.class)) {
            component.removeKeyListener(listener);
            FreeableUtils.freeObject(listener);
        }
        for (PropertyChangeListener listener : component.getListeners(PropertyChangeListener.class)) {
            component.removePropertyChangeListener(listener);
            FreeableUtils.freeObject(listener);
        }
        if (component instanceof JComponent) {
            for (AncestorListener listener : component.getListeners(AncestorListener.class)) {
                ((JComponent) component).removeAncestorListener(listener);
                FreeableUtils.freeObject(listener);
            }
            for (ContainerListener listener : component.getListeners(ContainerListener.class)) {
                ((JComponent) component).removeContainerListener(listener);
                FreeableUtils.freeObject(listener);
            }
            for (VetoableChangeListener listener : component.getListeners(VetoableChangeListener.class)) {
                ((JComponent) component).removeVetoableChangeListener(listener);
                FreeableUtils.freeObject(listener);
            }
        }
        if (component instanceof ItemSelectable) {
            for (ItemListener listener : component.getListeners(ItemListener.class)) {
                ((ItemSelectable) component).removeItemListener(listener);
                FreeableUtils.freeObject(listener);
            }
        }
        if (component instanceof AbstractButton) {
            for (ActionListener listener : component.getListeners(ActionListener.class)) {
                ((AbstractButton) component).removeActionListener(listener);
                FreeableUtils.freeObject(listener);
            }
        }
        if (component instanceof JMenuItem) {
            for (ActionListener listener : component.getListeners(ActionListener.class)) {
                ((JMenuItem) component).removeActionListener(listener);
                FreeableUtils.freeObject(listener);
            }
        }
        if (component instanceof JMenu) {
            for (ActionListener listener : component.getListeners(ActionListener.class)) {
                ((JMenu) component).removeActionListener(listener);
                FreeableUtils.freeObject(listener);
            }
        }
        if (component instanceof Window) {
            for (WindowFocusListener listener : component.getListeners(WindowFocusListener.class)) {
                ((Window) component).removeWindowFocusListener(listener);
                FreeableUtils.freeObject(listener);
            }
            for (WindowStateListener listener : component.getListeners(WindowStateListener.class)) {
                ((Window) component).removeWindowStateListener(listener);
                FreeableUtils.freeObject(listener);
            }
            for (WindowListener listener : component.getListeners(WindowListener.class)) {
                ((Window) component).removeWindowListener(listener);
                FreeableUtils.freeObject(listener);
            }
        }
        if (component instanceof JTable) {
            ListSelectionModel selectionModel = ((JTable) component).getSelectionModel();
            if (selectionModel instanceof DefaultListSelectionModel) {
                for (ListSelectionListener listener : ((DefaultListSelectionModel) selectionModel)
                    .getListSelectionListeners()) {
                    selectionModel.removeListSelectionListener(listener);
                    FreeableUtils.freeObject(listener);
                }
            }

            TableModel tableModel = ((JTable) component).getModel();
            if (tableModel instanceof AbstractTableModel) {
                for (TableModelListener listener : ((AbstractTableModel) tableModel)
                    .getListeners(TableModelListener.class)) {
                    tableModel.removeTableModelListener(listener);
                    FreeableUtils.freeObject(listener);
                }
            }
        }
    }

    public static void freeObject(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Component) {
            FreeableUtils.freeComponent((Component) obj);
        } else if (obj instanceof Freeable) {
            try {
                ((Freeable) obj).free();
            } catch (Exception err) {
                FreeableUtils.logger.error(null, err);
            }
        }

    }

    public static void clearMap(Map map) {
        if (map != null) {
            map.clear();
        }

    }

    public static void clearCollection(Collection coll, boolean freeContent) {
        if (coll != null) {
            for (Object obj : coll) {
                FreeableUtils.freeObject(obj);
            }
            coll.clear();
        }
    }

    public static void clearCollection(Collection coll) {
        FreeableUtils.clearCollection(coll, false);
    }

}
