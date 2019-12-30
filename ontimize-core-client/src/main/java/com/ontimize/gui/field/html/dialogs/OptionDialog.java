package com.ontimize.gui.field.html.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JPanel;

import com.ontimize.gui.field.html.dialogs.panels.HeaderPanel;

public class OptionDialog extends StandardDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel internalContentPane;
	protected Container contentPane;
	protected HeaderPanel hp;

	public OptionDialog(Frame parent, String headerTitle, String desc, Icon icon) {
		super(parent, headerTitle, StandardDialog.BUTTONS_RIGHT);
		this.init(headerTitle, desc, icon);
	}

	public OptionDialog(Dialog parent, String headerTitle, String desc, Icon icon) {
		super(parent, headerTitle, StandardDialog.BUTTONS_RIGHT);
		this.init(headerTitle, desc, icon);
	}

	protected void init(String title, String desc, Icon icon) {
		this.internalContentPane = new JPanel(new BorderLayout());
		this.hp = new HeaderPanel();
		this.hp.setTitle(title);
		this.hp.setDescription(desc);
		this.hp.setIcon(icon);
		this.internalContentPane.add(this.hp, BorderLayout.NORTH);

		super.setContentPane(this.internalContentPane);
	}

	@Override
	public Container getContentPane() {
		return this.contentPane;
	}

	@Override
	public void setContentPane(Container c) {
		this.contentPane = c;
		this.internalContentPane.add(c, BorderLayout.CENTER);
	}

	@Override
	public void setResourceBundle(ResourceBundle resourceBundle) {
		super.setResourceBundle(resourceBundle);
		if (this.hp != null) {
			this.hp.setResourceBundle(resourceBundle);
		}
	}
}
