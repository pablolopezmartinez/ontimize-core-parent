package com.ontimize.db.query;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.container.EJDialog;
import com.ontimize.gui.images.ImageManager;

public class QueryBuilderShowColsAndTypes extends JPanel {

    ResourceBundle bundle = null;

    String[] cols = null;

    int[] types = null;

    protected JLabel helpText = new JLabel();

    protected JLabel helpText2 = new JLabel();

    protected JPanel pShow = new JPanel();

    protected JPanel buttonPanel = new JPanel();

    protected JButton bOK = new com.ontimize.report.ReportDesignerButton(ImageManager.getIcon(ImageManager.OK));

    public QueryBuilderShowColsAndTypes(ResourceBundle bundle, String[] cols, int[] types, String entity) {
        this.cols = cols;
        this.types = types;
        this.bundle = bundle;

        String middle = new String("");

        for (int i = 0, a = types.length - 1; i < a; i++) {
            middle += "<TR><TD>&nbsp;&nbsp;</TD><TD>" + ApplicationManager.getTranslation(cols[i], bundle)
                    + "</TD><TD>&nbsp;&nbsp;</TD><TD>" + QueryBuilder
                        .getStringType(types[i])
                    + "</TD><TD>&nbsp;&nbsp;</TD></TR>";
        }

        this.helpText.setText("<html><body><BR>&nbsp;&nbsp;"
                + ApplicationManager.getTranslation("QueryBuilderShowColsAndTypesEntity",
                        bundle)
                + "&nbsp;&nbsp;<B>" + entity + "</B><BR><BR><BR>&nbsp;"
                + ApplicationManager.getTranslation("QueryBuilderShowColsAndTypesList",
                        bundle)
                + "<BR><table><TR><TD>&nbsp;&nbsp;</TD><TD align=center><B>"
                + ApplicationManager.getTranslation("QueryBuilderShowColsAndTypesColunm",
                        bundle)
                + "</B></TD><TD>&nbsp;&nbsp;</TD><TD align=center><B>"
                + ApplicationManager.getTranslation("QueryBuilderShowColsAndTypesType",
                        bundle)
                + "</B></TD><TD>&nbsp;&nbsp;</TD></TR>" + middle + "</table><BR></body></html>");

        this.bOK.setToolTipText(ApplicationManager.getTranslation("QueryBuilderOKCons", bundle));
        this.bOK.setText(ApplicationManager.getTranslation("QueryBuilderAceptar", bundle));
        this.bOK.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Window w = SwingUtilities.getWindowAncestor((Component) e.getSource());
                w.setVisible(false);
            }
        });

        this.pShow.setLayout(new BorderLayout());
        JScrollPane js = new JScrollPane(this.helpText);
        js.setPreferredSize(new Dimension(290, 400));

        this.pShow.add(js);

        this.buttonPanel.setLayout(new FlowLayout());
        this.buttonPanel.add(this.bOK);

        this.setLayout(new BorderLayout());
        this.add(this.pShow, BorderLayout.CENTER);
        this.add(this.buttonPanel, BorderLayout.SOUTH);
    }

    protected static EJDialog dialog = null;

    protected static QueryBuilderShowColsAndTypes qb = null;

    protected static Window w = null;

    public static void show(Component c, ResourceBundle bundle, String[] cols, int[] types, String entity) {

        if (!(c instanceof Frame) && !(c instanceof Dialog)) {
            QueryBuilderShowColsAndTypes.w = SwingUtilities.getWindowAncestor(c);
            if (QueryBuilderShowColsAndTypes.w instanceof Frame) {
                QueryBuilderShowColsAndTypes.dialog = new EJDialog((Frame) QueryBuilderShowColsAndTypes.w,
                        ApplicationManager.getTranslation("QueryBuilderShowColsAndTypesTitle", bundle), true);
            } else if (QueryBuilderShowColsAndTypes.w instanceof Dialog) {
                QueryBuilderShowColsAndTypes.dialog = new EJDialog((Dialog) QueryBuilderShowColsAndTypes.w,
                        ApplicationManager.getTranslation("QueryBuilderShowColsAndTypesTitle", bundle), true);
            }
        } else {
            if (c instanceof Frame) {
                QueryBuilderShowColsAndTypes.dialog = new EJDialog((Frame) c,
                        ApplicationManager.getTranslation("QueryBuilderShowColsAndTypesTitle", bundle), true);
            }
            if (c instanceof Dialog) {
                QueryBuilderShowColsAndTypes.dialog = new EJDialog((Dialog) c,
                        ApplicationManager.getTranslation("QueryBuilderShowColsAndTypesTitle", bundle), true);
            }
        }

        if (QueryBuilderShowColsAndTypes.dialog != null) {
            QueryBuilderShowColsAndTypes.qb = new QueryBuilderShowColsAndTypes(bundle, cols, types, entity);
            QueryBuilderShowColsAndTypes.dialog.getContentPane().add(new JScrollPane(QueryBuilderShowColsAndTypes.qb));
            QueryBuilderShowColsAndTypes.dialog.pack();
            if (QueryBuilderShowColsAndTypes.dialog != null) {
                ApplicationManager.center(QueryBuilderShowColsAndTypes.dialog);
            }
            QueryBuilderShowColsAndTypes.dialog.setVisible(true);
        }
    }

}
