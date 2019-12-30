package com.ontimize.db.query;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.container.EJDialog;
import com.ontimize.gui.images.ImageManager;

public class QueryBuilderHelp extends JPanel {

	protected JButton bOK = new com.ontimize.report.ReportDesignerButton(ImageManager.getIcon(ImageManager.OK));

	protected JPanel helpPanel = new JPanel();

	protected JPanel buttonPanel = new JPanel();

	protected JLabel helpText = new JLabel();

	public QueryBuilderHelp(ResourceBundle bundle, boolean okCancel, boolean showCols, int t) {

		this.bOK.setToolTipText(ApplicationManager.getTranslation("QueryBuilderOKCons", bundle));
		this.bOK.setText(ApplicationManager.getTranslation("QueryBuilderAceptar", bundle));
		this.bOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor((Component) e.getSource());
				w.setVisible(false);
			}
		});

		String helpText = "QueryBuilderAyudaUsoAplicacion";
		if (!(t > 1)) {
			if (okCancel) {
				if (showCols) {
					helpText = "QueryBuilderAyudaUsoAplicacionOkCancelCols";
				} else {
					helpText = "QueryBuilderAyudaUsoAplicacionOkCancel";
				}
			}
		}

		this.helpText.setText(ApplicationManager.getTranslation(helpText, bundle));

		this.helpPanel.setLayout(new BorderLayout());
		this.helpPanel.add(this.helpText);

		this.buttonPanel.setLayout(new FlowLayout());
		this.buttonPanel.add(this.bOK);

		this.setLayout(new BorderLayout());
		this.add(this.helpPanel, BorderLayout.CENTER);
		this.add(this.buttonPanel, BorderLayout.SOUTH);

	}

	protected static EJDialog dialog = null;

	protected static QueryBuilderHelp qb = null;

	protected static Window w = null;

	public static void show(Component c, ResourceBundle bundle, boolean okCancel, boolean showCols) {
		QueryBuilderHelp.show(c, bundle, okCancel, showCols, -1);
	}

	public static void show(Component c, ResourceBundle bundle, boolean okCancel, boolean showCols, int t) {

		if (!(c instanceof Frame) && !(c instanceof Dialog)) {
			QueryBuilderHelp.w = SwingUtilities.getWindowAncestor(c);
			if (QueryBuilderHelp.w instanceof Frame) {
				QueryBuilderHelp.dialog = new EJDialog((Frame) QueryBuilderHelp.w, ApplicationManager.getTranslation("QueryBuilderHelpTitulo", bundle), true);
			} else if (QueryBuilderHelp.w instanceof Dialog) {
				QueryBuilderHelp.dialog = new EJDialog((Dialog) QueryBuilderHelp.w, ApplicationManager.getTranslation("QueryBuilderHelpTitulo", bundle), true);
			}
		} else {
			if (c instanceof Frame) {
				QueryBuilderHelp.dialog = new EJDialog((Frame) c, ApplicationManager.getTranslation("QueryBuilderHelpTitulo", bundle), true);
			}
			if (c instanceof Dialog) {
				QueryBuilderHelp.dialog = new EJDialog((Dialog) c, ApplicationManager.getTranslation("QueryBuilderHelpTitulo", bundle), true);
			}
		}

		if (QueryBuilderHelp.dialog != null) {
			QueryBuilderHelp.qb = new QueryBuilderHelp(bundle, okCancel, showCols, t);
			QueryBuilderHelp.dialog.getContentPane().add(new JScrollPane(QueryBuilderHelp.qb));
			QueryBuilderHelp.dialog.pack();
			if (QueryBuilderHelp.dialog != null) {
				ApplicationManager.center(QueryBuilderHelp.dialog);
			}
			QueryBuilderHelp.dialog.setVisible(true);
		}
	}

}
