package com.ontimize.util.rmitunneling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.AbstractTableModel;

import com.ontimize.gui.ApplicationManager;

public class StreamInfoComponent extends JComponent {

	private final Dimension prefSize = new Dimension(350, 250);

	private static long[] lastSentBytes = new long[100];

	private static long[] lastReceivedBytes = new long[100];

	private static long totalSent = 0;

	private static long totalReceived = 0;

	private static String[] lastSentTraces = new String[100];

	private static String[] lastReceivedTraces = new String[100];

	private static long lastTimeMilestoneSent = 0;

	private static long lastBytesMilestoneSent = 0;

	private static long bytesPerSecondSent = 0;

	private static long lastTimeMilestoneRec = 0;

	private static long lastBytesMilestoneRec = 0;

	private static long bytesPerSecondRec = 0;

	public StreamInfoComponent() {
		this.setBackground(Color.black);
		ToolTipManager.sharedInstance().registerComponent(this);
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					StreamInfoComponent.this.clear();
				}
			}
		});
	}

	@Override
	public String getToolTipText(MouseEvent e) {
		float increment = this.getWidth() / (float) StreamInfoComponent.lastSentBytes.length;
		float offset = this.getZerosSent() * increment;
		float is = (e.getX() - offset) / increment;
		offset = this.getZerosReceived() * increment;
		float ir = (e.getX() - offset) / increment;
		if ((is < StreamInfoComponent.lastSentTraces.length) && (ir < StreamInfoComponent.lastReceivedTraces.length)) {
			String sent = StreamInfoComponent.lastSentTraces[(int) is];
			String recv = StreamInfoComponent.lastReceivedTraces[(int) ir];
			if ((sent != null) || (recv != null)) {
				return "<html><body><h2>sent</h2><pre>" + sent + "</pre><h2>Received</h2><pre>" + recv + "</pre></body></html>";
			}
			return null;
		} else {
			return null;
		}
	}

	public void clear() {
		for (int i = 0; i < StreamInfoComponent.lastReceivedBytes.length; i++) {
			StreamInfoComponent.lastReceivedBytes[i] = 0;
			StreamInfoComponent.lastReceivedTraces[i] = null;
		}
		for (int i = 0; i < StreamInfoComponent.lastSentBytes.length; i++) {
			StreamInfoComponent.lastSentBytes[i] = 0;
			StreamInfoComponent.lastSentTraces[i] = null;
		}
		this.repaint();
	}

	public String[] getLastSentTraces() {
		return StreamInfoComponent.lastSentTraces;
	}

	public String[] getLastReveivedTraces() {
		return StreamInfoComponent.lastReceivedTraces;
	}

	public void addSent(long b, String trace) {
		if (b != -1) {
			StreamInfoComponent.totalSent += b;
		}

		if (StreamInfoComponent.lastTimeMilestoneSent != 0) {
			if ((System.currentTimeMillis() - StreamInfoComponent.lastTimeMilestoneSent) > 200) {
				StreamInfoComponent.bytesPerSecondSent = (int) ((StreamInfoComponent.totalSent - StreamInfoComponent.lastBytesMilestoneSent) / (float) ((System
						.currentTimeMillis() - StreamInfoComponent.lastTimeMilestoneSent) / 1000.0));
				StreamInfoComponent.lastTimeMilestoneSent = System.currentTimeMillis();
				StreamInfoComponent.lastBytesMilestoneSent = StreamInfoComponent.totalSent;
			}
		} else {
			StreamInfoComponent.lastTimeMilestoneSent = System.currentTimeMillis();
			StreamInfoComponent.lastBytesMilestoneSent = StreamInfoComponent.totalSent;
		}

		if (StreamInfoComponent.lastSentBytes[StreamInfoComponent.lastSentBytes.length - 1] != 0) {
			for (int i = 1; i < StreamInfoComponent.lastSentBytes.length; i++) {
				StreamInfoComponent.lastSentBytes[i - 1] = StreamInfoComponent.lastSentBytes[i];
				StreamInfoComponent.lastSentTraces[i - 1] = StreamInfoComponent.lastSentTraces[i];
			}
			StreamInfoComponent.lastSentBytes[StreamInfoComponent.lastSentBytes.length - 1] = b;
			StreamInfoComponent.lastSentTraces[StreamInfoComponent.lastSentTraces.length - 1] = trace;
		} else {
			for (int i = 0; i < StreamInfoComponent.lastSentBytes.length; i++) {
				if (StreamInfoComponent.lastSentBytes[i] == 0) {
					StreamInfoComponent.lastSentBytes[i] = b;
					StreamInfoComponent.lastSentTraces[i] = trace;
					break;
				}
			}
		}
		if (b > 0) {
			this.addReceived(-1, null);
		}
		this.repaint();
	}

	public void addReceived(long b, String trace) {
		if (b != -1) {
			StreamInfoComponent.totalReceived += b;
		}

		if (StreamInfoComponent.lastTimeMilestoneRec != 0) {
			if ((System.currentTimeMillis() - StreamInfoComponent.lastTimeMilestoneRec) > 200) {
				StreamInfoComponent.bytesPerSecondRec = (int) ((StreamInfoComponent.totalReceived - StreamInfoComponent.lastBytesMilestoneRec) / (float) ((System
						.currentTimeMillis() - StreamInfoComponent.lastTimeMilestoneRec) / 1000.0));
				StreamInfoComponent.lastTimeMilestoneRec = System.currentTimeMillis();
				StreamInfoComponent.lastBytesMilestoneRec = StreamInfoComponent.totalReceived;
			}
		} else {
			StreamInfoComponent.lastTimeMilestoneRec = System.currentTimeMillis();
			StreamInfoComponent.lastBytesMilestoneRec = StreamInfoComponent.totalSent;
		}

		if (StreamInfoComponent.lastReceivedBytes[StreamInfoComponent.lastReceivedBytes.length - 1] != 0) {
			for (int i = 1; i < StreamInfoComponent.lastReceivedBytes.length; i++) {
				StreamInfoComponent.lastReceivedBytes[i - 1] = StreamInfoComponent.lastReceivedBytes[i];
				StreamInfoComponent.lastReceivedTraces[i - 1] = StreamInfoComponent.lastReceivedTraces[i];
			}
			StreamInfoComponent.lastReceivedBytes[StreamInfoComponent.lastReceivedBytes.length - 1] = b;
			StreamInfoComponent.lastReceivedTraces[StreamInfoComponent.lastReceivedTraces.length - 1] = trace;
		} else {
			for (int i = 0; i < StreamInfoComponent.lastReceivedBytes.length; i++) {
				if (StreamInfoComponent.lastReceivedBytes[i] == 0) {
					StreamInfoComponent.lastReceivedBytes[i] = b;
					StreamInfoComponent.lastReceivedTraces[i] = trace;
					break;
				}
			}
		}
		if (b > 0) {
			this.addSent(-1, null);
		}
		this.repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return this.prefSize;
	}

	@Override
	public Dimension getMinimumSize() {
		return this.prefSize;
	}

	@Override
	public Dimension getMaximumSize() {
		return this.prefSize;
	}

	public long getBytesPerSecondSent() {
		return StreamInfoComponent.bytesPerSecondSent;
	}

	public long getBytesPerSecondRec() {
		return StreamInfoComponent.bytesPerSecondRec;
	}

	public static long[] getSent() {
		return StreamInfoComponent.lastSentBytes;
	}

	public long getTotalSent() {
		return StreamInfoComponent.totalSent;
	}

	public long getTotalReceived() {
		return StreamInfoComponent.totalReceived;
	}

	public static long[] getReceived() {
		return StreamInfoComponent.lastReceivedBytes;
	}

	public int getZerosSent() {
		int j = 0;
		for (int i = 0; i < StreamInfoComponent.lastSentBytes.length; i++) {
			if (StreamInfoComponent.lastSentBytes[i] == 0) {
				j++;
			}
		}
		return j;
	}

	public int getZerosReceived() {
		int j = 0;
		for (int i = 0; i < StreamInfoComponent.lastReceivedBytes.length; i++) {
			if (StreamInfoComponent.lastReceivedBytes[i] == 0) {
				j++;
			}
		}
		return j;
	}

	public long getMax() {
		long max = 0;
		for (int i = 0; i < StreamInfoComponent.lastSentBytes.length; i++) {
			if (StreamInfoComponent.lastSentBytes[i] > max) {
				max = StreamInfoComponent.lastSentBytes[i];
			}
		}
		for (int i = 0; i < StreamInfoComponent.lastReceivedBytes.length; i++) {
			if (StreamInfoComponent.lastReceivedBytes[i] > max) {
				max = StreamInfoComponent.lastReceivedBytes[i];
			}
		}
		return max;
	}

	@Override
	public void paintComponent(Graphics g) {
		((Graphics2D) g).setBackground(this.getBackground());
		((Graphics2D) g).clearRect(0, 0, this.getWidth(), this.getHeight());
		FontMetrics fm = ((Graphics2D) g).getFontMetrics(g.getFont());
		int ascent = fm.getAscent();
		int descent = fm.getDescent();
		long max = this.getMax();

		// .. Draw allocated and used strings ..
		((Graphics2D) g).setColor(Color.green);
		((Graphics2D) g).drawString(String.valueOf((int) max) + "Max Bytes", 4.0f, ascent + 0.5f);

		int h = this.getHeight() - 5;
		int w = this.getWidth();

		float increment = w / (float) StreamInfoComponent.lastSentBytes.length;

		g.setColor(Color.gray);
		for (int i = 0; i < (h - 30); i = i + 30) {
			g.drawLine(0, h - i, w, h - i);
		}

		for (int i = 0; i < w; i = i + (int) increment) {
			g.drawLine(i, 30, i, h);
		}
		((Graphics2D) g).setColor(Color.red);
		((Graphics2D) g).drawString(this.getTotalSent() + " Sent", 90, ascent + 0.5f);
		Line2D graphLine = new Line2D.Float();

		float scale = (h - 30) / (float) max;
		float x = 0;
		float offset = this.getZerosSent() * increment;
		x = offset;
		for (int j = 0; j < (StreamInfoComponent.lastSentBytes.length - 1); j++) {
			if (StreamInfoComponent.lastSentBytes[j] != 0) {
				float y = h - (StreamInfoComponent.lastSentBytes[j] * scale);
				float y1 = h - (StreamInfoComponent.lastSentBytes[j + 1] * scale);
				graphLine.setLine(x, y, x + increment, y1);
				((Graphics2D) g).draw(graphLine);
				x = x + increment;
			}
		}

		offset = this.getZerosReceived() * increment;
		x = offset;
		((Graphics2D) g).setColor(Color.yellow);
		((Graphics2D) g).drawString(this.getTotalReceived() + " Received", 90, ((float) ascent * 2) + 0.5f);
		for (int j = 0; j < (StreamInfoComponent.lastReceivedBytes.length - 1); j++) {
			if (StreamInfoComponent.lastReceivedBytes[j] != 0) {
				float y = h - (StreamInfoComponent.lastReceivedBytes[j] * scale);
				float y1 = h - (StreamInfoComponent.lastReceivedBytes[j + 1] * scale);
				graphLine.setLine(x, y, x + increment, y1);
				((Graphics2D) g).draw(graphLine);
				x = x + increment;
			}
		}
		((Graphics2D) g).setColor(Color.blue.brighter().brighter());
		((Graphics2D) g).drawString(this.getBytesPerSecondSent() + " bytes/s up", 200, ascent + 0.5f);
		((Graphics2D) g).drawString(this.getBytesPerSecondRec() + " bytes/s down", 200, ((float) ascent * 2) + 0.5f);

	}

	public static class StreamTraceTable extends JScrollPane {

		protected StreamTraceModel model;

		public StreamTraceTable() {
			JTable table = new JTable();
			this.model = new StreamTraceModel();
			table.setModel(this.model);
			this.setViewportView(table);
			this.setPreferredSize(new Dimension(20, 60));
		}

		public void updateTraces(String[] sentTraces, String[] receivedTraces) {
			this.model.updateTraces(sentTraces, receivedTraces);
		}

		public static class StreamTraceModel extends AbstractTableModel {

			protected String[] lastReceivedTraces = new String[0];
			protected String[] lastSentTraces = new String[0];

			public void updateTraces(String[] sentTraces, String[] receivedTraces) {
				this.lastReceivedTraces = receivedTraces;
				this.lastSentTraces = sentTraces;
				this.fireTableDataChanged();
			}

			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public int getRowCount() {
				return this.lastSentTraces.length;
			}

			@Override
			public String getColumnName(int column) {
				switch (column) {
				case 0:
					return "sent";
				case 1:
					return "received";
				default:
					break;
				}
				return super.getColumnName(column);
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return this.lastSentTraces[this.lastSentTraces.length - rowIndex - 1];
				case 1:
					return this.lastReceivedTraces[this.lastReceivedTraces.length - rowIndex - 1];
				default:
					break;
				}
				return null;
			}
		}
	}

	public static class StreamInfoFrame extends JFrame {

		private static StreamInfoComponent streamInfoComponent = null;
		private static JButton buttonTrace;
		private static StreamTraceTable traceTable;

		public StreamInfoFrame() {
			super("HTTP/HTTPS Traffic");
			JPanel centerPanel = new JPanel(new GridBagLayout());
			StreamInfoFrame.streamInfoComponent = new StreamInfoComponent();

			// buttonTrace = new JButton("Show trace");
			// traceTable = new StreamTraceTable();

			centerPanel.add(StreamInfoFrame.streamInfoComponent,
					new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			if (StreamInfoFrame.buttonTrace != null) {
				centerPanel.add(StreamInfoFrame.buttonTrace,
						new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}

			if (StreamInfoFrame.traceTable != null) {
				centerPanel.add(StreamInfoFrame.traceTable,
						new GridBagConstraints(0, 2, 1, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			this.getContentPane().add(centerPanel);
			this.pack();
			ApplicationManager.setLocationNorthEast(this);
		}

		public void addSent(long b, String trace) {
			StreamInfoFrame.streamInfoComponent.addSent(b, trace);
			if (StreamInfoFrame.traceTable != null) {
				StreamInfoFrame.traceTable.updateTraces(StreamInfoFrame.streamInfoComponent.getLastSentTraces(), StreamInfoFrame.streamInfoComponent.getLastSentTraces());
			}
		}

		public void addReceived(long b, String trace) {
			StreamInfoFrame.streamInfoComponent.addReceived(b, trace);
			if (StreamInfoFrame.traceTable != null) {
				StreamInfoFrame.traceTable.updateTraces(StreamInfoFrame.streamInfoComponent.getLastSentTraces(), StreamInfoFrame.streamInfoComponent.getLastSentTraces());
			}
		}
	}

	private static StreamInfoFrame streamInfoWindow = null;

	public static StreamInfoFrame getStreamInfoFrame() {
		if (StreamInfoComponent.streamInfoWindow == null) {
			StreamInfoComponent.streamInfoWindow = new StreamInfoFrame();
		}
		return StreamInfoComponent.streamInfoWindow;
	}

	public static void setStreamInfoWindowVisible(boolean vis) {
		StreamInfoComponent.getStreamInfoFrame();
		if (StreamInfoComponent.streamInfoWindow != null) {
			StreamInfoComponent.streamInfoWindow.setVisible(vis);
		}
	}
}
