package com.ontimize.gui.field;

/**
 * This interface extends the <code>DataComponent</code> interface adding a method to support the
 * advanced query state:
 *
 * <ul>
 * <li>setAdvancedQueryMode()
 * </ul>
 *
 * <p>
 *
 * @author Imatia Innovation
 */
public interface AdvancedDataComponent extends DataComponent {

    /**
     * The advanced query key.
     */
    public static String ADVANCED_QUERY = "advanced_search";

    /**
     * This method must establish the advanced query state, to support advanced query conditions.
     * <p>
     * @param enabled the condition to enable this state
     */
    public void setAdvancedQueryMode(boolean enabled);

}
