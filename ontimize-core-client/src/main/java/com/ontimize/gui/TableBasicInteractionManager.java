package com.ontimize.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements an basic interaction manager to use with the table detail forms
 */
public class TableBasicInteractionManager extends BasicInteractionManager {

    static final Logger logger = LoggerFactory.getLogger(TableBasicInteractionManager.class);

    public TableBasicInteractionManager() {
        super(true, true);
    }

    public TableBasicInteractionManager(boolean update) {
        super(update, true);
    }

    @Override
    public InteractionManager cloneInteractionManager() {
        try {
            return super.cloneInteractionManager();
        } catch (Exception e) {
            TableBasicInteractionManager.logger.trace(null, e);
            return new TableBasicInteractionManager(this.afterUpdate);
        }
    }

}
