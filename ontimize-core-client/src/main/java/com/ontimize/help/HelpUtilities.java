package com.ontimize.help;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;

import javax.help.HelpSet;
import javax.help.JHelpContentViewer;
import javax.help.plaf.basic.BasicContentViewerUI;
import javax.help.plaf.basic.BasicHelpUI;
import javax.help.plaf.basic.BasicTOCNavigatorUI;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.ApplicationManager;
import com.ontimize.gui.MessageDialog;
import com.ontimize.gui.images.ImageManager;

public class HelpUtilities {

    private static final Logger logger = LoggerFactory.getLogger(HelpUtilities.class);

    /**
     * Complete path to .hs file. By default, "com/ontimize/help/OntimizeHelp.hs".
     */
    public static String default_Ontimize_help_hs = "com/ontimize/help/OntimizeHelp.hs";

    public static boolean DEBUG = false;

    /**
     * It indicates whether default ontimize help files are included in help dialog. By default, it is
     * true.
     */
    public static boolean show_Ontimize_Help = true;

    private static boolean HELP_ENABLED = true;

    public static String HELP_FILE_ERROR_MESSAGE = "helputilities.help_file_has_not_been_found";

    public static String ADVANCED_SEARCH_HELP = "application.advanced_search_help";

    public static String ADVANCED_SEARCH_HELP_FILE = "com/ontimize/help/advancedsearch.htm";

    protected static JEditorPane editorPane = null;

    protected static JDialog dialog = null;

    protected static JScrollPane scroll = null;

    protected static Object hs = null;

    protected static Object hsAp = null;

    protected static Object hb = null;

    protected static Object hbAp = null;

    private static JWindow loadingWindow = null;

    private static String mainHelpSet = null;

    protected static Locale lastLocale;

    static {
        HelpUtilities.recheck();
    }

    public static void recheck() {
        try {
            Class.forName("javax.help.CSH");
            UIManager.getDefaults().put("HelpUI", "com.ontimize.help.HelpUtilities$ModifiedHelpUI");
            UIManager.getDefaults()
                .put("HelpContentViewerUI", "com.ontimize.help.HelpUtilities$ModifiedHelpContentViewerUI");
            UIManager.getDefaults().put("HelpOnItemCursor", ApplicationManager.getHelpOnItemCursor());
            UIManager.getDefaults().put("HelpTOCNavigatorUI", "com.ontimize.help.HelpUtilities$ModifiedTOCNavigatorUI");

            HelpUtilities.HELP_ENABLED = true;
        } catch (Exception e) {
            HelpUtilities.logger
                .debug("HelpUtilities: No Java Help classes found (javax.help.CSH). Client help disabled.", e);
            HelpUtilities.HELP_ENABLED = false;
        }
    }

    public static class ModifiedTOCNavigatorUI extends BasicTOCNavigatorUI {

        public ModifiedTOCNavigatorUI(Object b) {
            super((javax.help.JHelpTOCNavigator) b);
            HelpUtilities.logger.debug("new ModifiedTOCNavigatorUI");
        }

        public static ComponentUI createUI(JComponent jcomponent) {
            return new ModifiedTOCNavigatorUI(jcomponent);
        }

        @Override
        public void merge(javax.help.NavigatorView view) {

            javax.help.TOCView tocView = (javax.help.TOCView) view; // should
                                                                    // succeed.

            // parse TOC data
            DefaultMutableTreeNode node = tocView.getDataAsTree();

            // This is a tricky one. As you remove the entries from one node to
            // another the list shrinks. So you can't use an Enumated list to do
            // the move.
            while (node.getChildCount() > 0) {
                this.topNode.add((DefaultMutableTreeNode) node.getFirstChild());
            }

            // reload the tree data
            ((DefaultTreeModel) this.tree.getModel()).reload();
            this.setCellRenderer(this.toc.getNavigatorView(), this.tree);
        }

    }

    public static class ModifiedHelpContentViewerUI extends BasicContentViewerUI {

        public ModifiedHelpContentViewerUI(JHelpContentViewer jhc) {
            super(jhc);
            HelpUtilities.logger.debug("new ModifiedHelpContentViewerUI");
        }

        public static ComponentUI createUI(JComponent jcomponent) {
            return new ModifiedHelpContentViewerUI((javax.help.JHelpContentViewer) jcomponent);
        }

