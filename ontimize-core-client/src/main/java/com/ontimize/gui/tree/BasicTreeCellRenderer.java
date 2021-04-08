package com.ontimize.gui.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a renderer for a tree node to show Date and <code>Timestamp</code> data type with the
 * appropriate format in function of the current locale . All the other data types are shown as
 * <code>String</code>.
 *
 * @since 5.2000 Default implementation
 * @since 5.2060EN Added pageable expression to the number of records. So, there are two formats:
 *        <ul>
 *        <li>Normal tree: (num_total_records) i.e. (8)
 *        <li>Pageable tree: (current_downloaded_records/num_total_records) i.e. (6/8)
 *        </ul>
 * @author Imatia Innovation SL
 */
public class BasicTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Logger logger = LoggerFactory.getLogger(BasicTreeCellRenderer.class);

    /**
     * The name of class. Used by L&F to put UI properties.
     *
     * @since 5.2062EN
     */
    public static final String TREECELLRENDERER_NAME = "Tree.cellRenderer";

    public static final String NULL = "NULL";

    protected Hashtable icons = new Hashtable();

    protected JTree[] trees = new JTree[0];

    protected boolean parentSelected;

    protected int childCount = -1;

    protected boolean leaf;

    protected int row;

    public static boolean firstNodeConfiguration = false;

    protected boolean root;

    public static Color organizationalForegroundColor = Color.BLUE;

    public static Color rootNodeForegroundColor;

    public static Color rootNodeSelectionForegroundColor;

    public static boolean includeChildCount = true;

    protected class ImgObserver implements java.awt.image.ImageObserver {

        @Override
        public boolean imageUpdate(java.awt.Image img, int flags, int x, int y, int w, int h) {
            if ((flags & (ImageObserver.FRAMEBITS | ImageObserver.ALLBITS)) != 0) {
                for (int i = 0; i < BasicTreeCellRenderer.this.trees.length; i++) {
                    if (BasicTreeCellRenderer.this.trees[i].isShowing()) {
                        BasicTreeCellRenderer.this.trees[i]
                            .repaint(BasicTreeCellRenderer.this.trees[i].getVisibleRect());
                    }
                }
            }
            return (flags & (ImageObserver.ALLBITS | ImageObserver.ABORT)) == 0;
        }

    }

    protected ImgObserver observer = null;

    protected String iconAttribute = null;

    public BasicTreeCellRenderer() {
        this.setOpaque(false);
    }

    @Override
    public String getName() {
        return BasicTreeCellRenderer.TREECELLRENDERER_NAME;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
    }

    public boolean isParentSelected() {
        return this.parentSelected;
    }

    public int getChildCount() {
        return this.childCount;
    }

    public int getRow() {
        return this.row;
    }

    protected void calculeIsParentSelected(JTree tree, int row) {
        TreePath[] paths = tree.getSelectionPaths();
        TreePath current = tree.getPathForRow(row);

        if ((paths != null) && (paths.length > 0) && (current != null)) {
            for (int i = 0; i < paths.length; i++) {
                TreePath path = paths[i];
                if ((current.getParentPath() != null) && current.getParentPath().equals(path)) {
                    this.parentSelected = true;
                    return;
                }
            }
        }
        this.parentSelected = false;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public boolean isLeaf() {
        return this.leaf;
    }

    public BasicTreeCellRenderer(String value, InputStream inProperties) throws Exception {
        this.iconAttribute = value;
        this.load(inProperties);
    }

    protected void load(InputStream inProperties) throws Exception {
        Properties props = new Properties();
        props.load(inProperties);
        this.observer = new ImgObserver();
        // Put icons in a cache
        ClassLoader cl = this.getClass().getClassLoader();
        Enumeration enumKeys = props.propertyNames();
        while (enumKeys.hasMoreElements()) {
            String sKey = (String) enumKeys.nextElement();
            String uri = props.getProperty(sKey);
            URL url = cl.getResource(uri);
            if (url == null) {
                BasicTreeCellRenderer.logger.debug(this.getClass().toString() + ": Not found " + uri);
            } else {
                ImageIcon icon = new ImageIcon(url);
                icon.setImageObserver(this.observer);
                this.icons.put(sKey, icon);
            }
        }
    }

    public BasicTreeCellRenderer(String value, String uriProperties) throws Exception {
        this.iconAttribute = value;
        URL url = this.getClass().getClassLoader().getResource(uriProperties);
        if (url == null) {
            throw new Exception("Not found " + uriProperties);
        }
        this.load(url.openStream());
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
    }

    protected Font cacheFont;

    protected void configureFirstNode(DefaultTreeCellRenderer cellRenderer, JTree tree, boolean isRoot,
            boolean selected) {
        if (isRoot) {
            if (BasicTreeCellRenderer.rootNodeForegroundColor != null) {
                if (selected && (BasicTreeCellRenderer.rootNodeSelectionForegroundColor != null)) {
                    cellRenderer.setForeground(BasicTreeCellRenderer.rootNodeSelectionForegroundColor);
                } else {
                    cellRenderer.setForeground(BasicTreeCellRenderer.rootNodeForegroundColor);
                }

            }

            String text = cellRenderer.getText().toUpperCase();
            cellRenderer.setText(text);
            if (this.cacheFont == null) {
                this.cacheFont = cellRenderer.getFont();
                Font currentFont = this.cacheFont.deriveFont(this.cacheFont.getSize2D() + 2.0f);
                currentFont = currentFont.deriveFont(Font.BOLD);
                cellRenderer.setFont(currentFont);
            }
        } else {
            if (this.cacheFont != null) {
                cellRenderer.setFont(this.cacheFont);
                this.cacheFont = null;
            }
        }
    }

    protected void configureTreeCellRenderer(JTree jTree, OTreeNode n, Component component, Object value,
            boolean selected) {

        Icon icon = n.getIcon();
        if (this.iconAttribute != null) {
            value = n.getValueForAttribute(this.iconAttribute);
            if (value != null) {
                String v = value.toString();
                if (this.icons.containsKey(v)) {
                    icon = (Icon) this.icons.get(v);
                }
            } else if (this.icons.containsKey(BasicTreeCellRenderer.NULL)) {
                icon = (Icon) this.icons.get(BasicTreeCellRenderer.NULL);
            }
        }

        if (icon != null) {
            if (component instanceof JLabel) {
                if ((n instanceof PageFetchTreeNode) && (n.getPageableIcon() != null)) {
                    ((JLabel) component).setIcon(n.getPageableIcon());
                } else {
                    ((JLabel) component).setIcon(icon);
                }
            }
        }

        if (n.isOrganizational()) {
            if (!selected) {
                component.setForeground(BasicTreeCellRenderer.organizationalForegroundColor);
            }
        }
        if (n.getRemark()) {
            if (!selected) {
                component.setForeground(Color.green.darker());
            }
            if (component instanceof JLabel) {
                final Icon i = ((JLabel) component).getIcon();
                if (i != null) {

                    Icon iconoEsp = new Icon() {

                        @Override
                        public int getIconHeight() {
                            return i.getIconHeight();
                        }

                        @Override
                        public int getIconWidth() {
                            return i.getIconWidth();
                        }

                        @Override
                        public void paintIcon(Component c, Graphics g, int x, int y) {
                            i.paintIcon(c, g, x, y);
                            // Add a red star in the top right
                            // corner
                            g.translate(x, y);
                            // 4 lines to the star
                            Color color = g.getColor();
                            int width = this.getIconWidth();
                            g.setColor(Color.red);

                            // g.drawLine(12, 0, 16, 4);
                            // g.drawLine(14, 0, 14, 4);
                            // g.drawLine(16, 0, 12, 4);
                            // g.drawLine(12, 2, 16, 2);

                            // g.fillOval(width-3, 2, 3, 3);

                            g.drawLine(width > 4 ? width - 4 : 0, 0, width, 4);
                            g.drawLine(width - 2, 0, width - 2, 4);
                            g.drawLine(width, 0, width - 4, 4);
                            g.drawLine(width - 4, 2, width, 2);
                            g.setColor(color);
                            g.translate(-x, -y);
                        }
                    };
                    if ((n instanceof PageFetchTreeNode) && (n.getPageableIcon() != null)) {
                        ((JLabel) component).setIcon(n.getPageableIcon());
                    } else {
                        ((JLabel) component).setIcon(iconoEsp);
                    }
                }
            }
        }

    }

    protected Component configureOrganizationalTreeCellRenderer(JTree jTree, OTreeNode node, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {

        StringBuilder nodeText = new StringBuilder(node.toString());

        if (jTree.isExpanded(this.getPath(node))) {
            this.childCount = node.getChildCount();
            if (this.childCount == 1) {
                if (((OTreeNode) node.getChildAt(0)).isEmptyNode()) {
                    this.childCount = 0;
                }
            }
        } else {
            if (Tree.enabledRowCount) {
                this.childCount = node.getCount();
            }
        }

        /***
         *
         */

        if ((this.childCount >= 0) && !node.isRoot()) {
            if (!node.isPageableEnabled() || (node.getChildCount() <= 1)) {
                if (!(node instanceof PageFetchTreeNode)) {
                    if (BasicTreeCellRenderer.includeChildCount) {
                        nodeText.append(" ");
                        nodeText.append("(");
                        if (node.getChildCount() == 1) {
                            nodeText.append(Math.max(node.getTotalCount(), this.childCount));
                        } else {
                            nodeText.append(this.childCount);
                        }
                        nodeText.append(")");
                    }
                }
            } else {
                if ((node.getChildCount() != 0)
                        && (node.getChildAt(node.getChildCount() - 1) instanceof PageFetchTreeNode)) {
                    this.childCount = this.childCount - 1;
                }

                if (BasicTreeCellRenderer.includeChildCount) {
                    nodeText.append(" ");
                    nodeText.append("(");
                    nodeText.append(this.childCount);
                    nodeText.append("/");
                    nodeText.append(node.getTotalCount());
                    nodeText.append(")");
                }
            }
        }

        return super.getTreeCellRendererComponent(jTree, nodeText.toString(), selected, expanded, leaf, row, hasFocus);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean selected, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        synchronized (this) {
            boolean ready = false;
            for (int i = 0; i < this.trees.length; i++) {
                if (jTree == this.trees[i]) {
                    ready = true;
                    break;
                }
            }
            if (!ready) {
                JTree[] auxTrees = new JTree[this.trees.length + 1];
                System.arraycopy(this.trees, 0, auxTrees, 0, this.trees.length);
                auxTrees[this.trees.length] = jTree;
                this.trees = auxTrees;
            }
        }

        this.row = row;
        this.calculeIsParentSelected(jTree, row);
        this.leaf = leaf;
        this.setRoot(jTree, value);

        // Component component = super.getTreeCellRendererComponent(jTree,
        // value, selected, expanded, leaf, row, hasFocus);
        Component component = null;
        if (value instanceof TreeNode) {
            this.childCount = ((TreeNode) value).getChildCount();
        }

        if ((value instanceof OTreeNode) && ((OTreeNode) value).isOrganizational()) {
            component = this.configureOrganizationalTreeCellRenderer(jTree, (OTreeNode) value, selected, expanded, leaf,
                    row, hasFocus);
        } else {
            this.childCount = -1;
            component = super.getTreeCellRendererComponent(jTree, value, selected, expanded, leaf, row, hasFocus);
        }

        try {
            TreePath path = jTree.getPathForRow(row);
            if (path != null) {
                Object oNode = path.getLastPathComponent();
                if (oNode != null) {
                    if (oNode instanceof OTreeNode) {
                        this.configureTreeCellRenderer(jTree, (OTreeNode) oNode, component, value, selected);
                    }
                }
            }

            if (BasicTreeCellRenderer.firstNodeConfiguration && (component instanceof DefaultTreeCellRenderer)) {
                this.configureFirstNode((DefaultTreeCellRenderer) component, jTree, this.isRoot(), selected);
            }
        } catch (Exception e) {
            BasicTreeCellRenderer.logger.error(null, e);
        }
        return component;
    }

    protected void setRoot(JTree jTree, Object value) {
        Object root = jTree.getModel().getRoot();
        if (root != null) {
            this.root = root.equals(value);
        } else {
            this.root = false;
        }
    }

    public boolean isRoot() {
        return this.root;
    }

    public TreePath getPath(TreeNode node) {
        List list = new ArrayList();

        // Add all nodes to list
        while (node != null) {
            list.add(node);
            node = node.getParent();
        }
        Collections.reverse(list);

        // Convert array of nodes to TreePath
        return new TreePath(list.toArray());
    }

    @Override
    public void updateUI() {
        super.updateUI();
        this.setLeafIcon(UIManager.getIcon("Tree.leafIcon"));
        this.setClosedIcon(UIManager.getIcon("Tree.closedIcon"));
        this.setOpenIcon(UIManager.getIcon("Tree.openIcon"));
        this.setTextSelectionColor(UIManager.getColor("Tree.selectionForeground"));
        this.setTextNonSelectionColor(UIManager.getColor("Tree.textForeground"));
        this.setBackgroundSelectionColor(UIManager.getColor("Tree.selectionBackground"));
        this.setBackgroundNonSelectionColor(UIManager.getColor("Tree.textBackground"));
        this.setBorderSelectionColor(UIManager.getColor("Tree.selectionBorderColor"));
        this.setOpaque(false);
    }

}
