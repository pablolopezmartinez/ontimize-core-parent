package com.ontimize.util.swing.transfer;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageTransferHandler extends TransferHandler {

    private static final Logger logger = LoggerFactory.getLogger(ImageTransferHandler.class);

    protected boolean isReadableByImageIO(DataFlavor flavor) {
        Iterator<?> readers = ImageIO.getImageReadersByMIMEType(flavor.getMimeType());
        if (readers.hasNext()) {
            Class<?> cls = flavor.getRepresentationClass();
            return InputStream.class.isAssignableFrom(cls) || URL.class.isAssignableFrom(cls)
                    || File.class.isAssignableFrom(cls);
        }
        return false;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (support.getUserDropAction() == TransferHandler.LINK) {
            return false;
        }

        for (DataFlavor flavor : support.getDataFlavors()) {
            if (flavor.equals(DataFlavor.imageFlavor) || flavor.equals(DataFlavor.javaFileListFlavor)
                    || this.isReadableByImageIO(flavor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!(support.getComponent() instanceof JLabel)) {
            return false;
        }

        if (!this.canImport(support)) {
            return false;
        }

        // There are three types of DataFlavor to check:
        // 1. A java.awt.Image object (DataFlavor.imageFlavor)
        // 2. A List<File> object (DataFlavor.javaFileListFlavor)
        // 3. Binary data with an image/* MIME type.

        if (support.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                Image image = (Image) support.getTransferable().getTransferData(DataFlavor.imageFlavor);
                JLabel label = (JLabel) support.getComponent();
                label.setIcon(new ImageIcon(image));
                return true;
            } catch (UnsupportedFlavorException e) {
                ImageTransferHandler.logger.error(null, e);
            } catch (IOException ex) {
                ImageTransferHandler.logger.error(null, ex);
            }
        }

        if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            try {
                Iterable<?> list = (Iterable<?>) support.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);
                Iterator<?> files = list.iterator();
                if (files.hasNext()) {
                    File file = (File) files.next();
                    Image image = ImageIO.read(file);
                    JLabel label = (JLabel) support.getComponent();
                    label.setIcon(new ImageIcon(image));
                    return true;
                }
            } catch (UnsupportedFlavorException e) {
                ImageTransferHandler.logger.error(null, e);
            } catch (IOException ex) {
                ImageTransferHandler.logger.error(null, ex);
            }
        }

        for (DataFlavor flavor : support.getDataFlavors()) {
            if (this.isReadableByImageIO(flavor)) {
                try {
                    Image image;

                    Object data = support.getTransferable().getTransferData(flavor);
                    if (data instanceof URL) {
                        image = ImageIO.read((URL) data);
                    } else if (data instanceof File) {
                        image = ImageIO.read((File) data);
                    } else {
                        image = ImageIO.read((InputStream) data);
                    }
                    JLabel label = (JLabel) support.getComponent();
                    label.setIcon(new ImageIcon(image));
                    return true;
                } catch (UnsupportedFlavorException e) {
                    ImageTransferHandler.logger.error(null, e);
                } catch (IOException ex) {
                    ImageTransferHandler.logger.error(null, ex);
                }
            }
        }
        return false;
    }

}
