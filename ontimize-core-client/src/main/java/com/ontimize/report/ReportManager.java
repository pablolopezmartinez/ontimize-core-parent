package com.ontimize.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.report.engine.dynamicjasper.DynamicJasperEngine;

public class ReportManager {

    private static final Logger logger = LoggerFactory.getLogger(ReportManager.class);

    private static ReportEngine reportEngine;

    public static boolean createReportEngine() {
        try {
            Class.forName("ar.com.fdvs.dj.domain.DynamicReport");
        } catch (Exception e) {
            ReportManager.logger.trace(null, e);
            try {
                Class.forName("org.jfree.report.JFreeReport");
            } catch (ClassNotFoundException e1) {
                ReportManager.logger.error("ReportManager: Neither DynamicJasper nor JFreeReport engines registered",
                        e1);
                return false;
            }

            try {
                Class rManager = Class.forName("com.ontimize.report.engine.jfreereport.FreeReportEngine");
                ReportManager.reportEngine = (ReportEngine) rManager.newInstance();
            } catch (Exception e1) {
                ReportManager.logger.debug(e1.getMessage(), e1);
            }

            if (ReportManager.reportEngine.checkLibraries()) {
                ReportManager.logger.info("Report engine: {} succesfully registered",
                        ReportManager.reportEngine.getReportEngineName());
                return true;
            } else {
                ReportManager.logger.warn("Report engine found: {} but missing some required libraries.",
                        ReportManager.reportEngine.getReportEngineName());
                return false;
            }
        }
        try {
            // try to use dynamic jasper 3.0.13 (only in ontimize-legacy)
            Class rManager = Class.forName("com.ontimize.report.engine.dynamicjasper.DynamicJasperEngine3");
            ReportManager.reportEngine = (ReportEngine) rManager.newInstance();
        } catch (Exception e) {
            ReportManager.logger.debug(null, e);
            // use new dynamic jasper 5.0.0
            ReportManager.reportEngine = new DynamicJasperEngine();
        }

        if (ReportManager.reportEngine.checkLibraries()) {
            ReportManager.logger.info("Report engine: {} succesfully registered",
                    ReportManager.reportEngine.getReportEngineName());
            return true;
        } else {
            ReportManager.logger.warn("Report engine found: {} but missing some required libraries.",
                    ReportManager.reportEngine.getReportEngineName());
            return false;
        }
    }

    public static void registerNewReportEngine(ReportEngine reportEngine) {
        ReportManager.reportEngine = reportEngine;
        if (reportEngine.checkLibraries()) {
            ReportManager.logger.info("Report engine: {} succesfully registered", reportEngine.getReportEngineName());
        } else {
            ReportManager.logger.warn("Report engine found: {} but missing some required libraries.",
                    reportEngine.getReportEngineName());
        }
    }

    public static synchronized boolean isReportsEnabled() {
        try {
            if (ReportManager.reportEngine == null) {
                return ReportManager.createReportEngine();
            }
            return true;
        } catch (Exception e) {
            ReportManager.logger.error("Report libraries are not available:", e);
            return false;
        }
    }

    public static ReportEngine getReportEngine() throws Exception {
        if (!ReportManager.isReportsEnabled()) {
            throw new Exception("ReportManager: No report engine configured. You must check libraries.");
        }
        return ReportManager.reportEngine;
    }

}
