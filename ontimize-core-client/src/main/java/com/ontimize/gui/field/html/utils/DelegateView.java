package com.ontimize.gui.field.html.utils;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position.Bias;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * This class is essentially a wrapper for another view. The paint method is left abstract so that
 * custom drawing can be done.
 *
 * This is useful for extending the functionallity of non-public Swing views such as
 * javax.swing.text.html.TableView
 *
 * @author Imatia S.L.
 */
public abstract class DelegateView extends View {

    protected View delegate;

    public DelegateView(View delegate) {
        super(delegate.getElement());
        this.delegate = delegate;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#append(javax.swing.text.View)
     */
    @Override
    public void append(View v) {
        this.delegate.append(v);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#breakView(int, int, float, float)
     */
    @Override
    public View breakView(int axis, int offset, float pos, float len) {
        return this.delegate.breakView(axis, offset, pos, len);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#changedUpdate(javax.swing.event.DocumentEvent, java.awt.Shape,
     * javax.swing.text.ViewFactory)
     */
    @Override
    public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        this.delegate.changedUpdate(e, a, f);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#createFragment(int, int)
     */
    @Override
    public View createFragment(int p0, int p1) {
        return this.delegate.createFragment(p0, p1);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getAlignment(int)
     */
    @Override
    public float getAlignment(int axis) {
        return this.delegate.getAlignment(axis);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getAttributes()
     */
    @Override
    public AttributeSet getAttributes() {
        return this.delegate.getAttributes();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getBreakWeight(int, float, float)
     */
    @Override
    public int getBreakWeight(int axis, float pos, float len) {
        return this.delegate.getBreakWeight(axis, pos, len);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getChildAllocation(int, java.awt.Shape)
     */
    @Override
    public Shape getChildAllocation(int index, Shape a) {
        return this.delegate.getChildAllocation(index, a);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getContainer()
     */
    @Override
    public Container getContainer() {
        return this.delegate.getContainer();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getDocument()
     */
    @Override
    public Document getDocument() {
        return this.delegate.getDocument();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getElement()
     */
    @Override
    public Element getElement() {
        return this.delegate.getElement();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getEndOffset()
     */
    @Override
    public int getEndOffset() {
        return this.delegate.getEndOffset();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getGraphics()
     */
    @Override
    public Graphics getGraphics() {
        return this.delegate.getGraphics();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getMaximumSpan(int)
     */
    @Override
    public float getMaximumSpan(int axis) {
        return this.delegate.getMaximumSpan(axis);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getMinimumSpan(int)
     */
    @Override
    public float getMinimumSpan(int axis) {
        return this.delegate.getMinimumSpan(axis);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getNextVisualPositionFrom(int, javax.swing.text.Position.Bias,
     * java.awt.Shape, int, javax.swing.text.Position.Bias[])
     */
    @Override
    public int getNextVisualPositionFrom(int pos, Bias b, Shape a, int direction, Bias[] biasRet)
            throws BadLocationException {
        return this.delegate.getNextVisualPositionFrom(pos, b, a, direction, biasRet);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getParent()
     */
    @Override
    public View getParent() {
        return this.delegate.getParent();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getPreferredSpan(int)
     */
    @Override
    public float getPreferredSpan(int axis) {
        return this.delegate.getPreferredSpan(axis);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getResizeWeight(int)
     */
    @Override
    public int getResizeWeight(int axis) {
        return this.delegate.getResizeWeight(axis);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getStartOffset()
     */
    @Override
    public int getStartOffset() {
        return this.delegate.getStartOffset();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getToolTipText(float, float, java.awt.Shape)
     */
    @Override
    public String getToolTipText(float x, float y, Shape allocation) {
        return this.delegate.getToolTipText(x, y, allocation);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getView(int)
     */
    @Override
    public View getView(int n) {
        return this.delegate.getView(n);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getViewCount()
     */
    @Override
    public int getViewCount() {
        return this.delegate.getViewCount();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getViewFactory()
     */
    @Override
    public ViewFactory getViewFactory() {
        return this.delegate.getViewFactory();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getViewIndex(float, float, java.awt.Shape)
     */
    @Override
    public int getViewIndex(float x, float y, Shape allocation) {
        return this.delegate.getViewIndex(x, y, allocation);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#getViewIndex(int, javax.swing.text.Position.Bias)
     */
    @Override
    public int getViewIndex(int pos, Bias b) {
        return this.delegate.getViewIndex(pos, b);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.delegate.hashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#insert(int, javax.swing.text.View)
     */
    @Override
    public void insert(int offs, View v) {
        this.delegate.insert(offs, v);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#insertUpdate(javax.swing.event.DocumentEvent, java.awt.Shape,
     * javax.swing.text.ViewFactory)
     */
    @Override
    public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        this.delegate.insertUpdate(e, a, f);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#isVisible()
     */
    @Override
    public boolean isVisible() {
        return this.delegate.isVisible();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#modelToView(int, javax.swing.text.Position.Bias, int,
     * javax.swing.text.Position.Bias, java.awt.Shape)
     */
    @Override
    public Shape modelToView(int p0, Bias b0, int p1, Bias b1, Shape a) throws BadLocationException {
        return this.delegate.modelToView(p0, b0, p1, b1, a);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#modelToView(int, java.awt.Shape, javax.swing.text.Position.Bias)
     */
    @Override
    public Shape modelToView(int pos, Shape a, Bias b) throws BadLocationException {
        return this.delegate.modelToView(pos, a, b);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#modelToView(int, java.awt.Shape)
     */
    @Override
    public Shape modelToView(int pos, Shape a) throws BadLocationException {
        return this.delegate.modelToView(pos, a);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#preferenceChanged(javax.swing.text.View, boolean, boolean)
     */
    @Override
    public void preferenceChanged(View child, boolean width, boolean height) {
        this.delegate.preferenceChanged(child, width, height);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#remove(int)
     */
    @Override
    public void remove(int i) {
        this.delegate.remove(i);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#removeAll()
     */
    @Override
    public void removeAll() {
        this.delegate.removeAll();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#removeUpdate(javax.swing.event.DocumentEvent, java.awt.Shape,
     * javax.swing.text.ViewFactory)
     */
    @Override
    public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        this.delegate.removeUpdate(e, a, f);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#replace(int, int, javax.swing.text.View[])
     */
    @Override
    public void replace(int offset, int length, View[] views) {
        this.delegate.replace(offset, length, views);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#setParent(javax.swing.text.View)
     */
    @Override
    public void setParent(View parent) {
        this.delegate.setParent(parent);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#setSize(float, float)
     */
    @Override
    public void setSize(float width, float height) {
        this.delegate.setSize(width, height);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.delegate.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#viewToModel(float, float, java.awt.Shape,
     * javax.swing.text.Position.Bias[])
     */
    @Override
    public int viewToModel(float x, float y, Shape a, Bias[] biasReturn) {
        return this.delegate.viewToModel(x, y, a, biasReturn);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.View#viewToModel(float, float, java.awt.Shape)
     */
    @Override
    public int viewToModel(float x, float y, Shape a) {
        return this.delegate.viewToModel(x, y, a);
    }

}
