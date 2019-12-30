package com.ontimize.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class CustomRowGridBagLayout extends GridBagLayout {

	public CustomRowGridBagLayout() {
		super();

		this.defaultConstraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
				0);
	}

	@Override
	public void addLayoutComponent(Component componente, Object constraints) {
		if (constraints instanceof GridBagConstraints) {
			((GridBagConstraints) constraints).gridx = GridBagConstraints.RELATIVE;
			((GridBagConstraints) constraints).gridy = 0;
			((GridBagConstraints) constraints).weighty = 1;
		}
		super.addLayoutComponent(componente, constraints);
	}

}
