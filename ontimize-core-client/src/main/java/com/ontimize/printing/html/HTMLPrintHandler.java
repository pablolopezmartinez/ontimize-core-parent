package com.ontimize.printing.html;

/**
 * Class to print HTML documents. This class tries to separate the document in different pages in an appropriate way
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.Position;
import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLPrintHandler {

	private static final Logger	logger					= LoggerFactory.getLogger(HTMLPrintHandler.class);

	public static boolean DEBUG = true;

	public static boolean DEBUG2 = true;

	protected boolean printJobAccessEnabled = true;

	protected PageFormat pf;

	private HTMLPrintHandler() {}

	private static void debug(String s) {
		if (HTMLPrintHandler.DEBUG) {
			HTMLPrintHandler.logger.debug(s);
		}
	}

	private static void debug2(String s) {
		if (HTMLPrintHandler.DEBUG2) {
			HTMLPrintHandler.logger.debug(s);
		}
	}

	protected static Rectangle getViewRect(View view, float f, float f_21_) {
		Rectangle rectangle = new Rectangle();
		view.setSize(f, f_21_);
		rectangle.width = (int) Math.max((long) Math.ceil(view.getMinimumSpan(0)), (long) f);
		rectangle.height = (int) Math.min((long) Math.ceil(view.getPreferredSpan(1)), 2147483647L);
		view.setSize(rectangle.width, rectangle.height);
		if (view.getView(0) instanceof BoxView) {
			BoxView boxview = (BoxView) view.getView(0);
			rectangle.width = boxview.getWidth();
			rectangle.height = boxview.getHeight();
		} else {
			rectangle.height = (int) Math.min((long) Math.ceil(view.getPreferredSpan(1)), 2147483647L);
		}
		return rectangle;
	}

	class HTMLPrintable implements Printable {

		boolean scaleToFit;

		JFrame frame;

		JEditorPane editor;

		Vector transforms;

		Vector clips;

		public HTMLPrintable(JFrame f, boolean bool) {
			this.scaleToFit = bool;
			this.frame = f;
			this.editor = (JEditorPane) f.getContentPane();
		}

		public Vector createTransforms(JEditorPane jeditorpane, PageFormat pageformat) {
			int i = 0;
			Vector vector = new Vector();
			double d = 0.0;
			double d_0_ = 0.0;
			double d_1_ = 0.0;
			double d_2_ = 1.0;
			View view = jeditorpane.getUI().getRootView(jeditorpane);
			Rectangle rectangle = HTMLPrintHandler.getViewRect(view, (float) pageformat.getImageableWidth(), (float) pageformat.getImageableHeight());
			HTMLPrintHandler.debug("viewRec=" + rectangle);
			Insets insets = jeditorpane.getInsets();
			this.frame.setBounds(0, 0, rectangle.width + insets.left + insets.right, rectangle.height + insets.top + insets.bottom);
			this.frame.setVisible(true);
			if (this.scaleToFit) {
				if (rectangle.getWidth() > pageformat.getImageableWidth()) {
					d_2_ = pageformat.getImageableWidth() / rectangle.getWidth();
				}
				HTMLPrintHandler.debug("scale=" + d_2_ + " ImageableWidth=" + pageformat.getImageableWidth() + " width=" + rectangle.getWidth());
			}
			Rectangle2D.Double var_double = new Rectangle2D.Double(0.0, 0.0, pageformat.getImageableWidth(), pageformat.getImageableHeight() / d_2_);
			HTMLPrintHandler.debug("printRec=" + var_double);
			Position.Bias[] biases = new Position.Bias[1];
			for (;;) {
				HTMLPrintHandler.debug("preparing page=" + i + " curHeight=" + d);
				double d_3_ = var_double.getHeight() + d;
				int i_4_ = view.viewToModel(0.0F, (float) d_3_, rectangle, biases);
				HTMLPrintHandler.debug2("point=" + i_4_);
				try {
					Shape shape = view.modelToView(i_4_, rectangle, biases[0]);
					Rectangle2D rectangle2d = shape.getBounds2D();
					HTMLPrintHandler.debug2("pointRec=" + rectangle2d);
					d_1_ = d;
					d_0_ = rectangle2d.getY() - 1.0;
					HTMLPrintHandler.debug2("Starting height=" + d_0_);
					if (d_3_ >= (rectangle2d.getY() + rectangle2d.getHeight())) {
						d_0_ = (rectangle2d.getY() + rectangle2d.getHeight()) - 1.0;
						HTMLPrintHandler.debug2("Adjusted height=" + d_0_);
					}
					double d_5_ = rectangle2d.getY();
					double d_6_ = (rectangle2d.getY() + rectangle2d.getHeight()) - 1.0;
					double d_7_ = rectangle2d.getX() + 20.0;
					double d_8_ = 0.0;
					double d_9_ = 0.0;
					Rectangle2D rectangle2d_10_ = rectangle2d;
					while (!(d_7_ > (pageformat.getImageableWidth() * d_2_))) {
						int i_11_ = view.viewToModel((float) d_7_, (float) d_3_, rectangle, biases);
						Shape shape_12_ = view.modelToView(i_11_, rectangle, biases[0]);
						Rectangle2D rectangle2d_13_ = shape_12_.getBounds2D();
						if (rectangle2d_10_.equals(rectangle2d_13_) || (rectangle2d_13_.getX() < d_7_)) {
							d_7_ += 20.0;
						} else {
							HTMLPrintHandler.debug2("pointRec2=" + rectangle2d_13_);
							d_8_ = rectangle2d_13_.getY();
							d_9_ = (rectangle2d_13_.getY() + rectangle2d_13_.getHeight()) - 1.0;
							if (d_9_ > d_5_) {
								if (d_9_ > d_6_) {
									if (d_9_ < d_3_) {
										d_0_ = d_9_;
										d_6_ = d_9_;
										if (d_8_ < d_5_) {
											d_5_ = d_8_;
										}
										HTMLPrintHandler.debug2("Adjust height to testheight " + d_0_);
									} else if (d_8_ > d_6_) {
										d_0_ = d_8_ - 1.0;
										d_6_ = d_9_;
										d_5_ = d_8_;
										HTMLPrintHandler.debug2("new base component " + d_0_);
									} else if (d_8_ < d_5_) {
										d_0_ = d_8_ - 1.0;
										d_5_ = d_8_;
										d_6_ = d_9_;
										HTMLPrintHandler.debug2("test height > maxheight. Adjust height testY - 1 " + d_0_);
									} else {
										d_0_ = d_5_ - 1.0;
										d_6_ = d_9_;
										HTMLPrintHandler.debug2("test height > maxheight. Adjust height baseY - 1 " + d_0_);
									}
								} else if (d_6_ < d_3_) {
									d_0_ = d_6_;
									if (d_8_ < d_5_) {
										d_5_ = d_8_;
									}
									HTMLPrintHandler.debug2("baseHeight ok " + d_0_);
								} else if (d_5_ <= d_8_) {
									d_0_ = d_5_ - 1.0;
									HTMLPrintHandler.debug2("baseHeight too long - height ok" + d_0_);
								} else {
									d_0_ = d_8_ - 1.0;
									d_5_ = d_8_;
									HTMLPrintHandler.debug2("baseHeight too long - use testY - 1 " + d_0_);
								}
							}
							rectangle2d_10_ = rectangle2d_13_;
							d_7_ = rectangle2d_10_.getX() + 20.0;
						}
					}
					PageTransform pagetransform = new PageTransform();
					pagetransform.translate(pageformat.getImageableX(), pageformat.getImageableY());
					HTMLPrintHandler.debug("t.translate=" + pagetransform);
					pagetransform.translate(-(double) insets.left * d_2_, -(insets.top + d_1_) * d_2_);
					HTMLPrintHandler.debug("t.translate=" + pagetransform);
					pagetransform.scale(d_2_, d_2_);
					HTMLPrintHandler.debug("t.scale=" + pagetransform);
					pagetransform.setHeight(d_0_ + insets.top);
					vector.add(i, pagetransform);
					d = d_0_ + 1.0;
					HTMLPrintHandler.debug("Setting curHeight=" + d);
					i++;
					if (d >= rectangle.getHeight()) {
						break;
					}
				} catch (BadLocationException badlocationexception) {
					HTMLPrintHandler.logger.trace(null, badlocationexception);
					break;
				}
			}
			return vector;
		}

		@Override
		public int print(Graphics graphics, PageFormat pageformat, int i) {
			Graphics2D graphics2d = (Graphics2D) graphics;
			this.editor.setDropTarget(null);
			if (this.transforms == null) {
				this.transforms = this.createTransforms(this.editor, pageformat);
			}
			HTMLPrintHandler.debug("\n\n\nPrinting page=" + i);
			if (i >= this.transforms.size()) {
				return 1;
			}
			if (graphics2d.getClip() == null) {
				HTMLPrintHandler.debug("Graphics clip=null");
				Rectangle2D.Double var_double = new Rectangle2D.Double(pageformat.getImageableX(), pageformat.getImageableY(), pageformat.getImageableWidth(),
						pageformat.getImageableHeight());
				graphics2d.setClip(var_double);
			}
			HTMLPrintHandler.debug("Graphics tansform=" + graphics2d.getTransform());
			HTMLPrintHandler.debug("Graphics clip=" + graphics2d.getClip());
			graphics2d.transform((AffineTransform) this.transforms.get(i));
			HTMLPrintHandler.debug("Graphics tansform=" + graphics2d.getTransform());
			HTMLPrintHandler.debug("Graphics clip=" + graphics2d.getClip());
			Shape shape = graphics2d.getClip();
			Rectangle2D rectangle2d = shape.getBounds2D();
			double d = ((PageTransform) this.transforms.get(i)).getHeight();
			double d_14_ = (rectangle2d.getY() + rectangle2d.getHeight()) - 1.0 - d;
			if (d_14_ > 0.0) {
				HTMLPrintHandler.debug("Graphics adjusted height=" + d_14_);
				Rectangle2D.Double var_double = new Rectangle2D.Double(rectangle2d.getX(), rectangle2d.getY(), rectangle2d.getWidth(), rectangle2d.getHeight() - d_14_);
				graphics2d.clip(var_double);
				shape = graphics2d.getClip();
				rectangle2d = shape.getBounds2D();
				HTMLPrintHandler.debug("Graphics tansform=" + graphics2d.getTransform());
				HTMLPrintHandler.debug("Graphics clip=" + graphics2d.getClip());
			}
			if (rectangle2d.getY() < d) {
				this.editor.paint(graphics2d);
			} else {
				return 1;
			}
			return 0;
		}
	}

	class PageTransform extends AffineTransform {

		private double height;

		public double getHeight() {
			return this.height;
		}

		public void setHeight(double d) {
			this.height = d;
		}
	}

	class Print1dot2 extends Thread {

		PageFormat pf;

		PrinterJob job;

		JFrame frame;

		JEditorPane editor;

		Print1dot2(PrinterJob printerjob, JEditorPane editorPane) {
			this.setDaemon(true);
			this.editor = editorPane;
			this.pf = HTMLPrintHandler.this.getPF();
			this.job = printerjob;
		}

		@Override
		public void run() {
			try {
				if (this.job.printDialog()) {
					this.frame = new JFrame();
					this.frame.setContentPane(this.editor);
					this.startPrinting();
				}
			} catch (Exception exception) {
				HTMLPrintHandler.logger.error(null, exception);
			}
		}

		public void startPrinting() {
			if (this.job != null) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException interruptedexception) {
					HTMLPrintHandler.logger.error(null, interruptedexception);
				}
				this.job.setPrintable(new HTMLPrintable(this.frame, true), this.pf == null ? this.job.defaultPage() : this.pf);
				try {
					this.job.print();
				} catch (PrinterException printerexception) {
					HTMLPrintHandler.logger.error(null, printerexception);
				}
			}
		}
	}

	class PageDialog extends Thread {

		PrinterJob job;

		PageFormat pf;

		PageDialog(PrinterJob printerjob) {
			this.setDaemon(true);
			this.pf = HTMLPrintHandler.this.getPF();
			this.job = printerjob;
		}

		@Override
		public void run() {
			try {
				PageFormat pageformat = this.job.pageDialog(this.pf == null ? this.job.defaultPage() : this.pf);
				if (this.pf != pageformat) {
					this.pf = pageformat;
					HTMLPrintHandler.this.setPF(this.pf);
				}
			} catch (Exception exception) {
				HTMLPrintHandler.logger.error(null, exception);
			}
		}
	}

	public void pageSetup(PrinterJob printerjob) {
		new PageDialog(printerjob).start();
	}

	public void print(JEditorPane editorPane) {
		PrinterJob printerjob = null;
		if (this.printJobAccessEnabled) {
			try {
				printerjob = PrinterJob.getPrinterJob();
			} catch (SecurityException securityexception) {
				HTMLPrintHandler.logger.error(null, securityexception);
				this.printJobAccessEnabled = false;
			}
		}
		if (printerjob != null) {
			new Print1dot2(printerjob, editorPane).start();
		}
	}

	public PageFormat getPF() {
		synchronized (this) {
			return this.pf;
		}
	}

	public void setPF(PageFormat pageformat) {
		synchronized (this) {
			this.pf = pageformat;
		}
	}

	/**
	 * Prints the document with the specified html code. Here the document codebase is not established then relative paths, like images, do not appear
	 *
	 * @param html
	 * @throws Exception
	 */
	public synchronized static void printDocument(String html) throws Exception {
		HTMLPrintHandler.printDocument(html, null);
	}

	public synchronized static void printDocument(String html, URL base) throws Exception {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setDoubleBuffered(false);
		HTMLEditorKit editorKit = new HTMLEditorKit();
		HTMLDocument doc = new HTMLDocument();
		if (base != null) {
			doc.setBase(base);
		}
		editorPane.setEditorKit(editorKit);
		editorPane.setDocument(doc);
		editorPane.read(new StringReader(html), null);
		// Start the printin job
		HTMLPrintHandler ph = new HTMLPrintHandler();
		ph.print(editorPane);
	}

	public synchronized static void printDocument(URL page) {}

	public synchronized static void printDocument(File f) throws Exception {
		HTMLPrintHandler.printDocument(f, null);
	}

	public synchronized static void printDocument(File f, URL base) throws Exception {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setDoubleBuffered(false);
		HTMLEditorKit editorKit = new HTMLEditorKit();
		HTMLDocument doc = new HTMLDocument();
		if (base != null) {
			doc.setBase(base);
		}
		editorPane.setEditorKit(editorKit);
		editorPane.setDocument(doc);
		editorPane.read(new FileReader(f), null);
		// Start the printing job
		HTMLPrintHandler ph = new HTMLPrintHandler();
		ph.print(editorPane);
	}

	public static void main(String args[]) throws Exception {
		JFileChooser fc = new JFileChooser();
		int iOption = fc.showOpenDialog(null);
		if (iOption == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			HTMLPrintHandler.printDocument(f);
		}
	}

}