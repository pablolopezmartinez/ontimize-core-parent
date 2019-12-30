package com.ontimize.printing;

import java.awt.Graphics;
import java.awt.print.PageFormat;

import javax.swing.SwingConstants;

/**
 * Interface to implement for printable elements.
 *
 * @author Imatia Innovation
 */
public interface PrintingElement {

	public static int LEFT = SwingConstants.LEFT;

	public static int CENTER = SwingConstants.CENTER;

	public static int RIGHT = SwingConstants.RIGHT;

	public void paintInPage(Graphics g, PageFormat pageFormat);

	public void paint(Graphics g, double scale);

	public String getId();

	public void setContent(Object content);

	public int getX();

	public int getY();

	public int getWidth();

	public int getHeight();

	public void setX(int x);

	public void setY(int y);

	public void setWidth(int w);

	public void setHeight(int h);
}