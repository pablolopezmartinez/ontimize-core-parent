package com.ontimize.util.math;

import org.apache.commons.jexl2.JexlEngine;

public class JexlExpressionParser {

	private static JexlEngine jexlEngine;

	public static JexlEngine getJexlEngine() {
		if (JexlExpressionParser.jexlEngine == null) {
			JexlExpressionParser.jexlEngine = new JexlEngine();
			JexlExpressionParser.jexlEngine.setCache(512);
			JexlExpressionParser.jexlEngine.setLenient(false);
			JexlExpressionParser.jexlEngine.setStrict(false);
		}
		return JexlExpressionParser.jexlEngine;
	}

}
