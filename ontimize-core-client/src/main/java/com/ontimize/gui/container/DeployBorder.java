package com.ontimize.gui.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.images.ImageManager;

public class DeployBorder extends TitledBorder implements IDeployBorder {

	private static final Logger	logger		= LoggerFactory.getLogger(DeployBorder.class);

	protected String previous = "     ";

	protected Point textLoc = new Point();

	protected Rectangle imageBound = null;

	public static final String DOWN_ICON = "down2.png";

	public static final String UP_ICON = "up2.png";

	public ImageIcon downIcon;

	public ImageIcon upIcon;

	public ImageIcon currentIcon;

	public boolean highlight;

	public int orientation = CollapsiblePanel.VERTICAL_ORIENTATION;

	public ImageIcon rightIcon = null;

	public ImageIcon leftIcon = null;

	protected Insets margin = null;

	public DeployBorder(Border border, int orientation) {
		super(border);
		this.orientation = orientation;
		this.init(false);
	}

	public DeployBorder(String title) {
		super(title);
		this.init(false);
	}

	public DeployBorder(Border border, String title) {
		super(border, title);
		this.init(false);
	}

	public DeployBorder(Border border, String title, int orientation, boolean reverse) {
		super(border, title);
		this.orientation = orientation;
		this.init(reverse);
	}

	protected void init(boolean reverse) {
		if (this.orientation == CollapsiblePanel.VERTICAL_ORIENTATION) {
			try {
				this.downIcon = ImageManager.getIcon(DeployBorder.DOWN_ICON);
			} catch (Exception ex) {
				DeployBorder.logger.trace(null, ex);
			}

			try {
				this.upIcon = ImageManager.getIcon(DeployBorder.UP_ICON);
			} catch (Exception ex) {
				DeployBorder.logger.trace(null, ex);
			}
			if (reverse) {
				ImageIcon temp = this.downIcon;
				this.downIcon = this.upIcon;
				this.upIcon = temp;
			}
			this.imageBound = new Rectangle(this.upIcon.getIconWidth(), this.upIcon.getIconHeight());
		} else {
			this.leftIcon = ImageManager.getIcon(ImageManager.LEFT);
			this.rightIcon = ImageManager.getIcon(ImageManager.RIGHT);
			this.imageBound = new Rectangle(this.leftIcon.getIconWidth(), this.rightIcon.getIconHeight());
			if (reverse) {
				ImageIcon temp = this.leftIcon;
				this.leftIcon = this.rightIcon;
				this.rightIcon = temp;
			}
		}
	}

	protected boolean isVerticalOrientation() {
		if (this.orientation == CollapsiblePanel.VERTICAL_ORIENTATION) {
			return true;
		}
		return false;
	}

	@Override
	public String getTitle() {
		if ((super.getTitle() != null) && (super.getTitle().length() > 0)) {
			return this.previous + super.getTitle();
		} else {
			return this.previous;
		}
	}

	/**
	 * @return the highlight
	 */
	public boolean isHighlight() {
		return this.highlight;
	}

