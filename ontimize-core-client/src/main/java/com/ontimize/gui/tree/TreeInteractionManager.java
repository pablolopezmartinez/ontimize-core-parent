package com.ontimize.gui.tree;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.db.Entity;
import com.ontimize.locator.EntityReferenceLocator;

public class TreeInteractionManager {

	private static final Logger	logger	= LoggerFactory.getLogger(TreeInteractionManager.class);

	/**
	 * Reference to the tree.
	 */
	Tree tree = null;

	/**
	 * Reference to the locator.
	 */
	EntityReferenceLocator locator = null;

	public TreeInteractionManager(Tree managedTree, EntityReferenceLocator locator) {
		this.locator = locator;
	}

	public void createOTreeNode(OTreeNode parentNode) {
		try {
			Entity entity = this.locator.getEntityReference(parentNode.getEntityName());
			// Needs an entity reference.
			if (entity == null) {
				return;
			}
			Vector vAttributes = new Vector();
			String[] sAttributes = parentNode.getAttributes();
			for (int i = 0; i < sAttributes.length; i++) {
				vAttributes.add(sAttributes[i]);
			}
			// For each parent field. we look for the value.
			// We looks for organizational nodes, moving across all branches.
			Vector vOrganizationNodes = new Vector();
			for (int i = 0; i < parentNode.getChildCount(); i++) {
				// If exists a non-organizational node is due to they have not
				// deleted yet
				if (((OTreeNode) parentNode.getChildAt(i)).isOrganizational()) {
					vOrganizationNodes.add(parentNode.getChildAt(i));
				} else {
					return;
				}
			}

			// For tree nodes, moreover 'attr' we need the 'parentkeys' to
			// identify
			// the node.
			for (int i = 0; i < parentNode.getKeys().size(); i++) {
				vAttributes.add(parentNode.getKeys().get(i));
			}

			// In query, we have to consider the upper data nodes
			Hashtable hSearchKeysValues = new Hashtable();
			OTreeNode otnParentNode;
			if (!parentNode.isRoot()) {
				otnParentNode = (OTreeNode) parentNode.getParent();
				Hashtable hAssociatedFields = parentNode.getAssociatedDataField();
				Hashtable hKeysValues = otnParentNode.getKeysValues();
				// From keys and values, we get the associated field
				Enumeration enumAssociatedFieldKeys = hAssociatedFields.keys();
				while (enumAssociatedFieldKeys.hasMoreElements()) {
					Object oParentField = enumAssociatedFieldKeys.nextElement();
					Object oChildField = hAssociatedFields.get(oParentField);
					hSearchKeysValues.put(oChildField, otnParentNode.getValueForAttribute(oParentField));
				}
			} else { // If it is the root, we look for all
			}
			try {
				Hashtable result = entity.query(hSearchKeysValues, vAttributes, this.locator.getSessionId());
				if (result.isEmpty()) {
					// If it is empty, remove all children.
					parentNode.removeAllChildren();
					Hashtable parameters = new Hashtable();
					parameters.put(OTreeNode.TEXT, OTreeNode.THERE_ARENT_RESULTS_KEY);
					parameters.put(OTreeNode.ATTR, "");
					parameters.put(OTreeNode.ENTITY, "");
					parameters.put(OTreeNode.ORG, "false");
					if (parentNode.getKeysString() != null) {
						parameters.put(OTreeNode.KEYS, parentNode.getKeysString());
					}
					if (parentNode.getStringAssociatedDataField() != null) {
						parameters.put(OTreeNode.PARENT_KEYS, parentNode.getStringAssociatedDataField());
					}
					OTreeNode warningNode = new OTreeNode(parameters);
					parentNode.add(warningNode);
					return;
				}

				// Create child nodes of this node. But, these child nodes must
				// be
				// placed
				// like child nodes for all nodes that will be added.

				Vector vResults = (Vector) result.get(parentNode.getAttributes()[0]);

				// Updates node result
				// parentNode.setQueryResult(result)
				// In associated form, update data.

				// Remove all children
				parentNode.removeAllChildren();
				for (int j = 0; j < vResults.size(); j++) {
					Hashtable parameters = new Hashtable();
					parameters.put(OTreeNode.TEXT, vResults.elementAt(j));
					parameters.put(OTreeNode.ATTR, parentNode.getAttr());
					parameters.put(OTreeNode.ENTITY, vResults.elementAt(j));
					parameters.put(OTreeNode.FORM, parentNode.getForm());
					parameters.put(OTreeNode.ORG, new Boolean(false));
					parameters.put(OTreeNode.SEPARATOR, parentNode.getSeparator());
					if (parentNode.getKeysString() != null) {
						parameters.put(OTreeNode.KEYS, parentNode.getKeysString());
					}
					if (parentNode.getStringAssociatedDataField() != null) {
						parameters.put(OTreeNode.PARENT_KEYS, parentNode.getStringAssociatedDataField());
					}
					Hashtable newNodeKeyValues = new Hashtable();
					Vector vKeys = parentNode.getKeys();
					for (int k = 0; k < vKeys.size(); k++) {
						Vector vKeyValues = (Vector) result.get(vKeys.get(k));
						if (vKeyValues != null) {
							newNodeKeyValues.put(vKeys.get(k), vKeyValues.get(j));
						}
					}
					parameters.put(OTreeNode.KEYS_VALUES, newNodeKeyValues);

					OTreeNode oDataNode = new OTreeNode(parameters);
					parentNode.add(oDataNode);
					// Adds all organizational nodes
					for (int k = 0; k < vOrganizationNodes.size(); k++) {
						oDataNode.add(((OTreeNode) vOrganizationNodes.elementAt(k)).cloneNodeAndChildren());
					}
				}
			} catch (Exception e) {
				if (com.ontimize.gui.ApplicationManager.DEBUG) {
					TreeInteractionManager.logger.debug("Exception in tree interaction manager: " + e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			if (com.ontimize.gui.ApplicationManager.DEBUG) {
				TreeInteractionManager.logger.debug("Exception in tree interaction manager: " + e.getMessage(), e);
			}
		}
	}

	public void removeNotOrganizationalNode(OTreeNode node) {
		// We have to move across the tree for deleting all data nodes.

		// If the node is organizational, it will contain data nodes.
		// If node is a data node, it will contain organizational nodes.

		// Child count must be greater than 1
		if (node.getChildCount() < 1) {
			return;
		}

		// Organizational node. We get the first child and create a vector with
		// all children of this node.
		Vector vChildNodes = new Vector();
		if (node.isOrganizational()) {
			OTreeNode childNode = (OTreeNode) node.getChildAt(0);
			// For organizational nodes we do nothing
			if (childNode.isOrganizational()) {
				return;
			}
			// We get the organizational nodes and add them to the vector
			for (int i = 0; i < childNode.getChildCount(); i++) {
				vChildNodes.add(childNode.getChildAt(i));
				// Recursive
				this.removeNotOrganizationalNode((OTreeNode) childNode.getChildAt(i));
			}
			// All children of organizational nodes are deleted.
			node.removeAllChildren();
			// Adds children.
			for (int i = 0; i < vChildNodes.size(); i++) {
				node.add((OTreeNode) vChildNodes.get(i));
			}
		}
		// If this node is not organizational
		else {
			// Call to this method for all organizational nodes
			for (int i = 0; i < node.getChildCount(); i++) {
				if (((OTreeNode) node.getChildAt(i)).isOrganizational()) {
					this.removeNotOrganizationalNode((OTreeNode) node.getChildAt(i));
				}
			}
		}
	}
}
