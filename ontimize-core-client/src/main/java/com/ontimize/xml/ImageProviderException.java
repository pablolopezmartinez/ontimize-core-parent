package com.ontimize.xml;

import com.ontimize.db.EntityResult;

public class ImageProviderException extends Exception {

	public ImageProviderException(String column, EntityResult er) {
		super("Image Entity Error. Column not found " + column + ". Results " + er);
	}
}
