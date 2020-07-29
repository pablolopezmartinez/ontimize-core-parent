package com.ontimize.util.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ZipUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    public static void compress(File in, File out) throws IOException {
        FileInputStream fIn = null;
        FileOutputStream fOut = null;
        try {
            fIn = new FileInputStream(in);
            fOut = new FileOutputStream(out);
            ZipUtils.compress(in.getName(), fIn, fOut, Deflater.DEFAULT_COMPRESSION);
        } finally {
            try {
                fIn.close();
                fOut.close();
            } catch (Exception ex) {
                ZipUtils.logger.trace(null, ex);
            }
        }
    }

    public static void compress(InputStream in, OutputStream out) throws IOException {
        ZipUtils.compress(null, in, out, Deflater.DEFAULT_COMPRESSION);
    }

    public static void compress(String entryName, InputStream in, OutputStream out) throws IOException {
        ZipUtils.compress(entryName, in, out, Deflater.DEFAULT_COMPRESSION);
    }

    public static void compressList(List<File> fileList, OutputStream out) throws IOException {
        ZipUtils.compressList(fileList, out, Deflater.DEFAULT_COMPRESSION);
    }

    public static void compress(String entryName, InputStream in, OutputStream out, int compressionLevel)
            throws IOException {
        ZipOutputStream zip = null;
        BufferedOutputStream bOut = null;
        BufferedInputStream bIn = null;
        try {
            zip = new ZipOutputStream(out);
            bOut = new BufferedOutputStream(zip, 64 * 1024);
            bIn = new BufferedInputStream(in);
            zip.setLevel(compressionLevel);
            ZipEntry entradaZip = new ZipEntry(entryName != null ? entryName : "file");
            zip.putNextEntry(entradaZip);
            int i = -1;
            while ((i = bIn.read()) != -1) {
                bOut.write(i);
            }
            bOut.flush();
            zip.closeEntry();
        } catch (IOException e) {
            ZipUtils.logger.error(null, e);
            throw e;
        } finally {
            try {
                zip.close();
            } catch (Exception e) {
                ZipUtils.logger.trace(null, e);
            }
        }
    }

    public static void compressList(List<File> fileList, OutputStream out, int compressionLevel) throws IOException {
        ZipOutputStream zip = null;
        BufferedOutputStream bOut = null;
        BufferedInputStream bIn = null;
        FileInputStream fIn = null;
        try {
            zip = new ZipOutputStream(out);
            bOut = new BufferedOutputStream(zip, 64 * 1024);
            zip.setLevel(compressionLevel);
            for (File f : fileList) {
                fIn = new FileInputStream(f);
                bIn = new BufferedInputStream(fIn);
                ZipEntry entradaZip = new ZipEntry(f.getName() != null ? f.getName() : "file");
                zip.putNextEntry(entradaZip);
                int i = -1;
                while ((i = bIn.read()) != -1) {
                    bOut.write(i);
                }
                bOut.flush();
                fIn.close();
            }
            zip.closeEntry();
        } catch (IOException e) {
            ZipUtils.logger.error(null, e);
            throw e;
        } finally {
            try {
                zip.close();
            } catch (Exception e) {
                ZipUtils.logger.trace(null, e);
            }
        }
    }

    public static void decompress(InputStream in, OutputStream out) throws IOException {
        ZipInputStream zip = null;
        BufferedOutputStream bOut = null;
        BufferedInputStream bIn = null;
        try {
            // Now descompress
            zip = new ZipInputStream(in);
            bIn = new BufferedInputStream(zip);
            ZipEntry inputZip = zip.getNextEntry();
            int i = -1;
            while ((i = bIn.read()) != -1) {
                out.write(i);
            }
            out.flush();
        } finally {
            try {
                bIn.close();
            } catch (Exception e) {
                ZipUtils.logger.trace(null, e);
            }
            try {
                bOut.close();
            } catch (Exception e) {
                ZipUtils.logger.trace(null, e);
            }
        }
    }

    public static void main(String[] args) throws Exception {

        javax.swing.UIDefaults defs = javax.swing.UIManager.getDefaults();
        java.util.Enumeration keys = defs.keys();
        while (keys.hasMoreElements()) {
            Object k = keys.nextElement();
            if (k.toString().toLowerCase().indexOf("tabbedpane") >= 0) {
                ZipUtils.logger.debug(k + " ->  " + defs.get(k));
            }
        }
    }

}
