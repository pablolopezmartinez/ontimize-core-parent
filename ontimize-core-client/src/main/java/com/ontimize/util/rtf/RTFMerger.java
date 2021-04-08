package com.ontimize.util.rtf;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.util.rtf.style.RTFDocument;

public class RTFMerger {

    private static final Logger logger = LoggerFactory.getLogger(RTFMerger.class);

    public static String mergeRTFStrings(List rtfContents) {
        RTFEditorKit editor = new RTFEditorKit();
        RTFDocument doc = new RTFDocument();
        for (int i = 0; i < rtfContents.size(); i++) {
            String currentRTFContent = (String) rtfContents.get(i);
            try {
                editor.read(new StringReader(currentRTFContent), doc, doc.getLength());
            } catch (Exception e) {
                RTFMerger.logger.error(null, e);
            }
        }
        OutputStream out = new ByteArrayOutputStream();
        try {
            editor.write(out, doc, 0, doc.getLength());
        } catch (Exception e) {
            RTFMerger.logger.error(null, e);
        }
        return out.toString();
    }

    public static OutputStream mergeRTFStreams(List rtfContents) {
        RTFEditorKit editor = new RTFEditorKit();
        RTFDocument doc = new RTFDocument();
        for (int i = 0; i < rtfContents.size(); i++) {
            InputStream currentRTFInput = (InputStream) rtfContents.get(i);
            try {
                editor.read(currentRTFInput, doc, doc.getLength());
            } catch (Exception e) {
                RTFMerger.logger.error(null, e);
            }
        }
        OutputStream out = new ByteArrayOutputStream();
        try {
            editor.write(out, doc, 0, doc.getLength());
        } catch (Exception e) {
            RTFMerger.logger.error(null, e);
        }
        return out;
    }

}
