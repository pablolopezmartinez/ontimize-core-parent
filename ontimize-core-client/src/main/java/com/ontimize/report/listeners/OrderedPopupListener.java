package com.ontimize.report.listeners;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrderedPopupListener extends MouseAdapter {

	@Override
	public void mouseClicked(MouseEvent e) {
		if ((e.getClickCount() == 1) && (e.getModifiers() == InputEvent.META_MASK)) {}
	}

}
