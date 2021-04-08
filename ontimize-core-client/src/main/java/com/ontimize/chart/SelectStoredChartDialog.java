package com.ontimize.chart;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
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

public class SelectStoredChartDialog extends EJDialog {

    protected JLabel lInfo = new JLabel("chartutilities.StoredChartConfigurations");

    protected JList lConfigList = new JList();

    protected JButton bAccept = new JButton("Accept");

    protected JButton bCancel = new JButton("Cancel");

    protected String configuration = null;

    public SelectStoredChartDialog(Frame o) {
        super(o, "chartutilities.ChartConfigurations", true);
        this.init();
    }

    public SelectStoredChartDialog(Dialog o) {
        super(o, "chartutilities.ChartConfigurations", true);
        this.init();
    }

    protected void init() {
        this.setAutoPackOnOpen(false);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(this.bAccept);
        buttonsPanel.add(this.bCancel);
        this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        this.getContentPane().add(this.lInfo, BorderLayout.NORTH);
        this.getContentPane().add(new JScrollPane(this.lConfigList));
        this.bAccept.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (SelectStoredChartDialog.this.lConfigList.getSelectedIndex() >= 0) {
                    SelectStoredChartDialog.this.configuration = (String) SelectStoredChartDialog.this.lConfigList
                        .getSelectedValue();
                }
                SelectStoredChartDialog.this.setVisible(false);
            }
        });
        this.bCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SelectStoredChartDialog.this.configuration = null;
                SelectStoredChartDialog.this.setVisible(false);
            }
        });

        this.lConfigList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (SelectStoredChartDialog.this.lConfigList.getSelectedIndex() >= 0) {
                        SelectStoredChartDialog.this.configuration = (String) SelectStoredChartDialog.this.lConfigList
                            .getSelectedValue();
                    }
                    SelectStoredChartDialog.this.setVisible(false);
                }
            }
        });

        this.lConfigList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.bAccept.setIcon(ImageManager.getIcon(ImageManager.OK));
        this.bCancel.setIcon(ImageManager.getIcon(ImageManager.CANCEL));
        this.pack();
        ApplicationManager.center(this);
    }

    public String show(List configuraciones, ResourceBundle res) {
        this.configuration = null;
        this.lConfigList.setListData(configuraciones.toArray());
        this.lInfo.setText(ApplicationManager.getTranslation("chartutilities.StoredChartConfigurations", res));
        this.setTitle(ApplicationManager.getTranslation("chartutilities.ChartConfigurations", res));
        this.bAccept.setText(ApplicationManager.getTranslation("Accept", res));
        this.bCancel.setText(ApplicationManager.getTranslation("Cancel", res));
        super.setVisible(true);
        return this.configuration;
    }

}
