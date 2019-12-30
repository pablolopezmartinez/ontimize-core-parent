package com.ontimize.gui.field.html.actions;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionUIFactory;

import com.ontimize.gui.field.html.utils.CompoundUndoManager;

/**
 * @author Imatia S.L.
 *
 */
public class HTMLEditorActionFactory {

	public static ActionList createEditActionList() {
		ActionList list = new ActionList("edit");
		list.add(CompoundUndoManager.UNDO);
		list.add(CompoundUndoManager.REDO);
		list.add(null);
		list.add(new CutAction());
		list.add(new CopyAction());
		list.add(new PasteAction());
		list.add(null);
		list.add(new SelectAllAction());
		return list;
	}

	public static ActionList createInlineActionList() {
		ActionList list = new ActionList("style");
		list.add(new HTMLInlineAction(HTMLInlineAction.BOLD));
		list.add(new HTMLInlineAction(HTMLInlineAction.ITALIC));
		list.add(new HTMLInlineAction(HTMLInlineAction.UNDERLINE));
		list.add(null);
		list.add(new HTMLInlineAction(HTMLInlineAction.CITE));
		list.add(new HTMLInlineAction(HTMLInlineAction.CODE));
		list.add(new HTMLInlineAction(HTMLInlineAction.EM));
		list.add(new HTMLInlineAction(HTMLInlineAction.STRONG));
		list.add(new HTMLInlineAction(HTMLInlineAction.SUB));
		list.add(new HTMLInlineAction(HTMLInlineAction.SUP));
		list.add(new HTMLInlineAction(HTMLInlineAction.STRIKE));
		list.add(null);
		list.add(new ClearStylesAction());

		return list;
	}

	public static ActionList createAlignActionList() {
		ActionList list = new ActionList("align");
		String[] t = HTMLAlignAction.getAlignments();
		for (int i = 0; i < t.length; i++) {
			list.add(new HTMLAlignAction(i));
		}

		return list;
	}

	public static ActionList createBlockElementActionList() {
		ActionList list = new ActionList("paragraph");
		list.add(new HTMLBlockAction(HTMLBlockAction.DIV));
		list.add(new HTMLBlockAction(HTMLBlockAction.P));
		list.add(null);
		list.add(new HTMLBlockAction(HTMLBlockAction.BLOCKQUOTE));
		list.add(new HTMLBlockAction(HTMLBlockAction.PRE));
		list.add(null);
		list.add(new HTMLBlockAction(HTMLBlockAction.H1));
		list.add(new HTMLBlockAction(HTMLBlockAction.H2));
		list.add(new HTMLBlockAction(HTMLBlockAction.H3));
		list.add(new HTMLBlockAction(HTMLBlockAction.H4));
		list.add(new HTMLBlockAction(HTMLBlockAction.H5));
		list.add(new HTMLBlockAction(HTMLBlockAction.H6));

		return list;
	}

	public static ActionList createListElementActionList() {
		ActionList list = new ActionList("list");
		list.add(new HTMLBlockAction(HTMLBlockAction.UL));
		list.add(new HTMLBlockAction(HTMLBlockAction.OL));

		return list;
	}

	public static ActionList createInsertTableElementActionList() {
		ActionList list = new ActionList("Insert into table");
		list.add(new TableEditAction(TableEditAction.INSERT_CELL));
		list.add(new TableEditAction(TableEditAction.INSERT_ROW));
		list.add(new TableEditAction(TableEditAction.INSERT_COL));
		return list;
	}

	public static ActionList createDeleteTableElementActionList() {
		ActionList list = new ActionList("Insert into table");
		list.add(new TableEditAction(TableEditAction.DELETE_CELL));
		list.add(new TableEditAction(TableEditAction.DELETE_ROW));
		list.add(new TableEditAction(TableEditAction.DELETE_COL));
		return list;
	}

	public static JMenu createMenu(ActionList lst, String menuName) {
		JMenu m = ActionUIFactory.getInstance().createMenu(lst);
		m.setText(menuName);
		return m;
	}

	public static JMenuItem createMenuItem(Action act) {
		JMenuItem m = ActionUIFactory.getInstance().createMenuItem(act);
		return m;
	}

	public static AbstractButton createButton(Action act) {
		AbstractButton b = ActionUIFactory.getInstance().createButton(act);
		return b;
	}
}
