package com.ontimize.builder;

import javax.swing.tree.TreeModel;

public interface TreeBuilder {

    /**
     * Create a TreeModel from the indicated file definition
     * @param uriFile URI to the tree definition file (typically XML File)
     * @return
     */
    public TreeModel buildTree(String uriFile);

    /**
     * Create a TreeModel from the indicated definition
     * @param content Definition of the TreeModel (typically XML)
     * @return
     */
    public TreeModel buildTree(StringBuffer content);

}
