package com.ontimize.util.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.images.ImageManager;

public class PrintablePivotTable implements Printable {

	private static final Logger	logger					= LoggerFactory.getLogger(PrintablePivotTable.class);

	public static String headerImage = "com/ontimize/report/imatia1.gif";
	public static ImageIcon baseImageIcon;

	protected ImageIcon headerImageIcon;

	private final int headerHeight = 60;
	private final int footerHeight = 25;

	private final JTable fixedColumnTable;
	private final JTable contentTable;

	private final int verticalMargin = 0;

	private int[] pageIndexes = null;
	protected int[] columnPageIndexes;
	protected int totalFixedColumnWidth = 0;
	protected int totalContentColumnWidth = 0;

	private PageFormat pf = null;
	private double scale = 1.0;
	private final PrinterJob pj = PrinterJob.getPrinterJob();
	private String pageTitle;
	private String pageSubtitle;
	private boolean isPrinting = false;
	private final int MIN_PAGE_HEIGHT = 150;
	protected boolean fitsInPage = true;
	protected int totalWidth = -1;
	protected String date = null;
	protected int printingPage = -1;

	protected SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();

	public PrintablePivotTable(JTable fixedColumnTable, JTable contentTable, boolean scaleToFit) {
		this.df.applyPattern("HH:mm  dd/MM/yyyy");
		this.date = this.df.format(new java.util.Date());
		this.fixedColumnTable = fixedColumnTable;
		this.fixedColumnTable.setAutoCreateColumnsFromModel(false);
		this.contentTable = contentTable;
		this.pf = this.pj.defaultPage();
		if (PrintablePivotTable.baseImageIcon != null) {
			this.headerImageIcon = PrintablePivotTable.baseImageIcon;
		} else {
			this.headerImageIcon = ImageManager.getIcon(PrintablePivotTable.headerImage);
		}
	}

	public JTable getFixedColumnTable() {
		return this.fixedColumnTable;
	}

	public JTable getContentTable() {
		return this.contentTable;
	}

	public void cancelPrinting() {
		this.pj.cancel();
	}

	public void setPageTitle(String t) {
		this.pageTitle = t;
	}

	public void setPageSubtitle(String t) {
		this.pageSubtitle = t;
	}

	public void setScale(double scale) {
		this.scale = scale;
		try {
			this.pageIndexes = this.preparePages(this.pf);
		} catch (Exception e) {
			PrintablePivotTable.logger.error(null, e);
		}
	}

	public PageFormat getPageFormat() {
		return this.pf;
	}

	public int setPageFormat(PageFormat pf) {
		if (pf.getImageableHeight() < this.MIN_PAGE_HEIGHT) {
			return 0;
		}
		this.pf = pf;
		try {
			this.pageIndexes = this.preparePages(pf);
		} catch (Exception e) {
			PrintablePivotTable.logger.error(null, e);
			return 0;
		}
		return this.pageIndexes.length;
	}

	private int preparePagesForPrinting(PageFormat pf) {
		if (pf.getImageableHeight() < this.MIN_PAGE_HEIGHT) {
			return 0;
		}
		this.pf = pf;
		try {
			this.pageIndexes = this.preparePages(pf);
		} catch (Exception e) {
			PrintablePivotTable.logger.error(null, e);
			return 0;
		}
		return this.pageIndexes.length;
	}

	public boolean fitsInPage() {
		return this.fitsInPage;
	}

	public boolean fitsInPage(int[] columnsWidth) {
		// int totalWidth = 0;
		// for (int i = 0; i < columnsWidth.length; i++) {
		// totalWidth += columnsWidth[i];
		// }
		//
		// PrintablePivotTable.logger.debug("Total width required by table: {}", totalWidth);
		// PrintablePivotTable.logger.debug("Available width: {}", ((int) (this.pf.getImageableWidth())));
		//
		// if (totalWidth > ((int) (this.pf.getImageableWidth()))) {
		// return false;
		// } else {
		// return true;
		// }
		return true;
	}

