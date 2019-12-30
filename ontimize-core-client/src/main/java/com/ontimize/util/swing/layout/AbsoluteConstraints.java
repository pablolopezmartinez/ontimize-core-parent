package com.ontimize.util.swing.layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbsoluteConstraints implements Cloneable {

	private static final Logger	logger	= LoggerFactory.getLogger(AbsoluteConstraints.class);

	protected int x;
	protected int y;
	protected int width;
	protected int height;

	public AbsoluteConstraints() {
		this.x = 0;
		this.y = 0;
		this.width = 100;
		this.height = 20;
	}

	public AbsoluteConstraints(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			AbsoluteConstraints.logger.trace(null, e);
			throw new InternalError();
		}
	}
}