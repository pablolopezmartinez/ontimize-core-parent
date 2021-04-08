package com.ontimize.ols;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.container.EJDialog;
import com.ontimize.gui.images.ImageManager;

class CharacterConstrainer extends PlainDocument {

    /**
     * Editor to limit the number of character in.
     */
    private final JTextField editor;

    /**
     * Maximum number of characters in the editor.
     */
    private final int maxCharacterCount;

    private final Component c;

    private boolean uppercase = false;

    /**
     * Creates an instance of CharacterConstrainer.
     * @param editor Editor to limit the number of characters in.
     * @param maximumCharacterCount Maximum number of characters in the editor.
     */
    public CharacterConstrainer(JTextField editor, int maximumCharacterCount, Component c, boolean uppercase) {
        this.editor = editor;
        this.maxCharacterCount = maximumCharacterCount;
        this.c = c;
        this.uppercase = uppercase;
    }

    /**
     * Metodo al que llama el editor cada vez que se intenta insertar caracteres. El metodo comprueba
     * que no se sobrepasa el límite. Si es así, llama al metodo de la clase padre para que se inserten
     * los caracteres. Si se sobrepasa el límite, retorna sin hacer nada.
     */
    @Override
    public void insertString(int arg0, String arg1, AttributeSet arg2) throws BadLocationException {
        if (this.uppercase) {
            arg1 = arg1.toUpperCase();
        }
        if ((this.editor.getText().length() + arg1.length()) > this.maxCharacterCount) {
            return;
        }
        if ((this.editor.getText().length() + arg1.length()) == this.maxCharacterCount) {
            if (this.c != null) {
                this.c.requestFocus();
            }
        }
        super.insertString(arg0, arg1, arg2);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
    }

}

/**
 * Escuchador para que haga el movimiento contrario, cuando se pulse VK_BACK_SPACE salte al
 * JTextField anterior.
 */
class CustomKey extends KeyAdapter {

    private JTextField c = null;

    private JTextField ant = null;

    private JButton b = null;

    public CustomKey(JTextField c, JTextField ant, JButton b) {
        this.c = c;
        this.ant = ant;
        this.b = b;
    }

    @Override
    public void keyTyped(KeyEvent e) {

        if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            if (this.c != null) {
                if (this.c.getText().equals("")) {
                    if (this.ant.getText().length() > 1) {
                        this.ant.setText(this.ant.getText().substring(0, this.ant.getText().length() - 1));
                    }
                    this.ant.requestFocus();
                    e.consume();
                }
            }
        }

        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            this.b.doClick();
        }
    }

}

