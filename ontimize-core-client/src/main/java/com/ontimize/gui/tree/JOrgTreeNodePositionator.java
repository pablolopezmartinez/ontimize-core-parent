package com.ontimize.gui.tree;

import java.awt.Dimension;

public interface JOrgTreeNodePositionator {

    public void calculateNodePositions(JOrgTree tree);

    public boolean calculated();

    public Dimension getPreferredSize();

}
