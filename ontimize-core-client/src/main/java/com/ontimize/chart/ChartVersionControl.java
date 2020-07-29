package com.ontimize.chart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ChartVersionControl {

    private static final Logger logger = LoggerFactory.getLogger(ChartVersionControl.class);

    private static boolean VERSION_1_0 = false;

    private static boolean CHART_ENABLED = false;

    public static boolean DEBUG = true;

    public static final int PIE = 0;

    public static final int PIE_3D = 1;

    public static final int BAR = 2;

    public static final int BAR_3D = 3;

    public static final int STACKED_3D = 4;

    public static final int LINE = 5;

    public static final int DAY = ChartInfo.DAY;

    public static final int MONTH = ChartInfo.MONTH;

    public static final int QUARTER = ChartInfo.QUARTER;

    public static final int YEAR = ChartInfo.YEAR;

    public static final int SUM = 0;

    public static final int MAX = 1;

    public static final int MIN = 2;

    public static final int AVG = 3;

    static {
        try {
            ChartVersionControl.logger.debug("Checking class org.jfree.data.category.DefaultCategoryDataset");
            Class.forName("org.jfree.data.category.DefaultCategoryDataset");
            ChartVersionControl.VERSION_1_0 = true;
            ChartVersionControl.CHART_ENABLED = true;
            ChartVersionControl.logger.debug("Class org.jfree.data.category.DefaultCategoryDataset found");
        } catch (Exception e) {
            ChartVersionControl.logger.debug("Chart classes 1.0 not found", e);
            try {
                ChartVersionControl.logger.debug("Checking class com.jrefinery.data.DefaultXYDataset");
                Class.forName("com.jrefinery.data.DefaultXYDataset");
                ChartVersionControl.logger.debug("Class com.jrefinery.data.DefaultXYDataset found");
                ChartVersionControl.VERSION_1_0 = false;
                ChartVersionControl.CHART_ENABLED = true;
            } catch (Exception ex) {
                ChartVersionControl.logger.debug("Chart classes not found", ex);
            }
        }
    }

    private ChartVersionControl() {
        // empty constructor
    }

    public static boolean isChartEnabled() {
        return ChartVersionControl.CHART_ENABLED;
    }

    public static boolean isVersion_1_0() {
        return ChartVersionControl.VERSION_1_0;
    }

}
