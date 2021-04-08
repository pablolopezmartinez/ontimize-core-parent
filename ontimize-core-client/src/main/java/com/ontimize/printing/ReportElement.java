package com.ontimize.printing;

/**
 * Interface that defines methods to implement by report elements.
 *
 * @author Imatia Innovation
 */
public interface ReportElement {

    public static boolean DEBUG = false;

    public static String TITLEID = "Titulo";

    public static String HEADERID = "Cabecera";

    public static String PIEID = "Pie";

    public static String SUMMARYID = "Resumen";

    public static final String ALIGN_LEFT = " align = 'left' ";

    public static final String ALIGN_CENTER = " align = 'center' ";

    public static final String ALIGN_RIGHT = " align = 'right' ";

    /**
     * Method that inserts the correct HTML code in report.
     * @param report The report frame
     * @param multipage condition to know when report is multipage or not
     * @throws Exception When an <code>Exception</code> occurs
     */
    public void insert(ReportFrame report, boolean multipage) throws Exception;

    /**
     * Method that inserts an element into a report based on a template design.
     * @param report The report frame
     * @param templatePositionId Position in template where element must be inserted
     * @param multipage Condition to know when report is multipage or not
     * @throws Exception When an <code>Exception</code> occurs
     */
    public void insert(ReportFrame report, String templatePositionId, boolean multipage) throws Exception;

}
