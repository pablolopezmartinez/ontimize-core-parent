package com.ontimize.util;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.gui.Form;
import com.ontimize.gui.MessageDialog;

public abstract class FileUtils {

	private static final Logger		logger			= LoggerFactory.getLogger(FileUtils.class);

	protected static JFileChooser fc = null;

	protected static File lastDirectory = null;

	public static class ExtensionFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {

		Vector extensions = new Vector();

		String descrip = "";

		public ExtensionFileFilter(Vector extAccepted, String description) {
			this.extensions = extAccepted;
			this.descrip = description;
		}

		public ExtensionFileFilter(String descripcion) {
			this.descrip = descripcion;
		}

		public void addExtension(String extension) {
			this.extensions.add(extension);
		}

		@Override
		public String getDescription() {
			return this.descrip;
		}

		public void setDescription(String description) {
			this.descrip = description;
		}

		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				int dotIndex = file.getPath().lastIndexOf(".");
				String extension = file.getPath().substring(dotIndex + 1);
				if (this.extensions.contains(extension)) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	public FileUtils() {}

	public static javax.swing.filechooser.FileFilter getExtensionFileFilter(String description, String[] extensions) {
		ExtensionFileFilter ff = new ExtensionFileFilter(description);
		for (int i = 0; i < extensions.length; i++) {
			ff.addExtension(extensions[i]);
		}
		return ff;
	}

	public static java.io.FileFilter getExtensionFileFilterIO(String description, String[] extensions) {
		ExtensionFileFilter ff = new ExtensionFileFilter(description);
		for (int i = 0; i < extensions.length; i++) {
			ff.addExtension(extensions[i]);
		}
		return ff;
	}

	public static void saveFile(File f, String contents) throws IOException {
		FileUtils.saveFile(f, contents, null);
	}

	protected static void saveFile(File f, String contents, String encoding) throws IOException {
		FileUtils.saveFile(f, contents, encoding, false);
	}

