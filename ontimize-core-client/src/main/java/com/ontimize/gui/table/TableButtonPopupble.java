package com.ontimize.gui.table;

import javax.swing.JPopupMenu;

import com.ontimize.util.swing.IMenuButton;

/**
 * Some table buttons show pop-upble menus (like save and order configuration button).
 *
 * This class implement the interface <code>IMenuButton</code> in order to allow displaying popup menu.
 *
 *
 * @author Imatia Innovation SL
 * @since 5.3.10
 *
 */
public class TableButtonPopupble extends TableButton implements IMenuButton {

	protected JPopupMenu menu;

	@Override
	public JPopupMenu getMenu() {
		return this.menu;
	}

	public void setMenu(JPopupMenu menu) {
		this.menu = menu;
	}

}
