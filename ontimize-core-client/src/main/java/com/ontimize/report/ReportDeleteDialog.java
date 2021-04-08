package com.ontimize.report;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.container.EJDialog;
import com.ontimize.gui.images.ImageManager;

public class ReportDeleteDialog extends EJDialog {

    static String lInfoKey = "report.saved_configurations";

    JLabel lInfo = new JLabel(ReportDeleteDialog.lInfoKey);

    JList configurationList = new JList();

    JButton deleteButton = new JButton("delete");

    JButton closeButton = new JButton("close");

    String configuration = null;

    public ReportDeleteDialog(Frame o) {
        super(o, "ConfiguracionesInformes", true);
        this.init();
    }

    public ReportDeleteDialog(Dialog o) {
        super(o, "ConfiguracionesInformes", true);
        this.init();
    }

    public void init() {
        this.setAutoPackOnOpen(false);
        // Label
        JPanel jpButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpButtonsPanel.add(this.deleteButton);
        jpButtonsPanel.add(this.closeButton);
        this.getContentPane().add(jpButtonsPanel, BorderLayout.SOUTH);
        this.getContentPane().add(this.lInfo, BorderLayout.NORTH);
        this.getContentPane().add(new JScrollPane(this.configurationList));
        this.deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ReportDeleteDialog.this.configurationList.getSelectedIndex() >= 0) {
                    ReportDeleteDialog.this.configuration = (String) ReportDeleteDialog.this.configurationList
                        .getSelectedValue();
                }
                ReportDeleteDialog.this.setVisible(false);
            }
        });
        this.closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ReportDeleteDialog.this.configuration = null;
                ReportDeleteDialog.this.setVisible(false);
            }
        });
        this.configurationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.deleteButton.setIcon(ImageManager.getIcon(ImageManager.OK));
        this.closeButton.setIcon(ImageManager.getIcon(ImageManager.CANCEL));
        this.pack();
        ApplicationManager.center(this);
    }

    public String showDeleteDialog(java.util.List configurations, ResourceBundle res) {
        this.configuration = null;
        this.configurationList.setListData(configurations.toArray());
        this.lInfo.setText(ReportUtils.getTranslation(ReportDeleteDialog.lInfoKey, res, null));
        this.setTitle(ReportUtils.getTranslation("report.saved_configurations", res, null));
        this.deleteButton.setText(ReportUtils.getTranslation("delete", res, null));
        this.closeButton.setText(ReportUtils.getTranslation("close", res, null));
        super.setVisible(true);
        return this.configuration;
    }

}
