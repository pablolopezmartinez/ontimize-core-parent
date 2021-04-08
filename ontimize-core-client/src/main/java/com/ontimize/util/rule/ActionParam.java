package com.ontimize.util.rule;

import com.ontimize.util.rule.RuleParser.Attributes;

public class ActionParam implements IActionParam {

    private String paramName;

    private String paramValue;

    public ActionParam() {
        this.paramName = new String();
        this.paramValue = new String();
    }

    public ActionParam(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    /**
     * @param paramName the paramName to set
     */
    @Override
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * @return the paramName
     */
    @Override
    public String getParamName() {
        return this.paramName;
    }

    /**
     * @param paramValue the paramValue to set
     */
    @Override
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    /**
     * @return the paramValue
     */
    @Override
    public String getParamValue() {
        return this.paramValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RuleParser.openTag(Attributes.PARAM_NAME));
        sb.append(this.paramName);
        sb.append(RuleParser.closeTag(Attributes.PARAM_NAME));
        sb.append(RuleParser.openTag(Attributes.PARAM_VALUE));
        sb.append(this.paramValue);
        sb.append(RuleParser.closeTag(Attributes.PARAM_VALUE));
        return sb.toString();
    }

}
