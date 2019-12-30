package com.ontimize.util.webstart;

public interface WebStartDownloadListener extends java.util.EventListener {

	public void downloadCompleted(WebStartDownloadEvent e);

	public void downloadFailed(WebStartDownloadEvent e);
}