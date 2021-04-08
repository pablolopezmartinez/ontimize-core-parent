package com.ontimize.gui.container;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.images.ImageManager;
import com.ontimize.util.ParseUtils;

// TODO DOCUMENT THIS CLASS

public class InformationEJDialog extends EJDialog {

    public static final String INFORMATION_TITLE = "servermonitor.remote_administration_message_title";

    public static String iconLabelString = null;

    protected JLabel messageLabel = new JLabel();

    protected JLabel iconLabel = new JLabel();

    protected JButton okButton = new JButton();

    protected ActionListener okButtonListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            InformationEJDialog.this.dispose();
        }
    };

    public InformationEJDialog(Frame parentComponent, String title, String message, boolean modal) {
        super(parentComponent, title, modal);
        this.createInformationDialog(parentComponent, message);
    }

    public InformationEJDialog(Dialog parentComponent, String title, String message, boolean modal) {
        super(parentComponent, title, modal);
        this.createInformationDialog(parentComponent, message);
    }

    public InformationEJDialog(Window parentComponent, String title, String message, boolean modal) {
        super(parentComponent, title, modal);
        this.createInformationDialog(parentComponent, message);
    }

    protected void createInformationDialog(Component parentComponent, String message) {
        this.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gbc_iconLabel = new GridBagConstraints();
        gbc_iconLabel.gridx = 0;
        gbc_iconLabel.gridy = 0;
        gbc_iconLabel.insets = new Insets(5, 5, 5, 5);
        this.getContentPane().add(this.iconLabel, gbc_iconLabel);

        GridBagConstraints gbc_messageLabel = new GridBagConstraints();
        gbc_messageLabel.gridx = 1;
        gbc_messageLabel.gridy = 0;
        gbc_messageLabel.weightx = 1.0;
        gbc_messageLabel.insets = new Insets(5, 0, 5, 5);
        this.getContentPane().add(this.messageLabel, gbc_messageLabel);

        GridBagConstraints gbc_okButton = new GridBagConstraints();
        gbc_okButton.gridx = 0;
        gbc_okButton.gridy = 1;
        gbc_okButton.gridwidth = 2;
        gbc_okButton.insets = new Insets(5, 5, 5, 5);
        this.getContentPane().add(this.okButton, gbc_okButton);

        if (InformationEJDialog.iconLabelString != null) {
            this.iconLabel.setIcon(ImageManager.getIcon(InformationEJDialog.iconLabelString));
        } else {
            this.iconLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        }
        this.messageLabel.setText(message);
        this.okButton.setText(ParseUtils
            .getString(ApplicationManager.getApplicationBundle().getString("application.accept"), "Accept"));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.okButton.addActionListener(this.okButtonListener);

        this.pack();
        // Dimension d = this.getPreferredSize();
        // d.setSize(230, d.height);
        // this.setMinimumSize(d);
        this.centerLocationOnParent(parentComponent);

    }

    public void centerLocationOnParent(Component parentComponent) {
        this.setLocationRelativeTo(parentComponent);
        Point p = this.getLocation();
        p.x = p.x - (this.getWidth() / 2);
        p.y = p.y - (this.getHeight() / 2);
    }

}
