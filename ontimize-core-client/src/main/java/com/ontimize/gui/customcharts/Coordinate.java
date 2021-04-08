package com.ontimize.gui.customcharts;

public class Coordinate {

    float x = 0;

    float y = 0;

    float z = 0;

    public Coordinate(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return this.x;
    }

    public float getZ() {
        return this.z;
    }

    public float getY() {
        return this.y;
    }

}
