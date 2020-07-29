package com.ontimize.gui.table.blocked;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BlockedBoundedRangeModel implements BoundedRangeModel, ChangeListener {

    protected BoundedRangeModel baseModel;

    protected List<ChangeListener> changeListeners;

    public BlockedBoundedRangeModel(BoundedRangeModel baseModel) {
        this.baseModel = baseModel;
        this.changeListeners = new ArrayList<ChangeListener>();
        this.baseModel.addChangeListener(this);
    }

    @Override
    public int getMinimum() {
        return this.baseModel.getMinimum();
    }

    @Override
    public void setMinimum(int newMinimum) {
        // Do nothing
    }

    @Override
    public int getMaximum() {
        return this.baseModel.getMaximum();
    }

    @Override
    public void setMaximum(int newMaximum) {
        // Do nothing
    }

    @Override
    public int getValue() {
        return this.baseModel.getValue();
    }

    @Override
    public void setValue(int newValue) {
        // Do nothing
    }

    @Override
    public void setValueIsAdjusting(boolean b) {
        // Do nothing
    }

    @Override
    public boolean getValueIsAdjusting() {
        return this.baseModel.getValueIsAdjusting();
    }

    @Override
    public int getExtent() {
        return this.baseModel.getExtent();
    }

    @Override
    public void setExtent(int newExtent) {
        // Do nothing
    }

    @Override
    public void setRangeProperties(int value, int extent, int min, int max, boolean adjusting) {
        // Do nothing
    }

    @Override
    public void addChangeListener(ChangeListener x) {
        this.changeListeners.add(x);
    }

    @Override
    public void removeChangeListener(ChangeListener x) {
        this.changeListeners.remove(x);
    }

    protected void fireStateChanged() {
        ChangeEvent changeEvent = new ChangeEvent(this);
        for (ChangeListener listener : this.changeListeners) {
            listener.stateChanged(changeEvent);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        this.fireStateChanged();
    }

}
