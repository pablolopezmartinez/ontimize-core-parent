package com.ontimize.gui.tree;

import java.awt.Component;

public interface JOrgTreeCellRenderer {

    public abstract Component getJOrgTreeCellRendererComponent(JOrgTree jorgtree, Object value, boolean selected,
            int row, boolean hasFocus);

}
