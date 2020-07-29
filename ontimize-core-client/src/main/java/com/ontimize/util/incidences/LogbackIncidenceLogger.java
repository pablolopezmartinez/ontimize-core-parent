package com.ontimize.util.incidences;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.util.zip.ZipUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;

public class LogbackIncidenceLogger implements IIncidenceLogger {

    private static final Logger logger = LoggerFactory.getLogger(LogbackIncidenceLogger.class);

    @Override
    public String getClientLogger() {
        return this.getClientAuditFileAppender();
    }

    @Override
    public ByteArrayOutputStream getCompressClientLogger() {

        String content = this.getClientLogger();
        if (content != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(System.getProperty("java.io.tmpdir"));
            builder.append("log_client");
            builder.append(System.currentTimeMillis());
            builder.append(".txt");

            File output = new File(builder.toString());
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(output);
                fileWriter.write(content);
                fileWriter.flush();

            } catch (Exception e) {
                LogbackIncidenceLogger.logger.error("Error writting a file writer.", e);
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException e1) {
                    LogbackIncidenceLogger.logger.error("Error closing file writer.", e1);
                }
            }

            output.deleteOnExit();

            FileInputStream in = null;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (output.exists()) {
                try {
                    in = new FileInputStream(output);
                } catch (FileNotFoundException e) {
                    LogbackIncidenceLogger.logger.error("File not found.", e);
                }
                try {
                    ZipUtils.compress(output.getName(), in, out);
                } catch (IOException e) {
                    LogbackIncidenceLogger.logger.error("Log file can't be compress.", e);
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e1) {
                        LogbackIncidenceLogger.logger.error("FileInputStream can't be closed.", e1);
                    }
                }
            }

            return out;
        }
        return null;
    }

    @Override
    public String getServerLogger() {
        return this.getServerAuditFileAppender();
    }

    @Override
    public ByteArrayOutputStream getCompressServerLogger() {

        String content = this.getClientLogger();
        if (content != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(System.getProperty("java.io.tmpdir"));
            builder.append("log_server");
            builder.append(System.currentTimeMillis());
            builder.append(".txt");

            File output = new File(builder.toString());
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(output);
                fileWriter.write(content);
                fileWriter.flush();

            } catch (Exception e) {
                LogbackIncidenceLogger.logger.error("Error writting a file writer.", e);
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException e1) {
                    LogbackIncidenceLogger.logger.error("Error closing file writer.", e1);
                }
            }

            output.deleteOnExit();

            FileInputStream in = null;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (output.exists()) {
                try {
                    in = new FileInputStream(output);
                } catch (FileNotFoundException e) {
                    LogbackIncidenceLogger.logger.error("File not found. ", e);
                }
                try {
                    ZipUtils.compress(output.getName(), in, out);
                } catch (IOException e) {
                    LogbackIncidenceLogger.logger.error("Log file can't be compress.", e);
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e1) {
                        LogbackIncidenceLogger.logger.error("FileInputStream can't be closed.", e1);
                    }
                }
            }

            return out;
        }
        return null;
    }

    public void readFromLast(ArrayDeque<String> queue, File file, int lines) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();

            while (line != null) {
                this.addToList(queue, line, lines);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            LogbackIncidenceLogger.logger.error("File not found.", e);
        } catch (IOException e) {
            LogbackIncidenceLogger.logger.error("File can't be read.", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                LogbackIncidenceLogger.logger.error("Buffered reader can't be closed.", e);
            }
        }
    }

    protected void addToList(ArrayDeque<String> queue, String element, int lines) {
        if (queue.size() >= lines) {
            queue.removeFirst();
            queue.offer(element);
        } else {
            queue.offer(element);
        }
    }

    public String readFile(File f) {
        ArrayDeque<String> queue = new ArrayDeque<String>(500);
        this.readFromLast(queue, f, 500);
        StringBuilder builder = new StringBuilder();
        while (!queue.isEmpty()) {
            builder.append(queue.removeFirst());
            builder.append("\n");
        }
        return builder.toString();
    }

    protected String getClientAuditFileAppender() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger loggerRoot = lc.getLogger("ROOT");
        Iterator<Appender<ILoggingEvent>> iteratorAppenders = loggerRoot.iteratorForAppenders();
        while (iteratorAppenders.hasNext()) {
            Appender<ILoggingEvent> a = iteratorAppenders.next();
            if (a instanceof RollingFileAppender) {
                RollingFileAppender rp = (RollingFileAppender) a;
                return this.readFile(new File(rp.getFile()));
            }
        }
        return null;
    }

    protected String getServerAuditFileAppender() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger loggerRoot = lc.getLogger("ROOT");
        Iterator<Appender<ILoggingEvent>> iteratorAppenders = loggerRoot.iteratorForAppenders();
        while (iteratorAppenders.hasNext()) {
            Appender<ILoggingEvent> a = iteratorAppenders.next();
            if (a instanceof RollingFileAppender) {
                RollingFileAppender rp = (RollingFileAppender) a;
                return this.readFile(new File(rp.getFile()));
            }
        }
        return null;
    }

}
