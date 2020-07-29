package com.ontimize.printing;

/**
 * Represents an HTML layer. It is used to provide an identifier and a size to the element inside
 */

public class Layer {

    protected String startTag = "<DIV>";

    protected String endTag = "</DIV>";

    /**
     * Creates a new layer with the specified identifier, width and height.
     * @param id
     * @param width
     * @param height
     * @param pixels If this is true then width and height are values in pixels in other case they are a
     *        percentage
     * @param align
     */
    public Layer(String id, int width, int height, boolean pixels, String align) {
        if (pixels) {
            this.startTag = "<DIV id='" + id + "'" + align + " style='width:" + Integer.toString(width) + ";height:"
                    + Integer.toString(height) + "'>";
        } else {
            this.startTag = "<DIV id='" + id + "'" + align + " style='width:" + Integer.toString(width) + "%;height:"
                    + Integer.toString(height) + "%'>";
        }
        this.endTag = "</DIV>";
    }

    /**
     * Creates a new layer using the specified identifier and size.
     * @param id
     * @param width
     * @param height
     * @param pixels If this is true then width and height are values in pixels in other case they are a
     *        percentage
     * @param align
     * @param leftMargin Used to locate the layer at the left side of the page
     */
    public Layer(String id, int width, int height, boolean pixels, String align, int leftMargin) {
        if (pixels) {
            this.startTag = "<DIV id='" + id + "'" + align + " style='width:" + Integer.toString(width) + ";height:"
                    + Integer
                        .toString(height)
                    + ";margin-left:" + leftMargin + "'>";
        } else {
            this.startTag = "<DIV id='" + id + "'" + align + " style='width:" + Integer.toString(width) + "%;height:"
                    + Integer
                        .toString(height)
                    + "%;margin-left:" + leftMargin + "'>";
        }
        this.endTag = "</DIV>";
    }

    public String getStartTag() {
        return this.startTag;
    }

    public String getEndTag() {
        return this.endTag;
    }

}
