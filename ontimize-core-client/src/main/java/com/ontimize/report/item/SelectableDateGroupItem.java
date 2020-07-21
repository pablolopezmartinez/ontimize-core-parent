package com.ontimize.report.item;

import java.util.Comparator;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.i18n.Internationalization;
import com.ontimize.report.DefaultReportDialog;
import com.ontimize.report.ReportUtils;

/**
 * Class created to be added to report group list when columns are dates. It contains information
 * about type of grouping: by day, by month, by quarter or by year.
 *
 * @author Imatia Innovation SL
 * @since 5.2059EN
 */
public class SelectableDateGroupItem extends SelectableItem implements Internationalization, Comparator {

    private static final Logger logger = LoggerFactory.getLogger(SelectableDateGroupItem.class);

    protected boolean selected = false;

    protected int groupingDateType = ReportUtils.GROUP_BY_DATE_TIME;

    protected String groupingDateText = DefaultReportDialog.GROUP_BY_DATE_TIME_KEY;

    public SelectableDateGroupItem(String text, ResourceBundle res) {
        super(text, res);
        this.setGroupDateText();
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void setResourceBundle(ResourceBundle res) {
        super.setResourceBundle(res);
        this.setGroupDateText();
    }

    @Override
    public void setSelected(boolean sel) {
        this.selected = sel;
    }

    public int getOperation() {
        return this.groupingDateType;
    }

    public void setOperation(int op) {
        this.groupingDateType = op;
        this.setGroupDateText();
    }

    protected void setGroupDateText() {
        if (this.res != null) {
            try {
                if (this.groupingDateType == ReportUtils.GROUP_BY_DATE_TIME) {
                    this.groupingDateText = DefaultReportDialog.GROUP_BY_DATE_TIME_KEY;
                } else if (this.groupingDateType == ReportUtils.GROUP_BY_DATE) {
                    this.groupingDateText = DefaultReportDialog.GROUP_BY_DATE_KEY;
                } else if (this.groupingDateType == ReportUtils.GROUP_BY_MONTH) {
                    this.groupingDateText = DefaultReportDialog.GROUP_BY_MONTH_KEY;
                } else if (this.groupingDateType == ReportUtils.GROUP_BY_MONTH_AND_YEAR) {
                    this.groupingDateText = DefaultReportDialog.GROUP_BY_MONTH_AND_YEAR_KEY;
                } else if (this.groupingDateType == ReportUtils.GROUP_BY_QUARTER) {
                    this.groupingDateText = DefaultReportDialog.GROUP_BY_QUARTER_KEY;
                } else if (this.groupingDateType == ReportUtils.GROUP_BY_QUARTER_AND_YEAR) {
                    this.groupingDateText = DefaultReportDialog.GROUP_BY_QUARTER_AND_YEAR_KEY;
                } else if (this.groupingDateType == ReportUtils.GROUP_BY_YEAR) {
                    this.groupingDateText = DefaultReportDialog.GROUP_BY_YEAR_KEY;
                }
                this.groupingDateText = this.res.getString(this.groupingDateText);

            } catch (Exception e) {
                SelectableDateGroupItem.logger.debug(e.getMessage(), e);
            }
        }
    }

    public int getGroupingDateType() {
        return this.groupingDateType;
    }

    @Override
    public String toString() {
        if (!this.isSelected()) {
            return this.translatedText;
        }
        return this.translatedText + " - " + this.groupingDateText;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof SelectableDateGroupItem)) {
            return -1;
        } else {
            SelectableDateGroupItem item = (SelectableDateGroupItem) o;
            return item.translatedText.compareTo(this.translatedText);
        }
    }

    @Override
    public int compare(Object o1, Object o2) {
        SelectableDateGroupItem item1 = (SelectableDateGroupItem) o1;
        SelectableDateGroupItem item2 = (SelectableDateGroupItem) o2;
        return item2.compareTo(item1);
    }

};
