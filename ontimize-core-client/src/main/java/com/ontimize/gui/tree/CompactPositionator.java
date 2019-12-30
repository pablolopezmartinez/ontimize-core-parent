package com.ontimize.gui.tree;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompactPositionator implements JOrgTreeNodePositionator {

	private static final Logger	logger			= LoggerFactory.getLogger(CompactPositionator.class);

	protected boolean calculated = false;

	protected Dimension preferredSize = new Dimension(0, 0);

	protected static boolean DEBUG = false;

	public CompactPositionator() {}

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
			int nivel = jtNode.getLevel();
			if (nivel == vLevels.size()) {
				vLevels.add(new Vector());
			}
			((Vector) vLevels.get(nivel)).add(jtNode);

			if (jtNode.isLeaf()) {
				vLeafNodes.add(jtNode);
			}

			vNodes.add(jtNode);
		}

		if ((orientation == JOrgTree.ORIENTATION_UP_DOWN) || (orientation == JOrgTree.ORIENTATION_DOWN_UP)) {

			this.preferredSize.height = ((2 * vMargin) + (vLevels.size() * (rendererHeight + levelSeparation))) - levelSeparation;
			this.preferredSize.width = ((2 * hMargin) + (vLeafNodes.size() * (rendererWidth + siblingSeparation))) - siblingSeparation;

			// Put leaf nodes from left to right
			int currentLevel = vLevels.size() - 1;
			for (int i = 0; i < vLeafNodes.size(); i++) {
				JOrgTreeNode jtNode = (JOrgTreeNode) vLeafNodes.get(i);
				jtNode.setY(vMargin + (jtNode.getLevel() * (rendererHeight + levelSeparation)));
				jtNode.setX(hMargin + (i * (rendererWidth + siblingSeparation)));
			}

			// Put the not leaf nodes from curren level - 1 to 0
			for (int i = currentLevel - 1; i >= 0; i--) {
				Vector vLevelI = (Vector) vLevels.get(i);
				for (int j = 0; j < vLevelI.size(); j++) {
					JOrgTreeNode jtNode = (JOrgTreeNode) vLevelI.get(j);
					if (!jtNode.isLeaf()) {

						jtNode.setY(vMargin + (i * (rendererHeight + levelSeparation)));

						int childrenCount = jtNode.getChildCount();
						if (childrenCount == 1) {
							jtNode.setX(((JOrgTreeNode) jtNode.getChildAt(0)).getX());
						} else {
							int xChildMin = ((JOrgTreeNode) jtNode.getChildAt(0)).getX();
							int xChildMax = ((JOrgTreeNode) jtNode.getChildAt(childrenCount - 1)).getX() + rendererWidth;
							jtNode.setX(((xChildMin + xChildMax) - rendererWidth) / 2);
						}
					}
				}
			}

			this.compactTreeVertically(root, vLevels, rendererWidth, siblingSeparation);

			// Center the tree horizontally
			this.centerTreeVertically(root, hMargin);

			// Recalculate preferedSize.width
			this.recalculatePreferredWidth(root, hMargin, rendererWidth);

			if (orientation == JOrgTree.ORIENTATION_DOWN_UP) {
				for (int i = 0; i < vNodes.size(); i++) {
					JOrgTreeNode jtNode = (JOrgTreeNode) vNodes.get(i);
					jtNode.setY(this.preferredSize.height - jtNode.getY() - rendererHeight);
				}
			}
		} else if ((orientation == JOrgTree.ORIENTATION_LEFT_RIGHT) || (orientation == JOrgTree.ORIENTATION_RIGHT_LEFT)) {

			this.preferredSize.width = ((2 * hMargin) + (vLevels.size() * (rendererWidth + levelSeparation))) - levelSeparation;
			this.preferredSize.height = ((2 * vMargin) + (vLeafNodes.size() * (rendererHeight + siblingSeparation))) - siblingSeparation;

			// Put leaf nodes from top to bottom.
			int currentLevel = vLevels.size() - 1;
			for (int i = 0; i < vLeafNodes.size(); i++) {
				JOrgTreeNode jtNode = (JOrgTreeNode) vLeafNodes.get(i);
				jtNode.setX(hMargin + (jtNode.getLevel() * (rendererWidth + levelSeparation)));
				jtNode.setY(vMargin + (i * (rendererHeight + siblingSeparation)));
			}

			// Put the not leaf nodes from current level - 1 to 0
			for (int i = currentLevel - 1; i >= 0; i--) {
				Vector vLevelI = (Vector) vLevels.get(i);
				for (int j = 0; j < vLevelI.size(); j++) {
					JOrgTreeNode jtNode = (JOrgTreeNode) vLevelI.get(j);
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

			this.compactTreeHorizontally(root, vLevels, rendererHeight, siblingSeparation);

			// Center the tree horizontally
			this.centerTreeHorizontally(root, vMargin);

			// Recalculate preferedSize.width
			this.recalculatePreferredHeight(root, vMargin, rendererHeight);

			if (orientation == JOrgTree.ORIENTATION_RIGHT_LEFT) {
				for (int i = 0; i < vNodes.size(); i++) {
					JOrgTreeNode node = (JOrgTreeNode) vNodes.get(i);
					node.setX(this.preferredSize.width - node.getX() - rendererWidth);
				}
			}
		}

		this.calculated = true;
	}

	public void centerTreeVertically(JOrgTreeNode root, int hMargin) {
		Enumeration enumNode = root.preorderEnumeration();
		int xMin = hMargin;
		while (enumNode.hasMoreElements()) {
			JOrgTreeNode node = (JOrgTreeNode) enumNode.nextElement();
			xMin = Math.min(xMin, node.getX());
		}

		if (xMin < hMargin) {
			int offset = hMargin - xMin;
			enumNode = root.preorderEnumeration();
			while (enumNode.hasMoreElements()) {
				JOrgTreeNode node = (JOrgTreeNode) enumNode.nextElement();
				node.setX(node.getX() + offset);
			}
		}
	}

	public void centerTreeHorizontally(JOrgTreeNode root, int vMargin) {
		Enumeration enumNode = root.preorderEnumeration();
		int yMin = vMargin;
		while (enumNode.hasMoreElements()) {
			JOrgTreeNode jtNode = (JOrgTreeNode) enumNode.nextElement();
			yMin = Math.min(yMin, jtNode.getY());
		}

		if (yMin < vMargin) {
			int offset = vMargin - yMin;
			enumNode = root.preorderEnumeration();
			while (enumNode.hasMoreElements()) {
				JOrgTreeNode node = (JOrgTreeNode) enumNode.nextElement();
				node.setY(node.getY() + offset);
			}
		}
	}

	protected void recalculatePreferredWidth(JOrgTreeNode root, int hMargin, int rendererWidth) {
		Enumeration enumNode = root.preorderEnumeration();
		int xMax = 0;
		while (enumNode.hasMoreElements()) {
			JOrgTreeNode node = (JOrgTreeNode) enumNode.nextElement();
			xMax = Math.max(xMax, node.getX());
		}
		this.preferredSize.width = xMax + rendererWidth + hMargin;
	}

	protected void recalculatePreferredHeight(JOrgTreeNode root, int vMargin, int rendererHeight) {
		Enumeration enumNode = root.preorderEnumeration();
		int yMax = 0;
		while (enumNode.hasMoreElements()) {
			JOrgTreeNode jtNode = (JOrgTreeNode) enumNode.nextElement();
			yMax = Math.max(yMax, jtNode.getY());
		}
		this.preferredSize.height = yMax + rendererHeight + vMargin;
	}

	public void compactTreeVertically(JOrgTreeNode root, Vector levels, int rendererWidth, int siblingSeparation) {
		int childrenCount = root.getChildCount();

		// Compact the subtrees
		for (int i = 0; i < childrenCount; i++) {
			this.compactTreeVertically((JOrgTreeNode) root.getChildAt(i), levels, rendererWidth, siblingSeparation);
		}

		// Try to move subtrees to the left except the first one
		// Start with the second on the left
		for (int i = 1; i < childrenCount; i++) {
			JOrgTreeNode childNode = (JOrgTreeNode) root.getChildAt(i);
			this.compactSubTreeLeft(childNode, levels, rendererWidth, siblingSeparation);
		}

		// Recalculate the root node position
		if (childrenCount == 1) {
			root.setX(((JOrgTreeNode) root.getChildAt(0)).getX());
		} else if (childrenCount > 1) {
			int xChildMin = ((JOrgTreeNode) root.getChildAt(0)).getX();
			int xChildMax = ((JOrgTreeNode) root.getChildAt(childrenCount - 1)).getX() + rendererWidth;
			root.setX(((xChildMin + xChildMax) - rendererWidth) / 2);
		}

	}

	public void compactTreeHorizontally(JOrgTreeNode root, Vector levels, int rendererHeight, int siblingSeparation) {
		int iChildCount = root.getChildCount();

		// Compact subtrees
		for (int i = 0; i < iChildCount; i++) {
			this.compactTreeHorizontally((JOrgTreeNode) root.getChildAt(i), levels, rendererHeight, siblingSeparation);
		}

		// Try to move subtrees to the left except the first one
		// Start with the second on the top
		for (int i = 1; i < iChildCount; i++) {
			JOrgTreeNode childNode = (JOrgTreeNode) root.getChildAt(i);
			this.compactSubTreeUp(childNode, levels, rendererHeight, siblingSeparation);
		}

		// Recalculate the root node position
		if (iChildCount == 1) {
			root.setY(((JOrgTreeNode) root.getChildAt(0)).getY());
		} else if (iChildCount > 1) {
			int yChildMin = ((JOrgTreeNode) root.getChildAt(0)).getY();
			int yChildMax = ((JOrgTreeNode) root.getChildAt(iChildCount - 1)).getY() + rendererHeight;
			root.setY(((yChildMin + yChildMax) - rendererHeight) / 2);
		}

	}

	public void compactSubTreeLeft(JOrgTreeNode root, Vector levels, int rendererWidth, int siblingSeparation) {
		Vector vLeftNodes = this.getLeftNodes(root);
		boolean bRightNode = false;
		int horizontalMinimumDistance = 0;

		if (CompactPositionator.DEBUG) {
			CompactPositionator.logger.debug("COMPACTING SUBTREE TO THE LEFT: " + root);
		}

		for (int i = 0; i < vLeftNodes.size(); i++) {
			JOrgTreeNode jtNode = (JOrgTreeNode) vLeftNodes.get(i);
			int nivel = jtNode.getLevel();
			Vector vLevelNodes = (Vector) levels.get(nivel);

			int iIndex = vLevelNodes.indexOf(jtNode);
			if (iIndex > 0) {
				JOrgTreeNode jtLeftNode = (JOrgTreeNode) vLevelNodes.get(iIndex - 1);
				int horizontalDistance = jtNode.getX() - (jtLeftNode.getX() + rendererWidth);
				if (!bRightNode) {
					bRightNode = true;
					horizontalMinimumDistance = horizontalDistance;
				} else {
					horizontalMinimumDistance = Math.min(horizontalMinimumDistance, horizontalDistance);
				}
			}
		}

		if (CompactPositionator.DEBUG) {
			CompactPositionator.logger.debug(" horizontal minimum distance: " + horizontalMinimumDistance + " siblingSeparation: " + siblingSeparation);
		}

		// If bRightNode is true then move to the left until
		// horizontalMinimumDistance=siblingSeparation
		if (bRightNode && (horizontalMinimumDistance > siblingSeparation)) {
			int offset = -(horizontalMinimumDistance - siblingSeparation);

			Enumeration enumNodes = root.preorderEnumeration();
			while (enumNodes.hasMoreElements()) {
				JOrgTreeNode jtNode = (JOrgTreeNode) enumNodes.nextElement();
				jtNode.setX(jtNode.getX() + offset);
				if (CompactPositionator.DEBUG) {
					CompactPositionator.logger.debug("  offset applied: " + offset);
				}
			}
		}
	}

	public void compactSubTreeUp(JOrgTreeNode root, Vector levels, int rendererHeight, int siblingSeparation) {
		// Left is bottom and right is top
		// For left to right orientation
		Vector vTopNodes = this.getLeftNodes(root);
		boolean bTopNode = false;
		int minimumVerticalDistance = 0;

		if (CompactPositionator.DEBUG) {
			CompactPositionator.logger.debug("COMPACTING SUBTREE UPWARDS: " + root);
		}

		for (int i = 0; i < vTopNodes.size(); i++) {
			JOrgTreeNode jtNode = (JOrgTreeNode) vTopNodes.get(i);
			int nivel = jtNode.getLevel();
			Vector vLevelNodes = (Vector) levels.get(nivel);

			int index = vLevelNodes.indexOf(jtNode);
			if (index > 0) {
				JOrgTreeNode topNode = (JOrgTreeNode) vLevelNodes.get(index - 1);
				int verticalDistance = jtNode.getY() - (topNode.getY() + rendererHeight);
				if (!bTopNode) {
					bTopNode = true;
					minimumVerticalDistance = verticalDistance;
				} else {
					minimumVerticalDistance = Math.min(minimumVerticalDistance, verticalDistance);
				}
			}
		}

		// Move up until minimumVerticalDistance=siblingSeparation
		// only if bTopNode is true
		if (bTopNode && (minimumVerticalDistance > siblingSeparation)) {
			int offset = -(minimumVerticalDistance - siblingSeparation);

			Enumeration enumNodes = root.preorderEnumeration();
			while (enumNodes.hasMoreElements()) {
				JOrgTreeNode jtNode = (JOrgTreeNode) enumNodes.nextElement();
				jtNode.setY(jtNode.getY() + offset);
				if (CompactPositionator.DEBUG) {
					CompactPositionator.logger.debug(" offset applied: " + offset);
				}
			}
		}
	}

	public Vector getLeftNodes(JOrgTreeNode root) {
		Vector v = new Vector();
		int rootLevel = root.getLevel();

		Vector vLevels = new Vector();
		Enumeration enumNodes = root.preorderEnumeration();
		while (enumNodes.hasMoreElements()) {
			JOrgTreeNode jtNode = (JOrgTreeNode) enumNodes.nextElement();
			int level = jtNode.getLevel() - rootLevel;
			if (level == vLevels.size()) {
				vLevels.add(new Vector());
			}
			((Vector) vLevels.get(level)).add(jtNode);
		}

		if (CompactPositionator.DEBUG) {
			CompactPositionator.logger.debug(this.getClass().getName() + "getLeftNodes: node: " + root + " levels: " + vLevels);
		}

		for (int i = 0; i < vLevels.size(); i++) {
			Vector nivelI = (Vector) vLevels.get(i);
			v.add(nivelI.get(0));
		}
		if (CompactPositionator.DEBUG) {
			CompactPositionator.logger.debug(this.getClass().getName() + "getLeftNodes: node: " + root + " left nodes: " + v);
		}
		return v;
	}

	public Vector getRightNodes(JOrgTreeNode root) {
		Vector v = new Vector();
		int rootLevel = root.getLevel();

		Vector vLevels = new Vector();
		Enumeration enumNodes = root.preorderEnumeration();
		while (enumNodes.hasMoreElements()) {
			JOrgTreeNode jtNode = (JOrgTreeNode) enumNodes.nextElement();
			int level = jtNode.getLevel() - rootLevel;
			if (level == vLevels.size()) {
				vLevels.add(new Vector());
			}
			((Vector) vLevels.get(level)).add(jtNode);
		}

		for (int i = 0; i < vLevels.size(); i++) {
			Vector vLevelI = (Vector) vLevels.get(i);
			v.add(vLevelI.get(vLevelI.size() - 1));
		}

		return v;
	}

	@Override
	public boolean calculated() {
		return this.calculated;
	}

}
