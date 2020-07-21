package com.ontimize.util.swing.printpreview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.Scrollable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.images.ImageManager;
import com.ontimize.util.swing.table.PrintablePivotTable;

public class PrintPreviewDialog extends JComponent {

    private static final Logger logger = LoggerFactory.getLogger(PrintPreviewDialog.class.getClass());

    protected Pageable rp = null;

    protected int pageNumber = 0;

    protected PageFormat pf;

    protected String jobName = null;

    protected JButton next = null;

    protected JButton previous = null;

    protected JButton print = null;

    protected JButton pageFormat = null;

    protected JButton bZmore = null;

    protected JButton bZless = null;

    protected JSpinner scaleSpinner;

    protected static JDialog dialogPreview = null;

    protected class Page extends JComponent implements Scrollable {

        protected PageFormat pf = null;

        protected double zoom = 1.0;

        public Page(PageFormat pf) {
            this.pf = pf;
            // this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
            // this.setBorder(BorderFactory.createLineBorder(Color.lightGray,
            // 10));
        }

        public void setPageFormat(PageFormat pf) {
            this.pf = pf;
            Printable printable = PrintPreviewDialog.this.rp.getPrintable(PrintPreviewDialog.this.pageNumber);
            if (printable instanceof PrintablePivotTable) {
                ((PrintablePivotTable) printable).setPageFormat(pf);
            }
        }

        public void setScale(double scale) {
            Printable printable = PrintPreviewDialog.this.rp.getPrintable(PrintPreviewDialog.this.pageNumber);
            if (printable instanceof PrintablePivotTable) {
                ((PrintablePivotTable) printable).setScale(scale);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(
                    (int) (this.pf.getWidth() * this.zoom) + this.getInsets().left + this.getInsets().right,
                    (int) (this.pf.getHeight() * this.zoom) + this.getInsets().top + this.getInsets().bottom);
        }

        public void setZoom(double z) {
            this.zoom = z;
            this.revalidate();
            this.repaint();
        }

        public double getZoom() {
            return this.zoom;
        }

        @Override
        public void paintComponent(final Graphics g2d) {
            try {
                Graphics2D newG = (Graphics2D) g2d.create();
                newG.setColor(Color.white);
                Insets insets = this.getInsets();
                newG.translate(insets.right, insets.top);
                newG.scale(this.zoom, this.zoom);
                newG.fillRect(0, 0, (int) this.pf.getWidth(), (int) this.pf.getHeight());
                PrintPreviewDialog.this.rp.getPrintable(PrintPreviewDialog.this.pageNumber)
                    .print(newG, this.pf, PrintPreviewDialog.this.pageNumber);
                newG.scale(1 / this.zoom, 1 / this.zoom);
                newG.translate(-insets.right, -insets.top);
                newG.dispose();
            } catch (Exception ex) {
                PrintPreviewDialog.logger.error("Error printing page: {}", ex.getMessage(), ex);
            }
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return this.getPreferredSize();
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 20;
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }

    }

    protected Page page = null;

    public PrintPreviewDialog(Pageable r, PageFormat pf, String jobnam) {
        this.rp = r;
        this.pf = pf;
        this.jobName = jobnam;
        this.page = new Page(pf);
        CompoundBorder border = new CompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.lightGray, 1));
        this.page.setBorder(border);

        this.setLayout(new BorderLayout());

        JPanel jpButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        this.previous = new JButton(ImageManager.getIcon(ImageManager.PREVIOUS_2));
        this.next = new JButton(ImageManager.getIcon(ImageManager.NEXT_2));
        this.print = new JButton(ImageManager.getIcon(ImageManager.PRINT));

        this.pageFormat = new JButton(ImageManager.getIcon(ImageManager.PAGE));

        this.bZmore = new JButton(ImageManager.getIcon(ImageManager.ZOOM_IN));
        this.bZless = new JButton(ImageManager.getIcon(ImageManager.ZOOM_OUT));

        this.previous.setMargin(new Insets(1, 1, 1, 1));
        this.next.setMargin(new Insets(1, 1, 1, 1));
        this.print.setMargin(new Insets(1, 1, 1, 1));
        this.pageFormat.setMargin(new Insets(1, 1, 1, 1));

