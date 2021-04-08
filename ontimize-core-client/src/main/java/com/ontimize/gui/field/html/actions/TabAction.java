package com.ontimize.gui.field.html.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.field.html.HTMLUtils;

/**
 * Tab action for tabbing between table cells
 *
 * @author Imatia S.L.
 *
 */
public class TabAction extends DecoratedTextAction {

    private static final Logger logger = LoggerFactory.getLogger(TabAction.class);

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int FORWARD = 0;

    public static final int BACKWARD = 1;

    protected int type;

    public TabAction(int type, Action defaultTabAction) {
        super("tabAction", defaultTabAction);
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JEditorPane editor;
        HTMLDocument document;

        editor = (JEditorPane) this.getTextComponent(e);
        document = (HTMLDocument) editor.getDocument();
        Element elem = document.getParagraphElement(editor.getCaretPosition());
        Element tdElem = HTMLUtils.getParent(elem, HTML.Tag.TD);
        if (tdElem != null) {
            try {
                if (this.type == TabAction.FORWARD) {
                    editor.setCaretPosition(tdElem.getEndOffset());
                } else {
                    editor.setCaretPosition(tdElem.getStartOffset() - 1);
                }
            } catch (IllegalArgumentException ex) {
                TabAction.logger.error(null, ex);
            }
        } else {
            this.delegate.actionPerformed(e);
        }
    }

}
