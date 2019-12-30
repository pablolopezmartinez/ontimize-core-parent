package com.ontimize.gui.actions;

import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.db.Entity;
import com.ontimize.db.FileManagementEntity;
import com.ontimize.gui.Form;
import com.ontimize.locator.EntityReferenceLocator;

public class DeleteAttachmentFileAction extends AbstractButtonAction {

	private static final Logger	logger		= LoggerFactory.getLogger(DeleteAttachmentFileAction.class);

	protected String entityName = null;

	protected boolean refresh = false;

	public DeleteAttachmentFileAction(String entity, boolean refresh) {
		this.entityName = entity;
		this.refresh = refresh;
	}

	public void setEntity(String entity) {
		this.entityName = entity;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final Form f = this.getForm(e);
		EntityReferenceLocator referenceLocator = f.getFormManager().getReferenceLocator();
		try {
			Entity entity = referenceLocator.getEntityReference(this.entityName);
			if (entity instanceof FileManagementEntity) {
				FileManagementEntity eGA = (FileManagementEntity) entity;
				Hashtable kv = this.getAttachmentValuesKeys(f);
				boolean delete = eGA.deleteAttachmentFile(kv, referenceLocator.getSessionId());
				if (this.refresh) {
					f.refreshCurrentDataRecord();
				}

				if (delete) {
					f.message("The attach file has been deleted successfully", JOptionPane.INFORMATION_MESSAGE);
				} else {
					f.message("Error when the attach file was being deleted", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (Exception ex) {
			DeleteAttachmentFileAction.logger.error(null, ex);
			f.message("Error when the attach file was being deleted: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE, ex);
		}
	}

	protected Hashtable getAttachmentValuesKeys(Form f) throws Exception {
		final Hashtable kv = new Hashtable();
		Vector vKeys = f.getKeys();
		if (vKeys.isEmpty()) {
			throw new Exception("The 'keys' parameter is necessary  in the parent form");
		}
		for (int i = 0; i < vKeys.size(); i++) {
			Object oKeyValue = f.getDataFieldValueFromFormCache(vKeys.get(i).toString());
			if (oKeyValue == null) {
				throw new Exception("Value of the key " + vKeys.get(i) + " not found in the parent form");
			}
			kv.put(vKeys.get(i), oKeyValue);
		}
		return kv;
	}

}
