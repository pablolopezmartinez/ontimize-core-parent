package com.ontimize.gui;

import com.ontimize.gui.field.DataComponent;

public interface ValueChangeDataComponent extends DataComponent {

	public void addValueChangeListener(ValueChangeListener l);

	public void removeValueChangeListener(ValueChangeListener l);
}