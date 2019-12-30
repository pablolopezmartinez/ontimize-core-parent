package com.ontimize.gui.button;

import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.Form;
import com.ontimize.gui.actions.DeleteAttachmentFileAction;

public class DeleteAttachmentButton extends Button {

	protected ActionListener action = null;

	protected String entity = null;

	protected boolean delete = false;

	protected boolean refresh = false;

	public DeleteAttachmentButton(Hashtable parameters) {
		super(parameters);
		if (parameters.containsKey("delete")) {
			this.delete = ApplicationManager.parseStringValue(parameters.get("delete").toString(), false);
		}
		ImageIcon icon = ApplicationManager.getDefaultDeleteAttachmentIcon();
		if (icon != null) {
			this.setIcon(icon);
		}

		if (parameters.containsKey("refresh")) {
			this.refresh = ApplicationManager.parseStringValue(parameters.get("refresh").toString(), false);
		}
		this.action = new DeleteAttachmentFileAction(null, this.refresh);
		this.addActionListener(this.action);
	}

	@Override
	public void setParentForm(Form f) {
		super.setParentForm(f);
		if (this.entity == null) {
			String sName = f.getEntityName();
			this.entity = sName;
			if (this.action instanceof DeleteAttachmentFileAction) {
				((DeleteAttachmentFileAction) this.action).setEntity(sName);
			}
		}
	}

	public void setActionListener(ActionListener listener) {
		this.action = listener;
	}

}
