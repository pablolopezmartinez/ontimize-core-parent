package com.ontimize.util.rule;

import com.ontimize.util.rule.RuleParser.Attributes;

public class Condition implements ICondition {

    private String expression;

    public Condition() {
    }

    public Condition(String expression) {
        this.expression = expression;
    }

    /**
     * @param expression the expression to set
     */
    @Override
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * @return the expression
     */
    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RuleParser.openTag(Attributes.CONDITION));
        if (this.expression != null) {
            sb.append(RuleParser.generateCDATA(this.expression));
        }
        sb.append(RuleParser.closeTag(Attributes.CONDITION));
        return sb.toString();
    }

}
