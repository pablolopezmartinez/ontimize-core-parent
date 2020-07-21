package com.ontimize.util.wia;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.ExtendedOperationThread;

import eu.gnome.morena.Configuration;
import eu.gnome.morena.Device;
import eu.gnome.morena.Manager;
import eu.gnome.morena.TransferListener;

public class WIAManager {

    private static final Logger logger = LoggerFactory.getLogger(WIAManager.class);

    protected Manager manager = null;

    protected Device selectedDevice = null;

    protected Image image = null;

    protected class AcquireThread extends ExtendedOperationThread {

        protected class TransferDoneListenerImpl implements TransferListener {

            protected boolean acquireFinish = false;

            @Override
            public void transferDone(File file) {
                try {
                    WIAManager.this.image = ImageIO.read(file);
                    this.acquireFinish = true;
                } catch (IOException e) {
                    WIAManager.logger.error("{}", e.getMessage(), e);
                }
            }

            @Override
            public void transferFailed(int code, String message) {
                this.acquireFinish = true;
                WIAManager.logger.error("Error acquiring the image: Error code{}, Message:{}", code, message);
            }

            @Override
            public void transferProgress(int percent) {
                AcquireThread.this.currentPosition = percent;

            }

            public boolean isAcquireFinished() {
                return this.acquireFinish;
            }

        }

        protected Window w = null;

        public AcquireThread(Window w) {
            this.w = w;
            this.progressDivisions = 100;
        }

        @Override
        public void run() {
            this.hasStarted = true;
            this.status = ApplicationManager.getTranslation("init_scan");
            TransferDoneListenerImpl transferListener = new TransferDoneListenerImpl();
            if (WIAManager.this.selectedDevice != null) {
                try {
                    // if (WIAManager.this.selectedDevice instanceof Scanner) {
                    // Scanner scanner = (Scanner)
                    // WIAManager.this.selectedDevice;
                    // if (scanner.setupDevice(this.w)) {
                    // scanner.startTransfer(transferListener);
                    // }
                    // } else {
                    WIAManager.this.selectedDevice.startTransfer(transferListener);
                    // }

                    while (!transferListener.isAcquireFinished()) {
                        Thread.sleep(100);
                    }

                    this.hasFinished = true;

                } catch (Exception e) {
                    WIAManager.logger.error("{}", e.getMessage(), e);
                }
            }

        }

    }

    public WIAManager() {
        Configuration.setLogLevel(Level.FINEST);
        // Configuration.setMode(Configuration.MODE_NATIVE_UI |
        // Configuration.MODE_WIA1_POLL_ENABLED);
        this.manager = Manager.getInstance();
        this.image = null;
    }

    public int getWIASourceCount() throws Exception {
        if (!WIAUtilities.isWIAEnabled()) {
            throw new Exception("WIA disabled -> there are not classes");
        }
        try {
            return this.manager.listDevices().size();
        } catch (Exception e) {
            WIAManager.logger.error("{}", e.getMessage(), e);
            return -1;
        }
    }

    public void selectWIASource(Window w) throws Exception {
        if (!WIAUtilities.isWIAEnabled()) {
            throw new Exception("WIA disabled -> there are not classes");
        }
        try {
            if (this.getWIASourceCount() > 1) {
                this.selectedDevice = this.manager.selectDevice(w);
            } else if (this.getWIASourceCount() == 1) {
                this.selectedDevice = this.manager.listDevices().get(0);
            } else {
                throw new Exception("No devices are detected");
            }
        } catch (Exception e) {
            WIAManager.logger.error("{}", e.getMessage(), e);
        }
    }

    public Image acquire(Window w) throws Exception {
        if (!WIAUtilities.isWIAEnabled()) {
            throw new Exception("WIA disabled -> there are not classes");
        }

        AcquireThread acquireThread = new AcquireThread(w);
        if (w instanceof Dialog) {
            ApplicationManager.proccessNotCancelableOperation((Dialog) w, acquireThread, 100);
        } else {
            ApplicationManager.proccessNotCancelableOperation((Frame) w, acquireThread, 100);
        }

        return this.getImage();
    }

    public Image getImage() {
        this.closeInstance();
        return this.image;
    }

    public void closeInstance() {
        this.manager.close();
    }

}
