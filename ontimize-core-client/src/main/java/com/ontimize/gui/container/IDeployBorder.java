package com.ontimize.gui.container;

import java.awt.Insets;
import java.awt.Rectangle;

public interface IDeployBorder {

	public Rectangle getImageBound();

	public void setHighlight(boolean b);

	public void setTitle(String translation);

	public void setMargin(Insets insets);

}
