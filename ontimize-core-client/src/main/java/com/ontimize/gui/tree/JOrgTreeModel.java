package com.ontimize.gui.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class JOrgTreeModel extends DefaultTreeModel {

	public JOrgTreeModel(JOrgTreeNode root) {
		super(root);
	}

	public JOrgTreeModel(JOrgTreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}

	@Override
	public void setRoot(TreeNode root) {
		if (!(root instanceof JOrgTreeNode)) {
			throw new IllegalArgumentException("Class must be JOrgTreeNode");
		} else {
			super.setRoot(root);
		}

	}
}
