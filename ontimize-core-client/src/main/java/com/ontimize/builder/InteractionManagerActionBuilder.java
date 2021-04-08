package com.ontimize.builder;

import java.io.InputStream;

import com.ontimize.gui.InteractionManager;
import com.ontimize.gui.InteractionManagerAction;

/**
 * <p>
 * Creates a {@link InteractionManagerAction} object model with a set of actions to manage the
 * parent form state with the given XML from the {@link InteractionManager}.
 *
 * @author Imatia Innovation S.L.
 * @since Ontimize 5.2059EN
 */
public interface InteractionManagerActionBuilder {

    /**
     * <p>
     * Loads a XML content from the given {@link String} <code>resource</code> and creates a
     * {@link InteractionManagerAction} action model.
     * @param resource {@link InputStream} XML source.
     * @return {@link InteractionManagerAction} action model.
     */
    public InteractionManagerAction buildAction(String resource);

    /**
     * <p>
     * Loads a XML content from the given {@link InputStream} <code>resource</code> and creates a
     * {@link InteractionManagerAction} action model.
     * @param resource {@link InputStream} XML source.
     * @return {@link InteractionManagerAction} action model.
     */
    public InteractionManagerAction buildAction(InputStream resource);

}
