package com.ontimize.util.incidences;

import java.io.ByteArrayOutputStream;

public interface IIncidenceLogger {

    public String getClientLogger();

    public ByteArrayOutputStream getCompressClientLogger();

    public String getServerLogger();

    public ByteArrayOutputStream getCompressServerLogger();

}
