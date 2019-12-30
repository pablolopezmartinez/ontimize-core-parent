package com.ontimize.gui.customcharts;

import java.awt.Point;
import java.awt.Rectangle;

public class Projector {

	private float scale_x;
	private float scale_y;
	private float scale_z;
	private float distance;
	private float _2D_scale;
	private float rotation;
	private float elevation;
	private float sin_rotation;
	private float cos_rotation;
	private float sin_elevation;
	private float cos_elevation;
	private int _2D_trans_x;
	private int _2D_trans_y;
	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private int center_x;
	private int center_y;
	private int trans_x;
	private int trans_y;
	private float factor;
	private float sx_cos;
	private float sy_cos;
	private float sz_cos;
	private float sx_sin;
	private float sy_sin;
	private float sz_sin;

	// private final float DEGTORAD = 0.01745329F;

	public Projector() {
		this.setScaling(1.0F);
		this.setRotationAngle(0.0F);
		this.setElevationAngle(0.0F);
		this.setDistance(10F);
		this.set2DScaling(1.0F);
		this.set2DTranslation(0, 0);
	}

	public void setProjectionArea(Rectangle rectangle) {
		this.x1 = rectangle.x;
		this.x2 = this.x1 + rectangle.width;
		this.y1 = rectangle.y;
		this.y2 = this.y1 + rectangle.height;
		this.center_x = (this.x1 + this.x2) / 2;
		this.center_y = (this.y1 + this.y2) / 2;
		this.trans_x = this.center_x + this._2D_trans_x;
		this.trans_y = this.center_y + this._2D_trans_y;
	}

	public void setRotationAngle(float f) {
		this.rotation = f;
		this.sin_rotation = (float) Math.sin(f * 0.01745329F);
		this.cos_rotation = (float) Math.cos(f * 0.01745329F);
		this.sx_cos = -this.scale_x * this.cos_rotation;
		this.sx_sin = -this.scale_x * this.sin_rotation;
		this.sy_cos = -this.scale_y * this.cos_rotation;
		this.sy_sin = this.scale_y * this.sin_rotation;
	}

	public float getRotationAngle() {
		return this.rotation;
	}

	public float getSinRotationAngle() {
		return this.sin_rotation;
	}

	public float getCosRotationAngle() {
		return this.cos_rotation;
	}

	public void setElevationAngle(float f) {
		this.elevation = f;
		this.sin_elevation = (float) Math.sin(f * 0.01745329F);
		this.cos_elevation = (float) Math.cos(f * 0.01745329F);
		this.sz_cos = this.scale_z * this.cos_elevation;
		this.sz_sin = this.scale_z * this.sin_elevation;
	}

	public float getElevationAngle() {
		return this.elevation;
	}

	public float getSinElevationAngle() {
		return this.sin_elevation;
	}

	public float getCosElevationAngle() {
		return this.cos_elevation;
	}

	public void setDistance(float f) {
		this.distance = f;
		this.factor = this.distance * this._2D_scale;
	}

	public float getDistance() {
		return this.distance;
	}

	public void setXScaling(float f) {
		this.scale_x = f;
		this.sx_cos = -this.scale_x * this.cos_rotation;
		this.sx_sin = -this.scale_x * this.sin_rotation;
	}

	public float getXScaling() {
		return this.scale_x;
	}

	public void setYScaling(float f) {
		this.scale_y = f;
		this.sy_cos = -this.scale_y * this.cos_rotation;
		this.sy_sin = this.scale_y * this.sin_rotation;
	}

	public float getYScaling() {
		return this.scale_y;
	}

	public void setZScaling(float f) {
		this.scale_z = f;
		this.sz_cos = this.scale_z * this.cos_elevation;
		this.sz_sin = this.scale_z * this.sin_elevation;
	}

	public float getZScaling() {
		return this.scale_z;
	}

	public void setScaling(float f, float f1, float f2) {
		this.scale_x = f;
		this.scale_y = f1;
		this.scale_z = f2;
		this.sx_cos = -this.scale_x * this.cos_rotation;
		this.sx_sin = -this.scale_x * this.sin_rotation;
		this.sy_cos = -this.scale_y * this.cos_rotation;
		this.sy_sin = this.scale_y * this.sin_rotation;
		this.sz_cos = this.scale_z * this.cos_elevation;
		this.sz_sin = this.scale_z * this.sin_elevation;
	}

	public void setScaling(float f) {
		this.scale_x = this.scale_y = this.scale_z = f;
		this.sx_cos = -this.scale_x * this.cos_rotation;
		this.sx_sin = -this.scale_x * this.sin_rotation;
		this.sy_cos = -this.scale_y * this.cos_rotation;
		this.sy_sin = this.scale_y * this.sin_rotation;
		this.sz_cos = this.scale_z * this.cos_elevation;
		this.sz_sin = this.scale_z * this.sin_elevation;
	}

	public void set2DScaling(float f) {
		this._2D_scale = f;
		this.factor = this.distance * this._2D_scale;
	}

	public float get2DScaling() {
		return this._2D_scale;
	}

	public void set2DTranslation(int i, int j) {
		this._2D_trans_x = i;
		this._2D_trans_y = j;
		this.trans_x = this.center_x + this._2D_trans_x;
		this.trans_y = this.center_y + this._2D_trans_y;
	}

	public void set2D_xTranslation(int i) {
		this._2D_trans_x = i;
		this.trans_x = this.center_x + this._2D_trans_x;
	}

	public int get2D_xTranslation() {
		return this._2D_trans_x;
	}

	public void set2D_yTranslation(int i) {
		this._2D_trans_y = i;
		this.trans_y = this.center_y + this._2D_trans_y;
	}

	public int get2D_yTranslation() {
		return this._2D_trans_y;
	}

	public Point project(float f, float f1, float f2) {
		float f3 = f;
		f = (f * this.sx_cos) + (f1 * this.sy_sin);
		f1 = (f3 * this.sx_sin) + (f1 * this.sy_cos);
		f3 = this.factor / (((f1 * this.cos_elevation) - (f2 * this.sz_sin)) + this.distance);
		return new Point(Math.round(f * f3) + this.trans_x, Math.round(((f1 * this.sin_elevation) + (f2 * this.sz_cos)) * -f3) + this.trans_y);
	}

}