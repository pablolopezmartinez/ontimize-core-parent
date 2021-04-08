package com.ontimize.gui.field;

import java.util.EventObject;

public class DictionaryEvent extends EventObject {

    protected String language = null;

    protected String word = null;

    public DictionaryEvent(Object source, String language, String word) {
        super(source);
        this.language = language;
        this.word = word;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getWord() {
        return this.word;
    }

}
