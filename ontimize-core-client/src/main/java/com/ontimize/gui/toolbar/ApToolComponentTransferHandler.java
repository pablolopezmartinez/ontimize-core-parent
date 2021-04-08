package com.ontimize.gui.toolbar;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.InputEvent;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationToolBar;
import com.ontimize.gui.table.ButtonTransferHandler;

public class ApToolComponentTransferHandler extends TransferHandler {

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
    protected void exportDone(JComponent source, Transferable data, int action) {
    }

    // Import Methods
    @Override
    public boolean canImport(TransferSupport support) {
        Component component = support.getComponent();
        if (component instanceof ApplicationToolBar) {
            ApplicationToolBar toolBar = (ApplicationToolBar) component;
            toolBar.setDropLocation(support);
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

            if (targetComponent instanceof ApplicationToolBar) {
                ApplicationToolBar applicationToolBar = (ApplicationToolBar) targetComponent;
                int locationIndex = this.getLocationIndex(support);
                applicationToolBar.add((JComponent) sourceComponent, locationIndex);
                applicationToolBar.doLayout();
            }
            return true;
        } catch (Exception e) {
            ApToolComponentTransferHandler.logger.error(null, e);
        }
        return super.importData(support);
    }

    protected int getLocationIndex(TransferSupport support) {
        ApplicationToolBar applicationToolBar = (ApplicationToolBar) support.getComponent();
        int members = applicationToolBar.getComponentCount();
        Point dropPoint = support.getDropLocation().getDropPoint();
        int locationIndex = 0;
        for (int i = 0; i < members; i++) {
            Rectangle bound = applicationToolBar.getComponent(i).getBounds();
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

    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        super.exportAsDrag(comp, e, action);
    }

}
