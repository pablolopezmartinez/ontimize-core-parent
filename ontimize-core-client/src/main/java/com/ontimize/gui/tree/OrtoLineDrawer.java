package com.ontimize.gui.tree;

import java.awt.Graphics;

public class OrtoLineDrawer implements JOrgTreeLineDrawer {

	public OrtoLineDrawer() {}

	@Override
	public void drawLines(JOrgTree tree, Graphics g) {
		JOrgTreeModel model = tree.getInnerModel();
		JOrgTreeNode root = (JOrgTreeNode) model.getRoot();
		this.drawLines(tree, root, g);
	}

	protected void drawLines(JOrgTree tree, JOrgTreeNode node, Graphics g) {
		int iChildCount = node.getChildCount();
		for (int i = 0; i < iChildCount; i++) {
			JOrgTreeNode childNode = (JOrgTreeNode) node.getChildAt(i);
			this.drawLine(tree, node, childNode, g);
			this.drawLines(tree, childNode, g);
		}
	}

	protected void drawLine(JOrgTree tree, JOrgTreeNode parentNode, JOrgTreeNode childNode, Graphics g) {
		int rendererHeight = tree.getRendererHeight();
		int rendererWidth = tree.getRendererWidth();
		int orientation = tree.getOrientation();

		if (orientation == JOrgTree.ORIENTATION_UP_DOWN) {
			int xParentNode = parentNode.getX() + (rendererWidth / 2);
			int yParentNode = parentNode.getY() + rendererHeight;

			int xChildNode = childNode.getX() + (rendererWidth / 2);
			int yChildNode = childNode.getY();

			int yInt = (yParentNode + yChildNode) / 2;

			g.drawLine(xParentNode, yParentNode, xParentNode, yInt);
			g.drawLine(xParentNode, yInt, xChildNode, yInt);
			g.drawLine(xChildNode, yInt, xChildNode, yChildNode);

		} else if (orientation == JOrgTree.ORIENTATION_DOWN_UP) {
			int xParentNode = parentNode.getX() + (rendererWidth / 2);
			int yParentNode = parentNode.getY();

			int xChildNode = childNode.getX() + (rendererWidth / 2);
			int yChildNode = childNode.getY() + rendererHeight;

			int yInt = (yParentNode + yChildNode) / 2;

			// g.drawLine(xParentNode, yParentNode, xChildNode, yChildNode);

			g.drawLine(xParentNode, yParentNode, xParentNode, yInt);
			g.drawLine(xParentNode, yInt, xChildNode, yInt);
			g.drawLine(xChildNode, yInt, xChildNode, yChildNode);

		} else if (orientation == JOrgTree.ORIENTATION_LEFT_RIGHT) {
			int xParentNode = parentNode.getX() + rendererWidth;
			int yParentNode = parentNode.getY() + (rendererHeight / 2);

			int xChildNode = childNode.getX();
			int yChildNode = childNode.getY() + (rendererHeight / 2);

			int xInt = (xParentNode + xChildNode) / 2;

			g.drawLine(xParentNode, yParentNode, xInt, yParentNode);
			g.drawLine(xInt, yParentNode, xInt, yChildNode);
			g.drawLine(xInt, yChildNode, xChildNode, yChildNode);
		} else if (orientation == JOrgTree.ORIENTATION_RIGHT_LEFT) {
			int xParentNode = parentNode.getX();
			int yParentNode = parentNode.getY() + (rendererHeight / 2);

			int xChildNode = childNode.getX() + rendererWidth;
			int yChildNode = childNode.getY() + (rendererHeight / 2);

			int xInt = (xParentNode + xChildNode) / 2;

			g.drawLine(xParentNode, yParentNode, xInt, yParentNode);
			g.drawLine(xInt, yParentNode, xInt, yChildNode);
			g.drawLine(xInt, yChildNode, xChildNode, yChildNode);
		}
	}

}
