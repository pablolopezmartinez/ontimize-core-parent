package com.ontimize.builder;

import javax.swing.JMenuBar;

public interface MenuBuilder {

	/**
	 * Creates a JMenuBar with the components specified in the file.<br>
	 * Parent component must be a JFrame, JDialog, JApplet, JInternalFrame,JRootPane
	 *
	 * @param uriFile
	 * @return
	 */
	public JMenuBar buildMenu(String uriFile);

	public void appendMenu(JMenuBar menuBar, String xmlDefinition);

}