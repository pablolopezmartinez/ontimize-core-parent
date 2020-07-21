package com.ontimize.gui.table;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.table.TableSorter.GroupTableModel.GroupItem;
import com.ontimize.gui.table.TableSorter.GroupTableModel.GroupList;

/**
 * Renders the table cell that represent grouped information
 */
public class GroupCellRenderer extends CellRenderer {

    private static final Logger logger = LoggerFactory.getLogger(GroupCellRenderer.class);

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object oValue, boolean selected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(jTable, oValue, selected, hasFocus, row, column);
        if (c instanceof JLabel) {
            StringBuilder buffer = new StringBuilder();
            if (oValue instanceof GroupList) {
                GroupList valueList = (GroupList) oValue;

                if (jTable instanceof EJTable) {
                    // If table is a EJTable then use the renderer for the
                    // column in each element of the list (only if the renderer
                    // is a JLabel)
                    TableCellRenderer noGroupedCellRenderer = ((EJTable) jTable).getNoGroupedCellRenderer(row, column);
                    if (valueList != null) {
                        for (int i = 0; i < valueList.size(); i++) {
                            Object value = ((GroupItem) valueList.get(i)).getValue();
                            Component tableCellRendererComponent = noGroupedCellRenderer
                                .getTableCellRendererComponent(jTable, value, selected, hasFocus, row, column);
                            if (tableCellRendererComponent instanceof JLabel) {
                                String text = ((JLabel) tableCellRendererComponent).getText();
                                if (i > 0) {
                                    buffer.append(", ");
                                }
                                buffer.append(text);
                            }
                        }
                    }
                }

                if (buffer.length() == 0) {
                    buffer.append(valueList.toString());
                }

            } else {
                buffer.append(oValue.toString());
            }
            ((JLabel) c).setText(buffer.toString());
            this.setTipWhenNeeded(jTable, oValue, column);
        }
        return c;
    }

    /**
     * Returns the object as a String, or an empty String in case the object is null
     * @param o
     * @return
     */
    protected String parser(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else if (o == null) {
            return "";
        }

        return o.toString();
    }

    /**
     * Sets the tip to the grouped columns.
     */
    @Override
    public void setTipWhenNeeded(JTable jTable, Object oValue, int column) {
        // TIP
        try {
            this.setToolTipText(null);
            if (jTable != null) {
                if (this.component instanceof JLabel) {
                    StringBuilder buffer = new StringBuilder();

                    if (oValue instanceof GroupList) {
                        buffer.append("<HTML><BODY>");
                        GroupList valueList = (GroupList) oValue;
                        for (int j = 0; j < valueList.size(); j++) {
                            if (j >= 100) {
                                if (j == 100) {
                                    buffer.append("...<BR>");
                                }
                                continue;
                            }
                            // buffer.append("<TR><TD>");
                            buffer.append(this.parser(((GroupItem) valueList.get(j)).getRenderedString()));
                            // buffer.append("</TD></TR>");
                            if (j != (valueList.size() - 1)) {
                                buffer.append("<BR>");
                            }
                        }
                        buffer.append("</BODY></HTML>");
                    }
                    this.component.setToolTipText(buffer.length() > 0 ? buffer.toString() : null);
                }
            }
        } catch (Exception e) {
            GroupCellRenderer.logger.error(null, e);
        }

    }

}
