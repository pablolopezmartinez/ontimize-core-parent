package com.ontimize.gui.tree;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

public class NotCompactPositionator implements JOrgTreeNodePositionator {

	protected boolean calculated = false;

	protected Dimension preferredSize = new Dimension(0, 0);

	public NotCompactPositionator() {}

	@Override
	public Dimension getPreferredSize() {
		if (this.calculated) {
			return this.preferredSize;
		} else {
			return new Dimension(0, 0);
		}
	}

	@Override
	public void calculateNodePositions(JOrgTree tree) {
		this.calculated = false;
		int orientation = tree.getOrientation();
		int hMargin = tree.getHMargin();
		int vMargin = tree.getVMargin();
		int siblingSeparation = tree.getSiblingSeparation();
		int levelSeparation = tree.getLevelSeparation();
		int rendererHeight = tree.getRendererHeight();
		int rendererWidth = tree.getRendererWidth();

		JOrgTreeNode root = (JOrgTreeNode) tree.getInnerModel().getRoot();
		Enumeration enumNodes = root.preorderEnumeration();
		Vector vLevels = new Vector();
		Vector vLeafNodes = new Vector();
		Vector vNodes = new Vector();
		vLevels = new Vector();

		while (enumNodes.hasMoreElements()) {
			JOrgTreeNode jtNode = (JOrgTreeNode) enumNodes.nextElement();
			jtNode.setX(0);
			jtNode.setY(0);
			int level = jtNode.getLevel();
			if (level == vLevels.size()) {
				vLevels.add(new Vector());
			}
			((Vector) vLevels.get(level)).add(jtNode);

			if (jtNode.isLeaf()) {
				vLeafNodes.add(jtNode);
			}

			vNodes.add(jtNode);
		}

		if ((orientation == JOrgTree.ORIENTATION_UP_DOWN) || (orientation == JOrgTree.ORIENTATION_DOWN_UP)) {

			this.preferredSize.height = ((2 * vMargin) + (vLevels.size() * (rendererHeight + levelSeparation))) - levelSeparation;
			this.preferredSize.width = ((2 * hMargin) + (vLeafNodes.size() * (rendererWidth + siblingSeparation))) - siblingSeparation;

			// Put the nodes from left to right
			int currentLevel = vLevels.size() - 1;
			for (int i = 0; i < vLeafNodes.size(); i++) {
				JOrgTreeNode jtNode = (JOrgTreeNode) vLeafNodes.get(i);
				jtNode.setY(vMargin + (jtNode.getLevel() * (rendererHeight + levelSeparation)));
				jtNode.setX(hMargin + (i * (rendererWidth + siblingSeparation)));
			}

			// Put leaf nodes from currenLevel-1 to 0
			for (int i = currentLevel - 1; i >= 0; i--) {
				Vector levelI = (Vector) vLevels.get(i);
				for (int j = 0; j < levelI.size(); j++) {
					JOrgTreeNode node = (JOrgTreeNode) levelI.get(j);
					if (!node.isLeaf()) {
						node.setY(vMargin + (i * (rendererHeight + levelSeparation)));
						int childrenCount = node.getChildCount();
						if (childrenCount == 1) {
							node.setX(((JOrgTreeNode) node.getChildAt(0)).getX());
						} else {
							int xChildMin = ((JOrgTreeNode) node.getChildAt(0)).getX();
							int xChildMax = ((JOrgTreeNode) node.getChildAt(childrenCount - 1)).getX() + rendererWidth;
							node.setX(((xChildMin + xChildMax) - rendererWidth) / 2);
						}
					}
				}
			}

			if (orientation == JOrgTree.ORIENTATION_DOWN_UP) {
				for (int i = 0; i < vNodes.size(); i++) {
					JOrgTreeNode jtNode = (JOrgTreeNode) vNodes.get(i);
					jtNode.setY(this.preferredSize.height - jtNode.getY() - rendererHeight);
				}
			}
		} else if ((orientation == JOrgTree.ORIENTATION_LEFT_RIGHT) || (orientation == JOrgTree.ORIENTATION_RIGHT_LEFT)) {

			this.preferredSize.width = ((2 * hMargin) + (vLevels.size() * (rendererWidth + levelSeparation))) - levelSeparation;
			this.preferredSize.height = ((2 * vMargin) + (vLeafNodes.size() * (rendererHeight + siblingSeparation))) - siblingSeparation;

			// Put left nodes from top to bottom
			int currentLevel = vLevels.size() - 1;
			for (int i = 0; i < vLeafNodes.size(); i++) {
				JOrgTreeNode node = (JOrgTreeNode) vLeafNodes.get(i);
				node.setX(hMargin + (node.getLevel() * (rendererWidth + levelSeparation)));
				node.setY(vMargin + (i * (rendererHeight + siblingSeparation)));
			}

			// Put the node that are not leaf from currentLevel-1 to 0
			for (int i = currentLevel - 1; i >= 0; i--) {
				Vector levelI = (Vector) vLevels.get(i);
				for (int j = 0; j < levelI.size(); j++) {
					JOrgTreeNode jtNode = (JOrgTreeNode) levelI.get(j);
					if (!jtNode.isLeaf()) {

						jtNode.setX(hMargin + (i * (rendererWidth + levelSeparation)));

						int childrenCount = jtNode.getChildCount();
						if (childrenCount == 1) {
							jtNode.setY(((JOrgTreeNode) jtNode.getChildAt(0)).getY());
						} else {
							int yChildMin = ((JOrgTreeNode) jtNode.getChildAt(0)).getY();
							int yChildMax = ((JOrgTreeNode) jtNode.getChildAt(childrenCount - 1)).getY() + rendererHeight;
							jtNode.setY(((yChildMin + yChildMax) - rendererHeight) / 2);
						}
					}
				}
			}

			if (orientation == JOrgTree.ORIENTATION_RIGHT_LEFT) {
				for (int i = 0; i < vNodes.size(); i++) {
					JOrgTreeNode jtNode = (JOrgTreeNode) vNodes.get(i);
					jtNode.setX(this.preferredSize.width - jtNode.getX() - rendererWidth);
				}
			}
		}

		this.calculated = true;
	}

	@Override
	public boolean calculated() {
		return this.calculated;
	}

}
