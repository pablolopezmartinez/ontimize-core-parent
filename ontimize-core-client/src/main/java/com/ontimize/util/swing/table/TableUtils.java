package com.ontimize.util.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;

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

public abstract class TableUtils {

    private static final Logger logger = LoggerFactory.getLogger(TableUtils.class);

    private static boolean DEBUG = false;

    private static boolean DEBUG_MEMORY = false;

    private static boolean DEBUG_TIMES = false;

    public static int SUM = 0;

    public static int AVG = 1;

    public static int COUNT = 2;

    public static class PrintableTable implements Printable {

        private JTable table = null;

        private final int sideMargin = 4;

        private final int verticalMargin = 4;

        private final int pageNumberMargin = 20;

        private final int titleMargin = 20;

        private int[] pageIndexes = null;

        private PageFormat pf = null;

        private final PrinterJob pj = PrinterJob.getPrinterJob();

        private String pageTitle = null;

        private boolean isPrinting = false;

        private final int MIN_PAGE_HEIGHT = 50;

        protected boolean fitsInPage = false;

        protected int totalWidth = -1;

        protected String date = null;

        protected int printingPage = -1;

        protected SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();

        public PrintableTable(JTable t, boolean scaleToFit) {
            this.df.applyPattern("HH:mm  dd/MM/yyyy");
            this.date = this.df.format(new java.util.Date());
            this.table = t;
            this.pf = this.pj.defaultPage();
        }

        public JTable getTable() {
            return this.table;
        }

        public void cancelPrinting() {
            this.pj.cancel();
        }

        public void setPageTitle(String t) {
            this.pageTitle = t;
        }

        public PageFormat getPageFormat() {
            return this.pf;
        }

        public int setPageFormat(PageFormat pf) {
            if (pf.getImageableHeight() < this.MIN_PAGE_HEIGHT) {
                return 0;
            }
            this.pf = pf;
            this.pageIndexes = this.preparePages(pf);
            return this.pageIndexes.length;
        }

        private int preparePagesForPrinting(PageFormat pf) {
            if (pf.getImageableHeight() < this.MIN_PAGE_HEIGHT) {
                return 0;
            }
            this.pf = pf;
            this.pageIndexes = this.preparePages(pf);
            return this.pageIndexes.length;
        }

        public boolean fitsInPage() {
            return this.fitsInPage;
        }

        public boolean fitsInPage(int[] columnsWidth) {
            int totalWidth = 0;
            for (int i = 0; i < columnsWidth.length; i++) {
                totalWidth += columnsWidth[i];
            }
            if (TableUtils.DEBUG) {
                TableUtils.logger.debug("Total width required by table: " + totalWidth);
            }
            if (TableUtils.DEBUG) {
                TableUtils.logger.debug("Available width: " + (int) (this.pf.getImageableWidth() - this.sideMargin));
            }

            if (totalWidth > (int) (this.pf.getImageableWidth() - this.sideMargin)) {
                return false;
            } else {
                return true;
            }
        }

        private boolean fitsInPage(PageFormat pf) {

            int[] columnsWidth = TableUtils.getPreferredColumnsWidth(this.table);
            // Calculate the total width
            this.totalWidth = 0;
            for (int i = 0; i < columnsWidth.length; i++) {
                this.totalWidth += columnsWidth[i];
            }
            if (TableUtils.DEBUG) {
                TableUtils.logger.debug("Total width needed: " + this.totalWidth);
            }
            if (TableUtils.DEBUG) {
                TableUtils.logger.debug("Available width: " + (int) (pf.getImageableWidth() - this.sideMargin));
            }

            if (this.totalWidth > (int) (pf.getImageableWidth() - this.sideMargin)) {
                if (this.pf.equals(pf)) {
                    this.fitsInPage = false;
                }
                return false;
            } else {
                if (this.pf.equals(pf)) {
                    this.fitsInPage = true;
                }
                return true;
            }
        }

        public int getTotalWidth() {
            return this.totalWidth;
        }

        public int getAvaliableWidth() {
            return (int) (this.pf.getImageableWidth() - this.sideMargin);
        }

        private void adjustToPage() {
            this.table.setBounds(0, 0, (int) this.pf.getImageableWidth() - this.sideMargin,
                    this.table.getPreferredSize().height);
            this.table.getTableHeader()
                .setSize((int) this.pf.getImageableWidth() - this.sideMargin,
                        this.table.getTableHeader().getPreferredSize().height);
            this.table.doLayout();
        }

