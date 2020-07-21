package com.ontimize.gui.tree;

import java.awt.Graphics;

public class BeeLineDrawer implements JOrgTreeLineDrawer {

    public BeeLineDrawer() {
    }

    @Override
    public void drawLines(JOrgTree tree, Graphics g) {
        JOrgTreeModel model = tree.getInnerModel();
        JOrgTreeNode root = (JOrgTreeNode) model.getRoot();
        this.drawLines(tree, root, g);
    }

    protected void drawLines(JOrgTree tree, JOrgTreeNode node, Graphics g) {
        int childrenCount = node.getChildCount();
        for (int i = 0; i < childrenCount; i++) {
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

            g.drawLine(xParentNode, yParentNode, xChildNode, yChildNode);

        } else if (orientation == JOrgTree.ORIENTATION_DOWN_UP) {
            int xParentNode = parentNode.getX() + (rendererWidth / 2);
            int yParentNode = parentNode.getY();

            int xChildNode = childNode.getX() + (rendererWidth / 2);
            int yChildNode = childNode.getY() + rendererHeight;

            g.drawLine(xParentNode, yParentNode, xChildNode, yChildNode);

        } else if (orientation == JOrgTree.ORIENTATION_LEFT_RIGHT) {
            int xParentNode = parentNode.getX() + rendererWidth;
            int yParentNode = parentNode.getY() + (rendererHeight / 2);

            int xChildNode = childNode.getX();
            int yChildNode = childNode.getY() + (rendererHeight / 2);

            g.drawLine(xParentNode, yParentNode, xChildNode, yChildNode);

        } else if (orientation == JOrgTree.ORIENTATION_RIGHT_LEFT) {
            int xParentNode = parentNode.getX();
            int yParentNode = parentNode.getY() + (rendererHeight / 2);

            int xChildNode = childNode.getX() + rendererWidth;
            int yChildNode = childNode.getY() + (rendererHeight / 2);

            g.drawLine(xParentNode, yParentNode, xChildNode, yChildNode);

        }
    }

}
