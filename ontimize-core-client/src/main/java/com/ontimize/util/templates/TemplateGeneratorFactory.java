package com.ontimize.util.templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TemplateGeneratorFactory {

    private static final Logger logger = LoggerFactory.getLogger(TemplateGeneratorFactory.class);

    public static final String WORD = "word.templates";

    public static final String PDF = "pdf.templates";

    public static final String OPEN_OFFICE = "open.office.templates";

    public static final String DOCX = "docx.templates";

    /**
     * Creates a TemplateGenerator object for the specified template type
     * @param type Template type. Values are: {@link #WORD}, {@link #PDF} or {@link #OPEN_OFFICE}
     * @return
     */
    public static TemplateGenerator templateGeneratorInstance(String type) {
        if (type == null) {
            return null;
        }
        if (type.equals(TemplateGeneratorFactory.WORD)) {
            TemplateGenerator temp = new WordTemplateGenerator();
            return temp;
        } else if (type.equals(TemplateGeneratorFactory.PDF)) {
            return new PdfTemplateGenerator();
        } else if (type.equals(TemplateGeneratorFactory.OPEN_OFFICE)) {
            return new ODFTemplateGenerator();
        } else if (type.equals(TemplateGeneratorFactory.DOCX)) {
            return new DocxTemplateGenerator();
        }
        return null;
    }

    public static boolean hasTemplateGenerator(String type) {
        if (type == null) {
            return false;
        }
        try {
            if (TemplateGeneratorFactory.OPEN_OFFICE.equals(type)) {
                return ODFTemplateGenerator.checkLibraries();
            } else {
                TemplateGenerator templateGenerator = TemplateGeneratorFactory.templateGeneratorInstance(type);
                if (templateGenerator != null) {
                    return true;
                }
            }
        } catch (Exception error) {
            TemplateGeneratorFactory.logger.trace(null, error);
            return false;
        }
        return false;
    }

}
