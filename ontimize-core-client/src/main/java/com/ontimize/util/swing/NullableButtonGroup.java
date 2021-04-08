package com.ontimize.util.swing;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JRadioButton;

/**
 * This class implements a ButtonGroup. Originally, this component always tries to select one of the
 * components (radio buttons typically) placed inside. With this class is allowed null selection (a
 * non-visible radio button is selected).
 *
 * @author Imatia Innovation SL
 */
public class NullableButtonGroup extends ButtonGroup {

    public static final String NULL_SELECTION = "nullselection";

    /**
     * Hidden JRadioButton to allow null selection in radio buttons. It is automatically selected when
     * not exist visible RadioButtons.
     */
    protected JRadioButton nullButton;

    /**
     * Boolean to allow null selection.
     */
    protected boolean allowNullSelection = false;

    public NullableButtonGroup(boolean allowNullSelection) {
        super();
        this.allowNullSelection = allowNullSelection;
        this.createcomponent();
    }

    public void createcomponent() {
        if (this.allowNullSelection) {
            this.nullButton = new JRadioButton(NullableButtonGroup.NULL_SELECTION);
            this.add(this.nullButton);
        }
    }

    // Overridden method because <code>ButtonGroup</code> calls automatically to
    // setSelected() to select one of
    // their elements when user deselect another one.
    @Override
    public void setSelected(ButtonModel m, boolean b) {
        boolean selectNull = !b && this.allowNullSelection && !this.nullButton.isSelected();

        if (this.getSelection() != null) {
            if (!m.equals(this.getSelection())) {
                selectNull = false;
            }
        }
        if (selectNull) {
            this.nullButton.setSelected(true);
        } else {
            super.setSelected(m, b);
        }
    }

}
