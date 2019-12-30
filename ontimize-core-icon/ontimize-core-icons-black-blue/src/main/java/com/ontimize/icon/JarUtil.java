package com.ontimize.icon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.jar.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.html.HTML;

 
public class JarUtil {
	protected static Properties data;
	static{
		URL current = JarUtil.class.getResource("data.properties");
		data=new Properties();
		try {
			data.load(current.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final String COMPONENT_NAME="Component-name";
	public static final String ONTIMIZE_VERSION="Ontimize-version-number";
	public static final String VERSION="Version-number";
	public static final String BUILT="Built-By";
	public static final String DATE="Version-date";
	
	protected static class ManifestInfo extends HashMap{
		protected String componentName;
		public void setComponentName(String name){
			this.componentName=name;
		}
		public String getComponentName(){
			return this.componentName;
		}
	}
	
	protected static class Header extends JLabel{
		protected String componentName;
		protected int h2=0;
		public Header(String name) {
			super(name);
			setFont(getFont().deriveFont(24F));
			setBackground(Color.black);
			setForeground(Color.white);
			setHorizontalAlignment(CENTER);
			setOpaque(true);	
		}
		
		public Dimension getPreferredSize() {
			Dimension d =  super.getPreferredSize();
			h2 = d.height /6;
			d.height=d.height+h2;
			return d;
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Dimension d=getSize();
			int h = d.height;
			int h1=  h/6;
			Color c=g.getColor();
			g.setColor(Color.red);
			g.fillRect(0, h-h1, d.width, h1);
			g.setColor(c);
		}
	}
	
	
	
	
	protected static class Body extends JPanel{
		public Body(ManifestInfo model){
			setLayout(new GridBagLayout());
			Iterator iterator = model.keySet().iterator();
			int i=0;
			while (iterator.hasNext()){
				Object currentKey = iterator.next();
				add(createTitle(currentKey.toString()),new GridBagConstraints(GridBagConstraints.RELATIVE,i,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
				add(createValue(model.get(currentKey)),new GridBagConstraints(GridBagConstraints.RELATIVE,i,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(1,1,1,1),0,0));
				i++;
			}	
			add(new JPanel(),new GridBagConstraints(GridBagConstraints.RELATIVE,i,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0));
		}
		
		protected JLabel createTitle(Object key){
			JLabel label =null;
			if (data.containsKey(key)){
				label=new JLabel(data.getProperty(key.toString()));
			}else
				label=new JLabel(key.toString());
			label.setOpaque(true);
			Font f=label.getFont().deriveFont(Font.BOLD);
			f=f.deriveFont(f.getSize()+3F);
			label.setFont(f);
			
			return label;
		}
		
		protected JLabel createValue(Object key){
			JLabel label =new JLabel(key.toString());
			label.setOpaque(true);
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			Font f=label.getFont();
			f=f.deriveFont(f.getSize()+3F);
			label.setFont(f);
//			label.setFont(label.getFont().deriveFont(Font.BOLD));
			return label;
		}
	}
	
	public static ManifestInfo getManifest(Component d) throws Exception {
		ManifestInfo info=new ManifestInfo();
		Manifest manifest=retrieveManifest();	
		if (manifest!=null){
			try {
				Enumeration keys=data.keys();
				while(keys.hasMoreElements()){
					Object current = keys.nextElement();
					String currentValue = getAttribute(current, manifest).toString();
					info.put(current,currentValue);
				}
				info.setComponentName(getAttribute(COMPONENT_NAME, manifest).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			info.put(BUILT,"Imatia Innovation S.L");
			info.put(VERSION,"666");
			info.put(ONTIMIZE_VERSION,"69");
			info.put(DATE,"12:30:24 19/02/2008");
			info.setComponentName("debug");
		}
		return info;
	}
	
//	protected static String getHtmlSource(){
//		URL urlHtml=JarUtil.class.getResource(TEMPLATE_PATH);
//		if (urlHtml!=null){
//			InputStream iS;
//			try {
//				iS = urlHtml.openStream();
//
//				BufferedReader bR= new BufferedReader(new InputStreamReader(iS));
//				StringBuffer html=new StringBuffer();
//				try{ 
//					String str;
//					while ((str = bR.readLine()) != null) {
//						html.append(str);
//					}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				bR.close();	
//				return html.toString();
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		}
//		return null;
//	}

	protected static String getAttribute(Object key,Manifest m){    
		Attributes.Name aN=new Attributes.Name(key.toString());
		Attributes ats=m.getMainAttributes();
		if (ats.containsKey(aN)){
			return ats.getValue(key.toString());
		}
		return null;
	}
	
	protected static Manifest retrieveManifest(){
		Enumeration enumeration = null;
		String pattern=null;
		try{
			URL url = JarUtil.class.getResource("");
			String packageName = JarUtil.class.getPackage().getName();
			
			packageName = packageName.replace('.', '/');
			String path = url.getFile();
			int index= path.lastIndexOf(packageName);
			if (index>=-1) {
				pattern = path.substring(0,index); 
			}
			enumeration = JarUtil.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		if ((enumeration==null) || (pattern == null)) return null;
		
		try{
			while (enumeration.hasMoreElements()){
				URL url = (URL) enumeration.nextElement();
				String path = url.getFile();
				if (pattern!=null){
					if (path.indexOf(pattern)>=0){
						Manifest m = new Manifest(url.openStream());
						return m;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public static class InformationDialog extends JFrame{
		protected JLabel lVersion=null;
		protected JLabel lHtml=null;
		protected JLabel iOntimize=null;
		protected JLabel tPanel=null;
		
		protected boolean hideFrame=false;
		

		class EAction extends AbstractAction {
			public void actionPerformed(ActionEvent e) {
				if(SwingUtilities.getWindowAncestor((Component)e.getSource()) instanceof InformationDialog) {
					((InformationDialog)SwingUtilities.getWindowAncestor((Component)e.getSource())).processWindowEvent(new WindowEvent(((InformationDialog)SwingUtilities.getWindowAncestor((Component)e.getSource())),WindowEvent.WINDOW_CLOSING));
				}
			}
		}
			
		public static ImageIcon getImatiaIcon(){
			URL url = JarUtil.class.getResource("iconimatia.gif");
			if (url==null) return null;
			ImageIcon icon = new ImageIcon(url);
			return icon;
		}
		
		
		public InformationDialog(boolean hideFrame) {
			this.hideFrame=hideFrame;
			ActionMap aM=((JComponent)getContentPane()).getActionMap();
			InputMap inMap = ((JComponent)this.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

			aM.put("close",new EAction());
			inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "close");

			setTitle("Imatia Innovation");
			ImageIcon iconImatia=getImatiaIcon();
			if (iconImatia!=null){
				setIconImage(iconImatia.getImage());
			}


			((JComponent)this.getContentPane()).setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inMap);
			((JComponent)getContentPane()).setActionMap(aM);

			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {
					if (InformationDialog.this.hideFrame){
						InformationDialog.this.setVisible(false);
					}else
						System.exit(0);
				}
			});

			setResizable(false);
			getContentPane().setBackground(new Color(209,209,209));
			
			ManifestInfo info=null;
			try {
				info = getManifest(this);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			String componentName = info.getComponentName();
			if (componentName==null){
				componentName="MANIFEST";
			}
		
			getContentPane().setLayout(new GridBagLayout());
			getContentPane().add(new Header(componentName),new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,0),0,0));
			getContentPane().add(new Body(info),new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,2,2,0),0,0));
			getContentPane().setFocusable(true);
			getContentPane().requestFocus();
			pack();
			Dimension d = getSize();
			if (d.width<250){
				int increment = 250- d.width;
				d.width=d.width+increment;
				d.height= d.height+increment;
			}
			setSize(d);
		}
		
	}
	
	public final static void main(String [] arg){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
//		ImageIcon icono=ApplicationManager.getIcon("com/ontimize/gui/images/iconimatia.gif");
//		if (icono!=null){
//			JFrame f=new JFrame("borrar");
//			f.setIconImage(icono.getImage());
//			
//			
//		}
		InformationDialog id=new InformationDialog(false);
		center(id);
		id.setVisible(true);
	}
	
	public static void center(Window f) {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = d.width/2-f.getWidth()/2;
		int y = d.height/2-f.getHeight()/2;
		if(x<0) x = 0;
		if(y<0) y = 0;
		if(x>d.width) x = 0;
		if(y>d.height) y = 0;
		f.setLocation(x,y);
	}
}
