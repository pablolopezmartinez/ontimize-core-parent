package com.ontimize.gui.login;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import com.ontimize.gui.ApplicationManager;

public class CertificateTableModel extends AbstractTableModel {

    private final List model = new ArrayList();

    private ResourceBundle bundle = null;

    public static final String CERTIFICATE_TABLE_MODEL_ALIAS = "certificatetablemodel.alias";

    public static final String CERTIFICATE_TABLE_MODEL_TYPE = "certificatetablemodel.type";

    public static final String CERTIFICATE_TABLE_MODEL_NOTBEFORE = "certificatetablemodel.notbefore";

    public static final String CERTIFICATE_TABLE_MODEL_NOTAFTER = "certificatetablemodel.notafter";

    public static final String CERTIFICATE_TABLE_MODEL_SUBJECTDN = "certificatetablemodel.subjectdn";

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return ApplicationManager.getTranslation(CertificateTableModel.CERTIFICATE_TABLE_MODEL_ALIAS, this.bundle);
        }
        if (column == 1) {
            return ApplicationManager.getTranslation(CertificateTableModel.CERTIFICATE_TABLE_MODEL_TYPE, this.bundle);
        }
        if (column == 2) {
            return ApplicationManager.getTranslation(CertificateTableModel.CERTIFICATE_TABLE_MODEL_NOTBEFORE,
                    this.bundle);
        }
        if (column == 3) {
            return ApplicationManager.getTranslation(CertificateTableModel.CERTIFICATE_TABLE_MODEL_NOTAFTER,
                    this.bundle);
        }
        if (column == 4) {
            return ApplicationManager.getTranslation(CertificateTableModel.CERTIFICATE_TABLE_MODEL_SUBJECTDN,
                    this.bundle);
        }
        return "";
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public int getRowCount() {
        return this.model.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return ((AliasCertPair) this.model.get(rowIndex)).getAlias();
        }
        if (columnIndex == 1) {
            return ((AliasCertPair) this.model.get(rowIndex)).getCert().getType();
        }
        if (!(((AliasCertPair) this.model.get(rowIndex)).getCert() instanceof X509Certificate)) {
            return "";
        }
        if (columnIndex == 2) {
            return ((X509Certificate) ((AliasCertPair) this.model.get(rowIndex)).getCert()).getNotBefore();
        }
        if (columnIndex == 3) {
            return ((X509Certificate) ((AliasCertPair) this.model.get(rowIndex)).getCert()).getNotAfter();
        }
        if (columnIndex == 4) {
            return ((X509Certificate) ((AliasCertPair) this.model.get(rowIndex)).getCert()).getSubjectDN();
        }
        return "";
    }

    public void setValues(List l) {
        this.model.clear();
        if (l != null) {
            List temp = new ArrayList();
            Iterator it = l.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof AliasCertPair) {
                    temp.add(obj);
                }
            }
            this.model.addAll(temp);
        }
        this.fireTableDataChanged();
    }

    public AliasCertPair getAliasCertAt(int row) {
        if (row > this.model.size()) {
            return null;
        }
        return (AliasCertPair) this.model.get(row);
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        this.fireTableStructureChanged();
    }

    public boolean isEmpty() {
        if (this.model == null) {
            return true;
        }
        return this.model.isEmpty();
    }

}
