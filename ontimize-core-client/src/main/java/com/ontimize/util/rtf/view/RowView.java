package com.ontimize.util.rtf.view;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.View;

import com.ontimize.util.rtf.style.RTFDocument.RowElement;

public class RowView extends BoxView {

    public RowView(Element elem, int axis) {
        super(elem, axis);
    }

    public RowView(Element elem) {
        super(elem, 0);
    }

    @Override
    public void paint(Graphics g, Shape a) {
        Rectangle alloc = a instanceof Rectangle ? (Rectangle) a : a.getBounds();
        int n = super.getViewCount();

        RowElement row = (RowElement) super.getElement();

        int cellWidth = row.getWidth() / row.getChildCount();
        int shift = 0;
        for (int i = 0; i < n; ++i) {
            Rectangle tempRect = new Rectangle(alloc.x + shift, alloc.y, row.getCellWidth(i), alloc.height);
            this.paintChild(g, tempRect, i);
            shift += row.getCellWidth(i);
        }
    }

    @Override
    public float getPreferredSpan(int axis) {
        float span = 0.0F;
        if (axis == 0) {
            RowElement row = (RowElement) super.getElement();
            span = row.getWidth();
        } else {
            span = 1.0F;
            for (int i = 0; i < super.getViewCount(); ++i) {
                span = Math.max(span, super.getView(i).getPreferredSpan(axis));
            }
        }
        return span;
    }

    @Override
    public float getMinimumSpan(int axis) {
        float span = 0.0F;
        if (axis == 0) {
            RowElement row = (RowElement) super.getElement();
            span = row.getWidth();
        } else {
            span = 1.0F;
            for (int i = 0; i < super.getViewCount(); ++i) {
                span = Math.max(span, super.getView(i).getMinimumSpan(axis));
            }
        }
        return span;
    }

    @Override
    public float getMaximumSpan(int axis) {
        float span = 0.0F;
        if (axis == 0) {
            RowElement row = (RowElement) super.getElement();
            span = row.getWidth();
        } else {
            span = 1.0F;
            for (int i = 0; i < super.getViewCount(); ++i) {
                span = Math.max(span, super.getView(i).getMaximumSpan(axis));
            }
        }
        return span;
    }

    @Override
    protected void paintChild(Graphics g, Rectangle alloc, int index) {
        View child = super.getView(index);
        child.paint(g, alloc);
    }

}
