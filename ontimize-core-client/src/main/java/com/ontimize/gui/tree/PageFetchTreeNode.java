package com.ontimize.gui.tree;

import java.util.Hashtable;

public class PageFetchTreeNode extends OTreeNode {

	public PageFetchTreeNode(Hashtable params) {
		super(params);
	}

	@Override
	public boolean isLeaf() {
		return super.isLeaf();
	}

	@Override
	protected void updateNodeTextCache() {
		this.cachedText = "more...";
		this.setUserObject(this.cachedText);
	}

	@Override
	public int compareTo(Object object) {
		return +1;
	}
}
