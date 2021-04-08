package com.ontimize.ols;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.images.ImageManager;

public class WindowLMessage extends JDialog {

    private static WindowLMessage wld = null;

    JLabel jlOnt = null;

    // JButton jbDetails = null;
    ImageIcon iiShowDetails = null;

    ImageIcon iiHideDetails = null;

    JButton bAcept = null;

    JTextArea jtaMsg = null;

    JTextArea jtaSpecific = null;

    JScrollPane jssp = null;

    boolean details = false;

    public WindowLMessage(Frame owner) {
        super(owner, true);

        this.jlOnt = new JLabel(ImageManager.getIcon(ImageManager.ONTIMIZE_48));
        // jbDetails = new JButton(new ImageIcon(urlShowDetails)){
        // public Dimension getPreferredSize(){
        // return new Dimension(16,16);
        // }
        // public Dimension getSize(){
        // return new Dimension(16,16);
        // }
        // };
        //
        // jbDetails.setBorder(new EmptyBorder(0,0,0,0));

        // jbDetails.addActionListener(new ActionListener(){
        // public void actionPerformed(ActionEvent e) {
        // if (!details) {// Mostrar detalle
        // jbDetails.setIcon(iiHideDetails);
        // jssp.setVisible(true);
        // }
        // else {// Ocultar detalle
        // jbDetails.setIcon(iiShowDetails);
        // jssp.setVisible(false);
        // }
        // details = !details;
        // SwingUtilities.getWindowAncestor((Component)e.getSource()).pack();
        // }
        // });

        this.bAcept = new JButton(ApplicationManager.getTranslation("OptionPane.okButtonText",
                ApplicationManager.getApplicationBundle()));
        this.bAcept.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                SwingUtilities.getWindowAncestor((Component) event.getSource()).setVisible(false);
            }
        });

        this.bAcept.setIcon(ImageManager.getIcon(ImageManager.OK));
        this.bAcept.setBorder(new EmptyBorder(0, 0, 0, 0));

        this.jtaMsg = new JTextArea();
        this.jtaMsg.setEnabled(false);
        this.jtaMsg.setEditable(false);
        this.jtaMsg.setRows(2);
        this.jtaSpecific = new JTextArea();
        this.jtaSpecific.setEnabled(false);
        this.jtaSpecific.setEditable(false);

        this.jtaMsg.setLineWrap(true);
        this.jtaMsg.setWrapStyleWord(true);

        this.jtaSpecific.setLineWrap(true);
        this.jtaSpecific.setWrapStyleWord(true);

        this.jtaSpecific.setRows(5);

        this.jtaMsg.setText(
                ApplicationManager.getTranslation("ONTIMIZE_LICENSE_TEXT", ApplicationManager.getApplicationBundle()));

        JPanel jp = new JPanel();

        this.jtaMsg.setBackground(jp.getBackground());
        this.jtaSpecific.setBackground(jp.getBackground());

        this.jssp = new JScrollPane(this.jtaSpecific);
        this.jssp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.jssp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // jssp.setVisible(false);

        JPanel jpContentAll = new JPanel();
        jpContentAll.setBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.BLACK.brighter(), Color.BLACK.darker()));

        jpContentAll.setLayout(new GridBagLayout());
        jpContentAll.add(this.jlOnt, new GridBagConstraints(0, 0, 1, 2, 0, 0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(2, 5, 2, 10), 2, 2));
        jpContentAll.add(this.jtaMsg, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(4, 2, 2, 2), 2, 2));
        // jpContentAll.add(jbDetails,new GridBagConstraints(1,1,1,1,0,0,
        // GridBagConstraints.EAST,GridBagConstraints.NONE,new
        // Insets(2,2,2,2),2,2));
        jpContentAll.add(this.jssp, new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 2, 2));
        jpContentAll.add(this.bAcept, new GridBagConstraints(0, 3, 2, 1, 0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 2, 2));

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(jpContentAll);

        this.setUndecorated(true);

        this.pack();
    }

    public void setSpecific(String msg) {
        this.jtaSpecific.setText(msg);
    }

    public static void setLMessage(String text) {
        if (WindowLMessage.wld == null) {
            WindowLMessage.wld = new WindowLMessage(ApplicationManager.getApplication().getFrame());
        }
        WindowLMessage.wld.setSpecific(text);
    }

    public static void showLMessage(ActionEvent event) {
        if (WindowLMessage.wld == null) {
            WindowLMessage.wld = new WindowLMessage(ApplicationManager.getApplication().getFrame());
        }
        WindowLMessage.wld.pack();

        Point punto = new Point();
        SwingUtilities.convertPointToScreen(punto, (JComponent) event.getSource());

        int x = (int) punto.getX() - WindowLMessage.wld.getWidth();
        int y = (int) punto.getY() + ((JComponent) event.getSource()).getHeight();

        WindowLMessage.wld.setLocation(x, y);
        WindowLMessage.wld.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(350, (int) (d.getHeight() > 400 ? 400 : d.getHeight()));
    }

}
