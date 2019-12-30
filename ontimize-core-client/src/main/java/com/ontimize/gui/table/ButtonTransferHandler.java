package com.ontimize.gui.table;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.util.swing.CollapsiblePopupPanel;

public class ButtonTransferHandler extends TransferHandler {

	private static final Logger logger = LoggerFactory.getLogger(ButtonTransferHandler.class);

	// Export Methods
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.MOVE;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		if (c instanceof Transferable) {
			return (Transferable) c;
		}
		return super.createTransferable(c);
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {}

	// Import Methods
	@Override
	public boolean canImport(TransferSupport support) {
		Component component = support.getComponent();
		if (component instanceof GroupTableButton) {
			Component c = ((GroupTableButton) component).getParent();
			if (c instanceof ControlPanel) {
				((ControlPanel) c).removeLocationComponent();
			}
			return true;
		} else if (component instanceof ControlPanel) {
			((ControlPanel) component).setDropLocation(support);
			return true;
		}
		return false;
	}

	protected boolean isValidTarget(Component targetComponent, Component sourceComponent) {
		if (targetComponent instanceof GroupTableButton) {
			targetComponent = targetComponent.getParent();
		}

		Component parent = SwingUtilities.getAncestorOfClass(CollapsiblePopupPanel.class, sourceComponent);
		if (parent instanceof CollapsiblePopupPanel) {
			sourceComponent = ((CollapsiblePopupPanel) parent).getInvoker();
		}

		ControlPanel source = (ControlPanel) SwingUtilities.getAncestorOfClass(ControlPanel.class, sourceComponent);

		if ((source != null) && (targetComponent != null) && source.equals(targetComponent)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean importData(TransferSupport support) {
		Component targetComponent = support.getComponent();
		Transferable t = support.getTransferable();
		try {
			Object sourceComponent = t.getTransferData(t.getTransferDataFlavors()[0]);

			if (!this.isValidTarget(targetComponent, (Component) sourceComponent)) {
				return false;
			}

			if ((targetComponent instanceof GroupTableButton) && (sourceComponent instanceof Component)) {
				((GroupTableButton) targetComponent).add((JComponent) sourceComponent, true);
				Component c = ((GroupTableButton) targetComponent).getParent();
				if (c instanceof ControlPanel) {
					((ControlPanel) c).doLayout();
				}
			} else if (targetComponent instanceof ControlPanel) {
				ControlPanel panel = (ControlPanel) targetComponent;
				int locationIndex = this.getLocationIndex(support);
				JComponent source = (JComponent) sourceComponent;
				if (!(source.getParent() instanceof ControlPanel)) {
					CollapsiblePopupPanel collapsible = (CollapsiblePopupPanel) SwingUtilities.getAncestorOfClass(CollapsiblePopupPanel.class, source);
					collapsible.setVisible(false);
				}
				panel.add((JComponent) sourceComponent, locationIndex);
				panel.doLayout();
			}
			((JComponent) sourceComponent).requestFocus();
			return true;
		} catch (Exception e) {
			ButtonTransferHandler.logger.error(null, e);
		}
		return super.importData(support);
	}

	protected int getLocationIndex(TransferSupport support) {
		ControlPanel panel = (ControlPanel) support.getComponent();
		int members = panel.getComponentCount();
		Point dropPoint = support.getDropLocation().getDropPoint();
		int locationIndex = 0;
		for (int i = 0; i < members; i++) {
			Rectangle bound = panel.getComponent(i).getBounds();
			if (dropPoint.x < (bound.x + (bound.width / 2))) {
				return locationIndex;
			}
			locationIndex++;
		}
		return -1;
	}

	protected boolean isDropSupported(DropTargetDropEvent dtde) {
		if (dtde.getSource() instanceof DropTarget) {
			DropTarget target = (DropTarget) dtde.getSource();
			Transferable transferable = dtde.getTransferable();
			Object sourceComponent;
			// try {
			// sourceComponent =
			// transferable.getTransferData(transferable.getTransferDataFlavors()[0]);
			// for(int i=0;i<getComponentCount();i++){
			// if (getComponent(i).equals(sourceComponent)){
			// return false;
			// }
			// }
			// } catch (Exception e) {
			// logger.error(null,e);
			// }

		}
		return true;
	}

	@Override
	public Icon getVisualRepresentation(Transferable t) {
		return super.getVisualRepresentation(t);
	}

}
