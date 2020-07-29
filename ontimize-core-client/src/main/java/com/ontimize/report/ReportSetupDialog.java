package com.ontimize.report;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class ReportSetupDialog extends EJDialog {

    static String lInfoKey = "report.saved_configurations";

    JLabel lInfo = new JLabel(ReportSetupDialog.lInfoKey);

    JList configurationList = new JList();

    JButton acceptButton = new JButton("application.accept");

    JButton cancelButton = new JButton("application.cancel");

    String configuration = null;

    public ReportSetupDialog(Frame o) {
        super(o, "ReportsConfiguration", true);
        this.init();
    }

    public ReportSetupDialog(Dialog o) {
        super(o, "ReportsConfiguration", true);
        this.init();
    }

    public void init() {
        this.setAutoPackOnOpen(false);
        // Label
        JPanel jpButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jpButtonsPanel.add(this.acceptButton);
        jpButtonsPanel.add(this.cancelButton);
        this.getContentPane().add(jpButtonsPanel, BorderLayout.SOUTH);
        this.getContentPane().add(this.lInfo, BorderLayout.NORTH);
        this.getContentPane().add(new JScrollPane(this.configurationList));
        this.acceptButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ReportSetupDialog.this.configurationList.getSelectedIndex() >= 0) {
                    ReportSetupDialog.this.configuration = (String) ReportSetupDialog.this.configurationList
                        .getSelectedValue();
                }
                ReportSetupDialog.this.setVisible(false);
            }
        });
        this.cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ReportSetupDialog.this.configuration = null;
                ReportSetupDialog.this.setVisible(false);
            }
        });
        this.configurationList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (ReportSetupDialog.this.configurationList.getSelectedIndex() >= 0) {
                        ReportSetupDialog.this.configuration = (String) ReportSetupDialog.this.configurationList
                            .getSelectedValue();
                    }
                    ReportSetupDialog.this.setVisible(false);
                }
            }
        });
        this.configurationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.acceptButton.setIcon(ImageManager.getIcon(ImageManager.OK));
        this.cancelButton.setIcon(ImageManager.getIcon(ImageManager.CANCEL));
        this.pack();
        ApplicationManager.center(this);
    }

    public String showSetupDialog(java.util.List configurations, ResourceBundle res) {
        this.configuration = null;
        this.configurationList.setListData(configurations.toArray());
        this.lInfo.setText(ReportUtils.getTranslation(ReportSetupDialog.lInfoKey, res, null));
        this.setTitle(ReportUtils.getTranslation("report.saved_configurations", res, null));
        this.acceptButton.setText(ReportUtils.getTranslation("application.accept", res, null));
        this.cancelButton.setText(ReportUtils.getTranslation("application.cancel", res, null));
        super.setVisible(true);
        return this.configuration;
    }

}
