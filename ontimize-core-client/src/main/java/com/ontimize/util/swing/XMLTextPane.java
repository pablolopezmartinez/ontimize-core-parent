package com.ontimize.util.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.util.swing.text.XMLEditorKit;

public class XMLTextPane extends JTextPane {

	private static final Logger	logger				= LoggerFactory.getLogger(XMLTextPane.class);

	private static final long serialVersionUID = 6270183148379328084L;

	public XMLTextPane() {
		// Set editor kit
		this.setEditorKitForContentType("text/xml", new XMLEditorKit());
		this.setContentType("text/xml");

		this.addKeyListener(new IndentKeyListener());
	}

	private class IndentKeyListener implements KeyListener {

		private boolean enterFlag;
		private final char NEW_LINE = '\n';

		@Override
		public void keyPressed(KeyEvent event) {
			this.enterFlag = false;
			if ((event.getKeyCode() == KeyEvent.VK_ENTER) && (event.getModifiers() == 0)) {
				if (XMLTextPane.this.getSelectionStart() == XMLTextPane.this.getSelectionEnd()) {
					this.enterFlag = true;
					event.consume();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent event) {
			if ((event.getKeyCode() == KeyEvent.VK_ENTER) && (event.getModifiers() == 0)) {
				if (this.enterFlag) {
					event.consume();

					int start, end;
					String text = XMLTextPane.this.getText();

					int caretPosition = XMLTextPane.this.getCaretPosition();
					try {
						if (text.charAt(caretPosition) == this.NEW_LINE) {
							caretPosition--;
						}
					} catch (IndexOutOfBoundsException e) {
						XMLTextPane.logger.trace(null, e);
					}

					start = text.lastIndexOf(this.NEW_LINE, caretPosition) + 1;
					end = start;
					try {
						if (text.charAt(start) != this.NEW_LINE) {
							while ((end < text.length()) && Character.isWhitespace(text.charAt(end)) && (text.charAt(end) != this.NEW_LINE)) {
								end++;
							}
							if (end > start) {
								XMLTextPane.this.getDocument().insertString(XMLTextPane.this.getCaretPosition(), this.NEW_LINE + text.substring(start, end), null);
							} else {
								XMLTextPane.this.getDocument().insertString(XMLTextPane.this.getCaretPosition(), new String(new char[] { this.NEW_LINE }), null);
							}
						} else {
							XMLTextPane.this.getDocument().insertString(XMLTextPane.this.getCaretPosition(), new String(new char[] { this.NEW_LINE }), null);
						}
					} catch (IndexOutOfBoundsException e) {
						XMLTextPane.logger.trace(null, e);
						try {
							XMLTextPane.this.getDocument().insertString(XMLTextPane.this.getCaretPosition(), new String(new char[] { this.NEW_LINE }), null);
						} catch (BadLocationException e1) {
							XMLTextPane.logger.error(null, e1);
						}
					} catch (BadLocationException e) {
						XMLTextPane.logger.error(null, e);
					}
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {}
	}

}
