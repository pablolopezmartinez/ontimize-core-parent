package com.ontimize.ols;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.images.ImageManager;

public class WindowLText extends JDialog {

	private static final Logger	logger	= LoggerFactory.getLogger(WindowLText.class);

	private static WindowLText wld = null;

	JButton bAcept = null;
	JTextArea jtaText = null;
	JScrollPane jssp = null;
	JLabel jlTitle = null;

	public WindowLText(Frame owner) {
		super(owner, true);

		this.bAcept = new JButton(ApplicationManager.getTranslation("OptionPane.okButtonText", ApplicationManager.getApplicationBundle()));
		this.bAcept.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				SwingUtilities.getWindowAncestor((Component) event.getSource()).setVisible(false);
			}
		});

		this.bAcept.setIcon(ImageManager.getIcon(ImageManager.OK));
		this.bAcept.setBorder(new EmptyBorder(0, 0, 0, 0));

		this.jtaText = new JTextArea();
		this.jtaText.setEnabled(false);
		this.jtaText.setEditable(false);
		this.jtaText.setRows(5);

		this.jtaText.setLineWrap(true);
		this.jtaText.setWrapStyleWord(true);

		this.jtaText.setText("");

		this.jssp = new JScrollPane(this.jtaText);
		this.jssp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.jssp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JLabel jlTitle = new JLabel(ApplicationManager.getTranslation("WindowLText.LicenseText", ApplicationManager.getApplication().getResourceBundle()));
		JPanel jp = new JPanel();
		this.jtaText.setBackground(jp.getBackground());

		JPanel jpContentAll = new JPanel();
		jpContentAll.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.BLACK.brighter(), Color.BLACK.darker()));

		jpContentAll.setLayout(new GridBagLayout());

		jpContentAll.add(jlTitle, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 5, 2, 10), 2, 2));

		jpContentAll.add(this.jssp, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 2, 2));

		jpContentAll.add(this.bAcept, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 2, 2));

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(jpContentAll);

		this.setUndecorated(true);
		this.pack();
	}

	public void refresh() {
		if (ApplicationManager.getApplication().getReferenceLocator() == null) {
			return;
		}

		String s = null;
		try {
			s = ((LOk) ApplicationManager.getApplication().getReferenceLocator()).getLContent();
		} catch (Exception ex) {
			s = "ERROR: " + ex.getMessage();
			WindowLText.logger.error(null, ex);
		}
		if (s != null) {
			this.jtaText.setText(s);
		}
	}

	public static void showLMessage(ActionEvent event) {
		if (WindowLText.wld == null) {
			WindowLText.wld = new WindowLText(ApplicationManager.getApplication().getFrame());
		}
		WindowLText.wld.refresh();
		WindowLText.wld.pack();
		ApplicationManager.center(WindowLText.wld);

		WindowLText.wld.setVisible(true);
	}

	@Override
	public Dimension getPreferredSize() {
		// Dimension d = super.getPreferredSize();
		// return new Dimension(350,(int) ( d.getHeight()>
		// 400?400:d.getHeight()));
		return new Dimension(450, 278);
	}

}
