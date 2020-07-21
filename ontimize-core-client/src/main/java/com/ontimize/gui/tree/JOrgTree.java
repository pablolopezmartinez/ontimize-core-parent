package com.ontimize.gui.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JOrgTree extends JPanel implements Scrollable {

    private static final Logger logger = LoggerFactory.getLogger(JOrgTree.class);

    public static boolean DEBUG = false;

    protected JOrgTreeModel innerModel = null; // Inner model

    protected TreeModel model = null; // MOdel. Inner model and model can be the
                                      // same

    protected JOrgTreeModelListener modelListener = null;

    protected JOrgTreeInnerModelListener innerModelListener = null;

    protected JOrgTreeNode selected = null;

    protected JOrgTreeNodePositionator positionator = null;

    protected JOrgTreeLineDrawer lineDrawer = null;

    protected JOrgTreeCellRenderer cellRenderer = null;

    protected int hMargin;

    protected int vMargin;

    protected int rendererWidth;

    protected int rendererHeight;

    protected int levelSeparation;

    protected int siblingSeparation;

    protected int orientation;

    public static int ORIENTATION_UP_DOWN = 1;

    public static int ORIENTATION_DOWN_UP = 2;

    public static int ORIENTATION_LEFT_RIGHT = 3;

    public static int ORIENTATION_RIGHT_LEFT = 4;

    protected static int ORIENTATION_UP_RIGHT = JOrgTree.ORIENTATION_UP_DOWN; // 1

    protected static int ORIENTATION_DOWN_RIGHT = JOrgTree.ORIENTATION_DOWN_UP; // 2

    protected static int ORIENTATION_DOWN_LEFT = JOrgTree.ORIENTATION_LEFT_RIGHT; // 3

    protected static int ORIENTATION_UP_LEFT = JOrgTree.ORIENTATION_RIGHT_LEFT; // 4

    public static int WIDTH_PREFERRED = 400;

    public static int HEIGHT_PREFERRED = 400;

    protected Dimension preferredSize = null;

    protected class JOrgTreeInnerModelListener implements TreeModelListener {

        @Override
        public void treeNodesChanged(TreeModelEvent e) {

        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
            JOrgTree.this.recalculateNodesPos();
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
            JOrgTree.this.recalculateNodesPos();
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            JOrgTree.this.recalculateNodesPos();
        }

    }

    protected class JOrgTreeModelListener implements TreeModelListener {

        @Override
        public void treeNodesChanged(TreeModelEvent e) {

        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
            JOrgTree.this.setModel(JOrgTree.this.model);
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
            JOrgTree.this.setModel(JOrgTree.this.model);
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            JOrgTree.this.setModel(JOrgTree.this.model);
        }

    }

    public void recalculateNodesPos() {
        if (this.positionator != null) {
            this.positionator.calculateNodePositions(this);
        }
    }

    public JOrgTree(TreeModel model) {
        this();
        this.setModel(model);
    }

    public JOrgTree() {
        this.model = null;
        this.cellRenderer = null;
        this.levelSeparation = 30;
        this.siblingSeparation = 30;
        this.rendererWidth = 60;
        this.rendererHeight = 40;
        this.hMargin = 60;
        this.vMargin = 60;
        this.selected = null;

        this.preferredSize = new Dimension(JOrgTree.WIDTH_PREFERRED, JOrgTree.HEIGHT_PREFERRED);

        this.innerModelListener = new JOrgTreeInnerModelListener();
        this.modelListener = new JOrgTreeModelListener();

        this.orientation = JOrgTree.ORIENTATION_UP_DOWN;

        this.setLayout(null);
        this.setBackground(Color.white);
        this.cellRenderer = new DefaultOrgTreeRenderer();
        this.positionator = new NotCompactPositionator();
        this.lineDrawer = new OrtoLineDrawer();

        this.setModel(this.model);

        this.installSelectionHandler();
    }

    @Override
    public Dimension getPreferredSize() {
        if (this.positionator != null) {
            Dimension d = this.positionator.getPreferredSize();
            return d;
        } else {
            return new Dimension(0, 0);
        }
    }

    protected void installSelectionHandler() {
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent mouseevent) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (JOrgTree.this.isEnabled()) {
                    JOrgTree.this.requestFocus();
                    if ((e.getClickCount() == 1) && SwingUtilities.isLeftMouseButton(e)) {
                        int xe = e.getX();
                        int ye = e.getY();
                        JOrgTree.this.selected = JOrgTree.this.getNodeAtCoordinates(xe, ye);
                        JOrgTree.this.repaint();
                    }
                }
            }
        });

        this.addFocusListener(new FocusListener() {

            Border border = BorderFactory.createLineBorder(Color.black);

            @Override
            public void focusGained(FocusEvent e) {
                JOrgTree.this.setBorder(this.border);
            }

            @Override
            public void focusLost(FocusEvent e) {
                JOrgTree.this.setBorder(null);
            }
        });
    }

    public void setLineDrawer(JOrgTreeLineDrawer lineDrawer) {
        if (lineDrawer == null) {
            throw new IllegalArgumentException("lineDrawer must not be null");
        } else {
            this.lineDrawer = lineDrawer;
            this.repaint();
            this.revalidate();
        }
    }

    public void setCellRenderer(JOrgTreeCellRenderer renderer) {
        this.cellRenderer = renderer;
        this.repaint();
        this.revalidate();
    }

    public int getRendererWidth() {
        return this.rendererWidth;
    }

    public void setRendererWidth(int rendererWidth) {
        if (rendererWidth <= 0) {
            throw new IllegalArgumentException("rendererWidth must be greater than zero");
        } else {
            this.rendererWidth = rendererWidth;
            if (this.innerModel != null) {
                if (this.positionator != null) {
                    this.positionator.calculateNodePositions(this);
                    this.repaint();
                    this.revalidate();
                }
            }
        }
    }

    public int getRendererHeight() {
        return this.rendererHeight;
    }

    public void setRendererHeight(int rendererHeight) {
        if (rendererHeight <= 0) {
            throw new IllegalArgumentException("rendererHeight must be greater than zero");
        } else {
            this.rendererHeight = rendererHeight;
            if (this.innerModel != null) {
                if (this.positionator != null) {
                    this.positionator.calculateNodePositions(this);
                    this.repaint();
                    this.revalidate();
                }
            }
        }
    }

    public JOrgTreeModel getInnerModel() {
        return this.innerModel;
    }

    public TreeModel getModel() {
        return this.model;
    }

    public void setModel(TreeModel newModel) {
        if (newModel == null) {
            this.innerModel = null;
            if (this.model != null) {
                this.model.removeTreeModelListener(this.modelListener);
            }
            this.model = null;
        } else {
            if (this.model != null) {
                if (this.model != newModel) {
                    this.model.removeTreeModelListener(this.modelListener);
                    newModel.addTreeModelListener(this.modelListener);
                    this.model = newModel;
                }
            } else {
                newModel.addTreeModelListener(this.modelListener);
                this.model = newModel;
            }

            if (newModel instanceof JOrgTreeModel) {
                this.innerModel = (JOrgTreeModel) newModel;
            } else {
                this.innerModel = this.generateJOrgTreeModel(newModel);
            }

            this.selected = null;
            if (this.positionator != null) {
                this.positionator.calculateNodePositions(this);

            }
            this.repaint();
            this.revalidate();
        }
    }

    public JOrgTreeModel generateJOrgTreeModel(TreeModel model) {
        TreeNode root = (TreeNode) model.getRoot();
        JOrgTreeNode newRoot = this.generateJOrgTreeNode(root);
        return new JOrgTreeModel(newRoot);
    }

    public JOrgTreeNode generateJOrgTreeNode(TreeNode node) {
        JOrgTreeNode newNode = new JOrgTreeNode(null);
        newNode.setTreeNode(node);

        int childrenCount = node.getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            TreeNode childNode = node.getChildAt(i);
            JOrgTreeNode jtChildNode = this.generateJOrgTreeNode(childNode);
            newNode.add(jtChildNode);
        }

        return newNode;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        if ((orientation < 1) && (orientation > 4)) {
            throw new IllegalArgumentException("Orientation not allowed");
        } else {
            this.orientation = orientation;
            if (this.innerModel != null) {
                if (this.positionator != null) {
                    this.positionator.calculateNodePositions(this);
                    this.repaint();
                    this.revalidate();
                }
            }
        }
    }

    public JOrgTreeNodePositionator getPositionator() {
        return this.positionator;
    }

    public void setPositionator(JOrgTreeNodePositionator positionator) {
        if (positionator == null) {
            throw new IllegalArgumentException("positionator must be not null");
        } else {
            this.positionator = positionator;
            if (this.innerModel != null) {
                if (positionator != null) {
                    positionator.calculateNodePositions(this);
                    this.repaint();
                    this.revalidate();
                }
            }
        }
    }

    public int getHMargin() {
        return this.hMargin;
    }

    public void setHMargin(int hMargin) {
        if (hMargin < 0) {
            throw new IllegalArgumentException("hMargin must be greater or equal than zero");
        } else {
            this.hMargin = hMargin;
            if (this.innerModel != null) {
                if (this.positionator != null) {
                    this.positionator.calculateNodePositions(this);
                    this.repaint();
                    this.revalidate();
                }
            }
        }
    }

    public int getVMargin() {
        return this.vMargin;
    }

    public void setVMargin(int vMargin) {
        if (vMargin < 0) {
            throw new IllegalArgumentException("vMargin must be greater or equal than zero");
        } else {
            this.vMargin = vMargin;
            if (this.innerModel != null) {
                if (this.positionator != null) {
                    this.positionator.calculateNodePositions(this);
                    this.repaint();
                    this.revalidate();
                }
            }
        }
    }

    public int getLevelSeparation() {
        return this.levelSeparation;
    }

    public void setLevelSeparation(int levelSeparation) {
        if (levelSeparation <= 0) {
            throw new IllegalArgumentException("levelSeparation must be greater than zero");
        } else {
            this.levelSeparation = levelSeparation;
            if (this.innerModel != null) {
                if (this.positionator != null) {
                    this.positionator.calculateNodePositions(this);
                    this.repaint();
                    this.revalidate();
                }
            }
        }
    }

    public int getSiblingSeparation() {
        return this.siblingSeparation;
    }

    public void setSiblingSeparation(int siblingSeparation) {
        if (siblingSeparation <= 0) {
            throw new IllegalArgumentException("siblingSeparation must be greater than zero");
        } else {
            this.siblingSeparation = siblingSeparation;
            if (this.innerModel != null) {
                if (this.positionator != null) {
                    this.positionator.calculateNodePositions(this);
                    this.repaint();
                    this.revalidate();
                }
            }
        }
    }

    public void paintNode(JOrgTreeNode node, Graphics g) {
        boolean bNodeSelected = node == this.selected;
        Component component = null;

        if (this.model == this.innerModel) {
            component = this.cellRenderer.getJOrgTreeCellRendererComponent(this, node, bNodeSelected, 1, false);
        } else {
            component = this.cellRenderer.getJOrgTreeCellRendererComponent(this, node.getTreeNode(), bNodeSelected, 1,
                    false);
        }

        if (JOrgTree.DEBUG) {
            JOrgTree.logger.debug("Painting node: " + node.getUserObject() + " " + node.getX() + "-" + node.getY());
        }

        int xNode = node.getX();
        int yNode = node.getY();

        g.translate(xNode, yNode);
        component.setSize(this.rendererWidth, this.rendererHeight);
        component.paint(g);
        g.translate(-xNode, -yNode);
    }

    @Override
    protected void paintComponent(Graphics g) {

        if (this.getParent() instanceof JViewport) {
            JViewport jvp = (JViewport) this.getParent();
            Rectangle clip = this.getBounds();
            clip.setSize(jvp.getSize());
            clip.setLocation((int) Math.abs(clip.getX()), (int) Math.abs(clip.getY()));

            if (this.isEnabled()) {
                g.setColor(this.getBackground());
            } else {
                g.setColor(Color.lightGray);
            }
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(this.getForeground());

            if (this.innerModel != null) {
                if (this.positionator != null) {
                    if (this.positionator.calculated()) {
                        JOrgTreeNode root = (JOrgTreeNode) this.innerModel.getRoot();
                        Enumeration enumNodes = root.preorderEnumeration();
                        Point p1 = new Point();
                        Point p2 = new Point();
                        Point p3 = new Point();
                        Point p4 = new Point();
                        int xleft;
                        int yup;
                        int xright;
                        int ydown;

                        while (enumNodes.hasMoreElements()) {
                            JOrgTreeNode jotn = (JOrgTreeNode) enumNodes.nextElement();
                            xleft = jotn.getX();
                            yup = jotn.getY();
                            xright = xleft + this.getRendererWidth();
                            ydown = yup + this.getRendererHeight();
                            p1.setLocation(xleft, yup);
                            p2.setLocation(xright, yup);
                            p3.setLocation(xleft, ydown);
                            p4.setLocation(xright, ydown);

                            if (clip.contains(p1) || clip.contains(p2) || clip.contains(p3) || clip.contains(p4)) {
                                this.paintNode(jotn, g);
                            }
                        }
                        if (this.lineDrawer != null) {
                            this.lineDrawer.drawLines(this, g);
                        }
                    }
                }
            }
        } else {
            if (this.isEnabled()) {
                g.setColor(this.getBackground());
            } else {
                g.setColor(Color.lightGray);
            }

            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(this.getForeground());

            if (this.innerModel != null) {
                if (this.positionator != null) {
                    if (this.positionator.calculated()) {
                        JOrgTreeNode root = (JOrgTreeNode) this.innerModel.getRoot();
                        Enumeration enumNodes = root.preorderEnumeration();
                        while (enumNodes.hasMoreElements()) {
                            this.paintNode((JOrgTreeNode) enumNodes.nextElement(), g);
                        }
                        if (this.lineDrawer != null) {
                            this.lineDrawer.drawLines(this, g);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.setFocusable(enabled);
        // this.repaint();
    }

    public JOrgTreeNode getNodeAtCoordinates(int x, int y) {
        if (this.innerModel != null) {
            JOrgTreeNode root = (JOrgTreeNode) this.innerModel.getRoot();
            Enumeration enumNodes = root.preorderEnumeration();
            while (enumNodes.hasMoreElements()) {
                JOrgTreeNode jtNode = (JOrgTreeNode) enumNodes.nextElement();
                if ((x >= jtNode.getX()) && (x <= (jtNode.getX() + this.rendererWidth)) && (y >= jtNode.getY())
                        && (y <= (jtNode.getY() + this.rendererHeight))) {
                    return jtNode;
                }
            }
        }
        return null;
    }

    public static void main(String args[]) {
        JFrame f = new JFrame("JOrgTree Example");

        JPanel panel = new JPanel();

        JLabel label = new JLabel("Position");
        panel.add(label);

        String[] pos = { "No compacted tree", "Compacted tree", "Directory structure" };
        JComboBox cbPos = new JComboBox(pos);
        panel.add(cbPos);
        panel.setVisible(true);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("1");
        TreeModel model = new DefaultTreeModel(root);
        JOrgTree.addTreeChildren(model, root, 1, 7, 0, 3);

        JOrgTree orgChart = new JOrgTree(model);

        orgChart.setFont(orgChart.getFont().deriveFont(9F));
        orgChart.setRendererHeight(20);
        orgChart.setRendererWidth(80);
        orgChart.setLevelSeparation(20);

        f.getContentPane().add(new Frame(orgChart), BorderLayout.NORTH);
        f.getContentPane().add(new JScrollPane(orgChart));

        f.setSize(800, 600);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
    }

    protected static class Frame extends JPanel {

        JOrgTree tree;

        JComboBox cbPos;

        JComboBox cbLin;

        JComboBox upDownOrientationComboBox;

        JComboBox upRightOrientationCombobox;

        JButton regenerateFrame;

        JLabel lLin;

        int positionator = 0;

        int lines = 0;

        int orientation = 1;

        public Frame(JOrgTree tree) {
            this.tree = tree;
            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            JLabel label = new JLabel("Posicionador");
            this.add(label);

            String[] pos = { "No compacted tree", "Compacted tree", "Directory structure" };
            this.cbPos = new JComboBox(pos);
            this.add(this.cbPos);

            this.lLin = new JLabel("Lines");
            this.add(this.lLin);

            String[] lin = { "Angle", "straight" };
            this.cbLin = new JComboBox(lin);
            this.add(this.cbLin);

            JLabel label3 = new JLabel("Orientation");
            this.add(label3);

            String[] ori1 = { "Up-Down", "Down-Up", "Left-Right", "Right-Left" };
            this.upDownOrientationComboBox = new JComboBox(ori1);
            this.add(this.upDownOrientationComboBox);

            String[] ori2 = { "Up-Down", "Down-Right", "Down-Left", "Up-Left" };
            this.upRightOrientationCombobox = new JComboBox(ori2);
            this.upRightOrientationCombobox.setVisible(false);
            this.add(this.upRightOrientationCombobox);

            this.regenerateFrame = new JButton("Regenerate model");
            this.add(this.regenerateFrame);

            this.regenerateFrame.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    DefaultMutableTreeNode root = new DefaultMutableTreeNode("1");
                    TreeModel model = new DefaultTreeModel(root);
                    JOrgTree.addTreeChildren(model, root, 1, 7, 0, 3);
                    Frame.this.tree.setModel(model);
                }
            });

            this.cbPos.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox cb = (JComboBox) e.getSource();
                    int index = cb.getSelectedIndex();
                    if (index != Frame.this.positionator) {
                        if (index == 0) {
                            Frame.this.tree.setPositionator(new NotCompactPositionator());
                        } else if (index == 1) {
                            Frame.this.tree.setPositionator(new CompactPositionator());
                        } else if (index == 2) {
                            Frame.this.tree.setPositionator(new DirectoryPositionator());
                        }

                        if ((index == 0) || (index == 1)) {
                            Frame.this.lLin.setVisible(true);
                            Frame.this.cbLin.setVisible(true);
                            Frame.this.cbLin.setSelectedIndex(0);

                            if (Frame.this.positionator == 2) {
                                Frame.this.tree.setLineDrawer(new OrtoLineDrawer());
                                Frame.this.upDownOrientationComboBox.setVisible(true);
                                Frame.this.upDownOrientationComboBox.setSelectedIndex(0);
                                Frame.this.tree.setOrientation(JOrgTree.ORIENTATION_UP_DOWN);
                                Frame.this.upRightOrientationCombobox.setVisible(false);
                            }
                        } else {
                            Frame.this.lLin.setVisible(false);
                            Frame.this.cbLin.setVisible(false);
                            Frame.this.cbLin.setSelectedIndex(0);

                            Frame.this.tree.setLineDrawer(new RectLineDrawer());

                            Frame.this.upDownOrientationComboBox.setVisible(false);
                            Frame.this.upDownOrientationComboBox.setSelectedIndex(0);
                            Frame.this.tree.setOrientation(JOrgTree.ORIENTATION_UP_RIGHT);
                            Frame.this.upRightOrientationCombobox.setVisible(true);
                            Frame.this.upRightOrientationCombobox.setSelectedIndex(0);

                        }

                        Frame.this.positionator = index;
                    }

                }
            });

            this.cbLin.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox cb = (JComboBox) e.getSource();
                    int index = cb.getSelectedIndex();
                    if (index == 0) {
                        Frame.this.tree.setLineDrawer(new OrtoLineDrawer());
                    } else {
                        Frame.this.tree.setLineDrawer(new BeeLineDrawer());
                    }
                }
            });

            this.upDownOrientationComboBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox cb = (JComboBox) e.getSource();
                    Frame.this.tree.setOrientation(cb.getSelectedIndex() + 1);
                }
            });

            this.upRightOrientationCombobox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox cb = (JComboBox) e.getSource();
                    Frame.this.tree.setOrientation(cb.getSelectedIndex() + 1);
                }
            });
        }

    }

    public static void addChildren(JOrgTreeModel model, JOrgTreeNode node, int level, int maxLevel, int minChildren,
            int maxChildren) {
        if (level < maxLevel) {
        }

        String sText = (String) node.getUserObject();
        int childrenCount = (int) Math.round(Math.random() * (maxChildren - minChildren)) + minChildren;
        for (int i = 0; i < childrenCount; i++) {
            JOrgTreeNode jotrChildNode = new JOrgTreeNode(sText + "." + (i + 1));
            model.insertNodeInto(jotrChildNode, node, i);
            if (level < (maxLevel - 1)) {
                JOrgTree.addChildren(model, jotrChildNode, level + 1, maxLevel, minChildren, maxChildren);
            }
        }
    }

    public static void addTreeChildren(TreeModel model, DefaultMutableTreeNode node, int level, int maxLevel,
            int minChildren, int maxChildren) {
        if (level < maxLevel) {
        }

        String sText = (String) node.getUserObject();
        int childrenCount = (int) Math.round(Math.random() * (maxChildren - minChildren)) + minChildren;
        for (int i = 0; i < childrenCount; i++) {
            JOrgTreeNode childNode = new JOrgTreeNode(sText + "." + (i + 1));
            node.add(childNode);
            if (level < (maxLevel - 1)) {
                JOrgTree.addTreeChildren(model, childNode, level + 1, maxLevel, minChildren, maxChildren);
            }
        }
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        if (this.getParent().getHeight() > this.getPreferredSize().height) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        if (this.getParent().getWidth() > this.getPreferredSize().width) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return this.preferredSize;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width;
        } else {
            return visibleRect.height;
        }
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        // int currentPosition = 0;
        // if (orientation == SwingConstants.HORIZONTAL) {
        // currentPosition = visibleRect.x;
        // } else {
        // currentPosition = visibleRect.y;
        // }

        return 10;
    }

}
