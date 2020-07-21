package com.ontimize.util.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DefaultBasicHTMLViewer extends BasicHTMLViewer {

    protected JTextArea text = new JTextArea();

    protected JScrollPane js;

    public DefaultBasicHTMLViewer() {
        super();
        this.text.setEnabled(false);
        this.js = new JScrollPane(this.text);

        this.js.getViewport().setPreferredSize(new Dimension(200, 400));

        this.setLayout(new GridBagLayout());
        this.add(this.js, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 2, 2));
    }

    @Override
    public void setHTML(String html) {
        this.text.setText(html);
    }

}
