package com.ontimize.util.wia;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.gnome.morena.Device;
import eu.gnome.morena.DeviceBase;
import eu.gnome.morena.TransferListener;

public class ScanSession {

	private static final Logger			logger				= LoggerFactory.getLogger(ScanSession.class);

	MultiFileTransferHandler th;
	private LinkedBlockingQueue<String> queue;
	boolean transferFinished = false;
	private AtomicInteger blockedThreadCount;
	boolean feederUsed = false;

	public boolean isFeederUsed() {
		return this.feederUsed;
	}

	public static final String EOP = ""; // string designating an End of
	// operation

	public void startSession(Device device, int item) throws Exception {
		this.startSession(device, item, 0); // 0 - means scanning until feeder
		// is empty
	}

	public void startSession(Device device, int item, int pages) throws Exception {
		this.queue = new LinkedBlockingQueue<String>();
		this.blockedThreadCount = new AtomicInteger(0);
		this.transferFinished = false;
		this.th = new MultiFileTransferHandler(pages);
		((DeviceBase) device).startTransfer(this.th, item);
	}

	public File getImageFile() {
		String filename = this.queue.poll();
		if ((filename == null) && !this.transferFinished) {
			try {
				this.blockedThreadCount.incrementAndGet();
				filename = this.queue.take();
				this.blockedThreadCount.decrementAndGet();
			} catch (InterruptedException e) {}
		}
		if (filename.isEmpty()) {
			this.releaseBlockedThreads();
		}

		if ((filename == null) || filename.isEmpty()) {
			return null;
		} else {
			this.feederUsed = true;
			return new File(filename);

		}
		// return (filename == null) || filename.isEmpty() ? null : new File(filename);
	}

	public boolean isEmptyFeeder() {
		return this.th != null ? this.th.code == 0 : false;
	}

	public int getErrorCode() {
		return this.th.code;
	}

	public String getErrorMessage() {
		return this.th.error;
	}

	private void releaseBlockedThreads() {
		int count = this.blockedThreadCount.getAndSet(0);
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				try {
					this.queue.put(ScanSession.EOP);
				} catch (InterruptedException e) {
					ScanSession.logger.error(null, e);
				}
			}
		}
	}

	public static String getExt(File file) {
		String name = file.getName();
		int ix = name.lastIndexOf('.');
		if ((ix > 0) && ((ix + 1) < name.length())) {
			return name.substring(ix + 1);
		}
		return "";
	}

	// ========================================================

	/**
	 * TransferDoneListener interface implementation that handles a scanned document as a File.
	 *
	 */
	class MultiFileTransferHandler implements TransferListener {

		int code;
		String error;
		int pages; // expected page count (0 - until feeder is empty)
		AtomicInteger pcounter; // page counter

		public MultiFileTransferHandler(int pages) {
			this.pages = pages;
			this.pcounter = new AtomicInteger(0);
			this.code = -1;
			this.error = "No error";
		}

		/**
		 * Transferred image is handled in this callback method. File containing the image is provided as an argument. The image type may vary depending on the interface (Wia/ICA)
		 * and the device driver. Typical format includes BMP for WIA scanners and JPEG for WIA camera and for ICA devices. Please note that this method runs in different thread
		 * than that where the device.startTransfer() has been called.
		 *
		 * @param file
		 *            - the file containing the acquired image
		 *
		 * @see eu.gnome.morena.TransferDoneListener#transferDone(java.io.File)
		 */

		// @Override
		@Override
		public void transferDone(File file) {
			try {
				ScanSession.this.queue.put(file.getAbsolutePath());
				if (this.pcounter.incrementAndGet() == this.pages) {
					ScanSession.this.queue.put(ScanSession.EOP);
				}
			} catch (InterruptedException e) {
				ScanSession.logger.error(null, e);
			}
		}

		/**
		 * This callback method is called when scanning process failed for any reason. Description of the problem is provided.
		 */

		// @Override
		@Override
		public void transferProgress(int percent) {
			ScanSession.logger.debug("transfer " + percent + "%");
		}

		// @Override
		@Override
		public void transferFailed(int code, String error) {
			this.code = code;
			this.error = error;
			ScanSession.this.transferFinished = true;
			try {
				ScanSession.this.queue.put(ScanSession.EOP);
			} catch (InterruptedException e) {
				ScanSession.logger.error(null, e);
			}
		}

		// private String getUniqueFileName(String ext)
		// { return new
		// String("mi_"+time+"_"+pcounter.incrementAndGet()+(!ext.isEmpty()?"."+ext:""));
		// }

	}

}
