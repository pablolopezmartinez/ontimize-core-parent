package com.ontimize.cache;

import java.util.Vector;

public interface CachedComponent {

	public String getEntity();

	public Vector getAttributes();

	public void setCacheManager(CacheManager c);
}