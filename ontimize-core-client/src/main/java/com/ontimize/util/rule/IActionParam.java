package com.ontimize.util.rule;

public interface IActionParam {

	public void setParamName(String paramName);

	public String getParamName();

	public void setParamValue(String paramValue);

	public String getParamValue();

	@Override
	public String toString();
}
