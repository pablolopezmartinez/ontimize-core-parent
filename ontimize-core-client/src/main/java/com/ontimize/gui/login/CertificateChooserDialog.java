package com.ontimize.gui.login;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.button.Button;

public class CertificateChooserDialog extends JDialog implements ActionListener {

	private static CertificateChooserDialog ccd = null;

	public static final String HELP_TEXT = "certificatechooserdialog.help";
	public static final String HELP_TITLE = "certificatechooserdialog.title";

	private CertificateTable ct = null;
	private final JLabel jlHelp = new JLabel();
	private Button okButton = null;
	private Button cancelButton = null;
	private AliasCertPair aliasCert = null;

	public CertificateChooserDialog(Frame owner) {
		super(owner, "select", true);

		this.ct = new CertificateTable();

		this.okButton = this.createOKButton();
		this.cancelButton = this.createCancelButton();

		this.okButton.setActionCommand("ok");
		this.okButton.addActionListener(this);
		this.okButton.setIcon(ApplicationManager.getDefaultOKIcon());
		this.okButton.setResourceBundle(ApplicationManager.getApplicationBundle());
		this.cancelButton.setActionCommand("cancel");
		this.cancelButton.addActionListener(this);
		this.cancelButton.setIcon(ApplicationManager.getDefaultCancelIcon());
		this.cancelButton.setResourceBundle(ApplicationManager.getApplicationBundle());

		this.ct.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 2) && (CertificateChooserDialog.this.ct.getSelectedRowCount() == 1)) {
					CertificateChooserDialog.this.okButton.doClick();
				}
			}
		});

		JScrollPane js = new JScrollPane(this.ct);
		this.getContentPane().setLayout(new GridBagLayout());
		this.add(this.jlHelp, new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 2, 2));
		this.add(js, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 2, 2));
		this.add(this.okButton, new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 2, 2));
		this.add(this.cancelButton, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 2, 2));
	}

	private Button createOKButton() {
		Hashtable p = new Hashtable();
		p.put("key", "application.accept");
		p.put("text", "application.accept");
		return new Button(p);
	}

	private Button createCancelButton() {
		Hashtable p = new Hashtable();
		p.put("key", "application.cancel");
		p.put("text", "application.cancel");
		return new Button(p);
	}

	public void setValues(List l) {
		this.ct.setValues(l);

	}

	private AliasCertPair getTableSelectedCertificate() {
		int row = this.ct.getSelectedRow();
		if (row == -1) {
			return null;
		}
		return this.ct.getAliasCertAt(row);
	}

	public AliasCertPair getSelectedCertificate() {
		return this.aliasCert;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(477, 355);
	}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			this.aliasCert = null;
		}
		super.setVisible(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("ok".equals(e.getActionCommand())) {
			this.aliasCert = this.getTableSelectedCertificate();
		}

		if ("cancel".endsWith(e.getActionCommand())) {
			this.aliasCert = null;
		}
		this.setVisible(false);
	}

	private void setBundle(ResourceBundle bundle) {
		if (this.jlHelp != null) {
			this.jlHelp.setText(ApplicationManager.getTranslation(CertificateChooserDialog.HELP_TEXT, bundle));
		}
		if (this.okButton != null) {
			this.okButton.setResourceBundle(bundle);
		}
		if (this.cancelButton != null) {
			this.cancelButton.setResourceBundle(bundle);
		}
		if (this.ct != null) {
			this.ct.setBundle(bundle);
		}
		this.setTitle(ApplicationManager.getTranslation(CertificateChooserDialog.HELP_TITLE, bundle));
	}

	public static AliasCertPair selectCertificate(List l, Frame f, ResourceBundle bundle) {
		if (CertificateChooserDialog.ccd == null) {
			CertificateChooserDialog.ccd = new CertificateChooserDialog(f);
		}
		CertificateChooserDialog.ccd.setValues(l);
		CertificateChooserDialog.ccd.setBundle(bundle);
		CertificateChooserDialog.ccd.pack();
		ApplicationManager.center(CertificateChooserDialog.ccd);
		CertificateChooserDialog.ccd.setVisible(true);
		return CertificateChooserDialog.ccd.getSelectedCertificate();
	}
}
