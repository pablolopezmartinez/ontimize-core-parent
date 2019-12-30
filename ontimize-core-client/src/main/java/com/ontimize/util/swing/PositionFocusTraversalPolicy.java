package com.ontimize.util.swing;

import javax.swing.LayoutFocusTraversalPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionFocusTraversalPolicy extends LayoutFocusTraversalPolicy {

	static final Logger logger = LoggerFactory.getLogger(PositionFocusTraversalPolicy.class);

	public PositionFocusTraversalPolicy() {
		super();
		this.setComparator(new PositionComparator());
	}

}
