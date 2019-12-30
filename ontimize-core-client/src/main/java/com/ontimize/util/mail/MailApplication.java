package com.ontimize.util.mail;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailApplication extends JFrame implements TreeSelectionListener {

	private static final Logger		logger	= LoggerFactory.getLogger(MailApplication.class);

	protected DefaultMailManager manager = null;

	protected static class FolderNode extends DefaultMutableTreeNode {

		protected Folder folder = null;

		protected boolean hasDownloadedMessages = false;

		public FolderNode(Folder f) {
			super(f.getFullName());
			this.folder = f;
		}

		public Folder getFolder() {
			return this.folder;
		}

		public void setHasDownloadedMessages(boolean d) {
			this.hasDownloadedMessages = d;
		}

		@Override
		public boolean isLeaf() {
			return false;
		}

		public boolean getHasDownloadedMessages() {
			return this.hasDownloadedMessages;
		}
	}

	protected static class MessageNode extends DefaultMutableTreeNode {

		protected Message message = null;

		public MessageNode(Message m) {
			super();
			this.message = m;
			try {
				Address[] from = m.getFrom();
				String subject = m.getSubject();
				StringBuilder descr = new StringBuilder();
				for (int i = 0; i < from.length; i++) {
					if (i < (from.length - 1)) {
						descr.append(from[i].toString() + ",");
					}
				}
				descr.append(" - ");
				descr.append(subject);
				this.setUserObject(descr.toString());
			} catch (Exception e) {
				MailApplication.logger.error(null, e);
			}
		}

		public Message getMessage() {
			return this.message;
		}
	}

	protected static class FolderTree extends JTree implements TreeWillExpandListener {

		public FolderTree(TreeSelectionListener listener) {
			super(new DefaultMutableTreeNode("Without connection"));
			this.addTreeWillExpandListener(this);
			this.addTreeSelectionListener(listener);
		}

		@Override
		public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
			if (event.getPath().getLastPathComponent() instanceof FolderNode) {
				FolderNode folder = (FolderNode) event.getPath().getLastPathComponent();
				if (folder.getHasDownloadedMessages()) {
					return;
				}
				Folder f = folder.getFolder();
				try {

					f.open(Folder.READ_ONLY);
					if (f.getMessageCount() == 0) {
						folder.add(new DefaultMutableTreeNode("No messages found"));
						return;
					}
					Message[] messages = f.getMessages();
					folder.setHasDownloadedMessages(true);
					for (int i = 0; i < messages.length; i++) {
						folder.add(new MessageNode(messages[i]));
					}
				} catch (Exception e) {
					MailApplication.logger.error(null, e);
					throw new ExpandVetoException(event, e.getMessage());
				} finally {
					try {
						f.close(false);
					} catch (Exception e) {
						MailApplication.logger.trace(null, e);
					}
				}
			}
		}

		@Override
		public void treeWillCollapse(TreeExpansionEvent event) {

		}
	}

	protected JEditorPane editorPane = new JEditorPane();

	protected JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

	protected FolderTree tree = null;

	public MailApplication(JFrame owner, String title) {
		super(title);
		if (owner != null) {
			this.setIconImage(owner.getIconImage());
		}
		this.tree = new FolderTree(this);
		JScrollPane scroll = new JScrollPane(this.tree);
		this.getContentPane().add(this.splitPane);
		JScrollPane scroll2 = new JScrollPane(this.editorPane);

		this.splitPane.add(scroll, JSplitPane.LEFT);
		this.splitPane.add(scroll2, JSplitPane.RIGHT);
		this.splitPane.setDividerLocation(200);
		this.setSize(700, 450);

	}

	public void connect(String mailServer, String userName) throws Exception {
		if (!this.isVisible()) {
			this.setVisible(true);
		}
		this.manager = new DefaultMailManager(mailServer, userName);
		Store s = null;
		try {
			s = this.manager.getStore();
		} catch (Exception e) {
			MailApplication.logger.trace(null, e);
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
		Folder f = s.getDefaultFolder();
		FolderNode node = new FolderNode(f);
		this.buildFolderTree(node, f);

		this.tree.setModel(new DefaultTreeModel(node));
	}

	protected void buildFolderTree(DefaultMutableTreeNode node, Folder f) throws MessagingException {
		if (f.getType() == Folder.HOLDS_FOLDERS) {
			Folder[] folders = f.list();
			for (int i = 0; i < folders.length; i++) {
				Folder fH = folders[i];
				FolderNode n = new FolderNode(fH);
				node.add(n);
				this.buildFolderTree(n, fH);
			}
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (e.getPath().getLastPathComponent() instanceof MessageNode) {
			FolderNode fNode = null;
			try {
				fNode = (FolderNode) ((MessageNode) e.getPath().getLastPathComponent()).getParent();
				fNode.getFolder().open(Folder.READ_ONLY);
				Message message = ((MessageNode) e.getPath().getLastPathComponent()).getMessage();

				if (message.isMimeType("text/plain")) {
					this.editorPane.setContentType(message.getContentType());
					this.editorPane.setText((String) message.getContent());
				} else if (!message.isMimeType("multipart/*")) {
					this.editorPane.setContentType(message.getContentType());
					this.editorPane.setText(message.getContent().toString());
				} else {

				}
			} catch (Exception ex) {
				MailApplication.logger.trace(null, ex);
				this.editorPane.setText("Error reading the message.\n" + ex.getMessage());
			} finally {
				if (fNode != null) {
					try {
						fNode.getFolder().close(false);
					} catch (Exception ex) {
						MailApplication.logger.trace(null, ex);
					}
				}
			}
		}
	}
}