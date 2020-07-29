package com.ontimize.util.rtf;

import java.awt.Color;
import java.io.Serializable;

public class BorderAttributes implements Serializable {

    public static final int TOP = 1;

    public static final int HORIZONTAL = 2;

    public static final int BOTTOM = 4;

    public static final int LEFT = 8;

    public static final int VERTICAL = 16;

    public static final int RIGHT = 32;

    public int borderTop;

    public int borderHorizontal;

    public int borderBottom;

    public int borderLeft;

    public int borderVertical;

    public int borderRight;

    public Color lineColor;

    public BorderAttributes() {
        this.borderTop = 0;

        this.borderHorizontal = 0;

        this.borderBottom = 0;

        this.borderLeft = 0;

        this.borderVertical = 0;

        this.borderRight = 0;

        this.lineColor = new Color(0, 0, 0);
    }

    public void setBorders(int borders) {
        int val = borders;

        this.borderTop = val % 2;
        val /= 2;
        this.borderHorizontal = val % 2;
        val /= 2;
        this.borderBottom = val % 2;
        val /= 2;

        this.borderLeft = val % 2;
        val /= 2;
        this.borderVertical = val % 2;
        val /= 2;
        this.borderRight = val % 2;
        val /= 2;
    }

    public int getBorders() {
        int result = 0;
        result += this.borderTop;
        result += this.borderHorizontal * 2;
        result += this.borderBottom * 4;

        result += this.borderLeft * 8;
        result += this.borderVertical * 16;
        result += this.borderRight * 32;
        return result;
    }

}
