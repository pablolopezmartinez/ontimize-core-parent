package com.ontimize.db.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 *
 * @version 1.0
 */

public abstract class ExportTxt {

    private static final Logger logger = LoggerFactory.getLogger(ExportTxt.class);

    public static String exportData(Hashtable data, char delimiter, char rowDelimiter) {
        if (data == null) {
            throw new IllegalArgumentException("ExportTxt: data is null");
        }
        if (data.isEmpty()) {
            throw new IllegalArgumentException("ExportTxt: data is empty");
        } else {
            Vector vKeys = new Vector();
            Enumeration eKeys = data.keys();
            while (eKeys.hasMoreElements()) {
                vKeys.add(eKeys.nextElement());
            }
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < vKeys.size(); i++) {
                res.append(vKeys.get(i));
                if (i < (vKeys.size() - 1)) {
                    res.append(delimiter);
                }
            }
            int size = ((Vector) data.get(vKeys.get(0))).size();
            for (int j = 0; j < size; j++) {
                res.append(rowDelimiter);
                for (int i = 0; i < vKeys.size(); i++) {
                    Vector v = (Vector) data.get(vKeys.get(i));
                    if (v.get(j) != null) {
                        res.append(v.get(j));
                    }
                    if (i < (vKeys.size() - 1)) {
                        res.append(delimiter);
                    }
                }
            }
            res.append(rowDelimiter);
            return res.toString();
        }
    }

    public static void exportData(Hashtable data, char delimiter, char rowDelimiter, File f) throws IOException {
        FileWriter fW = null;
        BufferedWriter bW = null;
        try {
            fW = new FileWriter(f);
            bW = new BufferedWriter(fW);
            bW.write(ExportTxt.exportData(data, delimiter, rowDelimiter));
            bW.flush();
        } catch (IOException e) {
            ExportTxt.logger.error(null, e);
            throw e;
        } finally {
            if (bW != null) {
                bW.close();
            }
            if (fW != null) {
                fW.close();
            }
        }
    }

}
