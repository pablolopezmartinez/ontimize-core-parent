package com.ontimize.gui.field;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomEditorPane extends JEditorPane {

    private static final Logger logger = LoggerFactory.getLogger(CustomEditorPane.class);

    protected Paint paint;

    public CustomEditorPane() {
        super("text/html", "");
        this.setEditorKit(new HTMLEditorKit() {

            @Override
            public ViewFactory getViewFactory() {
                return new HTMLFactoryX();
            }

            class HTMLFactoryX extends HTMLFactory implements ViewFactory {

                @Override
                public View create(Element elem) {
                    Object o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);
                    if (o instanceof HTML.Tag) {
                        HTML.Tag kind = (HTML.Tag) o;
                        if (kind == HTML.Tag.IMG) {
                            return new ImageView(elem) {

                                @Override
                                public URL getImageURL() {
                                    String s = (String) this.getElement()
                                        .getAttributes()
                                        .getAttribute(HTML.Attribute.SRC);
                                    URL url = this.getClass().getClassLoader().getResource(s);
                                    if (url == null) {
                                        url = super.getImageURL();
                                    }
                                    if (url == null) {
                                        try {
                                            url = new File(s).toURI().toURL();
                                        } catch (MalformedURLException e) {
                                            CustomEditorPane.logger.error(null, e);
                                        }
                                    }
                                    return url;
                                }
                            };
                        }
                    }
                    return super.create(elem);
                }

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.paint != null) {
            ((Graphics2D) g).setPaint(this.paint);
            g.fillRect(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);
            this.setOpaque(false);
            super.paintComponent(g);
            this.setOpaque(true);
        } else {
            super.paintComponent(g);
        }
    }

    @Override
    public void setText(String t) {
        super.setText(t);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(20, 20);
    }

    @Override
    public void layout() {
        try {
            // This method throws some exceptions, we don't want to see this
            // exceptions
            super.layout();
        } catch (Exception e) {
            CustomEditorPane.logger.trace(null, e);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

}
