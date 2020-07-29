package com.ontimize.util.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;

public class MathExpressionParserFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MathExpressionParserFactory.class);

    public static final String MATH_EXPRESSION_PARSER_PROPERTY = "com.ontimize.util.math.MathExpressionParser";

    public static MathExpressionParser getInstance() {
        try {
            String type = System.getProperty(MathExpressionParserFactory.MATH_EXPRESSION_PARSER_PROPERTY);
            if (MathExpressionParser.JEP.equalsIgnoreCase(type)) {
                return new JEPMathExpressionParser();
            } else if (MathExpressionParser.JEP3x.equalsIgnoreCase(type)) {
                Class jep3x = Class.forName("com.ontimize.util.math.JEP3xMathExpressionParser");
                return (MathExpressionParser) jep3x.newInstance();
            } else if (MathExpressionParser.MESP.equalsIgnoreCase(type)) {
                return new MespMathExpressionParser();
            } else {
                // Detect JEP version
                try {
                    Class jep3x = Class.forName("com.singularsys.jep.Jep");
                    return (MathExpressionParser) jep3x.newInstance();
                } catch (ClassNotFoundException e) {
                    MathExpressionParserFactory.LOGGER.trace(null, e);
                    // This is not version 3.3
                    try {
                        Class.forName("org.nfunk.jep.JEP");
                        return new JEPMathExpressionParser();
                    } catch (ClassNotFoundException ex) {
                        MathExpressionParserFactory.LOGGER.trace(null, ex);
                        Class.forName("com.graphbuilder.math.FuncMap");
                        return new MespMathExpressionParser();
                    }
                }
            }
        } catch (Exception e) {
            MathExpressionParserFactory.LOGGER
                .error(MathExpressionParserFactory.class.getName() + ": No math parser library found");
            if (ApplicationManager.DEBUG) {
                MathExpressionParserFactory.LOGGER.error(null, e);
            }
        } catch (Error e) {
            MathExpressionParserFactory.LOGGER
                .error(MathExpressionParserFactory.class.getName() + ": No math parser library found");
            if (ApplicationManager.DEBUG) {
                MathExpressionParserFactory.LOGGER.error(null, e);
            }
        }
        return null;
    }

}
