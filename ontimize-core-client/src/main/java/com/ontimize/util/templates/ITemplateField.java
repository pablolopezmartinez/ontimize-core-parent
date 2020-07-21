package com.ontimize.util.templates;

/**
 * This interface must be implements by each data field that will be using to fill/create a
 * template.
 *
 * @author Imatia Innovation S.L.
 * @since 5.2057EN-0.7
 */
public interface ITemplateField {

    public static final String TEMPLATE_DATA_TYPE = "templatedatatype";

    public static final String DATA_TYPE_FIELD_ATTR = "field";

    public static final String DATA_TYPE_IMAGE_ATTR = "image";

    public static final String DATA_TYPE_TABLE_ATTR = "table";

    public static final int DATA_TYPE_FIELD = 1;

    public static final int DATA_TYPE_IMAGE = 2;

    public static final int DATA_TYPE_TABLE = 3;

    /**
     * Returns the data type. The values that can return are:
     *
     * {@link #DATA_TYPE_FIELD} {@link #DATA_TYPE_IMAGE} {@link #DATA_TYPE_TABLE}
     * @return a <code>int</code> with the data type.
     */
    public int getTemplateDataType();

    /**
     * Gets the value to show in the template
     * @return
     */
    public Object getTemplateDataValue();

}
