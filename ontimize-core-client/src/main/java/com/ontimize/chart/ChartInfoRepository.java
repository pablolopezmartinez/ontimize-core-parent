package com.ontimize.chart;

import java.util.Hashtable;

public class ChartInfoRepository extends Hashtable {

    public void addChartInfo(ChartInfo chart, String description) {
        this.put(description, chart);
    }

    public ChartInfo getChartInfo(String description) {
        return (ChartInfo) this.get(description);
    }

    public void removeChart(String descr) {
        this.remove(descr);
    }

    public void removeAllCharts() {
        this.clear();
    }

}
