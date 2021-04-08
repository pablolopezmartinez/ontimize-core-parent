package com.ontimize.gui.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.ontimize.gui.ApplicationManager;

public class TableInformationPanel extends JPanel {

    protected JTextArea labelMessage;

    protected int topOffset;

    public TableInformationPanel() {
        super();
        this.topOffset = 0;
        this.build();
    }

    public TableInformationPanel(int topOffset) {
        super();
        this.topOffset = topOffset;
        this.build();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0, 65));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        super.paintComponent(g);
    }

    protected void build() {
        this.setLayout(new GridBagLayout());
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                TableInformationPanel.this.setVisible(false);
                e.consume();
            }
        });

        this.labelMessage = new JTextArea(ApplicationManager.getTranslation("table.information_message_error"));
        this.labelMessage.setOpaque(false);
        this.labelMessage.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.2f));
        Font f = this.labelMessage.getFont().deriveFont(16f);
        this.labelMessage.setFont(f.deriveFont(Font.BOLD));
        this.labelMessage.setForeground(new Color(0xdd1111));
        JScrollPane scroll = new JScrollPane(this.labelMessage);
        scroll.setOpaque(false);
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        JPanel filler2 = new JPanel();
        filler2.setOpaque(false);

        JTextArea labelTitle = new JTextArea(ApplicationManager.getTranslation("table.information_title_error"));
        labelTitle.setFont(this.labelMessage.getFont().deriveFont(24f));
        labelTitle.setForeground(new Color(0xdd1111));
        labelTitle.setOpaque(false);
        labelTitle.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.5f));

        int row = 0;

        this.add(filler, new GridBagConstraints(0, row++, 0, 3, 1, 2, GridBagConstraints.NORTH,
                GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
        this.add(labelTitle, new GridBagConstraints(0, row++, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(this.topOffset, 0, 0, 0), 0, 0));
        this.add(scroll, new GridBagConstraints(0, row++, 1, 1, 1, 8, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 30, 0, 30), 0, 0));
        this.add(filler2, new GridBagConstraints(0, row++, 1, 1, 1, 1, GridBagConstraints.SOUTH,
                GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

        this.setOpaque(false);
        this.labelMessage.setOpaque(false);
    }

    public void setMessage(String msg) {
        this.labelMessage.setText(msg);
    }

}