	/**
	 * @param highlight
	 *            the highlight to set
	 */
	@Override
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	/**
	 * Paints the border for the specified component with the specified position and size.
	 *
	 * @param c
	 *            the component for which this border is being painted
	 * @param g
	 *            the paint graphics
	 * @param x
	 *            the x position of the painted border
	 * @param y
	 *            the y position of the painted border
	 * @param width
	 *            the width of the painted border
	 * @param height
	 *            the height of the painted border
	 */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		if (this.isVerticalOrientation()) {
			this.paintVerticalOrientation(c, g, x, y, width, height);
		} else {
			this.paintHorizontalOrientation(c, g, x, y, width, height);
		}
	}

	public void paintVerticalOrientation(Component c, Graphics g, int x, int y, int width, int height) {

		this.setCurrentIcon(c);

		Border border = this.getBorder();

		if ((this.getTitle() == null) || this.getTitle().equals("")) {
			if (border != null) {
				border.paintBorder(c, g, x, y, width, height);
			}
			return;
		}

		Rectangle grooveRect = new Rectangle(x + TitledBorder.EDGE_SPACING, y + TitledBorder.EDGE_SPACING, width - (TitledBorder.EDGE_SPACING * 2),
				height - (TitledBorder.EDGE_SPACING * 2));
		Font font = g.getFont();
		Color color = g.getColor();

		g.setFont(this.getFont(c));

		FontMetrics fm = g.getFontMetrics();
		int fontHeight = fm.getHeight();
		int descent = fm.getDescent();
		int ascent = fm.getAscent();
		int diff;
		int stringWidth = fm.stringWidth(this.getTitle());
		Insets insets;
		if (border != null) {
			insets = border.getBorderInsets(c);
		} else {
			insets = new Insets(0, 0, 0, 0);
		}

		int titlePos = this.getTitlePosition();
		this.setTitlePosition(grooveRect, fontHeight, descent, ascent, insets, titlePos);

		int justification = this.setTitledBorderJustification(c);

		this.setTextJustification(grooveRect, stringWidth, insets, justification);

		int customHeight = ((CollapsiblePanel) c).customHeight;
		int minHeight = ((CollapsiblePanel) c).minHeight;

		// If title is positioned in middle of border AND its fontsize
		// is greater than the border's thickness, we'll need to paint
		// the border in sections to leave space for the component's
		// background
		// to show through the title.
		//
		if (border != null) {
			if ((((titlePos == TitledBorder.TOP) || (titlePos == TitledBorder.DEFAULT_POSITION)) && (grooveRect.y > (this.textLoc.y - ascent))) || ((titlePos == TitledBorder.BOTTOM) && ((grooveRect.y + grooveRect.height) < (this.textLoc.y + descent)))) {

				Rectangle clipRect = new Rectangle();

				// save original clip
				Rectangle saveClip = g.getClipBounds();
				Rectangle deployedClip = g.getClipBounds();
				if ((customHeight != -1) && (customHeight > grooveRect.height)) {
					saveClip.height = customHeight;
					deployedClip.height = customHeight;
				}

				clipRect.setBounds(saveClip);
				if (customHeight == minHeight) {
					clipRect.x += 4;
					clipRect.width -= 4;
					clipRect.height = fm.getHeight();
				}
				if (CollapsiblePanel.computeIntersection(clipRect, x, y, this.textLoc.x - 1 - x, height)) {
					g.setClip(clipRect);
					border.paintBorder(c, g, grooveRect.x, grooveRect.y, grooveRect.width, grooveRect.height);
				}

				// paint strip right of text
				clipRect.setBounds(deployedClip);
				if (customHeight == minHeight) {
					clipRect.width -= 4;
					clipRect.height = fm.getHeight();
				}
				if (CollapsiblePanel.computeIntersection(clipRect, this.textLoc.x + stringWidth + 1, y, (x + width) - (this.textLoc.x + stringWidth + 1), height)) {
					g.setClip(clipRect);
					border.paintBorder(c, g, grooveRect.x, grooveRect.y, grooveRect.width, grooveRect.height);
				}

				if ((titlePos == TitledBorder.TOP) || (titlePos == TitledBorder.DEFAULT_POSITION)) {
					// paint strip below text
					clipRect.setBounds(deployedClip);
					if (customHeight != minHeight) {
						if (CollapsiblePanel.computeIntersection(clipRect, this.textLoc.x - 1, this.textLoc.y + descent, stringWidth + 2,
								(y + height) - this.textLoc.y - descent)) {
							g.setClip(clipRect);
							border.paintBorder(c, g, grooveRect.x, grooveRect.y, grooveRect.width, grooveRect.height);
						}
					}

				} else { // titlePos == BOTTOM
					// paint strip above text
					clipRect.setBounds(deployedClip);
					if (CollapsiblePanel.computeIntersection(clipRect, this.textLoc.x - 1, y, stringWidth + 2, this.textLoc.y - ascent - y)) {
						g.setClip(clipRect);
						border.paintBorder(c, g, grooveRect.x, grooveRect.y, grooveRect.width, grooveRect.height);
					}
				}

				// restore clip
				g.setClip(saveClip);

			} else {
				border.paintBorder(c, g, grooveRect.x, grooveRect.y, grooveRect.width, grooveRect.height);
			}
		}

		Color colorTitle = this.getTitleColor();
		if (colorTitle != null) {
			if (this.isHighlight()) {
				g.setColor(colorTitle.brighter());
			} else {
				g.setColor(colorTitle);
			}
		}

		// g.setColor(Color.BLACK);
		g.drawString(this.getTitle(), this.textLoc.x, this.textLoc.y);
		this.imageBound.x = this.textLoc.x;
		this.imageBound.y = (this.textLoc.y - this.imageBound.height) + 1;
		g.drawImage(this.currentIcon.getImage(), this.imageBound.x, 0, null);
		if (this.isHighlight()) {
			g.setColor(new Color(70, 153, 207, 50));
			g.fillOval(this.imageBound.x - 1, -1, this.currentIcon.getIconWidth() + 2, this.currentIcon.getIconHeight() + 2);
		}

		// BasicGraphicsUtils.drawBezel(g, imageBound.x, imageBound.y,
		// imageBound.width, imageBound.height,
		// false, false, Color.black, Color.gray, Color.gray , Color.gray);
		g.setFont(font);
		g.setColor(color);
	}

	/**
	 * Method used to reduce the complexity of
	 * {@link #paintVerticalOrientation(Component, Graphics, int, int, int, int)}
	 * 
	 * @param c
	 * @return
	 */
	protected int setTitledBorderJustification(Component c) {
		int justification = this.getTitleJustification();
		if (c.getComponentOrientation().isLeftToRight()) {
			if ((justification == TitledBorder.LEADING) || (justification == TitledBorder.DEFAULT_JUSTIFICATION)) {
				justification = TitledBorder.LEFT;
			} else if (justification == TitledBorder.TRAILING) {
				justification = TitledBorder.RIGHT;
			}
		} else {
			if ((justification == TitledBorder.LEADING) || (justification == TitledBorder.DEFAULT_JUSTIFICATION)) {
				justification = TitledBorder.RIGHT;
			} else if (justification == TitledBorder.TRAILING) {
				justification = TitledBorder.LEFT;
			}
		}
		return justification;
	}

	/**
	 * Method used to reduce the complexity of
	 * {@link #paintVerticalOrientation(Component, Graphics, int, int, int, int)}
	 * 
	 * @param grooveRect
	 * @param stringWidth
	 * @param insets
	 * @param justification
	 */
	protected void setTextJustification(Rectangle grooveRect, int stringWidth, Insets insets, int justification) {
		switch (justification) {
		case LEFT:
			this.textLoc.x = grooveRect.x + TitledBorder.TEXT_INSET_H + insets.left;
			break;
		case RIGHT:
			this.textLoc.x = (grooveRect.x + grooveRect.width) - (stringWidth + TitledBorder.TEXT_INSET_H + insets.right);
			break;
		case CENTER:
			this.textLoc.x = grooveRect.x + ((grooveRect.width - stringWidth) / 2);
			break;
		}
	}

	/**
	 * Method used to reduce the complexity of
	 * {@link #paintVerticalOrientation(Component, Graphics, int, int, int, int)}
	 * 
	 * @param grooveRect
	 * @param fontHeight
	 * @param descent
	 * @param ascent
	 * @param insets
	 * @param titlePos
	 */
	protected void setTitlePosition(Rectangle grooveRect, int fontHeight, int descent, int ascent, Insets insets, int titlePos) {
		int diff;
		switch (titlePos) {
		case ABOVE_TOP:
			diff = (ascent + descent + Math.max(TitledBorder.EDGE_SPACING, TitledBorder.TEXT_SPACING * 2)) - TitledBorder.EDGE_SPACING;
			grooveRect.y += diff;
			grooveRect.height -= diff;
			this.textLoc.y = grooveRect.y - (descent + TitledBorder.TEXT_SPACING);
			break;
		case TOP:
		case DEFAULT_POSITION:
			diff = Math.max(0, ((ascent / 2) + TitledBorder.TEXT_SPACING) - TitledBorder.EDGE_SPACING);
			grooveRect.y += diff;
			grooveRect.height -= diff;
			this.textLoc.y = (grooveRect.y - descent) + ((insets.top + ascent + descent) / 2);
			break;
		case BELOW_TOP:
			this.textLoc.y = grooveRect.y + insets.top + ascent + TitledBorder.TEXT_SPACING;
			break;
		case ABOVE_BOTTOM:
			this.textLoc.y = (grooveRect.y + grooveRect.height) - (insets.bottom + descent + TitledBorder.TEXT_SPACING);
			break;
		case BOTTOM:
			grooveRect.height -= fontHeight / 2;
			this.textLoc.y = ((grooveRect.y + grooveRect.height) - descent) + (((ascent + descent) - insets.bottom) / 2);
			break;
		case BELOW_BOTTOM:
			grooveRect.height -= fontHeight;
			this.textLoc.y = grooveRect.y + grooveRect.height + ascent + TitledBorder.TEXT_SPACING;
			break;
		}
	}

	/**
	 * Method used to reduce the complexity of
	 * {@link #paintVerticalOrientation(Component, Graphics, int, int, int, int)}
	 * 
	 * @param c
	 */
	protected void setCurrentIcon(Component c) {
		CollapsiblePanel panel = null;
		if ((c != null) && (c instanceof CollapsiblePanel)) {
			panel = (CollapsiblePanel) c;
			if (panel.isDeploy()) {
				this.currentIcon = this.upIcon;
			} else {
				this.currentIcon = this.downIcon;
			}
		}
	}

	/**
	 * Override to force the java6 version of this method due to changes in java7 that not retrieves property from UIManager when null.
	 *
	 * @since 5.2079EN-0.1
	 */
	@Override
	public Color getTitleColor() {
		Color c = this.titleColor;
		if (c == null) {
			c = UIManager.getColor("TitledBorder.titleColor");
		}
		return c;
	}

	/**
	 * Override to force the use of java6 version of this method due to changes in java7 that not retrieves property from UIManager when null.
	 *
	 * @since 5.2079EN-0.1
	 */
	@Override
	public Font getTitleFont() {
		Font f = this.titleFont;
		if (f == null) {
			f = UIManager.getFont("TitledBorder.font");
		}
		return f;
	}

	protected String calculateTitle(FontMetrics fm, String title, Rectangle grooveRect) {
		int start = this.textLoc.y;
		int width = fm.stringWidth(title);
		if (start > (grooveRect.y + grooveRect.height)) {
			return "";
		}
		if ((start + width) < (grooveRect.y + grooveRect.height)) {
			return title;
		}

		String resultTemp = title.trim();
		String result = "";
		while (((start + width) > (grooveRect.y + grooveRect.height)) && (resultTemp.length() > 0)) {
			resultTemp = resultTemp.substring(0, resultTemp.length() - 1);
			result = this.previous + resultTemp + "...";
			width = fm.stringWidth(result);
		}

		if (resultTemp.length() == 0) {
			return "   ";
		}
		return result;
	}

	protected void paintHorizontalOrientation(Component c, Graphics g, int x, int y, int width, int height) {

		CollapsiblePanel panel = null;
		if ((c != null) && (c instanceof CollapsiblePanel)) {
			panel = (CollapsiblePanel) c;
			if (panel.isDeploy()) {
				this.currentIcon = this.leftIcon;
			} else {
				this.currentIcon = this.rightIcon;
			}
		}

		Border border = this.getBorder();

		if ((this.getTitle() == null) || this.getTitle().equals("")) {
			if (border != null) {
				border.paintBorder(c, g, x, y, width, height);
			}
			return;
		}

		Rectangle grooveRect = new Rectangle(x + TitledBorder.EDGE_SPACING, y + TitledBorder.EDGE_SPACING, width - (TitledBorder.EDGE_SPACING * 2),
				height - (TitledBorder.EDGE_SPACING * 2));

		Font font = g.getFont();
		Color color = g.getColor();

		g.setFont(this.getFont(c));

		// g.setColor(Color.red);
		// g.drawRect(grooveRect.x, grooveRect.y, grooveRect.width,
		// grooveRect.height);
		// g.setColor(color);

		FontMetrics fm = g.getFontMetrics();

		int descent = fm.getDescent();
		int ascent = fm.getAscent();
		int diff;

		Insets insets;
		if (border != null) {
			insets = border.getBorderInsets(c);
		} else {
			insets = new Insets(0, 0, 0, 0);
		}

		boolean deployed = ((CollapsiblePanel) c).isDeploy();

		diff = ascent / 2;
		grooveRect.x += diff;
		grooveRect.width -= diff;

		this.textLoc.y = grooveRect.y + insets.top + TitledBorder.TEXT_SPACING;
		this.textLoc.x = (grooveRect.x - diff) + 2;

		String currentTitle = this.calculateTitle(fm, this.getTitle(), grooveRect);
		int stringWidth = fm.stringWidth(currentTitle);

		if (border != null) {
			Rectangle clipRect = new Rectangle();
			//
			// // save original clip
			//
			Rectangle saveClip = g.getClipBounds();
			Rectangle deployedClip = g.getClipBounds();

			clipRect.setBounds(saveClip);
			if (!deployed) {
				clipRect.y += 4;
				clipRect.height -= 4;
				clipRect.width = fm.getHeight();
			}

			// paint strip up of text
			if (CollapsiblePanel.computeIntersection(clipRect, x, y, width, this.textLoc.y - 1 - y)) {
				g.setClip(clipRect);
				border.paintBorder(c, g, grooveRect.x, grooveRect.y, grooveRect.width, grooveRect.height);
			}

			// paint strip down of text
			clipRect.setBounds(deployedClip);

			if (!deployed) {
				clipRect.width = fm.getHeight();
				clipRect.height -= 4;
			}

			if (CollapsiblePanel.computeIntersection(clipRect, x, this.textLoc.y + stringWidth + 1, x + width, (y + height) - (this.textLoc.y + stringWidth + 1))) {
				g.setClip(clipRect);
				border.paintBorder(c, g, grooveRect.x, grooveRect.y, grooveRect.width, grooveRect.height);
			}

			clipRect.setBounds(deployedClip);
			if (deployed) {
				if (CollapsiblePanel.computeIntersection(clipRect, this.textLoc.x + ascent + descent, this.textLoc.y - 1, (x + width) - this.textLoc.x - descent,
						stringWidth + 2)) {
					g.setClip(clipRect);
					border.paintBorder(c, g, grooveRect.x, grooveRect.y, grooveRect.width, grooveRect.height);
				}
			}

			// restore clip
			g.setClip(saveClip);
		}

		if (this.isHighlight()) {
			g.setColor(this.getTitleColor().brighter());
		} else {
			g.setColor(this.getTitleColor());
		}

		Graphics2D g2d = (Graphics2D) g;
		this.imageBound.x = this.textLoc.x;
		this.imageBound.y = this.textLoc.y;
		g.drawImage(this.currentIcon.getImage(), this.imageBound.x - 2, this.imageBound.y, null);
		if (this.isHighlight()) {
			g.setColor(new Color(70, 153, 207, 50));
			g.fillOval(this.imageBound.x - 1, this.imageBound.y - 1, this.currentIcon.getIconWidth(), this.currentIcon.getIconHeight());
			g.setColor(color);
		}
		g2d.translate(this.textLoc.x, this.textLoc.y);
		g2d.rotate((90 * Math.PI) / 180);
		g2d.drawString(currentTitle, 0, 0);
		g2d.rotate((-90 * Math.PI) / 180);
		g2d.translate(-this.textLoc.x, -this.textLoc.y);
		// BasicGraphicsUtils.drawBezel(g, imageBound.x, imageBound.y,
		// imageBound.width, imageBound.height,
		// false, false, Color.black, Color.gray, Color.gray , Color.gray);
		g.setFont(font);
		g.setColor(color);
	}

	@Override
	public Rectangle getImageBound() {
		return this.imageBound;
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		FontMetrics fm;
		int descent = 0;
		int ascent = 16;
		int height = 16;

		Border border = this.getBorder();
		if (border != null) {
			if (border instanceof AbstractBorder) {
				((AbstractBorder) border).getBorderInsets(c, insets);
			} else {
				// Can't reuse border insets because the Border interface
				// can't be enhanced.
				Insets i = border.getBorderInsets(c);
				insets.top = i.top;
				insets.right = i.right;
				insets.bottom = i.bottom;
				insets.left = i.left;
			}
		} else {
			insets.left = insets.top = insets.right = insets.bottom = 0;
		}

		insets.left += TitledBorder.EDGE_SPACING + TitledBorder.TEXT_SPACING + (this.margin != null ? this.margin.left : 0);
		insets.right += TitledBorder.EDGE_SPACING + TitledBorder.TEXT_SPACING + (this.margin != null ? this.margin.right : 0);
		insets.top += TitledBorder.EDGE_SPACING + TitledBorder.TEXT_SPACING + (this.margin != null ? this.margin.top : 0);
		insets.bottom += TitledBorder.EDGE_SPACING + TitledBorder.TEXT_SPACING + (this.margin != null ? this.margin.bottom : 0);

		if ((c == null) || (this.getTitle() == null) || this.getTitle().equals("")) {
			return insets;
		}

		Font font = this.getFont(c);

		fm = c.getFontMetrics(font);

		if (fm != null) {
			descent = fm.getDescent();
			ascent = fm.getAscent();
			height = fm.getHeight();
		}

		if (this.isVerticalOrientation()) {
			switch (this.getTitlePosition()) {
			case ABOVE_TOP:
				insets.top += (ascent + descent + Math.max(TitledBorder.EDGE_SPACING, TitledBorder.TEXT_SPACING * 2)) - TitledBorder.EDGE_SPACING;
				break;
			case TOP:
			case DEFAULT_POSITION:
				insets.top += ascent + descent;
				break;
			case BELOW_TOP:
				insets.top += ascent + descent + TitledBorder.TEXT_SPACING;
				break;
			case ABOVE_BOTTOM:
				insets.bottom += ascent + descent + TitledBorder.TEXT_SPACING;
				break;
			case BOTTOM:
				insets.bottom += ascent + descent;
				break;
			case BELOW_BOTTOM:
				insets.bottom += height;
				break;
			}
		} else {
			insets.left += ascent + descent;
		}
		return insets;
	}

	@Override
	public void setMargin(Insets insets) {
		this.margin = insets;
	}

}