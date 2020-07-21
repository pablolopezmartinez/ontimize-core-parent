package com.ontimize.gui.field;

import java.util.EventListener;

public interface DictionaryChangeListener extends EventListener {

    public void wordAdded(DictionaryEvent e);

    public void wordRemoved(DictionaryEvent e);

}
