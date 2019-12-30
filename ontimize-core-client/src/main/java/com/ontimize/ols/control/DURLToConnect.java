package com.ontimize.ols.control;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.images.ImageManager;

public class DURLToConnect extends JDialog {

	private static final Logger		logger	= LoggerFactory.getLogger(DURLToConnect.class);

	private final JTextArea urlText = new JTextArea();
	private final JButton ok = new JButton(ImageManager.getIcon(ImageManager.OK));
	private final JLabel icon = new JLabel(ImageManager.getIcon(ImageManager.LICENSE_WARNING_48));

	private static DURLToConnect durl = null;
	private final JPopupMenu popup = new JPopupMenu();

	class PopupListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				DURLToConnect.this.popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				DURLToConnect.this.popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	public DURLToConnect(Dialog owner, ResourceBundle bundle) {
		super(owner);
		this.init(bundle);
	}

	public DURLToConnect(Frame owner, ResourceBundle bundle) {
		super(owner);
		this.init(bundle);
	}

	private void init(ResourceBundle bundle) {
		this.getContentPane().setLayout(new GridBagLayout());
		this.setTitle(ApplicationManager.getTranslation("DURLToConnect.Title", bundle));

		this.ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Window w = SwingUtilities.getWindowAncestor((Component) e.getSource());
				w.setVisible(false);
			}
		});

		this.urlText.setEditable(false);
		this.urlText.setRows(2);

		JMenuItem mi = new JMenuItem(ApplicationManager.getTranslation("DURLToConnect.COPIAR_PORTAPAPELES", bundle));
		mi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ApplicationManager.copyToClipboard(DURLToConnect.this.urlText.getText());
				} catch (Exception ex) {
					DURLToConnect.logger.error(null, ex);
				}
			}
		});

		this.popup.add(mi);

		this.urlText.addMouseListener(new PopupListener());

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttons.add(this.ok);

		this.getContentPane().add(this.icon, new GridBagConstraints(0, 0, 1, 3, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(30, 4, 2, 4), 0, 0));

		this.getContentPane().add(new JLabel(ApplicationManager.getTranslation("DURLToConnect.URL_TO_CONNECT_1", bundle)),
				new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		this.getContentPane().add(new JScrollPane(this.urlText),
				new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		this.getContentPane().add(new JLabel(ApplicationManager.getTranslation("DURLToConnect.URL_TO_CONNECT_2", bundle)),
				new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		this.getContentPane().add(buttons, new GridBagConstraints(0, 3, 2, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		this.pack();
		ApplicationManager.center(this);
	}

	public void setText(String url) {
		this.urlText.setText(url);
	}

	public static void showDURLToConnect(Component owner, ResourceBundle bundle, String text) {
		if (DURLToConnect.durl == null) {
			if (owner instanceof Frame) {
				DURLToConnect.durl = new DURLToConnect((Frame) owner, bundle);
			} else {
				DURLToConnect.durl = new DURLToConnect((Dialog) owner, bundle);
			}
		}
		DURLToConnect.durl.setText(text);
		DURLToConnect.durl.setVisible(true);

	}

}
