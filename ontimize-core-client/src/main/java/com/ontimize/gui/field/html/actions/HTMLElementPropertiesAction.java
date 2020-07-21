package com.ontimize.gui.field.html.actions;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.bushe.swing.action.ShouldBeEnabledDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.field.html.HTMLUtils;
import com.ontimize.gui.field.html.dialogs.ImageDialog;
import com.ontimize.gui.field.html.dialogs.ListDialog;
import com.ontimize.gui.field.html.dialogs.TablePropertiesDialog;
import com.ontimize.gui.field.html.utils.CompoundUndoManager;

/**
 * Action for editing an element's properties depending on the current caret position.
 *
 * Currently supports links, images, tables, lists, and paragraphs.
 *
 * @author Imatia S.L.
 *
 */
public class HTMLElementPropertiesAction extends HTMLTextEditAction {

    private static final Logger logger = LoggerFactory.getLogger(HTMLElementPropertiesAction.class);

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int TABLE_PROPS = 0;

    public static final int LIST_PROPS = 1;

    public static final int IMG_PROPS = 2;

    public static final int ELEM_PROPS = 3;

    private static final String[] PROPS = { "HTMLShef.table_properties_", "HTMLShef.list_properties_",
            "HTMLShef.image_properties_", "HTMLShef.object_properties_" };

    protected TablePropertiesDialog tableDlg;

    protected ListDialog listDlg;

    protected ImageDialog imgDlg;

    public HTMLElementPropertiesAction() {
        super(HTMLElementPropertiesAction.PROPS[HTMLElementPropertiesAction.ELEM_PROPS]);
        this.addShouldBeEnabledDelegate(new ShouldBeEnabledDelegate() {

            @Override
            public boolean shouldBeEnabled(Action a) {
                return (HTMLElementPropertiesAction.this.getCurrentEditor() != null) && (HTMLElementPropertiesAction
                    .elementAtCaretPosition(HTMLElementPropertiesAction.this.getCurrentEditor()) != null);
            }
        });
    }

