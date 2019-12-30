package com.ontimize.gui.tree;

import java.util.Collections;
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComparableDefaultMutableTreeNode extends DefaultMutableTreeNode implements Comparable {

	private static final Logger logger = LoggerFactory.getLogger(ComparableDefaultMutableTreeNode.class);

	protected Comparator comparableDefaultMutableTreeNodeComparator = new Comparator<TreeNode>() {

		@Override
		public int compare(TreeNode o1, TreeNode o2) {
			if (o1 instanceof ComparableDefaultMutableTreeNode) {
				return ((ComparableDefaultMutableTreeNode) o1).compareTo(o2);
			}
			return -1;
		}

	};

	public ComparableDefaultMutableTreeNode(Object o) {
		super(o);
	}

	public ComparableDefaultMutableTreeNode() {
		super();
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof DefaultMutableTreeNode) {
			Object userObject = ((DefaultMutableTreeNode) o).getUserObject();
			if (userObject == null) {
				return -1;
			} else {
				if (this.userObject == null) {
					return 1;
				}
				return this.userObject.toString().compareTo(userObject.toString());
			}
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void sort() {
		try {
			Collections.sort(this.children, this.comparableDefaultMutableTreeNodeComparator);
		} catch (Exception e) {
			ComparableDefaultMutableTreeNode.logger.error(null, e);
		}
	}

	public ComparableDefaultMutableTreeNode cloneComparableDefaultMutableTreeNode() {
		return new ComparableDefaultMutableTreeNode(this.userObject);
	}
}