package com.ontimize.util.templates;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.ontimize.util.FileUtils;
import com.ontimize.util.pdf.PdfFiller;

public class PdfTemplateGenerator extends AbstractTemplateGenerator implements TemplateGenerator {

    protected boolean showTemplate = true;

    /**
     * Template creation in PDF format is not supported
     */
    @Override
    public File createTemplate(Hashtable fieldValues, Hashtable valuesTable, Hashtable valuesImages) {
        throw new RuntimeException("It isn't supported");
    }

    @Override
    public File fillDocument(InputStream input, String nameFile, Hashtable fieldValues, Hashtable valuesTable,
            Hashtable valuesImages, Hashtable valuesPivotTable)
            throws Exception {
        File directory = FileUtils.createTempDirectory();
        File template = new File(directory.getAbsolutePath(), FileUtils.getFileName(nameFile));
        Vector imageField = new Vector();

        if ((valuesImages != null) && !valuesImages.isEmpty()) {
            Enumeration enu = valuesImages.keys();
            while (enu.hasMoreElements()) {
                Object key = enu.nextElement();
                Object value = valuesImages.get(key);
                fieldValues.put(key, value);
                imageField.add(key);
            }
        }

        PdfFiller.fillTextImageFields(input, new FileOutputStream(template), fieldValues, imageField, true);
        if (this.showTemplate) {
            com.ontimize.windows.office.WindowsUtils.openFile_Script(template);
        }
        return template;
    }

    @Override
    public void setShowTemplate(boolean show) {
        this.showTemplate = show;
    }

    @Override
    public List queryTemplateFields(String template) throws Exception {
        File templateFile = new File(template);
        if (templateFile.exists()) {
            return this.queryTemplateFields(templateFile);
        } else {
            throw new Exception("File " + template + " not found.");
        }

    }

    @Override
    public List queryTemplateFields(File template) throws Exception {
        FileInputStream pdfInputStream = new FileInputStream(template);

        ByteArrayOutputStream baOut = new ByteArrayOutputStream();
        BufferedInputStream bInput = new BufferedInputStream(pdfInputStream);
        for (int a = 0; (a = bInput.read()) != -1;) {
            baOut.write(a);
        }
        byte buffer[] = baOut.toByteArray();
        PdfReader reader = new PdfReader(buffer);
        AcroFields form = reader.getAcroFields();
        HashMap fields = form.getFields();
        Iterator names = fields.keySet().iterator();
        List result = new Vector();
        while (names.hasNext()) {
            result.add(names.next());
        }
        return result;
    }

}
