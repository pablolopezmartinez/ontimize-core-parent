package com.ontimize.gui.login;

import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JTable;

public class CertificateTable extends JTable {

	public CertificateTable() {
		super(new CertificateTableModel());
	}

	public void setValues(List l) {
		((CertificateTableModel) this.getModel()).setValues(l);
		if ((l != null) && !l.isEmpty()) {
			this.setRowSelectionInterval(0, 0);
		}
	}

	public AliasCertPair getAliasCertAt(int row) {
		return ((CertificateTableModel) this.getModel()).getAliasCertAt(row);
	}

	public void setBundle(ResourceBundle bundle) {
		((CertificateTableModel) this.getModel()).setBundle(bundle);
		if (!((CertificateTableModel) this.getModel()).isEmpty()) {
			this.setRowSelectionInterval(0, 0);
		}
	}
}
