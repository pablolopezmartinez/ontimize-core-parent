package com.ontimize.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class CustomColumnGridBagLayout extends GridBagLayout {

    // Reference to the element that have this component as layout
    private Container cont = null;

    private boolean expandLast = true;

    private int vAlignment = GridBagConstraints.NORTH;

    public CustomColumnGridBagLayout(Container container) {
        super();
        this.cont = container;
        this.defaultConstraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
                0);
    }

    public CustomColumnGridBagLayout(Container container, boolean expandLast) {
        super();
        this.cont = container;
        this.expandLast = expandLast;
        this.defaultConstraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
                0);
    }

    public CustomColumnGridBagLayout(Container container, int verticalAlignment) {
        super();
        this.cont = container;
        this.vAlignment = verticalAlignment;
        this.defaultConstraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
                0);
    }

    public CustomColumnGridBagLayout(Container container, int vAlignment, boolean expandLast) {
        this(container, vAlignment);
        this.expandLast = expandLast;
    }

    @Override
    public void addLayoutComponent(Component component, Object constraints) {
        if (constraints instanceof GridBagConstraints) {
            ((GridBagConstraints) constraints).gridx = 0;
            ((GridBagConstraints) constraints).gridy = GridBagConstraints.RELATIVE;
        }
        super.addLayoutComponent(component, constraints);
        if (this.vAlignment == GridBagConstraints.CENTER) {
            // All weights to 0
            int componentsNumber = this.cont.getComponentCount();
            GridBagConstraints auxConstraints = null;
            for (int i = 0; i < componentsNumber; i++) {
                auxConstraints = this.getConstraints(this.cont.getComponent(i));
                auxConstraints.weighty = 0.0;
                this.setConstraints(this.cont.getComponent(i), auxConstraints);
            }
        } else if (this.vAlignment == GridBagConstraints.SOUTH) {
            // Weight 1 for the component at the top
            int componentsNumber = this.cont.getComponentCount();
            GridBagConstraints auxConstraints = null;
            auxConstraints = this.getConstraints(this.cont.getComponent(0));
            auxConstraints.weighty = 1.0;
            this.setConstraints(this.cont.getComponent(0), auxConstraints);
            for (int i = 0; i < componentsNumber; i++) {
                auxConstraints = this.getConstraints(this.cont.getComponent(i));
                auxConstraints.anchor = GridBagConstraints.SOUTH;
                this.setConstraints(this.cont.getComponent(i), auxConstraints);
            }
        } else {
            if (this.expandLast) {
                // The component in the last row must have vertical weigh 1 to
                // expand itself in the remainder space and align all components
                // in
                // the top of the container

                int componentsNumber = this.cont.getComponentCount();
                int lastVisibleIndex = 0;
                GridBagConstraints auxConstraints = null;
                for (int i = 0; i < componentsNumber; i++) {
                    auxConstraints = this.getConstraints(this.cont.getComponent(i));
                    auxConstraints.weighty = 0.0;
                    this.setConstraints(this.cont.getComponent(i), auxConstraints);
                    if (this.cont.getComponent(i).isVisible()) {
                        lastVisibleIndex = i;
                    }
                }
                auxConstraints = this.getConstraints(this.cont.getComponent(lastVisibleIndex));
                auxConstraints.weighty = 1.0;
                this.setConstraints(this.cont.getComponent(lastVisibleIndex), auxConstraints);
            }
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        super.removeLayoutComponent(comp);
        int componentCount = this.cont.getComponentCount();
        if (componentCount > 1) {
            if (this.vAlignment == GridBagConstraints.CENTER) {
            } else if (this.vAlignment == GridBagConstraints.SOUTH) {
                GridBagConstraints auxConstraints = null;
                Component first = this.cont.getComponent(0);
                if (first.equals(comp)) {
                    first = this.cont.getComponent(1);
                }
                auxConstraints = this.getConstraints(first);
                auxConstraints.weighty = 1.0;
                this.setConstraints(first, auxConstraints);
            } else {
                if (this.expandLast && (this.cont.getComponentCount() > 0)) {
                    GridBagConstraints auxConstraints = null;
                    Component last = this.cont.getComponent(componentCount - 1);
                    if (last.equals(comp)) {
                        last = this.cont.getComponent(componentCount - 2);
                    }
                    auxConstraints = this.getConstraints(last);
                    auxConstraints.weighty = 1.0;
                    this.setConstraints(last, auxConstraints);
                }
            }
        }
    }

}