        this.bZmore.setMargin(new Insets(1, 1, 1, 1));
        this.bZless.setMargin(new Insets(1, 1, 1, 1));
        this.scaleSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 2, .05));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this.scaleSpinner, "0%");
        this.scaleSpinner.setEditor(editor);
        this.scaleSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner spinner = (JSpinner) e.getSource();
                Object value = spinner.getValue();
                if (value instanceof Double) {
                    PrintPreviewDialog.this.setScale((Double) value);
                }
            }
        });

        jpButtonsPanel.add(this.previous);
        jpButtonsPanel.add(this.next);
        jpButtonsPanel.add(this.print);
        jpButtonsPanel.add(this.pageFormat);

        jpButtonsPanel.add(this.bZmore);
        jpButtonsPanel.add(this.bZless);
        jpButtonsPanel.add(this.scaleSpinner);

        this.add(jpButtonsPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(this.page));

        this.validateButtons();
        this.print.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Print
                try {
                    final PrinterJob pj = PrinterJob.getPrinterJob();
                    if (PrintPreviewDialog.this.jobName != null) {
                        pj.setJobName(PrintPreviewDialog.this.jobName);
                    }
                    pj.setPageable(PrintPreviewDialog.this.rp);
                    boolean bStatus = pj.printDialog();
                    if (bStatus) {
                        pj.print();
                    }
                } catch (Exception ex) {
                    PrintPreviewDialog.logger.error(null, ex);
                }
            }
        });

        this.pageFormat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Print
                try {
                    final PrinterJob pj = PrinterJob.getPrinterJob();
                    PrintPreviewDialog.this.pf = pj.pageDialog(PrintPreviewDialog.this.pf);
                    PrintPreviewDialog.this.changePageFormat();
                } catch (Exception ex) {
                    PrintPreviewDialog.logger.error(null, ex);
                }
            }
        });
        this.next.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ((PrintPreviewDialog.this.rp.getNumberOfPages() - 1) > PrintPreviewDialog.this.pageNumber) {
                    PrintPreviewDialog.this.pageNumber++;
                    PrintPreviewDialog.this.repaint();
                    PrintPreviewDialog.this.validateButtons();
                }
            }
        });
        this.previous.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (PrintPreviewDialog.this.pageNumber > 0) {
                    PrintPreviewDialog.this.pageNumber--;
                    PrintPreviewDialog.this.repaint();
                    PrintPreviewDialog.this.validateButtons();
                }
            }
        });
        this.bZless.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PrintPreviewDialog.this.zoomLess();
            }
        });
        this.bZmore.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PrintPreviewDialog.this.zoomMore();
            }
        });
    }

    public void setScale(double scale) {
        this.page.setScale(scale);
        this.validateButtons();
        this.repaint();
    }

    public void zoomMore() {
        this.page.setZoom(this.page.getZoom() * 1.5);
        this.validateButtons();
        this.repaint();
    }

    public void zoomLess() {
        this.page.setZoom(this.page.getZoom() / 1.5);
        this.validateButtons();
        this.repaint();
    }

    public void changePageFormat() {
        this.page.setPageFormat(this.pf);
        this.page.doLayout();
        this.page.revalidate();
        this.page.repaint();
    }

    protected void validateButtons() {
        if (this.rp.getNumberOfPages() <= 1) {
            this.previous.setEnabled(false);
            this.next.setEnabled(false);
        } else {
            if (this.pageNumber == 0) {
                this.previous.setEnabled(false);
                this.next.setEnabled(true);
            } else if (this.pageNumber >= (this.rp.getNumberOfPages() - 1)) {
                this.previous.setEnabled(true);
                this.next.setEnabled(false);
            } else {
                this.previous.setEnabled(true);
                this.next.setEnabled(true);
            }
        }
        if (this.page.getZoom() < 0.33) {
            this.bZless.setEnabled(false);
            this.bZmore.setEnabled(true);
        } else if (this.page.getZoom() > 5.0) {
            this.bZless.setEnabled(true);
            this.bZmore.setEnabled(false);
        } else {
            this.bZless.setEnabled(true);
            this.bZmore.setEnabled(true);
        }
    }

    protected static class WrapperPrintable implements Pageable {

        protected Printable printer = null;

        protected PageFormat pf = null;

        public WrapperPrintable(Printable printer, PageFormat pf) {
            this.printer = printer;
            this.pf = pf;
        }

        @Override
        public int getNumberOfPages() {
            return 1;
        }

        @Override
        public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
            return this.pf;
        }

        @Override
        public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
            return this.printer;
        }

    }

    public static void showPrintPreviewDialog(Component c, Printable printable, PageFormat pf, String jobname) {
        if (PrintPreviewDialog.dialogPreview == null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if (w instanceof Frame) {
                PrintPreviewDialog.dialogPreview = new JDialog((Frame) w, true);
            } else if (w instanceof Dialog) {
                PrintPreviewDialog.dialogPreview = new JDialog((Dialog) w, true);
            }
        }
        PrintPreviewDialog.dialogPreview.setTitle(jobname);
        PageFormat format = null;
        if (pf == null) {
            format = PrinterJob.getPrinterJob().defaultPage();
        } else {
            format = pf;
        }

        PrintPreviewDialog preview = null;
        if (printable instanceof Pageable) {
            preview = new PrintPreviewDialog((Pageable) printable, format, jobname);
        } else {
            WrapperPrintable printer = new WrapperPrintable(printable, format);
            preview = new PrintPreviewDialog(printer, format, jobname);
        }
        PrintPreviewDialog.dialogPreview.getContentPane().removeAll();
        PrintPreviewDialog.dialogPreview.getContentPane().add((preview));
        PrintPreviewDialog.dialogPreview.pack();
        PrintPreviewDialog.dialogPreview.show();
    }

    public static void showPrintPreviewDialog(Component c, Pageable r, PageFormat pf, String jobname) {
        if (PrintPreviewDialog.dialogPreview == null) {
            Window w = SwingUtilities.getWindowAncestor(c);
            if (w instanceof Frame) {
                PrintPreviewDialog.dialogPreview = new JDialog((Frame) w, true);
            } else if (w instanceof Dialog) {
                PrintPreviewDialog.dialogPreview = new JDialog((Dialog) w, true);
            }
        }

        PrintPreviewDialog.dialogPreview.setTitle(jobname);
        PageFormat format = null;
        if (pf == null) {
            format = new PageFormat();
        } else {
            format = pf;
        }

        PrintPreviewDialog preview = new PrintPreviewDialog(r, format, jobname);
        PrintPreviewDialog.dialogPreview.getContentPane().removeAll();
        PrintPreviewDialog.dialogPreview.getContentPane().add((preview));
        PrintPreviewDialog.dialogPreview.pack();
        PrintPreviewDialog.dialogPreview.show();
    }

}
