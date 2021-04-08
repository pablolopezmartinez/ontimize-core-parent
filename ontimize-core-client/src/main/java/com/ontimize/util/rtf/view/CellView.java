package com.ontimize.util.rtf.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.SizeRequirements;
import javax.swing.text.BoxView;
import javax.swing.text.Element;

import com.ontimize.util.rtf.BorderAttributes;
import com.ontimize.util.rtf.style.RTFDocument;
import com.ontimize.util.rtf.style.RTFDocument.CellElement;

public class CellView extends BoxView {

    public CellView(Element elem) {
        super(elem, 1);
        RTFDocument.CellElement cell = (RTFDocument.CellElement) super.getElement();
        Insets margins = cell.getMargins();

        super.setInsets((short) margins.top, (short) margins.left, (short) margins.bottom, (short) margins.right);
    }

    @Override
    public float getPreferredSpan(int axis) {
        float span = 0.0F;
        if (axis == 0) {
            RTFDocument.CellElement cell = (CellElement) super.getElement();
            span = cell.getWidth();
        } else {
            CellElement cell = (CellElement) super.getElement();
            span = Math.max(super.getPreferredSpan(axis), cell.getHeight());
        }
        return span;
    }

    @Override
    public float getMinimumSpan(int axis) {
        float span = 0.0F;
        if (axis == 0) {
            CellElement cell = (CellElement) super.getElement();
            span = cell.getWidth();
        } else {
            CellElement cell = (CellElement) super.getElement();
            span = Math.max(super.getMinimumSpan(axis), cell.getHeight());
        }
        return span;
    }

    @Override
    public float getMaximumSpan(int axis) {
        float span = 0.0F;
        if (axis == 0) {
            CellElement cell = (CellElement) super.getElement();
            span = cell.getWidth();
        } else {
            CellElement cell = (CellElement) super.getElement();
            span = Math.max(super.getMaximumSpan(axis), cell.getHeight());
        }
        return span;
    }

    @Override
    protected SizeRequirements baselineRequirements(int axis, SizeRequirements r) {
        SizeRequirements sr = super.baselineRequirements(axis, r);
        if (axis == 1) {
            sr.alignment = 0.0F;
        }
        return sr;
    }

    @Override
    protected SizeRequirements calculateMajorAxisRequirements(int axis, SizeRequirements r) {
        SizeRequirements sr = super.calculateMajorAxisRequirements(axis, r);
        if (axis == 1) {
            sr.alignment = 0.0F;
        }
        return sr;
    }

    @Override
    protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
        SizeRequirements sr = super.calculateMinorAxisRequirements(axis, r);
        if (axis == 1) {
            sr.alignment = 0.0F;
        }
        return sr;
    }

    @Override
    protected void layout(int width, int height) {
        CellElement cell = (CellElement) super.getElement();
        Insets margins = cell.getMargins();
        super.setInsets((short) margins.top, (short) margins.left, (short) margins.bottom, (short) margins.right);

        super.layout(width, height);
    }

    @Override
    public void paint(Graphics g, Shape a) {
        Rectangle alloc = a instanceof Rectangle ? (Rectangle) a : a.getBounds();
        super.paint(g, a);
        CellElement cell = (CellElement) super.getElement();
        BorderAttributes ba = (BorderAttributes) cell.getAttribute("BorderAttributes");

        Color oldColor = g.getColor();
        g.setColor(ba.lineColor);

        if (ba.borderLeft != 0) {
            g.drawLine(alloc.x, alloc.y, alloc.x, alloc.y + alloc.height);
        }

        if (ba.borderRight != 0) {
            g.drawLine(alloc.x + alloc.width, alloc.y, alloc.x + alloc.width, alloc.y + alloc.height);
        }

        if (ba.borderTop != 0) {
            g.drawLine(alloc.x, alloc.y, alloc.x + alloc.width, alloc.y);
        }

        if (ba.borderBottom != 0) {
            g.drawLine(alloc.x, alloc.y + alloc.height, alloc.x + alloc.width, alloc.y + alloc.height);
        }
        g.setColor(oldColor);
    }

}
