package com.ontimize.gui.field.html.actions;

import java.awt.event.ActionEvent;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.field.html.HTMLUtils;
import com.ontimize.gui.field.html.utils.CompoundUndoManager;
import com.ontimize.gui.field.html.utils.ElementWriter;

/**
 * Action which properly inserts breaks for an HTMLDocument
 *
 * @author Imatia S.L.
 *
 */
public class EnterKeyAction extends DecoratedTextAction {

    private static final Logger logger = LoggerFactory.getLogger(EnterKeyAction.class);

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new EnterKeyAction.
     * @param defaultEnterAction Should be the default action
     */
    public EnterKeyAction(Action defaultEnterAction) {
        super("EnterAction", defaultEnterAction);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JEditorPane editor;
        HTMLDocument document;

        try {
            editor = (JEditorPane) this.getTextComponent(e);
            document = (HTMLDocument) editor.getDocument();
        } catch (ClassCastException ex) {
            EnterKeyAction.logger.trace(null, ex);
            // don't know what to do with this type
            // so pass off the event to the delegate
            this.delegate.actionPerformed(e);
            return;
        }

        Element elem = document.getParagraphElement(editor.getCaretPosition());
        Element parentElem = elem.getParentElement();
        HTML.Tag tag = HTML.getTag(elem.getName());
        HTML.Tag parentTag = HTML.getTag(parentElem.getName());
        int caret = editor.getCaretPosition();

        CompoundUndoManager.beginCompoundEdit(document);
        try {
            if (HTMLUtils.isImplied(elem)) {
                // are we inside a list item?
                if (parentTag.equals(HTML.Tag.LI)) {
                    // does the list item have any contents
                    if ((parentElem.getEndOffset() - parentElem.getStartOffset()) > 1) {
                        String txt = "";
                        // caret at start of listitem
                        if (caret == parentElem.getStartOffset()) {
                            document.insertBeforeStart(parentElem, this.toListItem(txt));
                        } // caret in the middle of list item content
                        else if ((caret < (parentElem.getEndOffset() - 1)) && (caret > parentElem.getStartOffset())) {
                            int len = parentElem.getEndOffset() - caret;
                            txt = document.getText(caret, len);
                            caret--;// hmmm
                            document.insertAfterEnd(parentElem, this.toListItem(txt));
                            document.remove(caret, len);
                        } else// caret at end of list item
                        {
                            document.insertAfterEnd(parentElem, this.toListItem(txt));
                        }

                        editor.setCaretPosition(caret + 1);
                    } else// empty list item
                    {
                        Element listParentElem = HTMLUtils.getListParent(parentElem).getParentElement();

                        if (this.isListItem(HTML.getTag(listParentElem.getName())))// nested
                                                                                   // list
                        {
                            HTML.Tag listParentTag = HTML.getTag(HTMLUtils.getListParent(listParentElem).toString());
                            /*
                             * HTMLEditorKit.InsertHTMLTextAction a = new HTMLEditorKit.InsertHTMLTextAction("insert",
                             * "", listParentTag, HTML.Tag.LI); a.actionPerformed(e);
                             */
                            int start = parentElem.getStartOffset();

                            Element nextElem = HTMLUtils.getNextElement(document, parentElem);

                            int len = nextElem.getEndOffset() - start;

                            String ml = HTMLUtils.getElementHTML(listParentElem, true);

                            ml = ml.replaceFirst("\\<li\\>\\s*\\<\\/li\\>\\s*\\<\\/ul\\>", "</ul>");
                            ml = ml.replaceFirst("\\<ul\\>\\s*\\<\\/ul\\>", "");

                            document.setOuterHTML(listParentElem, ml);
                            // document.remove(start, len);
                            // HTMLUtils.removeElement(elem);

                        } // are we directly under a table cell?
                        else if (listParentElem.getName().equals("td")) {
                            // reset the table cell contents nested in a <div>
                            // we do this because otherwise the next table cell
                            // would
                            // get deleted!! Perhaps this is a bug in swing's
                            // html implemenation?
                            this.encloseInDIV(listParentElem, document);
                            editor.setCaretPosition(caret + 1);
                        } else // end the list
                        {
                            if (this.isInList(listParentElem)) {
                                HTML.Tag listParentTag = HTML
                                    .getTag(HTMLUtils.getListParent(listParentElem).toString());
                                HTMLEditorKit.InsertHTMLTextAction a = new HTMLEditorKit.InsertHTMLTextAction("insert",
                                        "<li></li>", listParentTag, HTML.Tag.LI);
                                a.actionPerformed(e);
                            } else {
                                HTML.Tag root = HTML.Tag.BODY;
                                if (HTMLUtils.getParent(elem, HTML.Tag.TD) != null) {
                                    root = HTML.Tag.TD;
                                }

                                HTMLEditorKit.InsertHTMLTextAction a = new HTMLEditorKit.InsertHTMLTextAction("insert",
                                        "<p></p>", root, HTML.Tag.P);
                                a.actionPerformed(e);
                            }

                            HTMLUtils.removeElement(parentElem);
                        }
                    }
                } else // not a list
                {
                    if (parentTag.isPreformatted()) {
                        this.insertImpliedBR(e);
                    } else if (parentTag.equals(HTML.Tag.TD)) {
                        this.encloseInDIV(parentElem, document);
                        editor.setCaretPosition(caret + 1);
                    } else if (parentTag.equals(HTML.Tag.BODY) || this.isInList(elem)) {
                        this.insertParagraphAfter(elem, editor);
                    } else {
                        this.insertParagraphAfter(parentElem, editor);
                    }
                }
            } else // not implied
            {
                // we need to check for this here in case any straggling li's
                // or dd's exist
                if (this.isListItem(tag)) {
                    if ((elem.getEndOffset() - editor.getCaretPosition()) == 1) {
                        // caret at end of para
                        editor.replaceSelection("\n ");
                        editor.setCaretPosition(editor.getCaretPosition() - 1);
                    } else {
                        this.delegate.actionPerformed(e);
                    }
                } else {
                    // elem.getName());
                    this.insertParagraphAfter(elem, editor);
                }
            }
        } catch (Exception ex) {
            EnterKeyAction.logger.error(null, ex);
        }
        CompoundUndoManager.endCompoundEdit(document);
    }

