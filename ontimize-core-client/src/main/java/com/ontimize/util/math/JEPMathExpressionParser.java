package com.ontimize.util.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;

public class JEPMathExpressionParser implements MathExpressionParser {

    private static final Logger logger = LoggerFactory.getLogger(JEPMathExpressionParser.class);

    private Object parser;

    private final String parserClass = "org.nfunk.jep.JEP";

    public JEPMathExpressionParser() {
        try {
            this.parser = new org.nfunk.jep.JEP();
        } catch (Exception e) {
            if (ApplicationManager.DEBUG) {
                JEPMathExpressionParser.logger
                    .debug("Error creating JEP parser. Check whether jep library is included in build path.", e);
            }
        }
    }

    @Override
    public void addVariable(String arg0, double arg1) {
        try {
            if (this.parser != null) {
                ((org.nfunk.jep.JEP) this.parser).addVariable(arg0, arg1);
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
    }

    @Override
    public void parseExpression(String expression) {
        try {
            if (this.parser != null) {
                ((org.nfunk.jep.JEP) this.parser).parseExpression(expression);
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
    }

    @Override
    public void addVariableAsObject(String var, Object o) {
        try {
            if (this.parser != null) {
                ((org.nfunk.jep.JEP) this.parser).addVariableAsObject(var, o);
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
    }

    @Override
    public boolean hasError() {
        try {
            if (this.parser != null) {
                return ((org.nfunk.jep.JEP) this.parser).hasError();
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
        return true;
    }

    @Override
    public Object getValueAsObject() {
        try {
            if (this.parser != null) {
                return ((org.nfunk.jep.JEP) this.parser).getValueAsObject();
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
        return null;
    }

    @Override
    public String getErrorInfo() {
        try {
            if (this.parser != null) {
                return ((org.nfunk.jep.JEP) this.parser).getErrorInfo();
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
        return null;
    }

    @Override
    public void addFunction(String key, Object function) {
        try {
            if ((this.parser != null) && (function != null)
                    && (function instanceof org.nfunk.jep.function.PostfixMathCommandI)) {
                ((org.nfunk.jep.JEP) this.parser).addFunction(key,
                        (org.nfunk.jep.function.PostfixMathCommandI) function);
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
    }

    @Override
    public void addStandardFunctions() {
        try {
            if (this.parser != null) {
                ((org.nfunk.jep.JEP) this.parser).addStandardFunctions();
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
    }

    @Override
    public void addStandardConstants() {
        try {
            if (this.parser != null) {
                ((org.nfunk.jep.JEP) this.parser).addStandardConstants();
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
    }

    @Override
    public void removeVariable(String var) {
        try {
            if (this.parser != null) {
                ((org.nfunk.jep.JEP) this.parser).removeVariable(var);
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
    }

    @Override
    public void setTraverse(boolean value) {
        try {
            if (this.parser != null) {
                ((org.nfunk.jep.JEP) this.parser).setTraverse(value);
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
    }

    @Override
    public double getValue() {
        try {
            if (this.parser != null) {
                return ((org.nfunk.jep.JEP) this.parser).getValue();
            }
        } catch (Exception e) {
            JEPMathExpressionParser.logger.error(null, e);
        }
        return 0D;
    }

    public Object getParser() {
        return this.parser;
    }

    public String getParserClass() {
        return this.parserClass;
    }

}
