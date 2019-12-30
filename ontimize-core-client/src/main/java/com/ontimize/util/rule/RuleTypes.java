package com.ontimize.util.rule;

import java.util.Arrays;
import java.util.List;

import com.ontimize.util.rule.RuleParser.Attributes;

public class RuleTypes {

	protected static List dataComponentEventTypes = Arrays.asList(new String[] { RuleParser.Attributes.VALUE_TYPE_EVENT });

	protected static List buttonEventTypes = Arrays.asList(new String[] { RuleParser.Attributes.ACTION_TYPE_EVENT });

	protected static List listEventTypes = Arrays.asList(new String[] { RuleParser.Attributes.SELECTION_TYPE_EVENT });

	protected static List formEventTypes = Arrays.asList(new String[] { RuleParser.Attributes.FORM_TYPE_EVENT });

	protected static List actionTypes = Arrays.asList(new String[] { RuleParser.Attributes.SET_VALUE_ACTION, RuleParser.Attributes.SET_ENABLED_ACTION,
			RuleParser.Attributes.SET_REQUIRED_ACTION, RuleParser.Attributes.SHOW_MESSAGE_ACTION });

	protected static List setValueActionParameters = Arrays.asList(new String[] {});

	protected static List showMessageActionParameters = Arrays.asList(new String[] { "msg" });

	protected static List openDetailFormActionParameters = Arrays.asList(new String[] {});

	protected static List changeModeActionParameters = Arrays.asList(new String[] { Attributes.MODE });

	protected static List setRequiredActionParameters = Arrays.asList(new String[] {});

	protected static List setEnabledActionParameters = Arrays.asList(new String[] {});

}
