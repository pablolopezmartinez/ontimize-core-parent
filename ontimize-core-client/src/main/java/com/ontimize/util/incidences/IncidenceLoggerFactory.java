package com.ontimize.util.incidences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

public class IncidenceLoggerFactory {

    private static final Logger logger = LoggerFactory.getLogger(IncidenceLoggerFactory.class);

    public static IIncidenceLogger incidenceLoggerInstance(String loggerFactoryClassName) {

        if (loggerFactoryClassName == null) {
            IncidenceLoggerFactory.logger.error("No logger factory is binded");
            return null;
        }

        String loggerClassFactory = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();

        if (loggerFactoryClassName.equals(loggerClassFactory)) {
            return new LogbackIncidenceLogger();
        }

        return null;
    }

}