	private boolean fitsInPage(PageFormat pf) {

		// int[] columnsWidth = TableUtils.getPreferredColumnsWidth(this.fixedColumnTable);
		// // Calculate the total width
		// this.totalWidth = 0;
		// for (int i = 0; i < columnsWidth.length; i++) {
		// this.totalWidth += columnsWidth[i];
		// }
		//
		// PrintablePivotTable.logger.debug("Total width needed: {}", this.totalWidth);
		// PrintablePivotTable.logger.debug("Available width: {}", ((int) (pf.getImageableWidth())));
		//
		// if (this.totalWidth > ((int) (pf.getImageableWidth()))) {
		// if (this.pf.equals(pf)) {
		// this.fitsInPage = false;
		// }
		// return false;
		// } else {
		// if (this.pf.equals(pf)) {
		// this.fitsInPage = true;
		// }
		// return true;
		// }
		return true;
	}

	public int getTotalWidth() {
		return this.totalWidth;
	}

	public int getAvaliableWidth() {
		return (int) ((int) this.pf.getImageableWidth() * this.scale);
	}

	private void adjustToPage() {
		// TODO
		this.fixedColumnTable.setBounds(0, 0, this.totalFixedColumnWidth, this.fixedColumnTable.getPreferredSize().height);
		this.fixedColumnTable.getTableHeader().setSize(this.totalFixedColumnWidth, this.fixedColumnTable.getTableHeader().getPreferredSize().height);
		this.fixedColumnTable.doLayout();

		this.contentTable.setBounds(0, 0, this.totalContentColumnWidth, this.fixedColumnTable.getPreferredSize().height);
		this.contentTable.getTableHeader().setSize(this.totalContentColumnWidth, this.fixedColumnTable.getTableHeader().getPreferredSize().height);
		this.contentTable.doLayout();

	}

