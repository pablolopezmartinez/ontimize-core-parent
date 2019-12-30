package com.ontimize.gui.login;

import java.security.cert.Certificate;

public class AliasCertPair {

	private String alias = null;
	private Certificate cert = null;

	public AliasCertPair(String alias, Certificate cert) {
		this.alias = alias;
		this.cert = cert;
	}

	public String getAlias() {
		return this.alias;
	}

	public Certificate getCert() {
		return this.cert;
	}

}
