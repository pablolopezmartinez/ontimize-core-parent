package com.ontimize.gui;

import java.util.ResourceBundle;

import com.ontimize.gui.images.ImageManager;

public class SearchTopWindow extends TopWindow {

    public SearchTopWindow(ResourceBundle res) {
        super(res);
        this.updateText("performing_query");
        this.updateIcon(ImageManager.getIcon("images/searching.png"));
    }

}
