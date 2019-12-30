package com.ontimize.gui.field.html.utils;

import java.io.Writer;

import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLWriter;

/**
 * Writer for writing the html of a specified element only.
 *
 * @author Imatia S.L.
 *
 */
public class ElementWriter extends HTMLWriter {

	protected Element root;

	protected static int getStartPos(Element root, int start) {
		if ((start >= root.getStartOffset()) && (start <= root.getEndOffset())) {
			return start;
		}
		return root.getStartOffset();
	}

	protected static int getEndPos(Element root, int end) {
		if ((end >= root.getStartOffset()) && (end <= root.getEndOffset())) {
			return end;
		}
		return root.getEndOffset();
	}

	public ElementWriter(Writer out, Element root) {
		this(out, root, root.getStartOffset(), root.getEndOffset());
	}

	public ElementWriter(Writer w, HTMLDocument doc, int pos, int len) {
		super(w, doc, pos, len);
		this.setLineLength(Integer.MAX_VALUE);
	}

	public ElementWriter(Writer out, Element root, int startPos, int endPos) {
		super(out, (HTMLDocument) root.getDocument(), ElementWriter.getStartPos(root, startPos),
				Math.min(root.getDocument().getLength(), ElementWriter.getEndPos(root, endPos) - ElementWriter.getStartPos(root, startPos)));

		this.root = root;

		// setIndentSpace(0);
		this.setLineLength(Integer.MAX_VALUE);
	}

	@Override
	protected boolean synthesizedElement(Element e) {
		return (e.getStartOffset() < this.getStartOffset()) || ElementWriter.isAncestor(e, this.root) || super.synthesizedElement(e);
	}

	protected static boolean isAncestor(Element a, Element d) {
		for (Element e = d.getParentElement(); e != null; e = e.getParentElement()) {
			if (e == a) {
				return true;
			}
		}

		return false;
	}
}