        @Override
        protected void linkActivated(URL u) {
            if (HelpUtilities.DEBUG) {
                HelpUtilities.logger.debug("URL Enabled: " + u);
            }
            super.linkActivated(u);
        }

    }

    public static class ModifiedHelpUI extends BasicHelpUI {

        public ModifiedHelpUI(Object jh) {
            super((javax.help.JHelp) jh);
        }

        public static ComponentUI createUI(JComponent jcomponent) {
            return new ModifiedHelpUI(jcomponent);
        }

        @Override
        public void installUI(JComponent jcomponent) {
            super.installUI(jcomponent);
            this.splitPane.setDividerSize(10);
            JToolBar b = this.toolbar;
            Component c = b.getComponentAtIndex(0);
            if ((c != null) && (c instanceof JButton)) {
                ImageIcon im = null;
                try {
                    im = ImageManager.getIcon(ImageManager.PREV_HELP_UI);
                } catch (Exception e) {
                    if (HelpUtilities.DEBUG) {
                        HelpUtilities.logger.error(null, e);
                    } else {
                        HelpUtilities.logger.trace(null, e);
                    }
                }
                if (im != null) {
                    ((JButton) c).setIcon(im);
                }
                ((JButton) c).setMargin(new Insets(2, 2, 2, 2));
            }
            c = b.getComponentAtIndex(1);
            if ((c != null) && (c instanceof JButton)) {
                ImageIcon im = null;
                try {
                    im = ImageManager.getIcon(ImageManager.NEXT_HELP_UI);
                } catch (Exception e) {
                    if (HelpUtilities.DEBUG) {
                        HelpUtilities.logger.error(null, e);
                    } else {
                        HelpUtilities.logger.trace(null, e);
                    }
                }
                if (im != null) {
                    ((JButton) c).setIcon(im);
                }
                ((JButton) c).setMargin(new Insets(2, 2, 2, 2));
            }
            c = b.getComponentAtIndex(3);
            if ((c != null) && (c instanceof JButton)) {
                ImageIcon im = null;
                try {
                    im = ImageManager.getIcon(ImageManager.PRINT_HELP_UI);
                } catch (Exception e) {
                    if (HelpUtilities.DEBUG) {
                        HelpUtilities.logger.error(null, e);
                    } else {
                        HelpUtilities.logger.trace(null, e);
                    }
                }
                if (im != null) {
                    ((JButton) c).setIcon(im);
                }
                ((JButton) c).setMargin(new Insets(2, 2, 2, 2));
            }
            c = b.getComponentAtIndex(4);
            if ((c != null) && (c instanceof JButton)) {
                ImageIcon im = null;
                try {
                    im = ImageManager.getIcon(ImageManager.PAGE_SETUP_HELP_UI);
                } catch (Exception e) {
                    if (HelpUtilities.DEBUG) {
                        HelpUtilities.logger.error(null, e);
                    } else {
                        HelpUtilities.logger.trace(null, e);
                    }
                }
                if (im != null) {
                    ((JButton) c).setIcon(im);
                }
                ((JButton) c).setMargin(new Insets(2, 2, 2, 2));
            }
        }

    }

    public static class ExtDefaultHelpBroker extends javax.help.DefaultHelpBroker {

        protected Window ownerWindow2 = null;

        public ExtDefaultHelpBroker(Object hs) {
            super((javax.help.HelpSet) hs);
        }

        // public void setActivationWindow(Window w) {
        //
        // if (w != null && w instanceof Dialog) {
        // Dialog tmpDialog = (Dialog) w;
        // if (tmpDialog.isModal()) {
        // mw = w;
        // mw.
        // } else {
        // // Comprobamos si el cuadro de dialogo tiene un owner modal
        // if (tmpDialog.getOwner() != null && tmpDialog.getOwner() instanceof
        // Dialog) {
        // tmpDialog = (Dialog) tmpDialog.getOwner();
        // if (tmpDialog.isModal()) {
        // ownerWindow = w;
        // modallyActivated = true;
        // } else {
        // ownerWindow = null;
        // modallyActivated = false;
        // }
        // } else {
        // ownerWindow = null;
        // modallyActivated = false;
        // }
        // }
        // } else {
        // ownerWindow = null;
        // modallyActivated = false;
        // }
        //
        // this.ownerWindow2 = w;
        // if (ownerWindow2 != null && ownerWindow2 instanceof JFrame) {
        // this.frame.setIconImage(((JFrame) ownerWindow2).getIconImage());
        // }
        // }
        //
        // public void setDisplayed(boolean d) {
        // if (this.frame != null && ownerWindow2 != null && ownerWindow2
        // instanceof JFrame) {
        // this.frame.setIconImage(((JFrame) ownerWindow2).getIconImage());
        // }
        // super.setDisplayed(d);
        // }

    }

