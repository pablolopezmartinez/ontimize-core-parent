package com.ontimize.chart;

public class ChartInfo {

    public static final int NONE = -1;

    public static final int MONTH = 0;

    public static final int YEAR = 1;

    public static final int QUARTER = 2;

    public static final int DAY = 3;

    String colX = null;

    String[] colsY = null;

    String[] series = null;

    String xLabel = null;

    String yLabel = null;

    int type = 0;

    boolean hasIntervals = false;

    int intervalType = -1;

    boolean fillZeros = true;

    int[] operations = null;

    String columnSeriesValuesGen[];

    public ChartInfo(String xLabel, String yLabel, String colX, String[] colsY, String[] series, int type) {
        this(xLabel, yLabel, colX, colsY, series, type, null);
    }

    public ChartInfo(String xLabel, String yLabel, String colX, String[] colsY, String[] series, int type,
            int[] operations) {
        this.colX = colX;
        this.colsY = colsY;
        this.series = series;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.type = type;
        this.operations = operations;
    }

    public ChartInfo(String xLabel, String yLabel, String colX, String[] colsY, String[] series, int type,
            int intervalType) {
        this.colX = colX;
        this.colsY = colsY;
        this.series = series;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.type = type;
        this.hasIntervals = true;
        this.intervalType = intervalType;
    }

    public ChartInfo(String xLabel, String yLabel, String colX, String[] colsY, String[] series, int type,
            int intervalType, boolean fillZeros, int[] operations) {
        this.colX = colX;
        this.colsY = colsY;
        this.series = series;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.type = type;
        this.hasIntervals = true;
        this.intervalType = intervalType;
        this.fillZeros = fillZeros;
        this.operations = operations;
    }

    public ChartInfo(String xLabel, String yLabel, String colX, String colsY[], String series[], int type,
            int intervalType, boolean fillCeros, int operations[],
            String columnSeriesValuesGen[]) {
        this.colX = colX;
        this.colsY = colsY;
        this.series = series;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.type = type;
        this.hasIntervals = true;
        this.intervalType = intervalType;
        this.fillZeros = fillCeros;
        this.operations = operations;
        this.columnSeriesValuesGen = columnSeriesValuesGen;
    }

    public ChartInfo(String xLabel, String yLabel, String colX, String colsY[], String series[], int type,
            int operations[], String columnSeriesValuesGen[]) {
        this.colX = colX;
        this.colsY = colsY;
        this.series = series;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.type = type;
        this.operations = operations;
        this.columnSeriesValuesGen = columnSeriesValuesGen;
    }

    public boolean hasIntervals() {
        return this.hasIntervals;
    }

    public boolean fillZeros() {
        return this.fillZeros;
    }

    public int[] getOperations() {
        return this.operations;
    }

    public int getType() {
        return this.type;
    }

    public String[] getColumnSeriesValuesGen() {
        return this.columnSeriesValuesGen;
    }

}
