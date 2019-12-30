package com.ontimize.report;

import java.util.EventListener;

public interface ReportDesignerListener extends EventListener {

	public void reportDesignerChanged(ReportDesignerEvent event);

}
