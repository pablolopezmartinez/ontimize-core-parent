package com.ontimize.gui.field.html.actions;

import java.awt.event.ActionEvent;
import java.io.StringWriter;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.bushe.swing.action.ShouldBeEnabledDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.field.html.HTMLUtils;
import com.ontimize.gui.field.html.utils.CompoundUndoManager;
import com.ontimize.gui.field.html.utils.ElementWriter;

/**
 *
 * Action for adding and removing table elements
 *
 * @author Imatia S.L.
 *
 */
public class TableEditAction extends HTMLTextEditAction {

    private static final Logger logger = LoggerFactory.getLogger(TableEditAction.class);

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int INSERT_CELL = 0;

    public static final int DELETE_CELL = 1;

    public static final int INSERT_ROW = 2;

    public static final int DELETE_ROW = 3;

    public static final int INSERT_COL = 4;

    public static final int DELETE_COL = 5;

    private static final String NAMES[] = { "HTMLShef.insert_cell", "HTMLShef.delete_cell", "HTMLShef.insert_row",
            "HTMLShef.delete_row", "HTMLShef.insert_column", "HTMLShef.delete_column" };

    protected int type;

    public TableEditAction(int type) throws IllegalArgumentException {
        super("");
        if ((type < 0) || (type >= TableEditAction.NAMES.length)) {
            throw new IllegalArgumentException("Invalid type");
        }
        this.type = type;
        this.putValue("ID", TableEditAction.NAMES[type]);
        this.putValue(Action.NAME, HTMLTextEditAction.i18n.str(TableEditAction.NAMES[type]));
        this.addShouldBeEnabledDelegate(new ShouldBeEnabledDelegate() {

            @Override
            public boolean shouldBeEnabled(Action a) {
                return TableEditAction.this.isInTD(TableEditAction.this.getCurrentEditor());
            }
        });
    }

    @Override
    protected void editPerformed(ActionEvent e, JEditorPane ed) {
        HTMLDocument document = (HTMLDocument) ed.getDocument();

        Element curElem = document.getParagraphElement(ed.getCaretPosition());
        Element td = HTMLUtils.getParent(curElem, HTML.Tag.TD);
        Element tr = HTMLUtils.getParent(curElem, HTML.Tag.TR);
        // HTMLDocument document = getDocument();
        if ((td == null) || (tr == null) || (document == null)) {
            return;
        }

        CompoundUndoManager.beginCompoundEdit(document);
        try {
            if (this.type == TableEditAction.INSERT_CELL) {
                document.insertAfterEnd(td, "<td></td>");
            } else if (this.type == TableEditAction.DELETE_CELL) {
                this.removeCell(td);
            } else if (this.type == TableEditAction.INSERT_ROW) {
                this.insertRowAfter(tr);
            } else if (this.type == TableEditAction.DELETE_ROW) {
                this.removeRow(tr);
            } else if (this.type == TableEditAction.INSERT_COL) {
                this.insertColumnAfter(td);
            } else if (this.type == TableEditAction.DELETE_COL) {
                this.removeColumn(td);
            }
        } catch (Exception ex) {
            TableEditAction.logger.error(null, ex);
        }
        CompoundUndoManager.endCompoundEdit(document);
    }

    protected void removeCell(Element td) throws Exception {
        Element tr = HTMLUtils.getParent(td, HTML.Tag.TR);
        if ((tr != null) && td.getName().equals("td")) {
            if (td.getEndOffset() != tr.getEndOffset()) {
                this.remove(td);
            } else if (this.getRowCellCount(tr) <= 1) {
                this.remove(tr);
            } else {
                StringWriter out = new StringWriter();
                ElementWriter w = new ElementWriter(out, tr, tr.getStartOffset(), td.getStartOffset());
                w.write();

                HTMLDocument doc = (HTMLDocument) tr.getDocument();
                doc.setOuterHTML(tr, out.toString());
            }
        }
    }

    protected void insertRowAfter(Element tr) throws Exception {
        Element table = HTMLUtils.getParent(tr, HTML.Tag.TABLE);

        if ((table != null) && tr.getName().equals("tr")) {
            HTMLDocument doc = (HTMLDocument) tr.getDocument();
            if (tr.getEndOffset() != table.getEndOffset()) {
                doc.insertAfterEnd(tr, this.getRowHTML(tr));
            } else {
                AttributeSet atr = table.getAttributes();
                String tbl = HTMLUtils.getElementHTML(table, false);
                tbl += this.getRowHTML(tr);

                tbl = HTMLUtils.createTag(HTML.Tag.TABLE, atr, tbl);
                doc.setOuterHTML(table, tbl);
            }
        }
    }

