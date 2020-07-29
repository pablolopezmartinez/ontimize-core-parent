package com.ontimize.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

public class VirtualKeyboard {

    private static final String VIRTUAL_KEYBOARD_TITLE = "VirtualKeyboard";

    private static final String SELECTION_CHARACTER = "SelectCharacter";

    private static final String SYMBOLS = "Symbols";

    private static final String COPY = "Copy";

    private static VirtualKeyboard instance = null;

    private static Hashtable blocksCharacters = new Hashtable();

    private JTextComponent comp = null;

    private static class CharacterTableModel extends AbstractTableModel {

        private Vector keys = null;

        private final int columns = 15;

        public CharacterTableModel(Vector keys) {
            super();
            this.keys = keys;
        }

        public void setLetters(Vector keys) {
            this.keys = keys;
            this.fireTableChanged(new TableModelEvent(this));
        }

        @Override
        public Class getColumnClass(int c) {
            return Character.class;
        }

        @Override
        public int getRowCount() {
            double d = this.keys.size() / (double) this.columns;
            int i = this.keys.size() / this.columns;
            if (d > i) {
                return i + 1;
            } else {
                return i;
            }
        }

        @Override
        public int getColumnCount() {
            return this.columns;
        }

        @Override
        public Object getValueAt(int r, int c) {
            int index = (r * this.columns) + c;
            if (index >= this.keys.size()) {
                return null;
            } else {
                return this.keys.get(index);
            }
        }

    }

    private JDialog dialog = null;

    private final JLabel labelInfo = new JLabel();

    private final JTextField textField = new JTextField();

    private final JButton copyButton = new JButton(VirtualKeyboard.COPY);

    private VirtualKeyboard() {
        for (int i = 0; i < Character.MAX_VALUE; i++) {
            char caracter = (char) i;
            if (Character.isLetterOrDigit(caracter)) {
                Character.UnicodeBlock block = Character.UnicodeBlock.of(caracter);
                if (!VirtualKeyboard.blocksCharacters.containsKey(block)) {
                    Vector v = new Vector();
                    v.add(new Character(caracter));
                    VirtualKeyboard.blocksCharacters.put(block, v);
                } else {
                    Vector v = (Vector) VirtualKeyboard.blocksCharacters.get(block);
                    v.add(new Character(caracter));
                }
            }
        }
    }

    public void show(Window window, JTextComponent c) {
        if (this.dialog == null) {
            this.dialog = this.createDialog(window, c);

        } else {
            if (this.dialog.getOwner() != window) {
                this.dialog = this.createDialog(window, c);
            }
        }
        this.comp = c;
        SwingUtilities.updateComponentTreeUI(this.dialog);
        this.dialog.pack();
        ApplicationManager.center(this.dialog);
        this.dialog.setVisible(true);
    }

    public static String[] getAvaliableFonts() {
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String envfonts[] = gEnv.getAvailableFontFamilyNames();
        return envfonts;
    }