public class SerialNumber extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(SerialNumber.class);

    public static int CorrectSerialNumberSize = 16;

    protected JPopupMenu popup = new JPopupMenu();

    class PopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                SerialNumber.this.popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                SerialNumber.this.popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }

    protected JButton bOK = new JButton(ImageManager.getIcon(ImageManager.OK));

    protected JButton bCANCEL = new JButton(ImageManager.getIcon(ImageManager.CANCEL));

    protected JTextField t1 = new JTextField();

    protected JTextField t2 = new JTextField();

    protected JTextField t3 = new JTextField();

    protected JTextField t4 = new JTextField();

    protected String serial = null;

    protected ResourceBundle bundle = null;

    private String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                // highly unlikely since we are using a standard DataFlavor
                SerialNumber.logger.error(null, ex);
            } catch (IOException ex) {
                SerialNumber.logger.debug(null, ex);
            }
        }
        return result;
    }

    private void init(ResourceBundle b, boolean showButtons, boolean readOnly, boolean uppercase) {
        this.bundle = b;

        if (readOnly) {
            this.t1.setEditable(false);
            this.t2.setEditable(false);
            this.t3.setEditable(false);
            this.t4.setEditable(false);
        }

        JLabel title = new JLabel(ApplicationManager.getTranslation("SerialNumber.SERIAL_NUMBER_WRITE", this.bundle));

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.add(this.bOK);
        buttons.add(this.bCANCEL);

        JPanel serial = new JPanel();
        serial.setLayout(new FlowLayout());
        serial.add(this.t1);
        serial.add(this.t2);
        serial.add(this.t3);
        serial.add(this.t4);

        this.t1.setColumns(4);

        this.t2.setColumns(4);
        this.t3.setColumns(4);
        this.t4.setColumns(4);

        this.t1.setDocument(new CharacterConstrainer(this.t1, 4, this.t2, uppercase));
        this.t2.setDocument(new CharacterConstrainer(this.t2, 4, this.t3, uppercase));
        this.t3.setDocument(new CharacterConstrainer(this.t3, 4, this.t4, uppercase));
        this.t4.setDocument(new CharacterConstrainer(this.t4, 4, null, uppercase));

        this.t1.addKeyListener(new CustomKey(null, null, this.bOK));
        this.t2.addKeyListener(new CustomKey(this.t2, this.t1, this.bOK));
        this.t3.addKeyListener(new CustomKey(this.t3, this.t2, this.bOK));
        this.t4.addKeyListener(new CustomKey(this.t4, this.t3, this.bOK));

        JMenuItem mi = new JMenuItem(ApplicationManager.getTranslation("SerialNumber.ClearAll", b));
        mi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SerialNumber.this.t1.setText("");
                SerialNumber.this.t2.setText("");
                SerialNumber.this.t3.setText("");
                SerialNumber.this.t4.setText("");
                SerialNumber.this.t1.requestFocus();
            }
        });

        JMenuItem mi2 = new JMenuItem(ApplicationManager.getTranslation("datafield.paste", b));
        mi2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String s = SerialNumber.this.getClipboardContents();
                if ((s.indexOf("-") != -1) && (s.length() == 19)) {
                    String out = s.substring(0, 4);
                    out = out + s.substring(5, 9);
                    out = out + s.substring(10, 14);
                    out = out + s.substring(15);
                    s = out;
                }

                // s = s.replace('-',"");
                SerialNumber.this.setSerialNumber(s);
            }
        });

        this.popup.add(mi2);
        this.popup.add(mi);

        if (!readOnly) {
            // serial.addMouseListener(new PopupListener());
            this.t1.addMouseListener(new PopupListener());
            this.t2.addMouseListener(new PopupListener());
            this.t3.addMouseListener(new PopupListener());
            this.t4.addMouseListener(new PopupListener());
        }

        this.setLayout(new GridBagLayout());

        int i = 0;

        if (showButtons) {
            this.add(title, new GridBagConstraints(0, i, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(2, 2, 2, 2), 0, 0));
            i++;
        }

        this.add(serial, new GridBagConstraints(0, i, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(2, 2, 2, 2), 0, 0));
        i++;

        if (showButtons) {
            this.add(buttons, new GridBagConstraints(0, i, 1, 1, 1, 0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
            // Incrementar si se ponen mas
        }
        // Incrementar si se ponen mas

        this.bOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ((SerialNumber.this.t1.getText().length() == 4) && (SerialNumber.this.t2.getText().length() == 4)
                        && (SerialNumber.this.t4.getText().length() == 4)) {
                    Window w = SwingUtilities.getWindowAncestor((Component) e.getSource());
                    w.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog((Component) e.getSource(),
                            ApplicationManager.getTranslation("SerialNumber.SERIAL_NUMBER_ERROR_MESSAGE",
                                    SerialNumber.this.bundle),
                            ApplicationManager.getTranslation("SerialNumber.SERIAL_NUMBER_ERROR_TITLE",
                                    SerialNumber.this.bundle),
                            JOptionPane.OK_OPTION);
                }
            }
        });

        this.bCANCEL.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SerialNumber.this.t1.setText("");
                SerialNumber.this.t2.setText("");
                SerialNumber.this.t3.setText("");
                SerialNumber.this.t4.setText("");
                Window w = SwingUtilities.getWindowAncestor((Component) e.getSource());
                w.setVisible(false);
            }
        });
    }

    public SerialNumber(ResourceBundle b) {
        this.init(b, true, false, false);
    }

    public SerialNumber(ResourceBundle b, boolean showButtons) {
        this.init(b, showButtons, false, false);
    }

    public SerialNumber(ResourceBundle b, boolean showButtons, boolean readonly) {
        this.init(b, showButtons, readonly, false);
    }

    public SerialNumber(ResourceBundle b, boolean showButtons, boolean readonly, boolean uppercase) {
        this.init(b, showButtons, readonly, uppercase);
    }

    public void clearSerialNumber() {
        this.t1.setText("");
        this.t2.setText("");
        this.t3.setText("");
        this.t4.setText("");
    }

    public void setSerialNumber(String sn) {
        if ((sn != null) && (sn.length() == 16)) {

            this.t1.setText(sn.substring(0, 4));
            this.t2.setText(sn.substring(4, 8));
            this.t3.setText(sn.substring(8, 12));
            this.t4.setText(sn.substring(12, 16));

        }
    }

    public String getSerialNumber() {
        if ((this.t1.getText() == null) || (this.t2.getText() == null) || (this.t3.getText() == null)
                || (this.t4.getText() == null)) {
            return null;
        }
        String s = this.t1.getText() + this.t2.getText() + this.t3.getText() + this.t4.getText();
        if (s.equals("")) {
            return null;
        }
        return s;
    }

    public static String SerialNumber(Frame parent, ResourceBundle b) {
        return SerialNumber.showSerialNumber(parent, b, true, false, false);
    }

    public static String SerialNumber(Frame parent, ResourceBundle b, boolean showButtons) {
        return SerialNumber.showSerialNumber(parent, b, showButtons, false, false);
    }

    public static String SerialNumber(Frame parent, ResourceBundle b, boolean showButtons, boolean readonly) {
        return SerialNumber.showSerialNumber(parent, b, showButtons, readonly, false);
    }

    public static String showSerialNumber(Object parent, ResourceBundle b, boolean showButtons, boolean readonly,
            boolean uppercase) {
        EJDialog ejd = null;
        if (parent instanceof Frame) {
            ejd = new EJDialog((Frame) parent, true);
        } else {
            ejd = new EJDialog((Dialog) parent, true);
        }

        return SerialNumber.construct(ejd, b, showButtons, readonly, uppercase);
    }

    public static String showSerialNumber(Dialog parent, ResourceBundle b, boolean showButtons, boolean readonly,
            boolean uppercase) {
        EJDialog ejd = new EJDialog(parent, true);
        return SerialNumber.construct(ejd, b, showButtons, readonly, uppercase);
    }

    private static String construct(EJDialog ejd, ResourceBundle b, boolean showButtons, boolean readonly,
            boolean uppercase) {
        ejd.setTitle(ApplicationManager.getTranslation("SerialNumber.SERIAL_NUMBER_TITLE", b));
        SerialNumber sn = new SerialNumber(b, showButtons, readonly, uppercase);

        ejd.getContentPane().setLayout(new BorderLayout());
        ejd.getContentPane().add(sn, BorderLayout.CENTER);
        ejd.pack();
        ApplicationManager.center(ejd);
        ejd.setVisible(true);

        if ((sn.getSerialNumber() == null) || sn.getSerialNumber().equals("")
                || (sn.getSerialNumber().length() != SerialNumber.CorrectSerialNumberSize)) {
            return null;
        }

        return sn.getSerialNumber();
    }

}
