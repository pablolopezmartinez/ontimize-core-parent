package com.ontimize.gui.table;

public interface IRefreshable {

    public void addRefreshTableListener(RefreshTableListener l);

    public void removeRefreshTableListener(RefreshTableListener l);

    public void fireRefreshTableEvent(RefreshTableEvent refreshTableEvent);

}
