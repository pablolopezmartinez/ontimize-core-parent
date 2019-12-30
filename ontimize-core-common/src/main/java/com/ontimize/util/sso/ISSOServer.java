package com.ontimize.util.sso;

import java.rmi.Remote;
import java.util.Hashtable;

public interface ISSOServer extends Remote {

	public String getServerServiceName(String clientName) throws Exception;

	public byte[] returnMessage(String contextRef, byte[] inData) throws Exception;

	public Hashtable getConfigurationParameters(String clientName) throws Exception;

}
