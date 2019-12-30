package com.ontimize.ols;

import java.rmi.Remote;
import java.util.Hashtable;

public interface RemoteLControlAdministration extends Remote {

	public Hashtable getParameters(String login, String password) throws Exception;

	public Hashtable updateL(Hashtable h, String login, String password) throws Exception;

	public boolean ok(String login, String password) throws Exception;
}