    private HelpUtilities() {
    }

    private static void createLoadingWindow(Window parent) {
        if ((HelpUtilities.loadingWindow == null) || (HelpUtilities.loadingWindow.getOwner() != parent)) {
            if (parent == null) {
                parent = new JWindow();
            }
            HelpUtilities.loadingWindow = new JWindow(parent);

            JLabel label = new JLabel("Loading Help ...");
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            label.setIcon(ImageManager.getIcon(ImageManager.LOADING_HELP));
            HelpUtilities.loadingWindow.getContentPane().add(label);

            ((JComponent) HelpUtilities.loadingWindow.getContentPane())
                .setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED), new EmptyBorder(10, 10, 10, 10)));
            HelpUtilities.loadingWindow.pack();
            ApplicationManager.center(HelpUtilities.loadingWindow);
        }
    }

    public synchronized static void showDefaultHelp(final Window parent) {
        HelpUtilities.createLoadingWindow(parent);
        HelpUtilities.loadingWindow.setVisible(true);
        Thread t = new Thread() {

            @Override
            public void run() {
                HelpUtilities.showDefaultHelp_(parent);
            }
        };

        t.start();
    }

    protected static void showDefaultHelp_(Window parent) {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            if (HelpUtilities.DEBUG) {
                HelpUtilities.logger.error(null, e);
            } else {
                HelpUtilities.logger.trace(null, e);
            }
        }
        try {
            Thread.yield();
            URL hsURL = javax.help.HelpSet.findHelpSet(HelpUtilities.class.getClassLoader(),
                    HelpUtilities.default_Ontimize_help_hs, ApplicationManager.getLocale());
            Thread.yield();
            if (hsURL == null) {
                if (parent instanceof Frame) {
                    MessageDialog.showMessage((Frame) parent, HelpUtilities.HELP_FILE_ERROR_MESSAGE,
                            JOptionPane.ERROR_MESSAGE, null);
                } else {
                    MessageDialog.showMessage((Dialog) parent, HelpUtilities.HELP_FILE_ERROR_MESSAGE,
                            JOptionPane.ERROR_MESSAGE, null);
                }
                HelpUtilities.loadingWindow.setVisible(false);
                return;
            }
            Thread.yield();
            HelpUtilities.hs = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(), hsURL);
            if (HelpUtilities.DEBUG) {
                Enumeration ids = ((javax.help.HelpSet) HelpUtilities.hs).getCombinedMap().getAllIDs();
                while (ids.hasMoreElements()) {
                    javax.help.Map.ID id = (javax.help.Map.ID) ids.nextElement();
                    HelpUtilities.logger.debug("ID: " + id.toString());
                }
            }
            Thread.yield();
        } catch (Exception e) {
            HelpUtilities.logger.error("HelpSet help/guihelp.hs not found", e);
            return;
        }
        HelpUtilities.hb = new ExtDefaultHelpBroker(HelpUtilities.hs);
        Thread.yield();
        Object source = null;
        if ((parent != null) && (parent.getComponents() != null)) {
            source = parent.getComponents()[0];

        } else {
            source = new JLabel();
        }
        Thread.yield();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        ((javax.help.HelpBroker) HelpUtilities.hb).setSize(new Dimension(d.width - 50, d.height - 50));
        new javax.help.CSH.DisplayHelpFromSource((javax.help.HelpBroker) HelpUtilities.hb)
            .actionPerformed(new ActionEvent(source, 1001, null));
        if (HelpUtilities.loadingWindow != null) {
            HelpUtilities.loadingWindow.setVisible(false);
        }
    }

    public static synchronized void showDefaultHelp(final Window parent, final String target) {
        if (!HelpUtilities.HELP_ENABLED) {
            return;
        }
        Thread t = new Thread() {

            @Override
            public void run() {
                HelpUtilities.showDefaultHelp_(parent);
                if (target != null) {
                    try {

                        ((javax.help.DefaultHelpBroker) HelpUtilities.hb)
                            .setCurrentID(javax.help.Map.ID.create(target,
                                    ((javax.help.DefaultHelpBroker) HelpUtilities.hb).getHelpSet()));
                    } catch (Exception ee) {
                        HelpUtilities.logger.trace(null, ee);
                    }
                }
            }
        };
        t.start();
    }

    /**
     * Shows a modal dialog with the specified help file (html)
     * @param parent
     * @param urlPage
     * @param title
     * @throws Exception
     */
    public static void showHelpDialog(Window parent, URL urlPage, String title) throws Exception {

        if (HelpUtilities.editorPane == null) {
            HelpUtilities.editorPane = new JEditorPane();
            HelpUtilities.scroll = new JScrollPane(HelpUtilities.editorPane);
        }

        if (HelpUtilities.dialog != null) {
            if (parent == HelpUtilities.dialog.getOwner()) {
                HelpUtilities.editorPane.setPage(urlPage);
                HelpUtilities.dialog.setSize(500, 400);
                HelpUtilities.dialog.setVisible(true);
            } else {
                HelpUtilities.dialog.getContentPane().remove(HelpUtilities.scroll);
                HelpUtilities.dialog.dispose();
                if (parent instanceof Frame) {
                    HelpUtilities.dialog = new JDialog((Frame) parent, title, true);
                } else {
                    HelpUtilities.dialog = new JDialog((Dialog) parent, title, true);
                }
                HelpUtilities.dialog.getContentPane().add(HelpUtilities.scroll);
                HelpUtilities.dialog.setSize(500, 400);
                HelpUtilities.dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                ApplicationManager.center(HelpUtilities.dialog);
                HelpUtilities.editorPane.setPage(urlPage);
                HelpUtilities.dialog.setVisible(true);
            }
        } else {
            if (parent instanceof Frame) {
                HelpUtilities.dialog = new JDialog((Frame) parent, title, true);
            } else {
                HelpUtilities.dialog = new JDialog((Dialog) parent, title, true);
            }

            HelpUtilities.dialog.getContentPane().add(HelpUtilities.scroll);
            HelpUtilities.dialog.setSize(500, 400);
            HelpUtilities.dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            ApplicationManager.center(HelpUtilities.dialog);
            HelpUtilities.editorPane.setPage(urlPage);
            HelpUtilities.dialog.setVisible(true);
        }

    }

    public synchronized static void showDefaultHelpFromFocus(ActionEvent e) {
        if (!HelpUtilities.HELP_ENABLED) {
            return;
        }
        if (HelpUtilities.hb == null) {
            try {
                URL hsURL = javax.help.HelpSet.findHelpSet(HelpUtilities.class.getClassLoader(),
                        HelpUtilities.default_Ontimize_help_hs);
                if (hsURL == null) {
                    MessageDialog.showMessage((Frame) null, HelpUtilities.HELP_FILE_ERROR_MESSAGE,
                            JOptionPane.ERROR_MESSAGE, null);
                    return;
                }
                HelpUtilities.hs = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(), hsURL);
            } catch (Exception ex) {
                HelpUtilities.logger.error("HelpSet help/guihelp.hs not found", ex);
                return;
            }
            HelpUtilities.hb = new ExtDefaultHelpBroker(HelpUtilities.hs);

        }

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        ((javax.help.HelpBroker) HelpUtilities.hb).setSize(new Dimension(d.width - 50, d.height - 50));
        new javax.help.CSH.DisplayHelpFromFocus((javax.help.HelpBroker) HelpUtilities.hb).actionPerformed(e);
    }

    public synchronized static void showDefaultHelpOnItem(ActionEvent e) {
        if (!HelpUtilities.HELP_ENABLED) {
            return;
        }
        if (HelpUtilities.hb == null) {
            try {
                URL hsURL = javax.help.HelpSet.findHelpSet(HelpUtilities.class.getClassLoader(),
                        HelpUtilities.default_Ontimize_help_hs);
                if (hsURL == null) {
                    MessageDialog.showMessage((Frame) null, HelpUtilities.HELP_FILE_ERROR_MESSAGE,
                            JOptionPane.ERROR_MESSAGE, null);
                    return;
                }

                HelpUtilities.hs = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(), hsURL);

            } catch (Exception ex) {
                HelpUtilities.logger.error("HelpSet help/guihelp.hs not found", ex);
                return;
            }
            HelpUtilities.hb = new ExtDefaultHelpBroker(HelpUtilities.hs);

        }

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        ((javax.help.HelpBroker) HelpUtilities.hb).setSize(new Dimension(d.width - 50, d.height - 50));
        new javax.help.CSH.DisplayHelpAfterTracking((javax.help.HelpBroker) HelpUtilities.hb).actionPerformed(e);
    }

    public static void showDefaultAdvancedHelpDialog(Window parent, String title, Locale l) throws Exception {

        if (HelpUtilities.editorPane == null) {
            HelpUtilities.editorPane = new JEditorPane();
            HelpUtilities.scroll = new JScrollPane(HelpUtilities.editorPane);
        }

        URL urlPage = HelpUtilities.getURL(HelpUtilities.ADVANCED_SEARCH_HELP_FILE, l);
        HelpUtilities.showHelpDialog(parent, urlPage, title);

    }

    protected static URL getURL(String fileName, Locale l) {

        // Try to load the page with the specified locale. If this does not
        // exist
        // then try to load the same page without the locale sufix
        if (fileName != null) {

            String sLocaleSufix = "_" + l.getLanguage() + "_" + l.getCountry();
            String sVariant = l.getVariant();
            if ((sVariant != null) && !sVariant.equals("")) {
                sLocaleSufix = sLocaleSufix + "_ " + sVariant;
            }
            // File name:
            int iDotIndex = fileName.lastIndexOf(".");
            String sFileName = fileName.substring(0, iDotIndex);
            String extension = fileName.substring(iDotIndex, fileName.length());
            String sCompleteName = sFileName + sLocaleSufix + extension;
            URL pageURL = HelpUtilities.class.getClassLoader().getResource(sCompleteName);
            if (pageURL == null) {
                HelpUtilities.logger
                    .debug(HelpUtilities.class.getClass().toString() + " : Not found : " + sCompleteName);
                // Try without the locale
                pageURL = HelpUtilities.class.getClassLoader().getResource(fileName);
                if (pageURL == null) {
                    HelpUtilities.logger
                        .debug(HelpUtilities.class.getClass().toString() + " : Not found : " + fileName);
                    return null;
                } else {
                    return pageURL;
                }
            } else {
                return pageURL;
            }
        }
        return null;
    }

    public static void setHelpIdString(Component c, String helpId) {
        if (!HelpUtilities.HELP_ENABLED) {
            return;
        }
        try {
            javax.help.CSH.setHelpIDString(c, helpId);

        } catch (Exception e) {
            if (ApplicationManager.DEBUG) {
                HelpUtilities.logger.debug("Error setting HelpIdString: ", e);
            } else {
                HelpUtilities.logger.trace("Error setting HelpIdString: ", e);
            }
        }
    }

    public static Window getLoadingWindow(Window parent) {
        HelpUtilities.createLoadingWindow(parent);
        return HelpUtilities.loadingWindow;
    }

    public static javax.help.HelpSet getDefaultHelpSet() {
        if (!HelpUtilities.HELP_ENABLED) {
            return null;
        }
        if (HelpUtilities.hs == null) {
            try {
                URL hsURL = javax.help.HelpSet.findHelpSet(HelpUtilities.class.getClassLoader(),
                        "com/ontimize/help/guihelp.hs");
                if (hsURL == null) {
                    return null;
                }
                HelpUtilities.hs = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(), hsURL);
            } catch (Exception e) {
                HelpUtilities.logger.error("Help file .hs not found", e);
            }
        }
        return (javax.help.HelpSet) HelpUtilities.hs;
    }

    public static boolean isHelpEnabled() {
        return HelpUtilities.HELP_ENABLED;
    }

    public static void setMainHelpSet(String helpset) {
        HelpUtilities.logger.debug("HelpUtilities: Setting MainHelpSet " + helpset);
        if (HelpUtilities.mainHelpSet != null) {
            if (HelpUtilities.hb != null) {
                HelpUtilities.logger
                    .debug("HelpSet is already setted and the help is loaded: MainHelpSet can not be changed");
                return;
            } else {
                HelpUtilities.mainHelpSet = helpset;
            }
        } else {
            HelpUtilities.mainHelpSet = helpset;
        }
    }

    public static synchronized void showHelp(final Window parent, final String target) {
        if (!HelpUtilities.HELP_ENABLED) {
            return;
        }
        if (HelpUtilities.mainHelpSet == null) {
            HelpUtilities.logger.debug("Show components help");
            HelpUtilities.showDefaultHelp(parent, target);
        } else {
            Thread t = new Thread() {

                @Override
                public void run() {
                    HelpUtilities.showHelp_(parent);
                    if (target != null) {
                        try {

                            ((javax.help.DefaultHelpBroker) HelpUtilities.hbAp)
                                .setCurrentID(javax.help.Map.ID.create(target,
                                        ((javax.help.DefaultHelpBroker) HelpUtilities.hbAp).getHelpSet()));
                        } catch (Exception ee) {
                            HelpUtilities.logger.trace(null, ee);
                        }
                    }
                }
            };
            t.start();
        }

    }

    public static synchronized void showHelp(final Window parent) {
        if (!HelpUtilities.HELP_ENABLED) {
            return;
        }
        if (HelpUtilities.mainHelpSet == null) {
            HelpUtilities.showDefaultHelp(parent);
            return;
        } else {
            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        HelpUtilities.showHelp_(parent);
                    } catch (Exception e) {
                        HelpUtilities.logger.trace(null, e);
                    }
                }
            };

            t.start();
        }
    }

    protected static void showHelp_(Window parent) {
        HelpUtilities.createLoadingWindow(parent);
        HelpUtilities.loadingWindow.setVisible(true);
        try {

            Thread.sleep(50);

            if ((HelpUtilities.hbAp == null) || (ApplicationManager.getLocale() != HelpUtilities.lastLocale)) {
                try {
                    HelpUtilities.lastLocale = ApplicationManager.getLocale();
                    Thread.yield();
                    URL hsURL = null;
                    // with this variable enabled always are included default
                    // ontimize help files
                    if (HelpUtilities.show_Ontimize_Help) {
                        hsURL = javax.help.HelpSet.findHelpSet(HelpUtilities.class.getClassLoader(),
                                HelpUtilities.default_Ontimize_help_hs, ApplicationManager.getLocale());
                    }
                    URL hsURLAp = javax.help.HelpSet.findHelpSet(HelpUtilities.class.getClassLoader(),
                            HelpUtilities.mainHelpSet);
                    Thread.yield();
                    if ((hsURL == null) && (hsURLAp == null)) {
                        if (parent instanceof Frame) {
                            MessageDialog.showMessage((Frame) parent, "M_NO_SE_ENCONTRO_EL_ARCHIVO_DE_AYUDA",
                                    JOptionPane.ERROR_MESSAGE, null);
                        } else {
                            MessageDialog.showMessage((Dialog) parent, "M_NO_SE_ENCONTRO_EL_ARCHIVO_DE_AYUDA",
                                    JOptionPane.ERROR_MESSAGE, null);
                        }
                        HelpUtilities.loadingWindow.setVisible(false);
                        return;
                    }
                    Thread.yield();
                    if ((hsURLAp != null) && (hsURL != null)) {
                        HelpUtilities.hsAp = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(), hsURLAp);
                        javax.help.HelpSet hs = new HelpSet();
                        try {
                            hs = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(), hsURL);
                        } catch (Exception e) {
                            HelpUtilities.logger.error(null, e);
                        }
                        ((javax.help.HelpSet) HelpUtilities.hsAp).add(hs);
                    } else if (hsURLAp != null) {
                        HelpUtilities.hsAp = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(), hsURLAp);
                    } else if (hsURL != null) {
                        HelpUtilities.hsAp = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(), hsURL);
                    }
                    Thread.yield();
                } catch (Exception e) {
                    HelpUtilities.logger.error("Error loading help ", e);
                    return;
                }
                HelpUtilities.hbAp = new ExtDefaultHelpBroker(HelpUtilities.hsAp);
                Thread.yield();
                Object source = null;
                if ((parent != null) && (parent.getComponents() != null)) {
                    source = parent.getComponents()[0];
                } else {
                    source = new JLabel();
                }
                Thread.yield();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                ((javax.help.HelpBroker) HelpUtilities.hbAp).setSize(new Dimension(d.width - 50, d.height - 50));
                new javax.help.CSH.DisplayHelpFromSource((javax.help.HelpBroker) HelpUtilities.hbAp)
                    .actionPerformed(new ActionEvent(source, 1001, null));
            } else {
                Thread.yield();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                ((javax.help.DefaultHelpBroker) HelpUtilities.hbAp).setActivationWindow(parent);
                ((javax.help.DefaultHelpBroker) HelpUtilities.hbAp).setSize(new Dimension(d.width - 50, d.height - 50));
                ((javax.help.DefaultHelpBroker) HelpUtilities.hbAp).setDisplayed(true);
            }
            if (HelpUtilities.loadingWindow != null) {
                HelpUtilities.loadingWindow.setVisible(false);
            }
        } catch (Exception e) {
            if (HelpUtilities.DEBUG) {
                HelpUtilities.logger.error(null, e);
            } else {
                HelpUtilities.logger.trace(null, e);
            }
        } finally {
            if (HelpUtilities.loadingWindow != null) {
                HelpUtilities.loadingWindow.setVisible(false);
            }
        }
    }

    public static synchronized void showHelpOnItem(Window parentWindow, ActionEvent e) {
        if (!HelpUtilities.HELP_ENABLED) {
            return;
        }
        if (HelpUtilities.mainHelpSet == null) {
            HelpUtilities.showDefaultHelpOnItem(e);
            return;
        } else {
            if (parentWindow == null) {
                if (e.getSource() instanceof Component) {
                    parentWindow = SwingUtilities.getWindowAncestor((Component) e.getSource());
                }
            }
            final Window parent = parentWindow;
            HelpUtilities.createLoadingWindow(parent);
            try {
                Thread t = new Thread() {

                    @Override
                    public void run() {

                        if (HelpUtilities.hbAp == null) {
                            try {
                                Thread.yield();
                                URL hsURL = javax.help.HelpSet.findHelpSet(HelpUtilities.class.getClassLoader(),
                                        "com/ontimize/help/guihelp.hs");
                                URL hsURLAp = javax.help.HelpSet.findHelpSet(HelpUtilities.class.getClassLoader(),
                                        HelpUtilities.mainHelpSet);
                                Thread.yield();
                                if ((hsURL == null) && (hsURLAp == null)) {
                                    if (parent instanceof Frame) {
                                        MessageDialog.showMessage((Frame) parent, "M_HELP_FILE_NOT_FOUND",
                                                JOptionPane.ERROR_MESSAGE, null);
                                    } else {
                                        MessageDialog.showMessage((Dialog) parent, "M_HELP_FILE_NOT_FOUND",
                                                JOptionPane.ERROR_MESSAGE, null);
                                    }
                                    HelpUtilities.loadingWindow.setVisible(false);
                                    return;
                                }
                                Thread.yield();
                                if ((hsURLAp != null) && (hsURL != null)) {
                                    HelpUtilities.hsAp = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(),
                                            hsURLAp);
                                    javax.help.HelpSet hs = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(),
                                            hsURL);
                                    ((javax.help.HelpSet) HelpUtilities.hsAp).add(hs);
                                } else if (hsURLAp != null) {
                                    HelpUtilities.hsAp = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(),
                                            hsURLAp);
                                } else if (hsURL != null) {
                                    HelpUtilities.hsAp = new javax.help.HelpSet(HelpUtilities.class.getClassLoader(),
                                            hsURL);
                                }

                                HelpUtilities.hbAp = new com.ontimize.help.HelpUtilities.ExtDefaultHelpBroker(
                                        HelpUtilities.hsAp);
                                HelpUtilities.loadingWindow.setVisible(false);
                            } catch (Exception eH) {
                                HelpUtilities.logger.trace(null, eH);
                            } finally {
                                HelpUtilities.loadingWindow.setVisible(false);
                            }

                        }
                    }
                };
                t.start();
                t.join();
                if (HelpUtilities.hbAp != null) {

                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

                    try {
                        ((javax.help.HelpBroker) HelpUtilities.hbAp)
                            .setSize(new Dimension(d.width - 50, d.height - 50));
                    } catch (Exception ex) {
                        HelpUtilities.logger.trace(null, ex);
                    }
                    new javax.help.CSH.DisplayHelpAfterTracking((javax.help.HelpBroker) HelpUtilities.hbAp)
                        .actionPerformed(e);
                }
            } catch (Exception ex) {
                HelpUtilities.logger.trace(null, ex);
            } finally {
                if (HelpUtilities.loadingWindow != null) {
                    HelpUtilities.loadingWindow.setVisible(false);
                }
            }
        }
    }

    public static synchronized void showHelpOnItem(ActionEvent e) {
        HelpUtilities.showHelpOnItem(null, e);
    }

}
