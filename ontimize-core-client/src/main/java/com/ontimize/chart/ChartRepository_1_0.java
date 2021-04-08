package com.ontimize.chart;

import java.util.Hashtable;

import org.jfree.chart.JFreeChart;

public class ChartRepository_1_0 extends Hashtable {

    public void addChart(JFreeChart chart, String description) {
        this.put(description, chart);
    }

    public JFreeChart getChart(String description) {
        return (JFreeChart) this.get(description);
    }

    public void removeChart(String descr) {
        this.remove(descr);
    }

    public void removeAllCharts() {
        this.clear();
    }

};
