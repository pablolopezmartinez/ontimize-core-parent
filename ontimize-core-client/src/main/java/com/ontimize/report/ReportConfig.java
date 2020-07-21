package com.ontimize.report;

import com.ontimize.report.store.ReportStore;

/**
 * <p>
 * UI configuration interface.
 *
 * @see ReportData
 * @see ReportDataFactory
 * @author Imatia Innovation S.L.
 * @since 04/12/2008
 */
public interface ReportConfig {

    public java.awt.Window getAncestor();

    public com.ontimize.locator.EntityReferenceLocator getLocator();

    public com.ontimize.report.store.ReportStore[] getReportStores();

    public java.util.ResourceBundle getResourceBundle();

    public void setReportStores(ReportStore[] stores) throws Exception;

    public void setResourceBundle(java.util.ResourceBundle bundle);

    public void setVisible(boolean vis);

}
