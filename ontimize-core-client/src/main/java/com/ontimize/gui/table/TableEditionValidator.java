package com.ontimize.gui.table;

public interface TableEditionValidator {

    /**
     * Check the edition
     * @param ev
     * @return false when the edition is not valid
     */
    public boolean validEdition(CustomTableEditionEvent ev);

}
