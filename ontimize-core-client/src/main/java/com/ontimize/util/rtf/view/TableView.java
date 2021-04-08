package com.ontimize.util.rtf.view;

import javax.swing.SizeRequirements;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;

import com.ontimize.util.rtf.style.RTFDocument.TableElement;

public class TableView extends BoxView {

    public TableView(Element elem, int axis) {
        super(elem, axis);
    }

    public TableView(Element elem) {
        super(elem, 1);
    }

    @Override
    protected SizeRequirements baselineRequirements(int axis, SizeRequirements r) {
        SizeRequirements sr = super.baselineRequirements(axis, r);
        Element table = this.getElement();
        if (axis == 0) {
            int align = StyleConstants.getAlignment(table.getAttributes());
            switch (align) {
                case 0:
                    sr.alignment = 0.0F;
                    break;
                case 2:
                    sr.alignment = 1.0F;
                    break;
                default:
                    sr.alignment = 0.5F;
            }
        }
        return sr;
    }

    @Override
    protected SizeRequirements calculateMajorAxisRequirements(int axis, SizeRequirements r) {
        SizeRequirements sr = super.calculateMajorAxisRequirements(axis, r);
        Element table = this.getElement();
        if (axis == 0) {
            int align = StyleConstants.getAlignment(table.getAttributes());
            switch (align) {
                case 0:
                    sr.alignment = 0.0F;
                    break;
                case 2:
                    sr.alignment = 1.0F;
                    break;
                default:
                    sr.alignment = 0.5F;
            }
        }
        return sr;
    }

    @Override
    protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
        SizeRequirements sr = super.calculateMinorAxisRequirements(axis, r);
        Element table = this.getElement();
        if (axis == 0) {
            int align = StyleConstants.getAlignment(table.getAttributes());
            switch (align) {
                case 0:
                    sr.alignment = 0.0F;
                    break;
                case 2:
                    sr.alignment = 1.0F;
                    break;
                default:
                    sr.alignment = 0.5F;
            }
        }
        return sr;
    }

    @Override
    public float getPreferredSpan(int axis) {
        float span = 0.0F;
        span = super.getPreferredSpan(axis);
        TableElement table = (TableElement) super.getElement();

        if (axis == 0) {
            span = table.getWidth();
        }
        return span;
    }

    @Override
    public float getMinimumSpan(int axis) {
        float span = 0.0F;
        span = super.getMinimumSpan(axis);

        TableElement table = (TableElement) super.getElement();

        if (axis == 0) {
            span = table.getWidth();
        }
        return span;
    }

    @Override
    public float getMaximumSpan(int axis) {
        float span = 0.0F;
        span = super.getMaximumSpan(axis);

        TableElement table = (TableElement) super.getElement();

        if (axis == 0) {
            span = table.getWidth();
        }
        return span;
    }

    @Override
    public float getAlignment(int axis) {
        if (axis == 0) {
            int align = StyleConstants.getAlignment(super.getElement().getAttributes());
            float a;
            switch (align) {
                case 0:
                    a = 0.0F;
                    break;
                case 2:
                    a = 1.0F;
                    break;
                default:
                    a = 0.5F;
            }
            return a;
        }

        return super.getAlignment(axis);
    }

}