	private int[] preparePages(PageFormat pf) throws Exception {
		long t = System.currentTimeMillis();
		if (pf.getImageableHeight() < this.MIN_PAGE_HEIGHT) {
			return new int[1];
		}
		this.pf = pf;

		Thread.yield();
		this.fixedColumnTable.setDoubleBuffered(false);
		this.contentTable.setDoubleBuffered(false);

		PrintablePivotTable.logger.trace("PrintableTable Establishing columns width: Used memory: {}  kbytes",
				(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0);

		this.calculateColumnsWidth();
		// int[] iColumnsWidth =
		// TableUtils.getPreferredColumnsWidth(this.fixedColumnTable);

		PrintablePivotTable.logger.trace("PrintableTable established column width: Used memory: {} kbytes",
				(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0);

		// this.fitsInPage = this.fitsInPage(iColumnsWidth);
		Thread.yield();

		this.adjustToPage();

		int iStimatedPageNumber = (int) (this.fixedColumnTable.getPreferredSize().height / this.getImageableHeight());
		if (iStimatedPageNumber == 0) {
			iStimatedPageNumber++;
		}

		Thread.yield();
		int iRowsCount = this.fixedColumnTable.getRowCount();
		if (iRowsCount == 0) {
			return new int[0];
		}
		// TODO
		// if (this.fixedColumnTable.getColumnCount() <= 1) {
		// return new int[0];
		// }
		int iHeaderHeight = this.fixedColumnTable.getTableHeader().getHeight();

		PrintablePivotTable.logger.debug("Header height = {}", iHeaderHeight);

		// Calculates the index of the first row in the page.
		// If the top row is between two pages then print it, but the bottom
		// row is printed in the next page
		int iPageCount = 0;
		int iLastPageRowIndex = -1;
		int[] iLastpageRowsIndexes = new int[iStimatedPageNumber * 2];
		for (int i = 0; i < iLastpageRowsIndexes.length; i++) {
			iLastpageRowsIndexes[i] = -1;
		}

		PrintablePivotTable.logger.trace("PrintableTable searching last rows: Used memory: {} kbytes",
				(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0);

		while (iLastPageRowIndex < (iRowsCount - 1)) {
			int iButtonPagePosition = (iPageCount + 1) * ((int) (this.getImageableHeight() - iHeaderHeight - this.verticalMargin - this.getFooterHeight() - this.getHeaderHeight()));
			int iStimatedLastPageRowIndex = this.fixedColumnTable.rowAtPoint(new Point(0, iButtonPagePosition));
			if (iStimatedLastPageRowIndex > 0) {
				iLastPageRowIndex = iStimatedLastPageRowIndex - 1;
			}

			long tIni = System.currentTimeMillis();
			for (int i = iLastPageRowIndex; i < iRowsCount; i++) {
				Thread.yield();

				Rectangle rect = this.fixedColumnTable.getCellRect(i, 1, true);
				int lastRowPosition = rect.y + rect.height;
				if (lastRowPosition > iButtonPagePosition) {
					PrintablePivotTable.logger.trace("Page time: {} ", iPageCount + " " + (System.currentTimeMillis() - tIni));
					// This row goes to the next page
					iLastPageRowIndex = i - 1;
					if (iPageCount >= (iLastpageRowsIndexes.length - 1)) {
						// Resize
						int[] newBuffer = new int[iLastpageRowsIndexes.length * 2];
						for (int a = iLastpageRowsIndexes.length; a < newBuffer.length; a++) {
							newBuffer[a] = -1;
						}
						System.arraycopy(iLastpageRowsIndexes, 0, newBuffer, 0, iLastpageRowsIndexes.length);
						iLastpageRowsIndexes = newBuffer;
					}
					iLastpageRowsIndexes[iPageCount] = iLastPageRowIndex;
					iPageCount++;
					PrintablePivotTable.logger.debug("Page ready {}. Last row= {}", iPageCount, iLastPageRowIndex);
					break;
				}
				iLastPageRowIndex = i;
				iLastpageRowsIndexes[iPageCount] = iLastPageRowIndex;
			}
		}

		PrintablePivotTable.logger.trace("PrintableTable has search the maximum number of rows: Memory used: {} kbytes",
				(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0);

		PrintablePivotTable.logger.debug("Number of pages to print table= {}", iPageCount);

		// Search until index -1
		int[] index = new int[iPageCount + 1];
		for (int i = 0; i < (iPageCount + 1); i++) {
			index[i] = iLastpageRowsIndexes[i];
		}

		PrintablePivotTable.logger.trace("Time to {} print pages: {}", index.length, System.currentTimeMillis() - t);
		this.pageIndexes = index;
		return index;
	}

	public int getNumberOfPages() {
		return this.pageIndexes.length * this.columnPageIndexes.length;
	}

	public int getFontSize() {
		return this.fixedColumnTable.getFont().getSize();
	}

	public void setFontSize(int fontSize) {
		this.fixedColumnTable.getTableHeader().setFont(this.fixedColumnTable.getFont().deriveFont((float) fontSize));
		this.fixedColumnTable.setFont(this.fixedColumnTable.getFont().deriveFont((float) fontSize));
		this.preparePagesForPrinting(this.pf);
	}

	public int pageSetup() {
		PageFormat pfPrevious = this.pf;
		this.pf = this.pj.pageDialog(this.pf);
		if (this.pf != pfPrevious) {
			this.setPageFormat(this.pf);
		}
		return this.pageIndexes.length * this.columnPageIndexes.length;
	}

	public void setData(TableModel model, int rows) {
		TableModel oldModel = this.fixedColumnTable.getModel();
		if (model != oldModel) {
			this.fixedColumnTable.setModel(model);
			this.contentTable.setModel(model);

			while (this.fixedColumnTable.getColumnCount() > 0) {
				TableColumn column = this.fixedColumnTable.getColumnModel().getColumn(0);
				this.fixedColumnTable.getColumnModel().removeColumn(column);
			}

			for (int i = 0; i < rows; i++) {
				TableColumnModel columnModel = this.contentTable.getColumnModel();
				TableColumn column = columnModel.getColumn(0);
				columnModel.removeColumn(column);
				this.fixedColumnTable.getColumnModel().addColumn(column);
			}
		}
	}

	public void setColumnWidth(TableColumnModel fixedColumns, TableColumnModel contentColumns) {
		for (int i = 0; i < fixedColumns.getColumnCount(); i++) {
			TableColumn column = fixedColumns.getColumn(i);
			this.fixedColumnTable.getColumnModel().getColumn(i).setWidth(column.getWidth());
			this.fixedColumnTable.getColumnModel().getColumn(i).setPreferredWidth(column.getWidth());
		}

		for (int i = 0; i < contentColumns.getColumnCount(); i++) {
			TableColumn column = contentColumns.getColumn(i);
			this.contentTable.getColumnModel().getColumn(i).setWidth(column.getWidth());
			this.contentTable.getColumnModel().getColumn(i).setPreferredWidth(column.getWidth());
		}
	}

	public void moveColumnLeft(Object col) {
		TableColumnModel m = this.fixedColumnTable.getColumnModel();

		int previousIndex = m.getColumnIndex(col);
		if (previousIndex > 0) {
			m.moveColumn(previousIndex, previousIndex - 1);
		}
	}

	public void moveColumnRight(Object col) {
		TableColumnModel m = this.fixedColumnTable.getColumnModel();
		int previousIndex = m.getColumnIndex(col);
		if (previousIndex < (m.getColumnCount() - 1)) {
			m.moveColumn(previousIndex, previousIndex + 1);
		}
	}

	public boolean isPrinting() {
		return this.isPrinting;
	}

	public boolean print(String jobName) throws Exception {
		this.isPrinting = true;
		if (jobName != null) {
			this.pj.setJobName(jobName);
		}
		if (this.pj.printDialog()) {
			this.date = this.df.format(new java.util.Date());
			try {
				if (this.pf.getImageableHeight() < this.MIN_PAGE_HEIGHT) {
					return false;
				}
				Thread.yield();
				this.pj.setPrintable(this, this.pf);
				Thread.yield();
				this.pj.print();
				this.isPrinting = false;
			} finally {
				this.isPrinting = false;

			}
			return true;
		} else {
			this.isPrinting = false;
			return false;
		}
	}

	public boolean print() throws Exception {
		return this.print((String) null);
	}

	private int printInPage(Graphics g, PageFormat pf, int pageIndex, double scale) {
		if (pageIndex >= this.getNumberOfPages()) {
			return Printable.NO_SUCH_PAGE;
		}
		this.printingPage = pageIndex;
		try {
			Thread.sleep(10);
		} catch (Exception e) {
			PrintablePivotTable.logger.trace(null, e);
		}
		PrintablePivotTable.logger.debug("Printing page= {}", pageIndex + 1);

		int columnIndex = pageIndex % this.columnPageIndexes.length;
		pageIndex = pageIndex / this.columnPageIndexes.length;

		int fistPageColumnIndex = this.columnPageIndexes[columnIndex];
		int lastPageColumnIndex = 0;
		if (columnIndex >= (this.columnPageIndexes.length - 1)) {
			lastPageColumnIndex = this.contentTable.getColumnCount();
		} else {
			lastPageColumnIndex = this.columnPageIndexes[columnIndex + 1] - 1;
		}

		int tableHeaderHeight = this.fixedColumnTable.getTableHeader().getHeight();
		// Print the rows:
		int firstPageRowIndex = 0;
		int lastPageRowIndex = 0;
		if (pageIndex > 0) {
			firstPageRowIndex = this.pageIndexes[pageIndex - 1] + 1;
		}
		lastPageRowIndex = this.pageIndexes[pageIndex];
		// Print
		PrintablePivotTable.logger.debug("Printint rows from: {} to ", firstPageRowIndex, lastPageRowIndex);

		Graphics2D g2D = (Graphics2D) g;
		g2D.translate(pf.getImageableX(), pf.getImageableY());
		// TODO see
		// https://stackoverflow.com/questions/30792089/java-graphics2d-translate-and-scale

		g2D.scale(scale, scale);
		g2D.setColor(Color.white);
		g2D.fillRect(0, 0, (int) pf.getImageableWidth(), (int) pf.getImageableHeight());
		this.printHeader(g2D);
		this.printContent(g2D, firstPageRowIndex, lastPageRowIndex, fistPageColumnIndex, lastPageColumnIndex);
		this.printFooter(g2D, this.printingPage);
		return Printable.PAGE_EXISTS;
	}

	protected void printContent(Graphics2D g2D, int firstPageRowIndex, int lastPageRowIndex, int firstPageColumnIndex, int lastPageColumnIndex) {
		Graphics2D fixed2D = (Graphics2D) g2D.create();
		fixed2D.translate(0, this.headerHeight);
		this.fixedColumnTable.getTableHeader().paint(fixed2D);
		fixed2D.translate(0, this.fixedColumnTable.getTableHeader().getHeight());
		int firstRowFirstPixel = this.fixedColumnTable.getCellRect(firstPageRowIndex, 0, true).y;
		int lastRowLastPixel = this.fixedColumnTable.getCellRect(lastPageRowIndex, 0, true).y + this.fixedColumnTable.getCellRect(lastPageRowIndex, 0, true).height;
		fixed2D.translate(0, -firstRowFirstPixel);

		Rectangle clip = fixed2D.getClipBounds();
		int heightClip = lastRowLastPixel - firstRowFirstPixel;
		int heightAvailableClip = clip.height + clip.y;
		int resultHeightClip = 0;
		if (heightAvailableClip > heightClip) {
			resultHeightClip = heightClip;
		} else {
			resultHeightClip = heightAvailableClip;
		}
		fixed2D.setClip(clip.x, firstRowFirstPixel, clip.width, resultHeightClip);

		this.fixedColumnTable.paint(fixed2D);
		fixed2D.dispose();

		// Content table....
		Graphics2D content2D = (Graphics2D) g2D.create();
		content2D.translate(this.totalFixedColumnWidth, this.headerHeight);

		int firstColumnFirstPixel = this.contentTable.getCellRect(0, firstPageColumnIndex, true).x;
		int lastColumnLastPixel = this.contentTable.getCellRect(0, lastPageColumnIndex, true).x + this.contentTable.getCellRect(0, lastPageColumnIndex, true).width;

		content2D.translate(-firstColumnFirstPixel, 0);
		clip = content2D.getClipBounds();
		int widthClip = lastColumnLastPixel - firstColumnFirstPixel;
		int widthAvailableClip = clip.width + clip.x;
		if (widthAvailableClip > widthClip) {
			clip.width = widthClip;
		} else {
			clip.width = widthAvailableClip;
		}

		content2D.setClip(firstColumnFirstPixel, clip.y, clip.width, clip.height);
		this.contentTable.getTableHeader().paint(content2D);
		content2D.translate(0, this.contentTable.getTableHeader().getHeight());
		content2D.translate(0, -firstRowFirstPixel);

		content2D.setClip(firstColumnFirstPixel, firstRowFirstPixel, clip.width, resultHeightClip);
		this.contentTable.paint(content2D);
		content2D.dispose();
	}

	protected void printFooter(Graphics2D g2D, int pageIndex) {
		Graphics2D footerG2D = (Graphics2D) g2D.create();
		footerG2D.setColor(Color.WHITE);

		footerG2D.fillRect(0, (int) ((int) this.getImageableHeight() - this.getFooterHeight()), (int) this.getImageableWidth(), (int) this.getFooterHeight());

		Thread.yield(); // Page number
		StringBuilder pageNumberText = new StringBuilder();
		pageNumberText.append(pageIndex + 1);
		pageNumberText.append(" / ");
		pageNumberText.append(this.getNumberOfPages());

		String textoNP = pageNumberText.toString();
		footerG2D.setColor(Color.black);
		footerG2D.drawLine(0, (int) (this.getImageableHeight() - this.getFooterHeight()), (int) (this.getImageableWidth()),
				(int) (this.getImageableHeight() - this.getFooterHeight()));
		Font font = g2D.getFont();
		footerG2D.setFont(font.deriveFont(font.getSize() - 2l));

		footerG2D.drawString(textoNP, (int) (this.getImageableWidth() + ((footerG2D.getFontMetrics().stringWidth(textoNP)))) / 2,
				(int) (this.getImageableHeight() - (footerG2D.getFontMetrics().getHeight())));

		footerG2D.drawString(this.date, (int) (10 / this.scale), (int) (this.getImageableHeight() - (footerG2D.getFontMetrics().getHeight())));
		footerG2D.dispose();
		Thread.yield();

	}

	protected double getImageableWidth() {
		return this.pf.getImageableWidth() / this.scale;
	}

	protected double getImageableHeight() {
		return this.pf.getImageableHeight() / this.scale;
	}

	protected double getHeaderHeight() {
		return this.headerHeight;
	}

	protected double getFooterHeight() {
		return this.footerHeight;
	}

	protected void printHeader(Graphics2D g2D) {
		Graphics2D headerG2D = (Graphics2D) g2D.create();
		headerG2D.setColor(Color.white);
		headerG2D.fillRect(0, 0, (int) this.getImageableWidth(), (int) this.getHeaderHeight());

		int headerHeight2 = (int) this.getHeaderHeight() / 2;
		// Title
		if (this.pageTitle != null) {
			headerG2D.setColor(Color.black);
			Font titleFont = g2D.getFont().deriveFont(Font.BOLD).deriveFont(g2D.getFont().getSize() + 2L);

			headerG2D.setFont(titleFont);
			FontMetrics fontMetrics = headerG2D.getFontMetrics(titleFont);
			Rectangle2D bounds = fontMetrics.getStringBounds(this.pageTitle, 0, this.pageTitle.length(), headerG2D);

			int posY = (int) (headerHeight2 + bounds.getHeight()) / 2;

			headerG2D.drawString(this.pageTitle, 10, posY);
		}

		if (this.pageSubtitle != null) {
			headerG2D.setColor(Color.black);
			Font subTitleFont = g2D.getFont().deriveFont(Font.ITALIC).deriveFont(g2D.getFont().getSize() - 1L);
			headerG2D.setFont(subTitleFont);
			FontMetrics fontMetrics = headerG2D.getFontMetrics(subTitleFont);
			Rectangle2D bounds = fontMetrics.getStringBounds(this.pageSubtitle, 0, this.pageSubtitle.length(), headerG2D);
			int posY = (int) (headerHeight2 + bounds.getHeight());
			headerG2D.drawString(this.pageSubtitle, 10, posY);
		}

		if (this.headerImageIcon != null) {
			Image image = this.headerImageIcon.getImage();
			int imageHeight = image.getHeight(null);
			int imageWidth = 0;

			int headerImageHeight = this.headerHeight - 10;

			if (imageHeight > headerImageHeight) {
				float scale = (float) headerImageHeight / imageHeight;
				imageHeight = headerImageHeight;
				imageWidth = (int) (image.getWidth(null) * scale);
			} else {
				imageWidth = image.getWidth(null);
			}

			int dx1 = (int) this.getImageableWidth() - imageWidth;
			int dy1 = 0;
			int dx2 = (int) this.getImageableWidth();
			int dy2 = imageHeight;
			g2D.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, image.getWidth(null), image.getHeight(null), null);
		}
	}

	@Override
	public int print(Graphics g, PageFormat pf, int pageIndex) {
		return this.printInPage(g, pf, pageIndex, this.scale);
	}

	public String fitPage() {
		if (this.getImageableHeight() < this.MIN_PAGE_HEIGHT) {
			return "Page height is too short !!";
		}
		try {
			// If font size is 6 and there is not enough space then out.
			this.setFontSize(10);
			// int total = getNumberOfPages();
			if (!this.fitsInPage()) {
				int iTotalWidth = this.getTotalWidth();
				int iAvailableWidth = this.getAvaliableWidth();
				if (iTotalWidth > (iAvailableWidth * (10 / 8))) {
					this.setFontSize(6);
					// total = this.getNumberOfPages();
					if (!this.fitsInPage()) {
						return "Need more space";
					}
				} else {
					this.setFontSize(8);
					// total = this.getNumberOfPages();
					if (!this.fitsInPage()) {
						this.setFontSize(6);
						// total = this.getNumberOfPages();
						if (!this.fitsInPage()) {
							return "Need more space";
						}
					}
				}
			}
			this.adjustToPage();
			return null;
		} catch (OutOfMemoryError error) {
			PrintablePivotTable.logger.error("Memory Error: Trying free", error);
			return error.getMessage();
		}
	}

	protected void calculateColumnsWidth() throws Exception {
		// Calculates the minimum table width to ensure that the values are
		// visible
		// Fit the columns
		int[] fixedColumnWidth = new int[this.fixedColumnTable.getColumnCount()];
		int[] contentColumnWidth = new int[this.contentTable.getColumnCount()];

		double printableWidth = this.pf.getImageableWidth() / this.scale;

		try {
			this.totalFixedColumnWidth = 0;
			for (int i = 0; i < this.fixedColumnTable.getColumnCount(); i++) {
				String sName = this.fixedColumnTable.getColumnName(i);
				TableColumn tcColumns = this.fixedColumnTable.getColumn(sName);
				fixedColumnWidth[i] = this.getPreferredColumnWidth(this.fixedColumnTable, i);
				this.totalFixedColumnWidth = this.totalFixedColumnWidth + fixedColumnWidth[i];
			}

			int maxContentColumnWidth = 0;
			this.totalContentColumnWidth = 0;
			for (int i = 0; i < this.contentTable.getColumnCount(); i++) {
				String sName = this.contentTable.getColumnName(i);
				TableColumn tcColumns = this.contentTable.getColumn(sName);
				contentColumnWidth[i] = this.getPreferredColumnWidth(this.contentTable, i);
				maxContentColumnWidth = Math.max(maxContentColumnWidth, contentColumnWidth[i]);
				this.totalContentColumnWidth = this.totalContentColumnWidth + contentColumnWidth[i];
			}

			int count = 0;
			while ((maxContentColumnWidth + this.totalFixedColumnWidth) > printableWidth) {
				// reduce column width
				count++;
				if (count > 500) {
					break;
				}
				// TODO
				int reduce = (maxContentColumnWidth + this.totalFixedColumnWidth) - (int) printableWidth;
				if ((this.totalFixedColumnWidth - reduce) >= 0) {
					int items = this.fixedColumnTable.getColumnCount();
					reduce = reduce + (items - (reduce % items));
					int cReduce = reduce / this.fixedColumnTable.getColumnCount();

					this.totalFixedColumnWidth = 0;
					for (int i = 0; i < this.fixedColumnTable.getColumnCount(); i++) {
						String sName = this.fixedColumnTable.getColumnName(i);
						TableColumn tcColumns = this.fixedColumnTable.getColumn(sName);
						int width = tcColumns.getWidth();
						width = width - cReduce;
						tcColumns.setPreferredWidth(width);
						tcColumns.setWidth(width);
						fixedColumnWidth[i] = width;
						this.totalFixedColumnWidth = this.totalFixedColumnWidth + fixedColumnWidth[i];
					}
				}

				if ((this.totalContentColumnWidth - reduce) >= 0) {
					int items = this.contentTable.getColumnCount();
					reduce = reduce + (items - (reduce % items));
					int cReduce = reduce / this.contentTable.getColumnCount();

					this.totalContentColumnWidth = 0;
					maxContentColumnWidth = 0;
					for (int i = 0; i < this.contentTable.getColumnCount(); i++) {
						String sName = this.contentTable.getColumnName(i);
						TableColumn tcColumns = this.contentTable.getColumn(sName);
						int width = tcColumns.getWidth();
						width = width - cReduce;
						tcColumns.setPreferredWidth(width);
						tcColumns.setWidth(width);
						contentColumnWidth[i] = width;
						maxContentColumnWidth = Math.max(maxContentColumnWidth, contentColumnWidth[i]);
						this.totalContentColumnWidth = this.totalContentColumnWidth + contentColumnWidth[i];
					}
				}

			}

			// this.columnPageIndexes
			this.columnPageIndexes = new int[1];
			this.columnPageIndexes[0] = 0;
			int columnPage = 0;
			if (contentColumnWidth.length > 0) {
				int pageWidth = this.totalFixedColumnWidth + contentColumnWidth[0];
				for (int i = 1; i < contentColumnWidth.length; i++) {
					if ((pageWidth + contentColumnWidth[i]) > printableWidth) {
						columnPage++;
						int[] tempIndex = new int[columnPage + 1];
						System.arraycopy(this.columnPageIndexes, 0, tempIndex, 0, this.columnPageIndexes.length);
						tempIndex[tempIndex.length - 1] = i;
						this.columnPageIndexes = tempIndex;
						pageWidth = this.totalFixedColumnWidth + contentColumnWidth[i];
					} else {
						pageWidth = pageWidth + contentColumnWidth[i];
					}
				}
			}
		} catch (OutOfMemoryError errorMem) {
			PrintablePivotTable.logger.error("Memory Error", errorMem);
			throw errorMem;
		}
	}

	protected int getPreferredColumnWidth(JTable table, int i) {
		int iWidth = 0;
		String sName = table.getColumnName(i);

		TableColumn tcColumn = table.getColumn(sName);
		tcColumn.setMinWidth(10);
		tcColumn.setMaxWidth(10000);
		if (tcColumn != null) {
			return tcColumn.getWidth();
		}
		// If there are not data then initialize the columns width using the
		// header size

		if (table.getTableHeader().getDefaultRenderer() != null) {
			int headerPreferredWidth = 0;
			Component cHeaderRenderer = table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, sName, false, false, -1, -1);
			if (cHeaderRenderer instanceof JTextField) {
				FontMetrics metrics = ((JTextField) cHeaderRenderer).getFontMetrics(((JTextField) cHeaderRenderer).getFont());
				headerPreferredWidth = metrics.stringWidth(((JTextField) cHeaderRenderer).getText()) + 4;
			} else if (cHeaderRenderer instanceof JLabel) {
				FontMetrics metrics = ((JLabel) cHeaderRenderer).getFontMetrics(((JLabel) cHeaderRenderer).getFont());
				try {
					String text = ((JLabel) cHeaderRenderer).getText();
					if (text == null) {
						text = "";
					}
					headerPreferredWidth = metrics.stringWidth(text) + 4;
				} catch (Exception eM) {
					PrintablePivotTable.logger.trace(null, eM);
				}
			}
			iWidth = Math.max(headerPreferredWidth + 5, iWidth);
		}
		for (int j = 0; j < table.getRowCount(); j++) {
			Object oValue = table.getValueAt(j, i);
			TableCellRenderer renderer = table.getCellRenderer(j, i);
			// Now search column values
			Component componenteRender = renderer.getTableCellRendererComponent(table, oValue, false, false, 0, 0);
			int iPreferredWidth = componenteRender.getPreferredSize().width;
			if (componenteRender instanceof JComponent) {
				iPreferredWidth = iPreferredWidth - ((JComponent) componenteRender).getInsets().left - ((JComponent) componenteRender).getInsets().right;
			}
			if (componenteRender instanceof JTextField) {
				FontMetrics metrics = ((JTextField) componenteRender).getFontMetrics(((JTextField) componenteRender).getFont());
				iPreferredWidth = metrics.stringWidth(((JTextField) componenteRender).getText()) + 4;
			} else if (componenteRender instanceof JLabel) {
				FontMetrics metrics = ((JLabel) componenteRender).getFontMetrics(((JLabel) componenteRender).getFont());
				try {
					String text = ((JLabel) componenteRender).getText();
					if (text == null) {
						text = "";
					}
					iPreferredWidth = metrics.stringWidth(text) + 4;
				} catch (Exception eM) {
					PrintablePivotTable.logger.trace(null, eM);
				}
			}
			iWidth = Math.max(iPreferredWidth + 5, iWidth);

		}
		return iWidth;
	}
}
