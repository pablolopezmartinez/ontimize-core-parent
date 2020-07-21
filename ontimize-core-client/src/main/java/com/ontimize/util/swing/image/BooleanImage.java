package com.ontimize.util.swing.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BooleanImage extends BufferedImage {

    /**
     * String for true value.
     */
    public static String msStringForTrue = "Yes";

    /**
     * String for false value.
     */
    public static String msStringForFalse = "No";

    /**
     * Boolean value
     */
    protected boolean mValue;

    /**
     * BooleanImage constructor.
     * @param bImage Buffered Image
     */
    public BooleanImage(boolean value, BufferedImage bImage) {
        super(bImage.getWidth(), bImage.getHeight(), bImage.getType());
        // Save boolean value
        this.mValue = value;
        // Paint content
        Graphics g = this.createGraphics();
        g.drawImage(bImage, 0, 0, null);
        g.dispose();
    }

    /**
     * Gets boolean value.
     * @return Boolean value
     */
    public boolean getValue() {
        return this.mValue;
    }

    @Override
    public String toString() {
        return this.mValue ? BooleanImage.msStringForTrue : BooleanImage.msStringForFalse;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BooleanImage)) {
            return false;
        }
        return this.mValue == ((BooleanImage) obj).getValue();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
