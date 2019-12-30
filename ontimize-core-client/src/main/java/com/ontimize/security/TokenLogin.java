package com.ontimize.security;

public interface TokenLogin {

	public String getToken() throws Exception;

	public String getUserFromCert(String certString) throws Exception;

	public String getPasswordFromCert(String certString) throws Exception;
}
