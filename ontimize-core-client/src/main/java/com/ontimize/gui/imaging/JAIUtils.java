package com.ontimize.gui.imaging;

import java.awt.image.RenderedImage;
import java.io.OutputStream;
import java.net.URL;

import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JAIUtils {

    private static final Logger logger = LoggerFactory.getLogger(JAIUtils.class);

    private static boolean JAI_ENABLED = false;

    private static final void check() {
        try {
            Class.forName("javax.media.jai.JAI");
            JAIUtils.JAI_ENABLED = true;
        } catch (Exception e) {
            JAIUtils.JAI_ENABLED = false;
            JAIUtils.logger.error("Java Advanced Imaging no disponible", e);
        }
    }

    static {
        JAIUtils.check();
    }

    /**
     * Check if the JAI libraries are available
     * @return true if the class javax.media.jai.JAI exists in the classpath
     *
     */
    public boolean isJAIEnabled() {
        if (!JAIUtils.JAI_ENABLED) {
            JAIUtils.check();
        }
        return JAIUtils.JAI_ENABLED;
    }

    public static RenderedOp getImageFromFile(String fileName) {
        return JAI.create("fileload", fileName);
    }

    public static RenderedOp getImageFromURL(URL url) {
        return JAI.create("URL", url);
    }

    public static void saveImage(RenderedImage image, String format, OutputStream out) {
        JAIUtils.saveImage(image, format, out, null);
    }

    public static void saveImage(RenderedImage image, String format, OutputStream out,
            com.sun.media.jai.codec.ImageEncodeParam param) {
        ParameterBlockJAI pb = new ParameterBlockJAI("encode");
        pb.addSource(image);
        pb.setParameter("stream", out);
        pb.setParameter("format", format);
        if (param != null) {
            pb.setParameter("param", param);
        }
        JAI.create("encode", pb);
    }

    public static RenderedImage loadImage(com.sun.media.jai.codec.SeekableStream stream) {
        return JAI.create("stream", stream);
    }

}
