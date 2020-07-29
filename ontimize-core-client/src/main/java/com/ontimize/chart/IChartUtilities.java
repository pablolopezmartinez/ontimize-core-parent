package com.ontimize.chart;

public interface IChartUtilities {

    public static final String ROW_NUMBERS_KEY = "chartutilities.row_count";

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

    public void showDefaultChartDialog();

    public void showDefaultChartDialog(String configuration);

    public void showChart(String description);

    public ChartInfoRepository getChartInfoRepository();

    public void removeAllCharts();

    public void removeChart(String descr);

    public void configureChart(String xLabel, String yLabel, String xColumn, String[] yColumns, String[] series,
            String descr, int type);

    public void configureChartXDate(String xLabel, String yLabel, String xColumn, String[] yColumns, String[] series,
            String descr, int interval);

    public void configureChartXDate(String xLabel, String yLabel, String xColumn, String[] yColumns, String[] series,
            String descr, int interval, boolean fillZeros);

    public void setLoadButtonVisible(boolean visible);

    public void setSaveButtonVisible(boolean visible);

}
