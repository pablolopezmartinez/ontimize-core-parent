package com.ontimize.util.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.ByteArrayInputStream;

import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.swing.NaiveUserAgent;
import org.xhtmlrenderer.swing.ScalableXHTMLPanel;

public class AdvancedHTMLViewer extends BasicHTMLViewer {

	private static final Logger				logger				= LoggerFactory.getLogger(AdvancedHTMLViewer.class);

	protected ScalableXHTMLPanel view = null;
	protected NaiveUserAgent manager = null;

	protected JScrollPane scroll = null;

	private DocumentBuilder documentBuilder = null;

	private final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

	public AdvancedHTMLViewer() {
		super();
		this.manager = new NaiveUserAgent();
		this.view = new ScalableXHTMLPanel(this.manager);
		this.view.addDocumentListener(this.manager);
		this.view.setCenteredPagedView(true);
		this.scroll = new FSScrollPane(this.view);

		try {
			this.documentBuilder = this.docBuilderFactory.newDocumentBuilder();
		} catch (Exception ex) {
			AdvancedHTMLViewer.logger.error(null, ex);
		}

		this.setLayout(new GridBagLayout());

		this.add(this.scroll, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 2, 2));

	}

	@Override
	public void setHTML(String html) {
		if ((html == null) || "".equals(html)) {
			this.view.removeAll();
		} else {
			Document d = AdvancedHTMLViewer.htmlToDocument(this.documentBuilder, html);
			this.view.setDocument(d);
		}
	}

	public static Document htmlToDocument(DocumentBuilder documentBuilder, String htmltext) {
		Tidy tidy = new Tidy();
		tidy.setXHTML(false);
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(htmltext.getBytes());
			return tidy.parseDOM(bis, null);
		} catch (Exception ex) {
			AdvancedHTMLViewer.logger.error(null, ex);
		}
		return null;
	}
}
