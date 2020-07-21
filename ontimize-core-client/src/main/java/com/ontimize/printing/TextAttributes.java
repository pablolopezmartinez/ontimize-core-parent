package com.ontimize.printing;

/**
 * Class that represents possible attributes in text (font type, font style and font size).
 *
 * @author Imatia Innovation
 */
public class TextAttributes {

    // font type
    public static final String ALGERIAN = "Algerian";

    public static final String ARIAL = "Arial";

    public static final String TIMES_NEW_ROMAN = "Times New Roman";

    public static final String COURIER = "Courier";

    public static final String MS_SERIF = "MS Serif";

    public static final String SYMBOL = "Symbol";

    public static final String SYSTEM = "System";

    public static final String WINDSORT = "Windsort";

    // font-style
    public static final int BOLD = 0;

    public static final int ITALICS = 1;

    public static final int SUBRAYADO = 2;

    public static final int BOLD_ITALICS = 3;

    public static final int BOLD_UNDERLINED = 4;

    public static final int ITALICS_UNDERLINED = 5;

    public static final int BOLD_ITALICS_UNDERLINED = 6;

    public static final int NORMAL = 7;

    // font-size
    public static final int VERY_SMALL = 1;

    public static final int SMALL = 2;

    public static final int REGULAR = 3;

    public static final int LARGE = 4;

    public static final int VERY_LARGE = 5;

    public static final int EXTRA_LARGE = 6;

    public static final int HUGE = 7;

    // Attribute variables
    protected String font = TextAttributes.COURIER;

    protected int size = TextAttributes.REGULAR;

    protected int style = TextAttributes.NORMAL;

    protected String startStyleTag = "";

    protected String endStyleTag = "";

    /**
     * Empty constructor.
     */
    public TextAttributes() {
    }

    /**
     * Builds an object <code>TextAttributes</code> with specific values for attributes.
     * @param fonttype The font type
     * @param fontsize The font size
     * @param fontstyle The font style
     */
    public TextAttributes(String fonttype, int fontsize, int fontstyle) {
        this.font = fonttype;
        this.size = fontsize;
        this.style = fontstyle;
    }

    /**
     * Creates and gets the default attributes: font type: COURIER; font-size: SMALL and font-size:
     * NORMAL.
     * @return the text attributes
     */
    public static TextAttributes getDefaultAttributes() {
        return new TextAttributes(TextAttributes.COURIER, TextAttributes.SMALL, TextAttributes.NORMAL);
    }

    /**
     * Gets the start tag (<FONT ...>).
     * @return the start tag
     */
    public String getStartTag() {
        // Style tags
        this.startStyleTag = "";
        this.endStyleTag = "";
        switch (this.style) {
            case BOLD:
                this.startStyleTag = "<B>";
                this.endStyleTag = "</B>";
                break;
            case ITALICS:
                this.startStyleTag = "<I>";
                this.endStyleTag = "</I>";
                break;
            case SUBRAYADO:
                this.startStyleTag = "<U>";
                this.endStyleTag = "</U>";
                break;
            case BOLD_ITALICS:
                this.startStyleTag = "<B><I>";
                this.endStyleTag = "</I></B>";
                break;
            case BOLD_UNDERLINED:
                this.startStyleTag = "<B><U>";
                this.endStyleTag = "</U></B>";
                break;
            case ITALICS_UNDERLINED:
                this.startStyleTag = "<I><U>";
                this.endStyleTag = "</U></I>";
                break;
            case BOLD_ITALICS_UNDERLINED:
                this.startStyleTag = "<B><I><U>";
                this.endStyleTag = "</U></I></B>";
                break;
            default:
                this.startStyleTag = "";
                this.endStyleTag = "";
        }
        return "<FONT face='" + this.font + "' size='" + Integer.toString(this.size) + "'>" + this.startStyleTag;
    }

    /**
     * Gets the end tag (...</FONT>).
     * @return
     */
    public String getEndTag() {
        return this.endStyleTag + "</FONT>";
    }

    public void setFontSize(int fontsize) {
        this.size = fontsize;
    }

}
