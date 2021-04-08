package com.ontimize.gui;

public interface ISplash {

    public void show();

    public void show(boolean repaint);

    public void setRepaintTime(int repaintTime);

    public void hide();

    public void dispose();

    public void updateText(final String text);

}