    @Override
    protected void editPerformed(ActionEvent e, JEditorPane ed) {
        Element elem = HTMLElementPropertiesAction.elementAtCaretPosition(ed);
        int type = HTMLElementPropertiesAction.getElementType(elem);
        int caret = ed.getCaretPosition();

        if (type == HTMLElementPropertiesAction.IMG_PROPS) {
            this.editImageProps(elem);
        } else if (type == HTMLElementPropertiesAction.TABLE_PROPS) {
            this.editTableProps(elem);
        } else if (type == HTMLElementPropertiesAction.LIST_PROPS) {
            this.editListProps(elem);
        }

        try {
            ed.setCaretPosition(caret);
        } catch (Exception ex) {
            HTMLElementPropertiesAction.logger.trace(null, ex);
        }
    }

    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        super.setResourceBundle(resourceBundle);
        if (this.tableDlg != null) {
            this.tableDlg.setResourceBundle(resourceBundle);
        }
        if (this.listDlg != null) {
            this.listDlg.setResourceBundle(resourceBundle);
        }
        if (this.imgDlg != null) {
            this.imgDlg.setResourceBundle(resourceBundle);
        }
    }

    protected Map getAttribs(Element elem) {
        Map at = new HashMap();

        if (elem == null) {
            return at;
        }

        AttributeSet a = elem.getAttributes();
        for (Enumeration e = a.getAttributeNames(); e.hasMoreElements();) {
            Object n = e.nextElement();
            // don't return the name attribute
            if (n.toString().equals("name") && !elem.getName().equals("a")) {
                continue;
            }
            at.put(n.toString(), a.getAttribute(n).toString());
        }

        return at;
    }

    protected String getElementHTML(Element el, Map attribs) {
        String html = "<" + el.getName();
        for (Iterator e = attribs.keySet().iterator(); e.hasNext();) {
            Object name = e.next();
            Object val = attribs.get(name);
            html += " " + name + "=\"" + val + "\"";
        }

        String txt = HTMLUtils.getElementHTML(el, false);
        html += ">\n" + txt + "\n</" + el.getName() + ">";

        return html;
    }

    protected Map getLinkAttributes(Element elem) {
        String link = HTMLUtils.getElementHTML(elem, true).trim();
        Map attribs = new HashMap();
        if (link.startsWith("<a")) {
            link = link.substring(0, link.indexOf('>'));
            link = link.substring(link.indexOf(' '), link.length()).trim();

            attribs = HTMLUtils.tagAttribsToMap(link);
        }

        return attribs;
    }

    protected void editImageProps(Element elem) {
        if (this.imgDlg == null) {
            this.imgDlg = this.createImageDialog();
        }

        if (this.imgDlg != null) {
            Map imgAttribs = this.getAttribs(elem);
            this.imgDlg.setImageAttributes(imgAttribs);
            this.imgDlg.setLocationRelativeTo(this.imgDlg.getParent());
            this.imgDlg.setVisible(true);
            if (!this.imgDlg.hasUserCancelled()) {
                this.replace(elem, this.imgDlg.getHTML());
            }
        }
    }

    protected void editTableProps(Element paraElem) {
        HTMLDocument doc = null;
        try {
            doc = (HTMLDocument) paraElem.getDocument();
        } catch (Exception ex) {
            HTMLElementPropertiesAction.logger.error(null, ex);
            return;
        }

        Element tdElem = HTMLUtils.getParent(paraElem, HTML.Tag.TD);
        Element trElem = HTMLUtils.getParent(paraElem, HTML.Tag.TR);
        Element tableElem = HTMLUtils.getParent(paraElem, HTML.Tag.TABLE);
        if (this.tableDlg == null) {
            this.tableDlg = this.createTablePropertiesDialog();
        }
        if ((this.tableDlg == null) || (tdElem == null) || (trElem == null) || (tableElem == null)) {
            return; // no dialog or malformed table! Just return...
        }

        this.tableDlg.setCellAttributes(this.getAttribs(tdElem));
        this.tableDlg.setRowAttributes(this.getAttribs(trElem));
        this.tableDlg.setTableAttributes(this.getAttribs(tableElem));
        this.tableDlg.setLocationRelativeTo(this.tableDlg.getParent());
        this.tableDlg.setVisible(true);

        if (!this.tableDlg.hasUserCancelled()) {
            CompoundUndoManager.beginCompoundEdit(doc);
            try {
                String html = this.getElementHTML(tdElem, this.tableDlg.getCellAttributes());
                doc.setOuterHTML(tdElem, html);

                html = this.getElementHTML(trElem, this.tableDlg.getRowAttribures());
                doc.setOuterHTML(trElem, html);

                html = this.getElementHTML(tableElem, this.tableDlg.getTableAttributes());
                doc.setOuterHTML(tableElem, html);
            } catch (Exception ex) {
                HTMLElementPropertiesAction.logger.error(null, ex);
            }
            CompoundUndoManager.endCompoundEdit(doc);
        }
    }

    protected void editListProps(Element elem) {
        elem = HTMLUtils.getListParent(elem);
        if (elem == null) {
            return;
        }
        int type;
        if (elem.getName().equals("ul")) {
            type = ListDialog.UNORDERED;
        } else if (elem.getName().equals("ol")) {
            type = ListDialog.ORDERED;
        } else {
            return;
        }

        Map attr = this.getAttribs(elem);
        if (this.listDlg == null) {
            this.listDlg = this.createListDialog();
        }

        this.listDlg.setListType(type);
        this.listDlg.setListAttributes(attr);
        this.listDlg.setLocationRelativeTo(this.listDlg.getParent());
        this.listDlg.setVisible(true);
        if (!this.listDlg.hasUserCancelled()) {
            attr = this.listDlg.getListAttributes();
            String html = "";
            if (this.listDlg.getListType() != type) {
                HTML.Tag tag = HTML.Tag.UL;
                if (this.listDlg.getListType() == ListDialog.ORDERED) {
                    tag = HTML.Tag.OL;
                }
                String txt = HTMLUtils.getElementHTML(elem, false);
                html = "<" + tag;
                for (Iterator ee = attr.keySet().iterator(); ee.hasNext();) {
                    Object o = ee.next();
                    html += " " + o + "=" + attr.get(o);
                }
                html += ">" + txt + "</" + tag + ">";
            } else {
                html = this.getElementHTML(elem, attr);
            }

            this.replace(elem, html);
        }

    }

    protected ImageDialog createImageDialog() {
        Component c = this.getCurrentEditor();
        ImageDialog d = null;
        if (c != null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if ((w != null) && (w instanceof Frame)) {
                d = new ImageDialog((Frame) w);
            } else if ((w != null) && (w instanceof Dialog)) {
                d = new ImageDialog((Dialog) w);
            }
        }
        return d;
    }

    protected TablePropertiesDialog createTablePropertiesDialog() {
        Component c = this.getCurrentEditor();
        TablePropertiesDialog d = null;
        if (c != null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if ((w != null) && (w instanceof Frame)) {
                d = new TablePropertiesDialog((Frame) w);
            } else if ((w != null) && (w instanceof Dialog)) {
                d = new TablePropertiesDialog((Dialog) w);
            }
        }

        return d;
    }

    protected ListDialog createListDialog() {
        Component c = this.getCurrentEditor();
        ListDialog d = null;
        if (c != null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if ((w != null) && (w instanceof Frame)) {
                d = new ListDialog((Frame) w);
            } else if ((w != null) && (w instanceof Dialog)) {
                d = new ListDialog((Dialog) w);
            }
        }

        return d;
    }

    protected void replace(Element elem, String html) {
        HTMLDocument document = null;
        try {
            document = (HTMLDocument) elem.getDocument();
        } catch (Exception ex) {
            HTMLElementPropertiesAction.logger.error(null, ex);
        }

        CompoundUndoManager.beginCompoundEdit(document);
        try {
            document.setOuterHTML(elem, html);
        } catch (Exception ex) {
            HTMLElementPropertiesAction.logger.error(null, ex);
        }
        CompoundUndoManager.endCompoundEdit(document);
    }

    @Override
    protected void updateContextState(JEditorPane ed) {
        int t = HTMLElementPropertiesAction.ELEM_PROPS;
        Element elem = HTMLElementPropertiesAction.elementAtCaretPosition(ed);
        if (elem != null) {
            t = HTMLElementPropertiesAction.getElementType(elem);
        }

        this.putValue("ID", HTMLElementPropertiesAction.PROPS[t]);
        this.putValue(Action.NAME, HTMLTextEditAction.i18n.str(HTMLElementPropertiesAction.PROPS[t]));
        // Messages.setMnemonic(PROPS[t], this); TODO this won't set the right
        // mnemonic
    }

    protected static int getElementType(Element elem) {

        if (elem == null) {
            return HTMLElementPropertiesAction.ELEM_PROPS;
        }

        AttributeSet att = elem.getAttributes();
        String name = att.getAttribute(StyleConstants.NameAttribute).toString();

        // is it an image?
        if (name.equals("img")) {
            return HTMLElementPropertiesAction.IMG_PROPS;
        }

        // is it a list?
        if (HTMLUtils.getParent(elem, HTML.Tag.UL) != null) {
            return HTMLElementPropertiesAction.LIST_PROPS;
        }

        if (HTMLUtils.getParent(elem, HTML.Tag.OL) != null) {
            return HTMLElementPropertiesAction.LIST_PROPS;
        }

        // is it a table?
        if (HTMLUtils.getParent(elem, HTML.Tag.TD) != null) {
            return HTMLElementPropertiesAction.TABLE_PROPS;
        }

        // return the default
        return HTMLElementPropertiesAction.ELEM_PROPS;
    }

    /**
     * Computes the (inline or block) element at the focused editor's caret position
     * @return the element, or null of the element cant be retrieved
     */
    protected static Element elementAtCaretPosition(JEditorPane ed) {
        if (ed == null) {
            return null;
        }

        HTMLDocument doc = (HTMLDocument) ed.getDocument();
        int caret = ed.getCaretPosition();

        Element elem = doc.getParagraphElement(caret);
        HTMLDocument.BlockElement blockElem = (HTMLDocument.BlockElement) elem;
        return blockElem.positionToElement(caret);
    }

    public static boolean isTableElement(JEditorPane ed) {
        Element elem = HTMLElementPropertiesAction.elementAtCaretPosition(ed);
        if (elem == null) {
            return false;
        }
        int type = HTMLElementPropertiesAction.getElementType(elem);

        if (type == HTMLElementPropertiesAction.TABLE_PROPS) {
            return true;
        }
        return false;
    }

}