    private JDialog createDialog(Window window, final JTextComponent com) {
        JDialog dialog = null;
        if (window instanceof Frame) {
            dialog = new JDialog((Frame) window, VirtualKeyboard.VIRTUAL_KEYBOARD_TITLE, true);
        } else {
            dialog = new JDialog((Dialog) window, VirtualKeyboard.VIRTUAL_KEYBOARD_TITLE, true);
        }
        this.comp = com;
        Vector vBlocksNames = new Vector();
        Enumeration enumKeys = VirtualKeyboard.blocksCharacters.keys();
        while (enumKeys.hasMoreElements()) {
            vBlocksNames.add(enumKeys.nextElement());
        }
        this.labelInfo.setText(VirtualKeyboard.SELECTION_CHARACTER);

        final JComboBox blockCombo = new JComboBox(vBlocksNames);

        this.copyButton.setMargin(new Insets(0, 2, 0, 2));
        this.copyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ((VirtualKeyboard.this.textField.getSelectedText() == null)
                        || (VirtualKeyboard.this.textField.getSelectedText().length() == 0)) {
                    VirtualKeyboard.this.textField.selectAll();
                }
                VirtualKeyboard.this.textField.copy();
            }
        });

        final JTable jTable = new JTable() {

            @Override
            public Dimension getPreferredSize() {
                FontMetrics fm = this.getFontMetrics(this.getFont());
                int height = fm.getHeight();
                int maxWidth = fm.getMaxAdvance();
                Dimension d = this.getIntercellSpacing();
                this.setRowHeight(height);
                return new Dimension((this.getColumnCount() * maxWidth) + 20, this.getRowCount() * (height + d.height));
            }

            @Override
            public void updateUI() {
                super.updateUI();
                // Font size
                Font f = this.getFont();
                int t = f.getSize();
                t = t + 2;
                this.setFont(new FontUIResource(f.deriveFont((float) t)));
                int maxWidth = this.getFontMetrics(this.getFont()).getMaxAdvance();
                TableColumnModel tm = this.getColumnModel();
                for (int i = 0; i < this.getColumnCount(); i++) {
                    TableColumn tc = tm.getColumn(i);
                    tc.setMinWidth(maxWidth - 2);
                    tc.setPreferredWidth(maxWidth);
                }
                this.revalidate();
            }
        };
        jTable.getTableHeader().setReorderingAllowed(false);
        jTable.getTableHeader().setResizingAllowed(false);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        if (jTable.getDefaultRenderer(Character.class) instanceof JLabel) {
            ((JLabel) jTable.getDefaultRenderer(Character.class)).setHorizontalAlignment(SwingConstants.CENTER);
        }
        if (!VirtualKeyboard.blocksCharacters.isEmpty()) {
            enumKeys = VirtualKeyboard.blocksCharacters.keys();
            Object oKey = enumKeys.nextElement();
            Vector v = (Vector) VirtualKeyboard.blocksCharacters.get(oKey);
            CharacterTableModel m = new CharacterTableModel(v);
            jTable.setModel(m);
        }
        jTable.setRowSelectionAllowed(false);
        jTable.setColumnSelectionAllowed(false);
        jTable.setCellSelectionEnabled(true);
        JPanel comboPanel = new JPanel(new BorderLayout());
        comboPanel.setBorder(BorderFactory.createEtchedBorder());
        comboPanel.add(this.labelInfo, BorderLayout.NORTH);
        JPanel panelTextField = new JPanel(new GridBagLayout());
        panelTextField.add(this.textField, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
        panelTextField.add(this.copyButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
        comboPanel.add(panelTextField, BorderLayout.SOUTH);
        comboPanel.add(blockCombo);
        dialog.getContentPane().add(comboPanel, BorderLayout.NORTH);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(VirtualKeyboard.SYMBOLS));
        tablePanel.add(jTable);
        JScrollPane scroll = new JScrollPane(tablePanel);
        dialog.getContentPane().add(scroll);
        blockCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((CharacterTableModel) jTable.getModel())
                    .setLetters((Vector) VirtualKeyboard.blocksCharacters.get(blockCombo.getSelectedItem()));
                jTable.revalidate();
            }
        });

        jTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int f = jTable.getSelectedRow();
                    int c = jTable.getSelectedColumn();
                    Object oValue = jTable.getValueAt(f, c);
                    if (oValue != null) {
                        if (VirtualKeyboard.this.comp != null) {
                            VirtualKeyboard.this.comp.setText(VirtualKeyboard.this.comp.getText() + oValue);
                        }
                        VirtualKeyboard.this.textField.setText(VirtualKeyboard.this.textField.getText() + oValue);
                    }
                }
            }
        });
        dialog.pack();
        return dialog;
    }

    public static void showDialog(Window owner, JTextComponent c) {
        if (VirtualKeyboard.instance == null) {
            VirtualKeyboard.instance = new VirtualKeyboard();
        }
        VirtualKeyboard.instance.show(owner, c);
    }

}