	protected static void saveFile(File f, String contents, String encoding, boolean append) throws IOException {
		FileOutputStream out = null;
		OutputStreamWriter w = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(f.getCanonicalPath(), append);
			if (encoding != null) {
				w = new OutputStreamWriter(out, encoding);
			} else {
				w = new OutputStreamWriter(out);
			}
			bw = new BufferedWriter(w);

			int lineIndexJump = contents.indexOf('\n');
			if (lineIndexJump < 0) {
				bw.write(contents);
			} else {
				String s = contents.substring(0, lineIndexJump);
				bw.write(s);
				bw.newLine();
				int iPrevIndex = lineIndexJump;
				while ((lineIndexJump = contents.indexOf('\n', iPrevIndex + 1)) >= 0) {
					bw.write(contents.substring(iPrevIndex + 1, lineIndexJump));
					bw.newLine();
					iPrevIndex = lineIndexJump;
				}
				if (iPrevIndex < contents.length()) {
					bw.write(contents.substring(iPrevIndex + 1));
				}
			}

			bw.flush();
			bw.close();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (w != null) {
					w.close();
				}
				if (bw != null) {
					bw.close();
				}
			} catch (Exception e3) {
				FileUtils.logger.trace(null, e3);
			}
		}
	}

	public static boolean saveANSIFile(java.awt.Frame f, String dialogTitle, String contents, String extension, ResourceBundle resoruces) throws Exception {
		if (FileUtils.fc == null) {
			if (FileUtils.lastDirectory != null) {
				FileUtils.fc = new JFileChooser(FileUtils.lastDirectory);
			} else {
				FileUtils.fc = new JFileChooser();
			}
			String[] ext = new String[1];
			ext[0] = extension;
			FileUtils.fc.setFileFilter(FileUtils.getExtensionFileFilter("(*." + extension + ")", ext));
			FileUtils.fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		if (dialogTitle != null) {
			String tit = dialogTitle;
			try {
				if (resoruces != null) {
					tit = resoruces.getString(dialogTitle);
				}
			} catch (Exception ex) {
				FileUtils.logger.error(null, ex);
			}
			FileUtils.fc.setDialogTitle(tit);
		}

		File archivoSel = null;
		int option = -1;

		if (FileUtils.lastDirectory != null) {
			FileUtils.fc.setCurrentDirectory(FileUtils.lastDirectory);
		} else {
			FileUtils.lastDirectory = FileUtils.fc.getCurrentDirectory();
		}

		boolean save = true;

		while (true) {
			String arch = FileUtils.lastDirectory.getPath();
			FileUtils.fc.setSelectedFile(new File(arch));
			option = FileUtils.fc.showSaveDialog(f);
			archivoSel = FileUtils.fc.getSelectedFile();
			FileUtils.lastDirectory = FileUtils.fc.getCurrentDirectory();
			if (option != JFileChooser.APPROVE_OPTION) {
				int resp = MessageDialog.showMessage(f, "fileutils.cancel_file_saving_operation", null, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, resoruces);
				if (resp == JOptionPane.NO_OPTION) {} else {
					save = false;
					break;
				}
			} else {
				if (archivoSel.exists()) {
					int resp = MessageDialog.showMessage(f, "fielddata.overwrite_existing_file", null, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, resoruces);
					if (resp == JOptionPane.YES_OPTION) {
						break;
					}
				} else {
					break;
				}
			}
		}
		if ((option == JFileChooser.APPROVE_OPTION) && save) {
			FileUtils.saveANSIFile(archivoSel, contents);
			MessageDialog.showMessage(f, "M_ARCHIVO_SALVADO_CORRECTAMENTE", Form.INFORMATION_MESSAGE, resoruces);
			return true;
		} else {
			return false;
		}

	}

	public static void saveANSIFile(File f, String contents) throws Exception {
		FileUtils.saveFile(f, contents, "8859_1");
	}

	public static void saveANSIFile(File f, String contents, boolean append) throws Exception {
		FileUtils.saveFile(f, contents, "8859_1", append);
	}

	public static void saveOEMFile(File f, String contents) throws Exception {
		FileUtils.saveFile(f, contents, "Cp850");
	}

	public static byte[] getBytes(File source) throws Exception {
		FileInputStream input = new FileInputStream(source);
		ByteArrayOutputStream fOutput = new ByteArrayOutputStream();
		try {
			byte[] bytes = new byte[65536];
			int iBytesRead = 0;
			while ((iBytesRead = input.read(bytes)) >= 0) {
				fOutput.write(bytes, 0, iBytesRead);
			}
			fOutput.flush();
			fOutput.close();
			return fOutput.toByteArray();
		} finally {
			try {
				fOutput.close();
				input.close();
			} catch (Exception e3) {
				FileUtils.logger.trace(null, e3);
			}
		}
	}

	public static void copyFile(InputStream source, File dest) throws Exception {
		FileOutputStream fOutput = null;
		try {
			if (!dest.exists()) {
				dest.createNewFile();
			}
			fOutput = new FileOutputStream(dest);
			BufferedOutputStream bo = new BufferedOutputStream(fOutput);
			byte[] bytes = new byte[65536];
			int iBytesRead = 0;
			while ((iBytesRead = source.read(bytes)) >= 0) {
				bo.write(bytes, 0, iBytesRead);
			}
			bo.flush();
			bo.close();
			return;
		} finally {
			try {
				fOutput.close();
				source.close();
			} catch (Exception e3) {
				FileUtils.logger.trace(null, e3);
			}
		}
	}

	public static void copyFile(File source, File dest) throws Exception {
		InputStream input = null;
			input = new FileInputStream(source);
			FileUtils.copyFile(input, dest);
	}

	public static void saveJPEGImage(java.awt.Image im, OutputStream out) throws IOException {
		try {
			BufferedImage bi = new BufferedImage(im.getWidth(FileUtils.fc), im.getHeight(FileUtils.fc), BufferedImage.TYPE_3BYTE_BGR);
			Graphics g = bi.getGraphics();
			g.drawImage(im, 0, 0, FileUtils.fc);
			ImageIO.write(bi, "jpg", out);
			bi.flush();
		} catch (IOException e) {
			FileUtils.logger.error(null, e);
			throw e;
		}
	}

	public static boolean saveJPEGImage(Frame f, java.awt.Image im, ResourceBundle recursos) {
		FileOutputStream out = null;
		try {

			if (FileUtils.fc == null) {
				if (FileUtils.lastDirectory != null) {
					FileUtils.fc = new JFileChooser(FileUtils.lastDirectory);
				} else {
					FileUtils.fc = new JFileChooser();
				}
				String[] ext = new String[1];
				ext[0] = "jpg";
				FileUtils.fc.setFileFilter(FileUtils.getExtensionFileFilter("(*.jpg)", ext));
				FileUtils.fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			}

			FileUtils.fc.setDialogTitle("Save");

			File selectedFile = null;
			int iOption = -1;

			if (FileUtils.lastDirectory != null) {
				FileUtils.fc.setCurrentDirectory(FileUtils.lastDirectory);
			} else {
				FileUtils.lastDirectory = FileUtils.fc.getCurrentDirectory();
			}
			boolean bSave = true;

			while (true) {
				String arch = FileUtils.lastDirectory.getPath();
				FileUtils.fc.setSelectedFile(new File(arch));
				iOption = FileUtils.fc.showSaveDialog(f);
				selectedFile = FileUtils.fc.getSelectedFile();
				selectedFile = FileUtils.ensureFileExtension(selectedFile, "jpg");
				FileUtils.lastDirectory = FileUtils.fc.getCurrentDirectory();
				if (iOption != JFileChooser.APPROVE_OPTION) {
					int resp = MessageDialog.showMessage(f, "fileutils.cancel_file_saving_operation", null, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, recursos);
					if (resp == JOptionPane.NO_OPTION) {} else {
						bSave = false;
						break;
					}
				} else {
					if (selectedFile.exists()) {
						int resp = MessageDialog.showMessage(f, "fielddata.overwrite_existing_file", null, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, recursos);
						if (resp == JOptionPane.YES_OPTION) {
							break;
						}
					} else {
						break;
					}
				}
			}
			if ((iOption == JFileChooser.APPROVE_OPTION) && bSave) {
				BufferedImage bi = new BufferedImage(im.getWidth(FileUtils.fc), im.getHeight(FileUtils.fc), BufferedImage.TYPE_3BYTE_BGR);
				Graphics g = bi.getGraphics();
				g.drawImage(im, 0, 0, FileUtils.fc);

				out = new FileOutputStream(selectedFile);
				ImageIO.write(bi, "jpg", out);
				bi.flush();
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			FileUtils.logger.error(null, e);
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					FileUtils.logger.trace(null, e);
				}
			}
		}
	}

	public static File ensureExtension(File f, String extension) {
		return FileUtils.ensureFileExtension(f, extension);
	}

	private static File ensureFileExtension(File f, String extension) {
		if ((f == null) || (extension == null) || f.isDirectory()) {
			return f;
		} else {
			String sName = f.getName();
			int dotIndex = sName.lastIndexOf(".");
			if (dotIndex < 0) {
				return new File(f.getAbsolutePath() + "." + extension);
			} else {
				if (dotIndex == sName.length()) {
					return new File(f.getAbsolutePath() + extension);
				}
				String ext = sName.substring(dotIndex);
				if (!ext.equalsIgnoreCase(extension)) {
					return new File(sName.substring(0, dotIndex) + "." + extension);
				} else {
					return f;
				}
			}
		}
	}

	public static void saveFile(File f, byte[] bytes) throws IOException {
		FileUtils.saveFile(f, bytes, false, false);
	}

	public static void saveFile(File f, byte[] bytes, boolean readOnly) throws IOException {
		FileUtils.saveFile(f, bytes, readOnly, false);
	}

	public static void saveFile(File f, byte[] bytes, boolean readOnly, boolean append) throws IOException {
		if ((f == null) || f.isDirectory()) {
			throw new IOException("A directory can not be save");
		} else {
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(f.getCanonicalPath(), append);

				fOut.write(bytes);
				fOut.flush();
				fOut.close();
				if (readOnly) {
					f.setReadOnly();
				}
				FileUtils.logger.debug("FileUtils: Saved file '" + f + "' , size: " + (bytes.length / 1024.0) + " KB");
			} catch (IOException e) {
				FileUtils.logger.error(null, e);
				throw e;
			} finally {
				if (fOut != null) {
					fOut.close();
				}
			}
		}
	}

	public static String listFiles(File dir, String prefix) {
		if (!dir.isDirectory()) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			File[] fAll = dir.listFiles();
			for (int i = 0; i < fAll.length; i++) {
				if (!fAll[i].isDirectory()) {
					if (prefix != null) {
						sb.append(prefix);
					}
					sb.append(fAll[i].getAbsolutePath() + "\n");
				}
			}
			return sb.toString();
		}
	}

	public static File[] searchInFiles(String s, File f, java.io.FileFilter ff) throws Exception {
		if ((s == null) || (s.length() == 0) || (s.length() > 200)) {
			throw new IllegalArgumentException("search string can't be null, and length must be >0 and <=200");
		}
		if (!f.isDirectory()) {
			throw new IllegalArgumentException("file must be a directory");
		}
		File[] fFiles = f.listFiles(ff);
		StringBuilder sb = new StringBuilder();
		Vector res = new Vector();
		for (int i = 0; i < fFiles.length; i++) {
			FileReader r = new FileReader(fFiles[i]);
			BufferedReader br = new BufferedReader(r);
			int car = -1;
			boolean bFound = false;
			while ((car = br.read()) != -1) {
				sb.append((char) car);
				if (sb.length() > 2048) {
					// Leave the last 200
					sb.delete(0, sb.length() - 201);
				}
				if (sb.length() >= s.length()) {
					// Compare
					String sAux = sb.substring(sb.length() - s.length());
					if (s.equalsIgnoreCase(sAux)) {
						bFound = true;
						break;
					}
				}
			}
			br.close();
			r.close();
			if (bFound) {
				res.add(fFiles[i]);
			}
		}
		File[] arch = new File[res.size()];
		for (int i = 0; i < res.size(); i++) {
			arch[i] = (File) res.get(i);
		}
		return arch;
	}

	/**
	 * Creates into client temporal directory a folder
	 *
	 * @return a <code>File</code> reference
	 */
	public static File createTempDirectory() {
		String userDirectory = System.getProperty("java.io.tmpdir");
		File fNew = new File(userDirectory, "~" + System.currentTimeMillis());
		fNew.mkdir();
		return fNew;
	}

	/**
	 * Gets the file name that is obtained from entry parameter.
	 *
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		if (path == null) {
			return null;
		}
		int last = -1;
		if ((last = path.lastIndexOf('/')) == -1) {
			return path;
		}
		return path.substring(last);
	}

	private static char unescape(String s, int i) {
		return (char) Integer.parseInt(s.substring(i + 1, i + 3), 16);
	}

	public static String decode(String s) {
		StringBuilder StringBuilder = new StringBuilder();
		char c;
		for (int i = 0; i < s.length(); StringBuilder.append(c)) {
			c = s.charAt(i);
			if (c != '%') {
				i++;
				continue;
			}
			try {
				c = FileUtils.unescape(s, i);
				i += 3;
				if ((c & 128) == 0) {
					continue;
				}
				switch (c >> 4) {
				case 12: // '\f'
				case 13: // '\r'
					char c1 = FileUtils.unescape(s, i);
					i += 3;
					c = (char) (((c & 31) << 6) | (c1 & 63));
					break;

				case 14: // '\016'
					char c2 = FileUtils.unescape(s, i);
					i += 3;
					char c3 = FileUtils.unescape(s, i);
					i += 3;
					c = (char) (((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
					break;

				default:
					throw new IllegalArgumentException();
				}
			} catch (NumberFormatException numberformatexception) {
				throw new IllegalArgumentException(numberformatexception);
			}
		}

		return StringBuilder.toString();
	}

	/**
	 * Reads all the data from the input stream, and returns the bytes read.
	 */
	public static byte[] toByteArray(InputStream stream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buffer = new byte[4096];
		int read = 0;
		while (read != -1) {
			read = stream.read(buffer);
			if (read > 0) {
				baos.write(buffer, 0, read);
			}
		}

		return baos.toByteArray();
	}

}