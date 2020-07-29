package com.ontimize.util.logging;

import java.util.List;

import org.slf4j.Logger;

import com.ontimize.util.remote.BytesBlock;

public interface ILogManager {

    public List<Logger> getLoggerList();

    public Logger getLogger(String name);

    public Level getLevel(Logger logger);

    public void setLevel(Logger logger, Level level) throws Exception;

    public Object findAppenderOfType(Class interfaceOfAppender);
    // public Object serverMonitorAppender();
    // public void registerServerMonitor(ExtendedServerMonitor monitor);

    public BytesBlock getFileLogger();

}
