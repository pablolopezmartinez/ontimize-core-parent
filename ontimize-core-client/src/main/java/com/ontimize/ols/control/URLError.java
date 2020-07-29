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

public class URLError extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(URLError.class);

    private final JPopupMenu popup = new JPopupMenu();

    private final JButton ok = new JButton(ImageManager.getIcon(ImageManager.OK));

    private final JLabel icon = new JLabel(ImageManager.getIcon(ImageManager.LICENSE_WARNING_48));

    private final JTextArea tf = new JTextArea(10, 60);

    private static URLError urle = null;

    class PopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                URLError.this.popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                URLError.this.popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }

    public URLError(Dialog owner, ResourceBundle bundle) {
        super(owner);
        this.init(bundle);
    }

    public URLError(Frame owner, ResourceBundle bundle) {
        super(owner);
        this.init(bundle);
    }

    private void init(ResourceBundle bundle) {
        this.getContentPane().setLayout(new GridBagLayout());
        this.setTitle(ApplicationManager.getTranslation("URLError.Title", bundle));

        this.ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Window w = SwingUtilities.getWindowAncestor((Component) e.getSource());
                w.setVisible(false);
            }
        });

        JMenuItem mi = new JMenuItem(ApplicationManager.getTranslation("DURLToConnect.COPIAR_PORTAPAPELES", bundle));
        mi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ApplicationManager.copyToClipboard(URLError.this.tf.getText());
                } catch (Exception ex) {
                    URLError.logger.error(null, ex);
                }
            }
        });

        this.popup.add(mi);

        JScrollPane js = new JScrollPane();

        js.getViewport().add(this.tf);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(this.ok);

        this.tf.setEnabled(false);
        this.tf.setLineWrap(true);

        this.tf.addMouseListener(new PopupListener());

        this.getContentPane()
            .add(this.icon, new GridBagConstraints(0, 0, 1, 3, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(30, 4, 2, 4), 0, 0));

        this.getContentPane()
            .add(new JLabel(ApplicationManager.getTranslation("URLError.ResponseInformation", bundle)),
                    new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                            GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        this.getContentPane()
            .add(js, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 2), 0, 0));

        this.getContentPane()
            .add(new JLabel(ApplicationManager.getTranslation("URLError.ResponseTODO", bundle)),
                    new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                            GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        this.getContentPane()
            .add(buttons, new GridBagConstraints(0, 3, 2, 1, 1, 0, GridBagConstraints.NORTHWEST,
                    GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.pack();
        ApplicationManager.center(this);

    }

    public void setText(String url) {
        this.tf.setText(url);
    }

    public static void showURLError(Component owner, ResourceBundle bundle, String text) {
        if (URLError.urle == null) {
            if (owner instanceof Frame) {
                URLError.urle = new URLError((Frame) owner, bundle);
            } else {
                URLError.urle = new URLError((Dialog) owner, bundle);
            }
        }
        URLError.urle.setText(text);
        URLError.urle.setVisible(true);

    }

}
