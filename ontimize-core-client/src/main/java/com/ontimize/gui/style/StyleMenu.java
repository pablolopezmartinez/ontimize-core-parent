package com.ontimize.gui.style;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;

import com.ontimize.gui.Application;
import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.Menu;
import com.ontimize.gui.MessageDialog;
import com.ontimize.gui.PlafPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StyleMenu extends Menu {

    private static final Logger logger = LoggerFactory.getLogger(StyleMenu.class);

    protected ButtonGroup buttonGroup = new ButtonGroup();

    ItemListener listener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent ev) {
            // Event source
            Object oSource = ev.getSource();
            if (oSource instanceof StyleMenuItem) {
                StyleMenuItem styleMenuItem = (StyleMenuItem) oSource;
                Application application = ApplicationManager.getApplication();

                if (styleMenuItem.isSelected()){
                    boolean bApply = MessageDialog.showQuestionMessage(application.getFrame(), "apply_style_configuration",
                        application.getResourceBundle());
                    if (bApply){
                        String style = styleMenuItem.getStyle();
                        PlafPreferences instance = PlafPreferences.getInstance();
                        if (instance!=null){
                            instance.setStylePreference(style);
                        }
                    }
                }
            }
        }
    };

    public StyleMenu(Hashtable parameters) {
        super(parameters);
    }

    @Override
    public JMenuItem add(JMenuItem menuItem) {

        if (menuItem instanceof StyleMenuItem) {
            StyleMenuItem styleMenuItem = (StyleMenuItem)menuItem;
            this.buttonGroup.add(menuItem);
            String selectedStyle = System.getProperty("com.ontimize.gui.lafstyle");
            if (selectedStyle!=null){
                if (selectedStyle.equals(styleMenuItem.getStyle())){
                    styleMenuItem.setSelected(true);
                }
            }
            styleMenuItem.addItemListener(this.listener);
        }
        return super.add(menuItem);
    }
}
