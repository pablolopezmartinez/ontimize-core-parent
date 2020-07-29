package com.ontimize.gui.table;

import java.util.EventListener;

public interface RefreshTableListener extends EventListener {

    public void postCorrectRefresh(RefreshTableEvent e);

    public void postIncorrectRefresh(RefreshTableEvent e);

}
