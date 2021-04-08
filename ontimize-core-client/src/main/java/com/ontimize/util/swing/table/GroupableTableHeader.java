package com.ontimize.util.swing.table;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.ontimize.gui.i18n.Internationalization;

public class GroupableTableHeader extends JTableHeader implements Internationalization {

    protected Vector columnGroups = null;

    public GroupableTableHeader(TableColumnModel model) {
        super(model);
        this.setUI(new GroupableTableHeaderUI());
        this.setReorderingAllowed(false);
    }

    public GroupableTableHeader() {
        super();
        this.setUI(new GroupableTableHeaderUI());
        this.setReorderingAllowed(false);
    }

    @Override
    public void setColumnModel(TableColumnModel columnModel) {
        super.setColumnModel(columnModel);
        Vector vColumnGroup = new Vector();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn tc = columnModel.getColumn(i);
            boolean has = false;
            for (int j = 0; j < vColumnGroup.size(); j++) {
                GroupableColumnGroup cG = (GroupableColumnGroup) vColumnGroup.get(j);
                if (cG.hasTableColumn(tc)) {
                    has = true;
                    cG.add(tc);
                    break;
                }
            }
            if (!has) {
                GroupableColumnGroup cG = new GroupableColumnGroup(tc.getHeaderValue().toString());
                cG.add(tc);
                vColumnGroup.add(cG);
            }
        }
        this.setColumnGroup(vColumnGroup);
    }

    public void addTableColumn(TableColumn tc) {
        boolean has = false;
        if (this.columnGroups == null) {
            this.columnGroups = new Vector();
        }

        for (int j = 0; j < this.columnGroups.size(); j++) {
            Object o = this.columnGroups.get(j);
            if (o instanceof GroupableColumnGroup) {
                GroupableColumnGroup cG = (GroupableColumnGroup) this.columnGroups.get(j);
                if (cG.hasTableColumn(tc)) {
                    has = true;
                    cG.add(tc);
                    break;
                }
            } else if (o instanceof TableColumn) {
                if (tc.getHeaderValue().equals(((TableColumn) o).getHeaderValue())) {
                    has = true;
                    this.columnGroups.remove(o);
                    GroupableColumnGroup cG = new GroupableColumnGroup(tc.getHeaderValue().toString());
                    cG.add(tc);
                    cG.add(o);
                    this.columnGroups.insertElementAt(cG, j);
                    break;
                }
            }
        }
        if (!has) {
            this.columnGroups.add(tc);
        }
    }

    public void removeAllColumnGroups() {
        this.columnGroups = new Vector();
    }

    @Override
    public void updateUI() {
        this.setUI(new GroupableTableHeaderUI());
    }

    @Override
    public void setReorderingAllowed(boolean b) {
        this.reorderingAllowed = false;
    }

    public void addColumnGroup(GroupableColumnGroup g) {
        if (this.columnGroups == null) {
            this.columnGroups = new Vector();
        }
        this.columnGroups.addElement(g);
    }

    public void setColumnGroup(Vector cg) {
        this.columnGroups = cg;
    }

    public Enumeration getColumnGroups(TableColumn col) {
        if (this.columnGroups == null) {
            return null;
        }
        Enumeration e = this.columnGroups.elements();
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            GroupableColumnGroup cGroup = null;
            if (!(o instanceof GroupableColumnGroup)) {
                cGroup = new GroupableColumnGroup(((TableColumn) o).getHeaderValue().toString());
            } else {
                cGroup = (GroupableColumnGroup) o;
            }
            Vector v_ret = cGroup.getColumnGroups(col, new Vector());
            if (v_ret != null) {
                return v_ret.elements();
            }
        }
        return null;
    }

    public void setColumnMargin() {
        if (this.columnGroups == null) {
            return;
        }
        int columnMargin = this.getColumnModel().getColumnMargin();
        Enumeration e = this.columnGroups.elements();
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if (o instanceof GroupableColumnGroup) {
                GroupableColumnGroup cGroup = (GroupableColumnGroup) o;
                cGroup.setColumnMargin(columnMargin);
            }
        }
    }

    @Override
    public Vector getTextsToTranslate() {
        return null;
    }

    @Override
    public void setComponentLocale(Locale locale) {

    }

    @Override
    public void setResourceBundle(ResourceBundle resourcebundle) {
        if (this.getUI() instanceof GroupableTableHeaderUI) {
            ((GroupableTableHeaderUI) this.getUI()).setResourceBundle(resourcebundle);
        }
    }

}
