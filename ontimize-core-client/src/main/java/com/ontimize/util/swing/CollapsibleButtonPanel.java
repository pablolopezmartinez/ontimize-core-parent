package com.ontimize.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ColorConstants;
import com.ontimize.gui.Form;
import com.ontimize.gui.images.ImageManager;

/**
 * @author Imatia Innovation S.L.
 * @since 5.2057EN-0.6
 */

public class CollapsibleButtonPanel extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(CollapsibleButtonPanel.class);

    /**
     * The name of class. Used by L&F to put UI properties.
     *
     * @since 5.2062EN
     */
    public static final String COLLAPSIBLEBUTTONPANEL = "CollapsibleButtonPanel";

    /**
     * The key for orientation
     */
    public static final String ORIENTATION = "orientation";

    public static final String REVERSE_ICON = "reverseicon";

    public static final String HORIZONTAL_ORIENTATION_VALUE = "horizontal";

    public static final String VERTICAL_ORIENTATION_VALUE = "vertical";

    public static final int HORIZONTAL_ORIENTATION = 0;

    public static final int VERTICAL_ORIENTATION = 1;

    public static String leftIconPath = ImageManager.ALL_LEFT_ARROW;

    public static String rightIconPath = ImageManager.ALL_RIGHT_ARROW;

    public static Color backgroundColor;

    public static Color lineBorderColor;

    static {
        try {
            CollapsibleButtonPanel.backgroundColor = ColorConstants.parseColor("#B0B0B0");
            CollapsibleButtonPanel.lineBorderColor = ColorConstants.parseColor("#CCCCCC");
        } catch (Exception e1) {
            CollapsibleButtonPanel.logger.error(null, e1);
            CollapsibleButtonPanel.backgroundColor = Color.darkGray;
        }
    }

    protected int orientation = CollapsibleButtonPanel.HORIZONTAL_ORIENTATION;

    protected JPanel innerComponent;

    protected JViewport viewPort = null;

    protected boolean deployedState = false;

    protected boolean expandHorizontal = false;

    protected boolean expandVertical = false;

    protected boolean expandLast = true;

    protected int verticalAlignment = GridBagConstraints.NORTH;

    protected Object attribute;

    protected int borderStyle = EtchedBorder.RAISED;

    protected String title;

    protected int deployTime = 200;

    protected boolean animated = true;

    protected Timer timer = null;

    public int customHeight = -1;

    public int customWidth = -1;

    public int minHeight = -1;

    public int minWidth = -1;

    protected String tiptext = "";

    protected boolean doFirstShow = false;

    protected boolean firstTime = true;

    protected String baseTooltip;

    protected boolean reverseIcons = false;

    protected boolean initiatedPreferences = false;

    protected Form parentForm;

    protected CollapsibleLabel collapsibleLabel = null;

    public CollapsibleButtonPanel(boolean right2left) {
        super();

        this.innerComponent = new JPanel();
        this.viewPort = new JViewport();
        this.viewPort.setView(this.innerComponent);

        this.collapsibleLabel = new CollapsibleLabel(right2left);

        this.setOpaque(false);

        super.setLayout(new GridBagLayout());

        if (right2left) {
            super.addImpl(this.collapsibleLabel, new GridBagConstraints(0, 0, 1, 1, 0, 1, GridBagConstraints.WEST,
                    GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0), -1);
            super.addImpl(this.viewPort, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0), -1);
        } else {
            super.addImpl(this.collapsibleLabel, new GridBagConstraints(1, 0, 1, 1, 0, 1, GridBagConstraints.WEST,
                    GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0), -1);
            super.addImpl(this.viewPort, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0), -1);
        }

        if (this.deployTime < 0) {
            this.deployTime = 0;
        }

        this.collapsibleLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    CollapsibleButtonPanel.this.doActionDeploy(CollapsibleButtonPanel.this.animated);
                    CollapsibleButtonPanel.this.collapsibleLabel.reverseIcon();
                }
            }
        });

        ActionListener target = new ActionListener() {

            protected boolean start = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                long totalTime = currentTime - CollapsibleButtonPanel.this.cycleStart;
                if ((totalTime > CollapsibleButtonPanel.this.deployTime) && !this.start) {
                    CollapsibleButtonPanel.this.cycleStart = currentTime;
                    totalTime = 0;
                    this.start = true;
                }

                float fraction = (float) totalTime / CollapsibleButtonPanel.this.deployTime;
                fraction = Math.min(1.0f, fraction);
                CollapsibleButtonPanel.this.calculatedCustomWidth(fraction, true);

                if (Float.compare(fraction, 1) == 0) {
                    CollapsibleButtonPanel.this.timer.stop();
                    this.start = false;
                }
            }
        };

        if (this.animated) {
            this.timer = new Timer(35, target);
            this.timer.setInitialDelay(0);
        }
    }

    protected long cycleStart = 0;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (this.firstTime) {
            if (!this.doFirstShow) {
                this.deployedState = true;
                this.doActionDeploy(false);
                this.doFirstShow = true;
            } else {
                this.doActionDeploy(false);
            }
            this.firstTime = false;
        }
    }

    // protected void calculatedCustomHeight(float fraction, boolean animated) {
    // if (animated) {
    // if (deployedState) {
    // customHeight = (int) ((viewPort.getView().getPreferredSize().height) *
    // fraction) + getInsets().top + getInsets().bottom;
    // } else {
    // customHeight = (int) ((viewPort.getView().getPreferredSize().height) * (1
    // - fraction)) + getInsets().top;
    // minHeight = customHeight;
    // }
    // } else {
    // if (deployedState) {
    // customHeight = (int) viewPort.getView().getPreferredSize().height +
    // getInsets().top + getInsets().bottom;
    // } else {
    // customHeight = (int) getInsets().top;
    // minHeight = customHeight;
    // }
    // }
    //
    // setBounds(getX(), getY(), getWidth(), customHeight);
    // validate();
    // }

    protected void calculatedCustomWidth(float fraction, boolean animated) {
        if (animated) {
            if (this.deployedState) {
                this.customWidth = (int) (this.viewPort.getView().getPreferredSize().width * fraction)
                        + this.collapsibleLabel.getPreferredSize().width + this
                            .getInsets().left
                        + this.getInsets().right;
            } else {
                this.customWidth = (int) (this.viewPort.getView().getPreferredSize().width * (1 - fraction))
                        + this.getInsets().left + this.collapsibleLabel
                            .getPreferredSize().width;
                this.minWidth = this.customWidth;

            }
        } else {
            if (this.deployedState) {
                this.customWidth = this.viewPort.getView().getPreferredSize().width + this.getInsets().left
                        + this.getInsets().right + this.collapsibleLabel
                            .getPreferredSize().width;
            } else {
                this.customWidth = this.getInsets().left + this.collapsibleLabel
                    .getPreferredSize().width
                        + (this.orientation == CollapsibleButtonPanel.VERTICAL_ORIENTATION ? 10 : 0);
                this.minWidth = this.customWidth;
            }
        }
        this.customHeight = Math.max(this.viewPort.getView().getPreferredSize().height,
                this.collapsibleLabel.getPreferredSize().height);
        this.setBounds(this.getX(), this.getY(), this.customWidth, this.customHeight);
        this.validate();
    }

    @Override
    public void validate() {
        // super.validate();
        Container parent = SwingUtilities.getAncestorOfClass(CollapsibleButtonPanel.class, this);

        if (parent == null) {
            parent = this.getParent();
        } else {
            parent = parent.getParent();
        }

        if (parent != null) {
            LayoutManager manager = parent.getLayout();

            if (!this.isVerticalOrientation()) {
                if (manager instanceof GridBagLayout) {
                    GridBagConstraints currentConstraints = ((GridBagLayout) manager).getConstraints(this);

                    if (this.deployedState && this.expandHorizontal) {
                        currentConstraints.weightx = 1;
                    } else {
                        currentConstraints.weightx = 0;
                    }
                    ((GridBagLayout) manager).setConstraints(this, currentConstraints);
                }
            } else {
                if (manager instanceof GridBagLayout) {
                    GridBagConstraints currentConstraints = ((GridBagLayout) manager).getConstraints(this);

                    if (this.deployedState && this.expandVertical) {
                        currentConstraints.weighty = 1;
                    } else {
                        currentConstraints.weighty = 0;
                    }
                    ((GridBagLayout) manager).setConstraints(this, currentConstraints);
                }
            }
            if (parent instanceof JComponent) {
                ((JComponent) parent).revalidate();
            } else {
                parent.invalidate();
            }
            parent.doLayout();
            parent.repaint();
        }
    }

    public void doActionDeploy(boolean animate) {
        if (animate) {
            if (this.timer != null) {
                this.timer.stop();
                this.setDeploy(!this.isDeploy());
                this.timer.start();
            }
        } else {
            this.setDeploy(!this.isDeploy());
            // if (isVerticalOrientation()) {
            // calculatedCustomHeight(0, false);
            // } else {
            this.calculatedCustomWidth(0, false);
            // }
        }

        if (!this.firstTime) {
            this.firePropertyChange("deploy", !this.isDeploy(), this.isDeploy());
        }
    }

    public boolean isDeploy() {
        return this.deployedState;
    }

    public void setDeploy(boolean deploy) {
        this.deployedState = deploy;
    }

    public static boolean computeIntersection(Rectangle dest, int rx, int ry, int rw, int rh) {
        int x1 = Math.max(rx, dest.x);
        int x2 = Math.min(rx + rw, dest.x + dest.width);
        int y1 = Math.max(ry, dest.y);
        int y2 = Math.min(ry + rh, dest.y + dest.height);
        dest.x = x1;
        dest.y = y1;
        dest.width = x2 - x1;
        dest.height = y2 - y1;

        if ((dest.width <= 0) || (dest.height <= 0)) {
            return false;
        }
        return true;
    }

    public void initPermissions() {

    }

    public boolean isRestricted() {
        return false;
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        if (this.innerComponent != null) {
            this.innerComponent.setLayout(mgr);
        }
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        if (this.innerComponent != null) {
            ((Container) this.innerComponent).add(comp, constraints, index);
        }
    }

    @Override
    public Dimension getPreferredSize() {

        Dimension viewPortDim = this.viewPort.getView().getPreferredSize();
        Dimension labelDim = this.collapsibleLabel.getPreferredSize();

        Dimension dim = new Dimension(viewPortDim.width + labelDim.width,
                Math.max(labelDim.height, viewPortDim.height));

        if (this.isVerticalOrientation()) {
            if (this.customHeight != -1) {
                dim.height = this.customHeight;
            } else if (this.deployedState) {
                dim.height = 0;
            }
        } else {
            if (this.customWidth != -1) {
                dim.width = this.customWidth;
                dim.height = this.customHeight;
            } else if (this.deployedState) {
                dim.width = 0;
            }
        }
        return dim;
    }

    @Override
    public Dimension getMaximumSize() {
        return this.getPreferredSize();
    }

    protected boolean isVerticalOrientation() {
        if (this.orientation == CollapsibleButtonPanel.VERTICAL_ORIENTATION) {
            return true;
        }
        return false;
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        ((JComponent) this.viewPort.getView()).setPreferredSize(preferredSize);
    }

    @Override
    public LayoutManager getLayout() {
        if (this.innerComponent != null) {
            return this.innerComponent.getLayout();
        }
        return null;
    }

    @Override
    public void setOpaque(boolean isOpaque) {
        super.setOpaque(isOpaque);
        if (this.innerComponent != null) {
            this.innerComponent.setOpaque(isOpaque);
        }
        if (this.viewPort != null) {
            this.viewPort.setOpaque(isOpaque);
        }
    }

    /**
     * @return true if the first time the component must be expanded and false if it must be shown
     *         collapsed
     */
    public boolean isFirstShow() {
        return this.doFirstShow;
    }

    /**
     * Configure how the component must appear the first time (expanded or collapsed)
     * @param doFirstShow
     */
    public void setFirstShow(boolean doFirstShow) {
        this.doFirstShow = doFirstShow;
    }

    /**
     * @return true if the panel has not be painted yet
     */
    public boolean isFirstTime() {
        return this.firstTime;
    }

    public static class CollapsibleLabel extends JLabel {

        protected Color backgroundColor = null;

        protected ImageIcon leftIcon = ImageManager.getIcon(CollapsibleButtonPanel.leftIconPath);

        protected ImageIcon rightIcon = ImageManager.getIcon(CollapsibleButtonPanel.rightIconPath);

        public CollapsibleLabel(boolean right2left) {

            if (right2left) {
                this.setIcon(this.leftIcon);
            } else {
                this.setIcon(this.rightIcon);
            }

            try {
                if (CollapsibleButtonPanel.lineBorderColor != null) {
                    this.setBorder(new LineBorder(CollapsibleButtonPanel.lineBorderColor));
                }
            } catch (Exception e2) {
                CollapsibleButtonPanel.logger.error(null, e2);
            }
            this.setAlignmentY(SwingConstants.CENTER);
            this.setOpaque(true);

            this.backgroundColor = CollapsibleButtonPanel.backgroundColor;
            if (this.backgroundColor != null) {
                Color darker = this.backgroundColor.darker();
                this.setBackground(darker);
            } else {
                this.setOpaque(false);
            }

            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    CollapsibleLabel label = (CollapsibleLabel) e.getSource();
                    if (CollapsibleLabel.this.backgroundColor != null) {
                        label.setBackground(CollapsibleLabel.this.backgroundColor);
                        label.repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    CollapsibleLabel label = (CollapsibleLabel) e.getSource();
                    if (CollapsibleLabel.this.backgroundColor != null) {
                        Color darker = CollapsibleLabel.this.backgroundColor.darker();
                        label.setBackground(darker);
                        label.repaint();
                    }
                }
            });
        }

        @Override
        public String getName() {
            return CollapsibleButtonPanel.COLLAPSIBLEBUTTONPANEL;
        }

        @Override
        public Dimension getPreferredSize() {
            return super.getPreferredSize();
        }

        public void reverseIcon() {
            if (this.leftIcon.equals(this.getIcon())) {
                this.setIcon(this.rightIcon);
            } else {
                this.setIcon(this.leftIcon);
            }
            if (this.backgroundColor != null) {
                Color darker = this.backgroundColor.darker();
                this.setBackground(darker);
            }
        }

    }

}