    protected boolean isListItem(HTML.Tag t) {
        return t.equals(HTML.Tag.LI) || t.equals(HTML.Tag.DT) || t.equals(HTML.Tag.DD);
    }

    protected String toListItem(String txt) {
        return "<li>" + txt + "</li>";
    }

    protected boolean isInList(Element el) {
        return HTMLUtils.getListParent(el) != null;
    }

    protected void insertImpliedBR(ActionEvent e) {
        HTMLEditorKit.InsertHTMLTextAction hta = new HTMLEditorKit.InsertHTMLTextAction("insertBR", "<br>",
                HTML.Tag.IMPLIED, HTML.Tag.BR);
        hta.actionPerformed(e);
    }

    protected void encloseInDIV(Element elem, HTMLDocument document) throws Exception {
        HTML.Tag tag = HTML.getTag(elem.getName());
        String html = HTMLUtils.getElementHTML(elem, false);
        html = HTMLUtils.createTag(tag, elem.getAttributes(), "<div>" + html + "</div><div></div>");

        document.setOuterHTML(elem, html);
    }

    /**
     * Inserts a paragraph after the current paragraph of the same type
     * @param elem
     * @param editor
     * @throws BadLocationException
     * @throws java.io.IOException
     */
    protected void insertParagraphAfter(Element elem, JEditorPane editor)
            throws BadLocationException, java.io.IOException {
        int cr = editor.getCaretPosition();
        HTMLDocument document = (HTMLDocument) elem.getDocument();
        HTML.Tag t = HTML.getTag(elem.getName());
        int endOffs = elem.getEndOffset();
        int startOffs = elem.getStartOffset();

        // if this is an implied para, make the new para a div
        if ((t == null) || elem.getName().equals("p-implied")) {
            t = HTML.Tag.DIV;
        }

        String html;
        // got to test for this here, otherwise <hr> and <br>
        // get duplicated
        if (cr == startOffs) {
            html = this.createBlock(t, elem, "");
        } else // split the current para at the cursor position
        {
            StringWriter out = new StringWriter();
            ElementWriter w = new ElementWriter(out, elem, startOffs, cr);
            w.write();
            html = this.createBlock(t, elem, out.toString());
        }

        if (cr == (endOffs - 1)) {
            html += this.createBlock(t, elem, "");
        } else {
            StringWriter out = new StringWriter();
            ElementWriter w = new ElementWriter(out, elem, cr, endOffs);
            w.write();
            html += this.createBlock(t, elem, out.toString());
        }

        // copy the current para's character attributes
        AttributeSet chAttribs;
        if ((endOffs > startOffs) && (cr == (endOffs - 1))) {
            chAttribs = new SimpleAttributeSet(document.getCharacterElement(cr - 1).getAttributes());
        } else {
            chAttribs = new SimpleAttributeSet(document.getCharacterElement(cr).getAttributes());
        }

        document.setOuterHTML(elem, html);

        cr++;
        Element p = document.getParagraphElement(cr);
        if (cr == endOffs) {
            // update the character attributes for the added paragraph
            // FIXME If the added paragraph is at the start/end
            // of the document, the char attrs dont get set
            this.setCharAttribs(p, chAttribs);
        }

        editor.setCaretPosition(p.getStartOffset());
    }

    protected String createBlock(HTML.Tag t, Element elem, String html) {
        AttributeSet attribs = elem.getAttributes();
        return HTMLUtils.createTag(t, attribs, HTMLUtils.removeEnclosingTags(elem, html));
    }

    protected void setCharAttribs(Element p, AttributeSet chAttribs) {
        HTMLDocument document = (HTMLDocument) p.getDocument();
        int start = p.getStartOffset();
        int end = p.getEndOffset();

        SimpleAttributeSet sas = new SimpleAttributeSet(chAttribs);
        sas.removeAttribute(HTML.Attribute.SRC);
        // if the charattribs contains a br, hr, or img attribute, it'll erase
        // any content in the paragraph
        boolean skipAttribs = false;
        for (Enumeration ee = sas.getAttributeNames(); ee.hasMoreElements();) {
            Object n = ee.nextElement();
            String val = chAttribs.getAttribute(n).toString();
            skipAttribs = val.equals("br") || val.equals("hr") || val.equals("img");
        }

        if (!skipAttribs) {
            document.setCharacterAttributes(start, end - start, sas, true);
        }
    }

}
