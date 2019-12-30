package com.ontimize.util.rule;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.manager.IFormManager;

public class DefaultRuleEngineLoader implements RuleEngineLoader {

	private static final Logger	logger			= LoggerFactory.getLogger(DefaultRuleEngineLoader.class);

	public static String ruleEngineClass = "com.ontimize.util.rule.RuleEngine";

	private static Class ruleEngine = null;

	@Override
	public RuleEngine getRuleEngine(IFormManager fM) {
		try {
			if (DefaultRuleEngineLoader.ruleEngine == null) {
				DefaultRuleEngineLoader.ruleEngine = Class.forName(DefaultRuleEngineLoader.ruleEngineClass);
			}
			Object[] parameters = { fM };
			Class[] classes = { IFormManager.class };
			Constructor constructor = null;
			try {
				constructor = DefaultRuleEngineLoader.ruleEngine.getConstructor(classes);
			} catch (Exception e) {
				DefaultRuleEngineLoader.logger.trace(null, e);
			}
			return (RuleEngine) constructor.newInstance(parameters);
		} catch (Exception e) {
			DefaultRuleEngineLoader.logger.error(null, e);
			return null;
		}
	}
}