    protected void removeRow(Element tr) throws Exception {
        Element table = HTMLUtils.getParent(tr, HTML.Tag.TABLE);
        if ((table != null) && tr.getName().equals("tr")) {
            if (tr.getEndOffset() != table.getEndOffset()) {
                this.remove(tr);
            } else if (this.getTableRowCount(table) <= 1) {
                this.remove(table);
            } else {
                StringWriter out = new StringWriter();
                ElementWriter w = new ElementWriter(out, table, table.getStartOffset(), tr.getStartOffset());
                w.write();

                HTMLDocument doc = (HTMLDocument) tr.getDocument();
                doc.setOuterHTML(table, out.toString());
            }
        }
    }

    protected int getTableRowCount(Element table) {
        int count = 0;
        for (int i = 0; i < table.getElementCount(); i++) {
            Element e = table.getElement(i);
            if (e.getName().equals("tr")) {
                count++;
            }
        }

        return count;
    }

    protected int getRowCellCount(Element tr) {
        int count = 0;
        for (int i = 0; i < tr.getElementCount(); i++) {
            Element e = tr.getElement(i);
            if (e.getName().equals("td")) {
                count++;
            }
        }

        return count;
    }

    protected void remove(Element el) throws BadLocationException {
        int start = el.getStartOffset();
        int len = el.getEndOffset() - start;
        Document document = el.getDocument();

        if (el.getEndOffset() > document.getLength()) {
            len = document.getLength() - start;
        }
        document.remove(start, len);
    }

    protected int getCellIndex(Element tr, Element td) {
        int tdIndex = -1;
        for (int i = 0; i < tr.getElementCount(); i++) {
            Element e = tr.getElement(i);
            if (e.getStartOffset() == td.getStartOffset()) {
                tdIndex = i;
                break;
            }
        }

        return tdIndex;
    }

    protected void removeColumn(Element td) throws Exception {
        Element tr = HTMLUtils.getParent(td, HTML.Tag.TR);

        int tdIndex = this.getCellIndex(tr, td);
        if (tdIndex == -1) {
            return;
        }

        Element table = HTMLUtils.getParent(tr, HTML.Tag.TABLE);
        for (int i = 0; i < table.getElementCount(); i++) {
            Element row = table.getElement(i);
            if (row.getName().equals("tr")) {
                Element e = row.getElement(tdIndex);
                if ((e != null) && e.getName().equals("td")) {
                    this.removeCell(e);
                }
            }
        }
    }

    protected void insertColumnAfter(Element td) throws Exception {
        Element tr = HTMLUtils.getParent(td, HTML.Tag.TR);
        HTMLDocument doc = (HTMLDocument) tr.getDocument();

        int tdIndex = this.getCellIndex(tr, td);
        if (tdIndex == -1) {
            return;
        }

        Element table = HTMLUtils.getParent(tr, HTML.Tag.TABLE);
        for (int i = 0; i < table.getElementCount(); i++) {
            Element row = table.getElement(i);
            if (row.getName().equals("tr")) {
                AttributeSet attr = row.getAttributes();
                int cellCount = row.getElementCount();

                String rowHTML = "";
                String cell = "<td></td>";
                for (int j = 0; j < cellCount; j++) {
                    Element e = row.getElement(j);
                    rowHTML += HTMLUtils.getElementHTML(e, true);
                    if (j == tdIndex) {
                        rowHTML += cell;
                    }
                }

                int tds = row.getElementCount() - 1;
                if (tds < tdIndex) {
                    for (; tds <= tdIndex; tds++) {
                        rowHTML += cell;
                    }
                }

                rowHTML = HTMLUtils.createTag(HTML.Tag.TR, attr, rowHTML);
                doc.setOuterHTML(row, rowHTML);
            }
        }
    }

    protected String getRowHTML(Element tr) {
        String trTag = "<tr>";
        if (tr.getName().equals("tr")) {
            for (int i = 0; i < tr.getElementCount(); i++) {
                if (tr.getElement(i).getName().equals("td")) {
                    trTag += "<td></td>";
                }
            }
        }
        trTag += "</tr>";
        return trTag;
    }

    protected boolean isInTD(JEditorPane tc) {
        Element td = null;
        if (tc != null) {
            HTMLDocument doc = (HTMLDocument) tc.getDocument();
            try {
                Element curElem = doc.getParagraphElement(tc.getCaretPosition());
                td = HTMLUtils.getParent(curElem, HTML.Tag.TD);
            } catch (Exception ex) {
                TableEditAction.logger.trace(null, ex);
            }
        }

        return td != null;
    }

    @Override
    protected void updateContextState(JEditorPane wysEditor) {
        boolean isInTd = this.isInTD(wysEditor);
        if ((isInTd && !this.isEnabled()) || (this.isEnabled() && !isInTd)) {
            this.updateEnabled();
        }
    }

}
