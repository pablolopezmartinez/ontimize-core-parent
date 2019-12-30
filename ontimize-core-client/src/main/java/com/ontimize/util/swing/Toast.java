package com.ontimize.util.swing;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.ColorConstants;
import com.ontimize.util.AWTUtilities;
import com.ontimize.util.ParseUtils;

public class Toast extends JPanel {

	private static final Logger	logger				= LoggerFactory.getLogger(Toast.class);

	public static final int ERROR_MESSAGE = 0;

	public static final int INFORMATION_MESSAGE = 1;

	public static final int WARNING_MESSAGE = 2;

	public static String backgroundColor = "#E3E3E3";

	protected String text;
	protected int time;
	protected ResourceBundle resourceBundle;
	protected int type = 1;

	public Toast(String text, int type, ResourceBundle bundle, int time) {
		this.text = text;
		this.time = time;
		this.resourceBundle = bundle;
		this.type = type;
		this.initComponents();
	}

	protected int getTime() {
		return this.time;
	}

	private void initComponents() {
		this.setLayout(new GridBagLayout());
		this.setOpaque(false);
		String textToShow = this.text != null ? ApplicationManager.getTranslation(this.text, this.resourceBundle) : "";
		JLabel label = new JLabel(textToShow);
		label.setFont(this.createFont());
		label.setForeground(this.createForegroundColor(this.type));
		label.setOpaque(false);
		this.add(label, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(30, 20, 30, 20), 0, 0));
	}

	protected Font createFont() {
		Font font = new Font("Arial", Font.BOLD, 14);
		return font;
	}

	protected Color createBackgroundColor() {
		try {
			return ColorConstants.parseColor(Toast.backgroundColor);
		} catch (Exception e) {
			Toast.logger.error(null, e);
		}
		return Color.white;
	}

	protected Color createForegroundColor(int type) {
		try {
			if (Toast.ERROR_MESSAGE == type) {
				return ColorConstants.parseColor("#E60000");
			} else if (Toast.INFORMATION_MESSAGE == type) {
				return ColorConstants.parseColor("#669900");
			} else if (Toast.WARNING_MESSAGE == type) {
				return ColorConstants.parseColor("#5B5B5B");
			}
		} catch (Exception e) {
			Toast.logger.error(null, e);
		}
		return Color.red;
	}

	public ResourceBundle getResourceBundle() {
		return this.resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	protected static Window getWindowForComponent(Component parentComponent) {
		if (parentComponent == null) {
			// return getRootFrame();
			return null;
		}
		if ((parentComponent instanceof Frame) || (parentComponent instanceof Dialog)) {
			return (Window) parentComponent;
		}
		return SwingUtilities.getWindowAncestor(parentComponent);
	}

	protected JDialog createDialog(Component parentComponent) throws HeadlessException {

		final JDialog dialog;

		Window window = Toast.getWindowForComponent(parentComponent);
		if (window instanceof Frame) {
			dialog = new ToastDialog((Frame) window);
		} else {
			dialog = new ToastDialog((Dialog) window);
		}

		this.initDialog(dialog, parentComponent);
		return dialog;
	}

	protected void initDialog(JDialog dialog, Component parentComponent) {

		// AWTUtilities.setWindowOpacity(dialog, 0.7f);

		ContentDialog contentPane = new ContentDialog();
		contentPane.setBorder(new BorderDialog());
		dialog.setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		contentPane.add(this, BorderLayout.CENTER);
		contentPane.setBackground(this.createBackgroundColor());

		dialog.setUndecorated(true);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setLocationRelativeTo(parentComponent);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		AWTUtilities.setWindowOpaque(dialog, false);
		// AWTUtilities.setWindowShape(dialog, new
		// RoundRectangle2D.Float(0,0,dialog.getWidth(),dialog.getHeight(),10,10));
	}

	public static void showMessage(Component parentComponent, String message, ResourceBundle bundle, int time) {
		Toast mainPane = new Toast(message, Toast.INFORMATION_MESSAGE, bundle, time);
		JDialog dialog = mainPane.createDialog(parentComponent);
		dialog.setVisible(true);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toast.showMessage(f, "Display", null, 5000);
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			Toast.logger.trace(null, e);
		}
	}

	protected static Color[] borderColors = new Color[] { ParseUtils.getColor("#0000000F", Color.black), ParseUtils.getColor("#0000001E", Color.black), ParseUtils
			.getColor("#0000002D", Color.black), ParseUtils.getColor("#0000003C", Color.black), ParseUtils.getColor("#0000004B", Color.black), };

	protected class ContentDialog extends JPanel {

		@Override
		protected void paintComponent(Graphics g) {
			Color color = this.getBackground();
			Color previousColor = g.getColor();
			Insets insets = this.getInsets();
			Shape center = new RoundRectangle2D.Float(insets.left, insets.top, this.getWidth() - insets.left - insets.right, this.getHeight() - insets.top - insets.bottom, 10, 10);
			g.setColor(color);
			((Graphics2D) g).fill(center);
			g.setColor(previousColor);
		}
	}

	protected class BorderDialog implements Border {

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

			Graphics2D g2D = (Graphics2D) g;

			Color previousColor = g.getColor();
			Paint previousPaint = g2D.getPaint();
			RenderingHints rh = g2D.getRenderingHints();
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// outer
			int h = height;
			int w = width;
			GeneralPath path = new GeneralPath();
			g2D.setStroke(new BasicStroke(1));
			for (int i = 0; i < Toast.borderColors.length; i++) {
				g2D.setColor(Toast.borderColors[i]);
				path.reset();
				path.moveTo(i, 10);
				path.curveTo(i, 10, i, i, 10, i);
				path.lineTo(w - 10, i);
				path.curveTo(w - 10, i, w - i, i, w - i, 10);

				path.lineTo(w - i, h - 10);
				path.curveTo(w - i, h - 10, w - i, h - i, w - 10, h - i);
				path.lineTo(10, h - i);
				path.curveTo(10, h - i, i, h - i, i, h - 10);
				path.closePath();

				g2D.draw(path);
			}

			// inner
			g.setColor(ParseUtils.getColor("#b1b1b1", null));
			g2D.setStroke(new BasicStroke(4));

			int stroke = 4;
			g2D.translate(x + 5, y + 5);

			h = height - 10;
			w = width - 10;

			path.reset();
			path.moveTo(0, 5);
			path.curveTo(0, 5, 0, 0, 5, 0);
			path.lineTo(w - 5, 0);
			path.curveTo(w - 5, 0, w, 0, w, 5);

			path.lineTo(w, h - 5);
			path.curveTo(w, h - 5, w, h, w - 5, h);
			path.lineTo(5, h);
			path.curveTo(5, h, 0, h, 0, h - 5);
			path.closePath();

			g2D.draw(path);

			g2D.translate(-x - 5, -y - 5);

			g2D.setPaint(previousPaint);
			g2D.setRenderingHints(rh);
			g.setColor(previousColor);
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(6, 6, 6, 6);
		}

		@Override
		public boolean isBorderOpaque() {
			return true;
		}
	}

	protected class ToastDialog extends JDialog {

		protected Component previousGlassPane = null;

		public ToastDialog(Frame owner) {
			super(owner, true);
		}

		public ToastDialog(Dialog owner) {
			super(owner, true);
		}

		@Override
		public void setVisible(boolean b) {
			if (b) {
				Timer timer = new Timer(Toast.this.getTime(), new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						ToastDialog.this.setVisible(false);
					}
				});
				timer.setRepeats(false);
				timer.start();
			}

			if (!OGlassPanel.disable) {
				if (b) {
					if (this.isModal() && (this.getOwner() instanceof RootPaneContainer)) {

						if (this.previousGlassPane == null) {
							this.previousGlassPane = ((RootPaneContainer) this.getOwner()).getGlassPane();
							this.previousGlassPane.setVisible(false);
							((RootPaneContainer) this.getOwner()).setGlassPane(new OGlassPanel());
							((RootPaneContainer) this.getOwner()).getGlassPane().setVisible(true);
							((RootPaneContainer) this.getOwner()).getGlassPane().repaint();
							// ((Container)this.getOwner()).validate();
						}
					}
				} else {
					try {
						if (this.previousGlassPane != null) {
							((RootPaneContainer) this.getOwner()).getGlassPane().setVisible(false);
							((RootPaneContainer) this.getOwner()).setGlassPane(this.previousGlassPane);
							this.previousGlassPane = null;
						}
					} catch (Exception ex) {
						Toast.logger.trace(null, ex);
					}
				}
			}
			super.setVisible(b);
		}
	}
}