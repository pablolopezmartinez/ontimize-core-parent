package com.ontimize.util.swing.table;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.i18n.Internationalization;

public class GroupableColumnGroup implements Internationalization {

    protected TableCellRenderer renderer;

    protected Vector list;

    protected String text;

    protected int margin = 0;

    protected ResourceBundle resourcebundle;

    public GroupableColumnGroup(String text) {
        this(null, text);
    }

    public GroupableColumnGroup(TableCellRenderer renderer, String text) {
        if (renderer == null) {
            this.renderer = new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    JTableHeader header = table.getTableHeader();
                    if (header != null) {
                        this.setForeground(header.getForeground());
                        this.setBackground(header.getBackground());
                        this.setFont(header.getFont());
                    }
                    this.setHorizontalAlignment(SwingConstants.CENTER);
                    this.setText(value == null ? "" : ApplicationManager.getTranslation(value.toString(),
                            GroupableColumnGroup.this.resourcebundle));
                    this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                    return this;
                }
            };
        } else {
            this.renderer = renderer;
        }
        this.text = text;
        this.list = new Vector();
    }

    /**
     * @param obj TableColumn or ColumnGroup
     */
    public void add(Object obj) {
        if (obj == null) {
            return;
        }
        this.list.add(obj);
    }

    /**
     * @param c TableColumn
     * @param v ColumnGroups
     */
    public Vector getColumnGroups(TableColumn c, Vector g) {
        g.addElement(this);
        if (this.list.contains(c)) {
            return g;
        }
        Enumeration e = this.list.elements();
        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            if (obj instanceof GroupableColumnGroup) {
                Vector groups = ((GroupableColumnGroup) obj).getColumnGroups(c, (Vector) g.clone());
                if (groups != null) {
                    return groups;
                }
            }
        }
        return null;
    }

    public TableCellRenderer getHeaderRenderer() {
        return this.renderer;
    }

    public void setHeaderRenderer(TableCellRenderer renderer) {
        if (renderer != null) {
            this.renderer = renderer;
        }
    }

    public Object getHeaderValue() {
        return this.text;
    }

    public boolean hasTableColumn(TableColumn tc) {
        TableColumn root = (TableColumn) this.list.get(0);
        if (root.getHeaderValue().equals(tc.getHeaderValue())) {
            return true;
        }
        return false;
    }

    public Dimension getSize(JTable table) {
        Component comp = this.renderer.getTableCellRendererComponent(table, this.getHeaderValue(), false, false, -1, 0);
        int hMin = comp.getFontMetrics(comp.getFont()).getHeight();
        int height = comp.getPreferredSize().height;

        if (hMin > height) {
            height = hMin;
        }

        int width = 0;
        Enumeration e = this.list.elements();
        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            if (obj instanceof TableColumn) {
                TableColumn aColumn = (TableColumn) obj;
                width += aColumn.getWidth();
            } else {
                width += ((GroupableColumnGroup) obj).getSize(table).width;
            }
        }
        return new Dimension(width, height);
    }

    public void setColumnMargin(int margin) {
        this.margin = margin;
        Enumeration e = this.list.elements();
        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            if (obj instanceof GroupableColumnGroup) {
                ((GroupableColumnGroup) obj).setColumnMargin(margin);
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
        this.resourcebundle = resourcebundle;
    }

}
