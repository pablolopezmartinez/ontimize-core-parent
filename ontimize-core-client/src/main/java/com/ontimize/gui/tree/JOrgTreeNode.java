package com.ontimize.gui.tree;

import java.awt.Point;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class JOrgTreeNode extends DefaultMutableTreeNode {

	// Left top corner referenced to the same corner in the rectangle that
	// contains the tree
	protected int x;

	protected int y;

	protected TreeNode node;

	@Override
	public String toString() {
		String string;
		if (this.getUserObject() != null) {
			string = this.getUserObject().toString();
		} else {
			if (this.node != null) {
				string = this.node.toString();
			} else {
				string = this.x + " - " + this.y;
			}
		}

		return string;
	}

	public JOrgTreeNode(Object userObject) {
		super(userObject);
		this.x = 0;
		this.y = 0;
	}

	public JOrgTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		this.x = 0;
		this.y = 0;
	}

	public void setTreeNode(TreeNode node) {
		this.node = node;
	}

	public TreeNode getTreeNode() {
		return this.node;
	}

	@Override
	public void add(MutableTreeNode newChild) {
		if (!(newChild instanceof JOrgTreeNode)) {
			throw new IllegalArgumentException("newChild must be JOrgTreeNode");
		} else {
			super.add(newChild);
		}
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Point getPos() {
		return new Point(this.x, this.y);
	}

	public void setPos(Point newPos) {
		this.x = (int) newPos.getX();
		this.y = (int) newPos.getY();
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void applyOffset(int xOffset, int yOffset) {
		this.x += xOffset;
		this.y += yOffset;
	}

}