        private int[] preparePages(PageFormat pf) {
            long t = System.currentTimeMillis();
            if (pf.getImageableHeight() < this.MIN_PAGE_HEIGHT) {
                return new int[1];
            }
            this.pf = pf;

            Thread.yield();
            this.table.setDoubleBuffered(false);
            if (TableUtils.DEBUG_MEMORY) {
                TableUtils.logger.debug("PrintableTable Establishing columns width: Used memory: "
                        + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
                            .freeMemory()) / 1024.0)
                        + " kbytes");
            }
            int[] iColumnsWidth = TableUtils.getPreferredColumnsWidth(this.table);
            if (TableUtils.DEBUG_MEMORY) {
                TableUtils.logger.debug(
                        "PrintableTable established column width: Used memory: "
                                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0)
                                + " kbytes");
            }

            this.fitsInPage = this.fitsInPage(iColumnsWidth);
            Thread.yield();

            this.adjustToPage();

            int iStimatedPageNumber = (int) (this.table.getPreferredSize().height / pf.getImageableHeight());
            if (iStimatedPageNumber == 0) {
                iStimatedPageNumber++;
            }

            Thread.yield();
            int iRowsCount = this.table.getRowCount();
            if (iRowsCount == 0) {
                return new int[0];
            }
            if (this.table.getColumnCount() <= 1) {
                return new int[0];
            }
            int iHeaderHeight = this.table.getTableHeader().getHeight();
            if (TableUtils.DEBUG) {
                TableUtils.logger.debug("Header height = " + iHeaderHeight);
            }
            // Calculates the index of the first row in the page.
            // If the top row is between two pages then print it, but the bottom
            // row is printed in the next page
            int iPageCount = 0;
            int iLastPageRowIndex = -1;
            int[] iLastpageRowsIndexes = new int[iStimatedPageNumber * 2];
            for (int i = 0; i < iLastpageRowsIndexes.length; i++) {
                iLastpageRowsIndexes[i] = -1;
            }

            if (TableUtils.DEBUG_MEMORY) {
                TableUtils.logger.debug(
                        "PrintableTable searching last rows: Used memory: "
                                + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0)
                                + " kbytes");
            }

            while (iLastPageRowIndex < (iRowsCount - 1)) {
                int iButtonPagePosition = (iPageCount + 1) * ((int) pf.getImageableHeight() - iHeaderHeight
                        - this.verticalMargin - this.pageNumberMargin - this.titleMargin);
                int iStimatedLastPageRowIndex = this.table.rowAtPoint(new Point(0, iButtonPagePosition));
                if (iStimatedLastPageRowIndex > 0) {
                    iLastPageRowIndex = iStimatedLastPageRowIndex - 1;
                }

                long tIni = System.currentTimeMillis();
                for (int i = iLastPageRowIndex; i < iRowsCount; i++) {
                    Thread.yield();

                    Rectangle rect = this.table.getCellRect(i, 1, true);
                    int lastRowPosition = rect.y + rect.height;
                    if (lastRowPosition > iButtonPagePosition) {
                        if (TableUtils.DEBUG_TIMES) {
                            TableUtils.logger
                                .debug("Page time: " + iPageCount + " " + (System.currentTimeMillis() - tIni));
                        }
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
                        if (TableUtils.DEBUG) {
                            TableUtils.logger.debug("Page ready " + iPageCount + " . Last row= " + iLastPageRowIndex);
                        }
                        break;
                    }
                    iLastPageRowIndex = i;
                    iLastpageRowsIndexes[iPageCount] = iLastPageRowIndex;
                }
            }

            if (TableUtils.DEBUG_MEMORY) {
                TableUtils.logger.debug("PrintableTable has search the maximum number of rows: Memory used: "
                        + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
                            .freeMemory()) / 1024.0)
                        + " kbytes");
            }

            if (TableUtils.DEBUG) {
                TableUtils.logger.debug("Number of pages to print table= " + iPageCount);
            }

            // Search until index -1
            int[] index = new int[iPageCount + 1];
            for (int i = 0; i < (iPageCount + 1); i++) {
                index[i] = iLastpageRowsIndexes[i];
            }

            if (TableUtils.DEBUG_TIMES) {
                TableUtils.logger
                    .debug("Time to " + index.length + " print pages: " + (System.currentTimeMillis() - t));
            }
            this.pageIndexes = index;
            return index;
        }

        public int getNumberOfPages() {
            return this.pageIndexes.length;
        }

        public int getFontSize() {
            return this.table.getFont().getSize();
        }

        public void setFontSize(int fontSize) {
            this.table.getTableHeader().setFont(this.table.getFont().deriveFont((float) fontSize));
            this.table.setFont(this.table.getFont().deriveFont((float) fontSize));
            this.preparePagesForPrinting(this.pf);
        }

        public int pageSetup() {
            PageFormat pfPrevious = this.pf;
            this.pf = this.pj.pageDialog(this.pf);
            if (this.pf != pfPrevious) {
                this.setPageFormat(this.pf);
            }
            return this.pageIndexes.length;
        }

        public void setData(TableModel model) {
            this.table.setModel(model);
        }

        public void moveColumnLeft(Object col) {
            TableColumnModel m = this.table.getColumnModel();

            int previousIndex = m.getColumnIndex(col);
            if (previousIndex > 0) {
                m.moveColumn(previousIndex, previousIndex - 1);
            }
        }

        public void moveColumnRight(Object col) {
            TableColumnModel m = this.table.getColumnModel();
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
            if (pageIndex >= this.pageIndexes.length) {
                return Printable.NO_SUCH_PAGE;
            }
            this.printingPage = pageIndex;
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                TableUtils.logger.trace(null, e);
            }

            if (TableUtils.DEBUG) {
                TableUtils.logger.debug("Printing page= " + (pageIndex + 1));
            }
            int headerHeight = this.table.getTableHeader().getHeight();
            // Print the rows:
            int firstPageRowIndex = 0;
            int lastPageRowIndex = 0;
            if (pageIndex > 0) {
                firstPageRowIndex = this.pageIndexes[pageIndex - 1] + 1;
            }
            lastPageRowIndex = this.pageIndexes[pageIndex];
            // Print
            if (TableUtils.DEBUG) {
                TableUtils.logger.debug("Printing rows from: " + firstPageRowIndex + " to " + lastPageRowIndex);
            }
            Graphics2D g2D = (Graphics2D) g;
            if (Double.compare(scale, 1.0) != 0) {
                g2D.scale(scale, scale);
            }
            g2D.translate(pf.getImageableX() + (this.sideMargin / 2),
                    pf.getImageableY() + (this.verticalMargin / 2) + this.titleMargin);
            // Title
            if (this.pageTitle != null) {
                g2D.setColor(Color.black);
                g2D.setFont(g2D.getFont().deriveFont(Font.BOLD));
                g2D.drawString(this.pageTitle,
                        (int) ((pf.getImageableWidth() - g2D.getFontMetrics().stringWidth(this.pageTitle)
                                - this.sideMargin) / 2),
                        0 - g2D.getFontMetrics().getDescent() - 2);
            }
            Thread.yield();
            // Page number
            StringBuilder pageNumberText = new StringBuilder();
            pageNumberText.append(pageIndex + 1);
            pageNumberText.append(" / ");
            pageNumberText.append(this.pageIndexes.length);
            String textoNP = pageNumberText.toString();

            g2D.setColor(Color.black);
            g2D.drawLine(this.sideMargin,
                    (int) (pf.getImageableHeight() - g2D.getFontMetrics().getDescent() - (this.verticalMargin / 2)
                            - this.titleMargin) - 10,
                    (int) (pf.getImageableWidth() - this.sideMargin),
                    (int) (pf.getImageableHeight() - g2D.getFontMetrics().getDescent() - (this.verticalMargin / 2)
                            - this.titleMargin) - 10);
            g2D.setFont(g2D.getFont().deriveFont((float) 8));
            g2D.drawString(textoNP,
                    (int) (pf.getImageableWidth() - g2D.getFontMetrics().stringWidth(textoNP) - this.sideMargin),
                    (int) (pf.getImageableHeight() - g2D.getFontMetrics().getDescent() - (this.verticalMargin / 2)
                            - this.titleMargin));

            g2D.drawString(this.date, this.sideMargin, (int) (pf.getImageableHeight()
                    - g2D.getFontMetrics().getDescent() - (this.verticalMargin / 2) - this.titleMargin));
            Thread.yield();

            int firstRowFirstPixel = this.table.getCellRect(firstPageRowIndex, 0, true).y;
            int lastRowLastIndex = this.table.getCellRect(lastPageRowIndex, 0, true).y
                    + this.table.getCellRect(lastPageRowIndex, 0, true).height;
            // Transform the height
            Thread.yield();
            int traslationHeight2 = -firstRowFirstPixel;
            g2D.setClip(0, 0, this.table.getTableHeader().getWidth(), this.table.getTableHeader().getHeight());
            Thread.yield();
            this.table.getTableHeader().paint(g2D);
            g2D.setClip(-1, headerHeight, (int) pf.getImageableWidth(), lastRowLastIndex - firstRowFirstPixel);
            g2D.translate(0, traslationHeight2);
            g2D.translate(0, headerHeight);
            Thread.yield();
            this.table.paint(g2D);
            Thread.yield();
            return Printable.PAGE_EXISTS;
        }

        @Override
        public int print(Graphics g, PageFormat pf, int pageIndex) {
            return this.printInPage(g, pf, pageIndex, 1.0);
        }

        public String fitPage() {
            if (this.pf.getImageableHeight() < this.MIN_PAGE_HEIGHT) {
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
                TableUtils.logger.debug("Memory Error: Trying free", error);
                return error.getMessage();
            }
        }

    }

    public static int[] getPreferredColumnsWidth(JTable table) {
        // Calculates the minimum table width to ensure that the values are
        // visible
        // Fit the columns

        int iTableWidth = table.getWidth();
        int iColumnsWidth = 0;
        int nColsGreatestTableWidth = 0;
        int[] iWidths = new int[table.getColumnCount()];
        int[] greaterTableColumnWidth = new int[table.getColumnCount()];
        try {
            for (int i = 0; i < table.getColumnCount(); i++) {
                String sName = table.getColumnName(i);
                TableColumn tcColumns = table.getColumn(sName);
                iWidths[i] = TableUtils.getPreferredColumnWidth(table, i);
                if (iWidths[i] > iTableWidth) {
                    greaterTableColumnWidth[i] = iWidths[i];
                    nColsGreatestTableWidth++;
                } else {
                    iColumnsWidth += iWidths[i];
                }

                tcColumns.setWidth(iWidths[i]);
                tcColumns.setPreferredWidth(iWidths[i]);
            }
            // Share de available space
            int iAvailable = iTableWidth - iColumnsWidth;
            if ((iAvailable > 0) && (nColsGreatestTableWidth > 0)) {
                int iForEach = iAvailable / nColsGreatestTableWidth;
                for (int i = 0; i < iWidths.length; i++) {
                    if (greaterTableColumnWidth[i] > 0) {
                        String sName = table.getColumnName(i);

                        TableColumn tcColumn = table.getColumn(sName);
                        tcColumn.setWidth(iForEach);
                        tcColumn.setPreferredWidth(iForEach);
                        iWidths[i] = iForEach;
                    }
                }
            }
        } catch (OutOfMemoryError errorMem) {
            TableUtils.logger.error("Memory Error", errorMem);
            for (int i = 0; i < table.getColumnCount(); i++) {
                String sName = table.getColumnName(i);
                TableColumn tcColumn = table.getColumn(sName);
                iWidths[i] = tcColumn.getPreferredWidth();
            }
            throw errorMem;
        }
        return iWidths;
    }

    protected static int getPreferredColumnWidth(JTable table, int i) {
        int iWidth = 0;
        String sName = table.getColumnName(i);

        TableColumn tcColumn = table.getColumn(sName);
        tcColumn.setMinWidth(10);
        tcColumn.setMaxWidth(10000);
        // If there are not data then initialize the columns width using the
        // header size

        for (int j = 0; j < table.getRowCount(); j++) {
            Object oValue = table.getValueAt(j, i);
            TableCellRenderer renderer = table.getCellRenderer(j, i);
            // Now search column values
            Component componenteRender = renderer.getTableCellRendererComponent(table, oValue, false, false, 0, 0);
            int iPreferredWidth = componenteRender.getPreferredSize().width;
            if (componenteRender instanceof JComponent) {
                iPreferredWidth = iPreferredWidth - ((JComponent) componenteRender).getInsets().left
                        - ((JComponent) componenteRender).getInsets().right;
            }
            if (componenteRender instanceof JTextField) {
                FontMetrics metrics = ((JTextField) componenteRender)
                    .getFontMetrics(((JTextField) componenteRender).getFont());
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
                    TableUtils.logger.trace(null, eM);
                }
            }
            iWidth = Math.max(iPreferredWidth + 5, iWidth);

        }
        return iWidth;
    }

}
