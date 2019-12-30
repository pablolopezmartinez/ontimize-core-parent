package com.ontimize.util.rtf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

import com.ontimize.util.rtf.style.RTFDocument;

public class RTFEditorKit extends StyledEditorKit {

	@Override
	public Object clone() {
		return new RTFEditorKit();
	}

	@Override
	public String getContentType() {
		return "text/rtf";
	}

	@Override
	public void read(InputStream in, Document doc, int pos) throws IOException, BadLocationException {
		RTFReader db = new RTFReader(doc);
		InputStreamReader isr = new InputStreamReader(in);
		db.read(isr, pos);
	}

	@Override
	public void read(Reader in, Document doc, int pos) throws IOException, BadLocationException {
		RTFReader db = new RTFReader(doc);
		db.read(in, pos);
	}

	@Override
	public void write(OutputStream out, Document doc, int pos, int len) throws IOException, BadLocationException {
		OutputStreamWriter w = new OutputStreamWriter(out);
		RTFGenerator writer = new RTFGenerator(doc);
		writer.write(w, pos, len);
	}

	@Override
	public void write(Writer out, Document doc, int pos, int len) throws IOException, BadLocationException {
		RTFGenerator writer = new RTFGenerator(doc);
		writer.write(out, pos, len);
	}

	public void write(String fileName, Document doc) throws IOException, BadLocationException {
		RTFGenerator writer = new RTFGenerator(doc);
		writer.write(fileName);
	}

	@Override
	public Document createDefaultDocument() {
		RTFDocument doc = new RTFDocument();
		return doc;
	}

	@Override
	public ViewFactory getViewFactory() {
		return new com.ontimize.util.rtf.view.RTFViewFactory();
	}

	public String printDoc(Reader in) throws IOException {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[] buffer) method. We iterate until the Reader return -1 which means there's no more data to read. We use
		 * the StringWriter class to produce the string.
		 */
		BufferedReader br = new BufferedReader(in);
		String nextLine = "";
		StringBuilder sb = new StringBuilder();
		while ((nextLine = br.readLine()) != null) {
			sb.append(nextLine);
			//
			// note:
			// BufferedReader strips the EOL character
			// so we add a new one!
			//
		}
		return sb.toString();
	}

}
