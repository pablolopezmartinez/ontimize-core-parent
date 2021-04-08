package com.ontimize.util.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.ExpressionTree;

public class MespMathExpressionParser implements MathExpressionParser {

    private static final Logger logger = LoggerFactory.getLogger(MespMathExpressionParser.class);

    protected com.graphbuilder.math.FuncMap funcMap = new com.graphbuilder.math.FuncMap();

    protected com.graphbuilder.math.VarMap varMap = new com.graphbuilder.math.VarMap();

    protected Expression expression = null;

    protected String errorInfo = null;

    protected boolean error = false;

    public MespMathExpressionParser() {
        this.error = false;
    }

    @Override
    public void addFunction(String key, Object function) {
        try {
            if ((this.funcMap != null) && (function != null)
                    && (function instanceof com.graphbuilder.math.func.Function)) {
                this.funcMap.setFunction(key, (com.graphbuilder.math.func.Function) function);
            }
        } catch (Exception e) {
            MespMathExpressionParser.logger.error(null, e);
            this.error = true;
            this.errorInfo = e.getMessage();
        }
    }

    @Override
    public void addStandardConstants() {

    }

    @Override
    public void addStandardFunctions() {
        try {
            if (this.funcMap != null) {
                this.funcMap.loadDefaultFunctions();
            }
        } catch (Exception e) {
            MespMathExpressionParser.logger.trace(null, e);
            this.error = true;
            this.errorInfo = e.toString();
        }
    }

    @Override
    public void addVariable(String var, double value) {
        try {
            if (this.varMap != null) {
                this.varMap.setValue(var, value);
            }
        } catch (Exception e) {
            MespMathExpressionParser.logger.trace(null, e);
            this.error = true;
            this.errorInfo = e.toString();
        }
    }

    @Override
    public void addVariableAsObject(String var, Object o) {
        try {
            if ((o == null) || !(o instanceof Number)) {
                return;
            }
            Number current = (Number) o;
            this.addVariable(var, current.doubleValue());
        } catch (Exception e) {
            MespMathExpressionParser.logger.trace(null, e);
            this.error = true;
            this.errorInfo = e.toString();
        }
    }

    @Override
    public String getErrorInfo() {
        if (this.error) {
            return this.errorInfo;
        }
        return null;
    }

    @Override
    public double getValue() {
        try {
            if (this.expression != null) {
                double d = this.expression.eval(this.varMap, this.funcMap);
                this.error = false;
                return d;
            } else {
                this.error = true;
                this.errorInfo = "Expression is null";
            }
        } catch (Exception e) {
            MespMathExpressionParser.logger.trace(null, e);
            this.error = true;
            this.errorInfo = e.toString();
        }
        return 0;
    }

    @Override
    public Object getValueAsObject() {
        try {
            double d = this.getValue();
            if (this.error) {
                return null;
            } else {
                return new Double(d);
            }
        } catch (Exception ex) {
            MespMathExpressionParser.logger.trace(null, ex);
            this.error = true;
            this.errorInfo = ex.toString();
        }
        return null;
    }

    @Override
    public boolean hasError() {
        return this.error;
    }

    @Override
    public void parseExpression(String expression) {
        try {
            this.expression = ExpressionTree.parse(expression);
        } catch (Exception exc) {
            MespMathExpressionParser.logger.trace(null, exc);
            this.error = true;
            this.errorInfo = exc.toString();
        }
    }

    @Override
    public void removeVariable(String var) {
        if (this.varMap != null) {
            this.varMap.remove(var);
        }
    }

    @Override
    public void setTraverse(boolean value) {
    }

}
