package com.ontimize.util.templates;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.finders.ClassFinder;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.vml.CTShape;
import org.docx4j.vml.CTTextbox;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTTxbxContent;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.Pict;
import org.docx4j.wml.R;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontimize.db.EntityResult;
import com.ontimize.gui.table.Table;
import com.ontimize.util.FileUtils;
import com.ontimize.util.remote.BytesBlock;
import com.ontimize.windows.office.WindowsUtils;

public class DocxTemplateGenerator extends AbstractTemplateGenerator {

	private static final Logger logger = LoggerFactory.getLogger(DocxTemplateGenerator.class);

	private static final String HIDE = "HIDE";
	public static final String OPEN_PLACEHOLDER = "${";
	public static final String CLOSE_PLACEHOLDER = "}";
	protected boolean showTemplate = false;
	private static org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();

	/**
	 * Populate the templates with the information given in the {@link Hashtable}s.
	 *
	 * @param input
	 *            : An {@link InputStream} from the original template, which will remain. unaltered
	 * @param nameFile
	 *            : The name of the new *.docx document, with extension included.
	 * @param fieldValues
	 *            : A {@link Hashtable} with the data used in the fields of the form. As key, the attribute to replace and as value, their replace.
	 * @param valuesTable
	 *            : A {@link Hashtable} with the data used in the tables of the form. As key, the entity name of the table, as value, the {@link EntityResult} will all table data.
	 * @param valuesImages
	 *            : A {@link Hashtable} with the images in the form. As key, the attribute to replace and as value, their image replace (like a {@link BytesBlock} or a
	 *            {@link BufferedImage})
	 * @param valuesPivotTable
	 *            : A {@link Hashtable} of pivots table (NOT USED)
	 * @throws Exception
	 */
	@Override
	public File fillDocument(InputStream input, String nameFile, Hashtable fieldValues, Hashtable valuesTable, Hashtable valuesImages, Hashtable valuesPivotTable)
			throws Exception {
		fieldValues = this.translateDotFields(fieldValues);
		fieldValues = this.translateToPatternFields(fieldValues);
		valuesImages = this.translateDotFields(valuesImages);
		valuesImages = this.translateToPatternFields(valuesImages);
		File templateFilled = new File(System.getProperty("java.io.tmpdir"), nameFile);
		templateFilled.deleteOnExit();
		FileUtils.copyFile(input, templateFilled);

		this.findAndReplaceDocument(templateFilled, fieldValues, valuesTable, valuesImages, valuesPivotTable);

		if (this.showTemplate) {
			WindowsUtils.openFile_Script(templateFilled);
		}

		return templateFilled;
	}

	/**
	 * Find and replace the placeholders of the template with the data given in the {@link Hashtable}
	 *
	 * @param templateFilled
	 *            : The {@link File} of template to fill.
	 * @param fieldValues
	 *            : A {@link Hashtable} with the data used in the fields of the form. As key, the placeholder to replace and as value, their replace.
	 * @param valuesTable
	 *            : A {@link Hashtable} with the data used in the tables of the form. As key, the entity name of the table, as value, the {@link EntityResult} will all table data.
	 * @param valuesImages
	 *            : A {@link Hashtable} with the images in the form. As key, the placeholder to replace and as value, their image replace (like a {@link BytesBlock} or a
	 *            {@link BufferedImage})
	 * @param valuesPivotTable
	 *            : A {@link Hashtable} of pivots table (NOT USED)
	 * @throws Exception
	 */
	protected void findAndReplaceDocument(File templateFilled, Hashtable fieldValues, Hashtable valuesTable, Hashtable valuesImages, Hashtable valuesPivotTable) throws Exception {
		WordprocessingMLPackage mlp = WordprocessingMLPackage.load(templateFilled);
		this.findAndReplaceMainTables(mlp, valuesTable);
		this.findAndReplaceMainFields(mlp, fieldValues);
		this.findAndReplaceMainImages(mlp, valuesImages);
		this.findAndReplaceTextBox(mlp, valuesTable, valuesImages, fieldValues);
		mlp.save(templateFilled);
	}

	/**
	 * Find and replace the main tables in template which have reference an entity.
	 *
	 * @param mlp
	 *            : The {@link WordprocessingMLPackage} obtained from load the template file.
	 * @param valuesTable
	 *            : A {@link Hashtable} with the data used in the tables of the form. As key, the entity name of the table, as value, the {@link EntityResult} with all table data.
	 */
	protected void findAndReplaceMainTables(WordprocessingMLPackage mlp, Hashtable valuesTable) {

		if (!valuesTable.isEmpty()) {
			Enumeration tableEntitiesKey = valuesTable.keys();
			while (tableEntitiesKey.hasMoreElements()) {
				Object oActualTableEntity = tableEntitiesKey.nextElement();

				// Search for template tables
				this.findAndReplaceTables(mlp.getMainDocumentPart(), valuesTable, oActualTableEntity);

				RelationshipsPart rp = mlp.getMainDocumentPart().getRelationshipsPart();
				for (Relationship r : rp.getRelationships().getRelationship()) {
					if (r.getType().equals(Namespaces.HEADER) || r.getType().equals(Namespaces.FOOTER)) {
						Part part = rp.getPart(r);
						this.findAndReplaceTables(part, valuesTable, oActualTableEntity);
					}
				}
			}
		}
	}

	/**
	 * Find and replace the tables in template which have reference an entity.
	 *
	 * @param element
	 *            : The element witch has a table content
	 * @param valuesTable
	 *            : A {@link Hashtable} with the data used in the tables of the form. As key, the entity name of the table, as value, the {@link EntityResult} will all table data.
	 * @param oActualTableEntity
	 *            : Name of the actual table
	 * @param sActualTableEntity
	 *            : Name of the actual table as placeholder
	 */
	protected void findAndReplaceTables(Object element, Hashtable valuesTable, Object oActualTableEntity) {

		String sActualTableEntity = this.entityAsPlaceholder(oActualTableEntity.toString());

		List<Tbl> tables = this.getElements(element, Tbl.class);
		for (Tbl entityTables : tables) {
			// Search for table rows
			List<Tr> rows = this.getElements(entityTables, Tr.class);
			for (Tr row : rows) {
				List<Tc> cols = this.getElements(row, Tc.class);
				for (Tc col : cols) {
					// Search for row cells
					List<Text> texts = this.getElements(col, Text.class);
					for (Text oText : texts) {
						// Search inside cells for entity
						// placeholder
						int indexB = oText.getValue().indexOf(sActualTableEntity);
						if (indexB > -1) {
							int indexRowEntityPlaceholder = rows.indexOf(row);
							entityTables.getContent().remove(row);
							Object actualTableValues = valuesTable.get(oActualTableEntity);
							// Populate table
							if (actualTableValues instanceof EntityResult) {
								EntityResult actualValues = (EntityResult) actualTableValues;
								this.populateTbl(entityTables, actualValues, indexRowEntityPlaceholder);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Populate the table which references a {@link Table} in the Ontimize form.
	 *
	 * @param entityTables
	 *            : The {@link Tbl} WITHOUT the row that indicates its entity.
	 * @param actualValues
	 *            : The {@link EntityResult} of table data.
	 * @param indexRowEntityPlaceholder
	 *            : The index of row entity placeholder BEFORE its removal
	 * @param mlp
	 *            : The {@link WordprocessingMLPackage} obtained from load the template file.
	 */
	protected void populateTbl(Tbl entityTables, EntityResult actualValues, int indexRowEntityPlaceholder) {
		Vector<String> placeholders = new Vector<String>();
		// Vector<String> cellTexts = new Vector<String>();
		List persistentRows = new ArrayList();
		// Search for table rows
		List<Tr> rows = this.getElements(entityTables, Tr.class);
		for (Tr row : rows) {
			if (rows.indexOf(row) >= indexRowEntityPlaceholder) {
				persistentRows.add(row);
				entityTables.getContent().remove(row);
			}
		}

		// Populate the table
		int records = actualValues.calculateRecordNumber();
		this.translateToPatternFields(actualValues);
		for (int i = 0; i < records; i++) {
			Hashtable actualRecord = actualValues.getRecordValues(i);
			for (Object persistentRow : persistentRows) {
				Tr recordRow = (Tr) XmlUtils.deepCopy(persistentRow);
				List<Tc> cells = this.getElements(recordRow, Tc.class);
				for (Tc cell : cells) {
					List<Text> cellTexts = this.getElements(cell, Text.class);
					for (Text oText : cellTexts) {
						int indexB = oText.getValue().indexOf(DocxTemplateGenerator.OPEN_PLACEHOLDER);
						if (indexB > -1) {
							String placeholder = oText.getValue().substring(indexB, oText.getValue().indexOf(DocxTemplateGenerator.CLOSE_PLACEHOLDER) + 1);
							try {
								oText.setValue(oText.getValue().replace(placeholder, actualRecord.get(placeholder).toString()));
							} catch (Exception e) {
								DocxTemplateGenerator.logger.debug("Error obtaining the value for the placeholder: {}", placeholder, e);
							}
						}
					}
				}
				entityTables.getContent().add(recordRow);
			}
		}
	}

	/**
	 * Return the entity name of a table as placeholder
	 *
	 * @param entity
	 *            : The entity name {@link String}
	 * @return The entity name placeholder.
	 */
	protected String entityAsPlaceholder(String entity) {
		StringBuilder toRet = new StringBuilder();
		toRet.append(DocxTemplateGenerator.OPEN_PLACEHOLDER);
		toRet.append(entity.toUpperCase());
		toRet.append(DocxTemplateGenerator.CLOSE_PLACEHOLDER);
		return toRet.toString();
	}

	/**
	 * Find and replace the placeholder of main images in template.
	 *
	 * @param mlp
	 *            : The {@link WordprocessingMLPackage} obtained from load the template file.
	 * @param valuesTable
	 *            : A {@link Hashtable} with the images in the form. As key, the placeholder to replace and as value, their image replace (like a {@link BytesBlock} or a
	 *            {@link BufferedImage})
	 *
	 * @throws Exception
	 */
	protected void findAndReplaceMainImages(WordprocessingMLPackage mlp, Hashtable valuesImages) throws Exception {

		if (!valuesImages.isEmpty()) {

			this.findAndReplaceImages(mlp.getMainDocumentPart(), valuesImages, mlp);

			String[] keyIndex = (String[]) valuesImages.keySet().toArray(new String[0]);
			ArrayList<ArrayList> replaceImages = new ArrayList<ArrayList>();

			RelationshipsPart rp = mlp.getMainDocumentPart().getRelationshipsPart();
			for (Relationship r : rp.getRelationships().getRelationship()) {
				if (r.getType().equals(Namespaces.HEADER) || r.getType().equals(Namespaces.FOOTER)) {
					Part part = rp.getPart(r);
					List<Tbl> partTables = this.getElements(part, Tbl.class);
					for (Tbl imgTables : partTables) {
						// Search for table rows
						List<Tr> rows = this.getElements(imgTables, Tr.class);
						for (Tr row : rows) {
							// Search for row cells
							List<Tc> cols = this.getElements(row, Tc.class);
							for (Tc col : cols) {
								List<Text> texts = this.getElements(col, Text.class);
								for (Text oText : texts) {
									// Search inside cells for image
									// placeholder
									int indexB = StringUtils.indexOfAny(oText.getValue(), keyIndex);
									if (indexB > -1) {
										String tString = oText.getValue().substring(indexB, oText.getValue().indexOf(DocxTemplateGenerator.CLOSE_PLACEHOLDER) + 1);
										Object oValue = valuesImages.get(tString);

										ArrayList params = new ArrayList();
										params.add(col);
										params.add(oValue);
										params.add(tString);
										params.add(part);
										replaceImages.add(params);

									}
								}

							}
						}
					}
				}

			}

			for (ArrayList l : replaceImages) {
				Tc col = (Tc) l.get(0);
				Object oValue = l.get(1);
				String tString = l.get(2).toString();
				Part part = (Part) l.get(3);
				byte[] imageBytes = null;

				if (oValue instanceof BytesBlock) {
					imageBytes = ((BytesBlock) oValue).getBytes();
				} else if (oValue instanceof BufferedImage) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write((RenderedImage) oValue, "png", baos);
					baos.flush();
					imageBytes = baos.toByteArray();
				}

				List<R> rs = this.getElements(col, R.class);
				for (R o : rs) {
					R run = o;
					if (imageBytes != null) {
						R newRun = this.createImageRun(mlp, imageBytes, tString, part, col.getTcPr().getTcW().getW().longValue());
						P p = (P) run.getParent();
						p.getContent().remove(run);
						p.getContent().add(newRun);

					}
				}
			}
		}
	}

	protected void findAndReplaceImages(Object element, Hashtable valuesImages, WordprocessingMLPackage mlp) throws Exception {

		String[] keyIndex = (String[]) valuesImages.keySet().toArray(new String[0]);

		// Search for template tables
		List<Tbl> tables = this.getElements(element, Tbl.class);
		for (Tbl imgTables : tables) {
			// Search for table rows
			List<Tr> rows = this.getElements(imgTables, Tr.class);
			for (Tr row : rows) {
				// Search for row cells
				List<Tc> cols = this.getElements(row, Tc.class);
				for (Tc col : cols) {
					List<Text> texts = this.getElements(col, Text.class);
					for (Text oText : texts) {
						// Search inside cells
						// for image placeholder
						int indexB = StringUtils.indexOfAny(oText.getValue(), keyIndex);
						if (indexB > -1) {
							String tString = oText.getValue().substring(indexB, oText.getValue().indexOf(DocxTemplateGenerator.CLOSE_PLACEHOLDER) + 1);
							Object oValue = valuesImages.get(tString);

							if (oValue instanceof BytesBlock) {
								P paragraphWithImage = this.paragraphContentImage(this.inlineImage(((BytesBlock) oValue).getBytes(), mlp, tString));
								col.getContent().remove(0);
								col.getContent().add(paragraphWithImage);
							}

							if (oValue instanceof BufferedImage) {
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								ImageIO.write((RenderedImage) oValue, "png", baos);
								baos.flush();
								P paragraphWithImage = this.paragraphContentImage(this.inlineImage(baos.toByteArray(), mlp, tString));
								col.getContent().remove(0);
								col.getContent().add(paragraphWithImage);

							}
						}
					}
				}
			}
		}

	}

	/**
	 * Create a {@link P} with the {@link Inline} image received by parameter.
	 *
	 * @param createInlineImage
	 *            : An {@link Inline} image to be sorrounded with a {@link P}
	 * @return A {@link P} paragraph with the image
	 */
	protected P paragraphContentImage(Inline createInlineImage) {
		ObjectFactory factory = new ObjectFactory();
		P p = factory.createP();
		R r = factory.createR();
		p.getContent().add(r);
		Drawing drawing = factory.createDrawing();
		r.getContent().add(drawing);
		drawing.getAnchorOrInline().add(createInlineImage);
		return p;
	}

	public R createImageRun(WordprocessingMLPackage wordMLPackage, byte[] imageBytes, String tString, Part part, long imageWidth) throws Exception {
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage.getPackage(), part, imageBytes);
		Inline inline = imagePart.createImageInline(null, tString, 1, 2, imageWidth, false);
		org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
		org.docx4j.wml.R run = factory.createR();
		org.docx4j.wml.Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		return run;
	}

	/**
	 * Creates an {@link Inline} image.
	 *
	 * @param ba
	 *            : A bytes array from the image
	 * @param mlp
	 *            : A {@link WordprocessingMLPackage} from load a template {@link File}
	 * @param filenameHint
	 *            : A {@link String} with the image hint, for example, the original name
	 * @return An {@link Inline} object built with the bytes array of an image
	 * @throws Exception
	 */
	protected Inline inlineImage(byte[] ba, WordprocessingMLPackage mlp, String filenameHint) throws Exception {

		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(mlp, ba);

		int docPrId = 1;
		int cNvPrId = 2;

		return imagePart.createImageInline(filenameHint, "Image", docPrId, cNvPrId, false);
	}

	/**
	 * Get a {@link List} of elements of the class specified by classFinder, inside the obj.
	 *
	 * @param obj
	 *            : The source object.
	 * @param classFinder
	 *            : The {@link Class} of the object to search.
	 * @return A {@link List} with the objects of type classFinder inside obj
	 */
	public <T> List<T> getElements(Object obj, Class<T> classFinder) {
		List<T> result = new ArrayList<T>();
		if (obj instanceof JAXBElement) {
			obj = ((JAXBElement<?>) obj).getValue();
		}

		if (obj.getClass().equals(classFinder)) {
			result.add((T) obj);
		} else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(this.getElements(child, classFinder));
			}
		}

		return result;
	}

	/**
	 * Find and replace the placeholder of main fields in template.
	 *
	 * @param doc
	 *            : The element with has fields placeholders.
	 * @param fieldValues
	 *            : A {@link Hashtable} with the images in the form. As key, the placeholder to replace and as value, their replace.
	 */
	public void findAndReplaceMainFields(WordprocessingMLPackage doc, Hashtable fieldValues) {
		if (!fieldValues.isEmpty()) {

			this.findAndReplaceFields(doc.getMainDocumentPart(), fieldValues);

			RelationshipsPart rp = doc.getMainDocumentPart().getRelationshipsPart();
			for (Relationship r : rp.getRelationships().getRelationship()) {
				if (r.getType().equals(Namespaces.HEADER) || r.getType().equals(Namespaces.FOOTER)) {
					Part part = rp.getPart(r);
					this.findAndReplaceFields(part, fieldValues);
				}
			}
		}
	}

	/**
	 * Find and replace the placeholder of fields in template.
	 *
	 * @param doc
	 *            : The {@link WordprocessingMLPackage} obtained from load the template file.
	 * @param fieldValues
	 *            : A {@link Hashtable} with the images in the form. As key, the placeholder to replace and as value, their replace.
	 */
	protected void findAndReplaceFields(Object element, Hashtable fieldValues) {
		String[] keyIndex = (String[]) fieldValues.keySet().toArray(new String[0]);

		List<Text> texts = this.getElements(element, Text.class);
		for (Text text : texts) {
			String tString;
			while (StringUtils.indexOfAny(text.getValue(), keyIndex) > -1) {
				int indexB = StringUtils.indexOfAny(text.getValue(), keyIndex);
				if (indexB > -1) {
					tString = text.getValue().substring(indexB, text.getValue().indexOf(DocxTemplateGenerator.CLOSE_PLACEHOLDER) + 1);
					try {
						String sValue = fieldValues.get(tString).toString();
						text.setValue(text.getValue().replace(tString, sValue));
					} catch (Exception e) {
						DocxTemplateGenerator.logger.debug("Error obtaining the value for the placeholder: {}", tString, e);
					}

				}
			}
		}
	}

	/**
	 * Find and replace the placeholder of an field in template in a textBox.
	 *
	 * @param doc
	 *            : The {@link WordprocessingMLPackage} obtained from load the template file.
	 * @param fieldValues
	 *            : A {@link Hashtable} with the fieldValues in the form. As key, the placeholder to replace and as value, their replace.
	 * @param fieldValues2
	 * @param valuesImages
	 * @throws Exception
	 */
	protected void findAndReplaceTextBox(WordprocessingMLPackage doc, Hashtable valuesTable, Hashtable valuesImages, Hashtable fieldValues) throws Exception {

		/* Check if field values is empty */
		if (!fieldValues.isEmpty()) {
			String[] keyIndex = (String[]) fieldValues.keySet().toArray(new String[0]);

			List<Pict> pict = this.getElements(doc.getMainDocumentPart(), Pict.class);
			for (Pict p : pict) {
				List<Object> pictContent = p.getAnyAndAny();
				for (Object o : pictContent) {
					List<CTShape> filterCTShape = this.getElements(o, CTShape.class);
					if (!filterCTShape.isEmpty()) {
						for (CTShape ctShape : filterCTShape) {
							List<JAXBElement<?>> ctShapeContent = ctShape.getEGShapeElements();
							for (Object oJaxbActual : ctShapeContent) {
								List<CTTextbox> ctTextBox = this.getElements(oJaxbActual, org.docx4j.vml.CTTextbox.class);
								for (CTTextbox cttextbox : ctTextBox) {
									CTTxbxContent textboxContent = cttextbox.getTxbxContent();
									if (!fieldValues.isEmpty()) {
										this.findAndReplaceFields(textboxContent, fieldValues);
									}

									if (!valuesTable.isEmpty()) {
										Enumeration tableEntitiesKey = valuesTable.keys();
										while (tableEntitiesKey.hasMoreElements()) {
											Object oActualTableEntity = tableEntitiesKey.nextElement();
											this.findAndReplaceTables(textboxContent, valuesTable, oActualTableEntity);
										}
									}

									if (!valuesImages.isEmpty()) {
										this.findAndReplaceImages(textboxContent, valuesImages, doc);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Set if the template will be open after its creation
	 *
	 * @param show
	 *            Boolean that indicates if the template will be open after its creation or not.
	 */
	@Override
	public void setShowTemplate(boolean show) {
		this.showTemplate = show;

	}

	/**
	 * The {@link DocxTemplateGenerator} adds the possibility to put the fields in the template in a specified order.<br>
	 * The parameter <code>fieldValues</code> can be a {@link EntityResult} object, and use the {@link EntityResult#setColumnOrder(List)} method to specified the order of the
	 * fields in the template.<br>
	 *
	 * @return The template file.
	 */
	@Override
	public File createTemplate(Hashtable fieldValues, Hashtable valuesTable, Hashtable valuesImages) throws Exception {
		try {
			fieldValues = this.translateDotFields(fieldValues);
			valuesImages = this.translateDotFields(valuesImages);

			if (valuesImages != null) {
				if (fieldValues instanceof EntityResult) {
					List orderColumns = ((EntityResult) fieldValues).getOrderColumns();
					if ((orderColumns != null) && (orderColumns.size() > 0)) {
						Enumeration imageKeys = valuesImages.keys();
						while (imageKeys.hasMoreElements()) {
							Object key = imageKeys.nextElement();
							orderColumns.add(key);
							fieldValues.put(key, valuesImages.get(key));
						}
						((EntityResult) fieldValues).setColumnOrder(orderColumns);
					}
				} else {
					fieldValues.putAll(valuesImages);
				}
			}
		} catch (Exception e) {
			DocxTemplateGenerator.logger.error(null, e);
		}

		String userDirectory = System.getProperty("java.io.tmpdir");
		File template = new File(userDirectory, FileUtils.getFileName("~template_" + System.currentTimeMillis() + ".docx"));
		this.generateDocxTemplate(template, fieldValues, valuesTable, valuesImages);
		if (this.showTemplate) {
			WindowsUtils.openFile_Script(template);
		}
		template.deleteOnExit();
		return template;

	}

	@Override
	public List queryTemplateFields(File template) throws Exception {

		List toRet = new ArrayList<String>();
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(template);
		MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();

		ClassFinder finder = new ClassFinder(P.class);
		TraversalUtil tUtil = new TraversalUtil(mdp.getContent(), finder);

		Pattern p = Pattern.compile("\\$\\{([^}]++)\\}");

		for (Object obj : finder.results) {
			Matcher m = p.matcher(obj.toString());
			while (m.find()) {
				toRet.add(m.group());
			}
		}

		return toRet;
	}

	@Override
	public List queryTemplateFields(String template) throws Exception {

		File fileTemplate = new File(template);
		return this.queryTemplateFields(fileTemplate);
	}

	/**
	 * Change dot character "." on keys with "ç" character
	 *
	 * @param values
	 *            : Original Hashtable
	 * @return A Hashtable with the key field translated
	 */
	protected Hashtable translateDotFields(Hashtable values) {
		try {
			Hashtable translations = new Hashtable();
			if (values == null) {
				return translations;
			}
			Iterator valuesit = values.keySet().iterator();
			while (valuesit.hasNext()) {
				Object o = valuesit.next();
				if ((o != null) && (o instanceof String)) {
					String str = o.toString();
					if (str.indexOf(".") > -1) {
						String stralt = str.replaceAll("\\.", "ç");
						translations.put(str, stralt);
					}
				}
			}
			Iterator transit = translations.keySet().iterator();
			while (transit.hasNext()) {
				String val = transit.next().toString();
				values.put(translations.get(val), values.remove(val));
			}
			if (values instanceof EntityResult) {
				((EntityResult) values).setColumnOrder(new Vector(translations.values()));
			}
		} catch (Exception e) {
			DocxTemplateGenerator.logger.error(null, e);
		}
		return values;
	}

	/**
	 * Change the form attributes keys for placeholder keys pattern
	 *
	 * @param values
	 * @return A Hashtable with the key field translated
	 */
	public Hashtable translateToPatternFields(Hashtable values) {
		try {
			Hashtable translations = new Hashtable();
			if (values == null) {
				return translations;
			}
			Iterator valuesit = values.keySet().iterator();
			while (valuesit.hasNext()) {
				Object o = valuesit.next();

				if ((o != null) && (o instanceof String)) {
					if (!((o.toString().indexOf("${") > -1) && (o.toString().lastIndexOf("}") > -1))) {

						String str = o.toString();
						StringBuilder buffer = new StringBuilder();
						buffer.append(DocxTemplateGenerator.OPEN_PLACEHOLDER + str.toUpperCase() + DocxTemplateGenerator.CLOSE_PLACEHOLDER);
						translations.put(str, buffer.toString());
					}
				}
			}
			Iterator transit = translations.keySet().iterator();
			while (transit.hasNext()) {
				String val = transit.next().toString();
				values.put(translations.get(val), values.remove(val));
			}
			if (values instanceof EntityResult) {
				((EntityResult) values).setColumnOrder(new Vector(translations.values()));
			}
		} catch (Exception e) {
			DocxTemplateGenerator.logger.error(null, e);
		}
		return values;
	}

	/**
	 * Create a *.docx document template
	 *
	 * @param template
	 *            The file template
	 * @param fieldValues
	 *            This object contains the data fields attributes and labels to show in the template
	 * @param valuesTable
	 *            The object contains the table information to insert in the template. This map must have the table entity name as key and the value must be other Hashtable with
	 *            the columns attributes and names to show (column name - column label)
	 * @param valuesImages
	 *            The object contains information about the image fields which owns the form. This map contains the name of the image field (value) and its attribute (key)
	 * @throws Exception
	 */
	protected void generateDocxTemplate(File template, Hashtable fieldValues, Hashtable valuesTable, Hashtable valuesImages) throws Exception {

		WordprocessingMLPackage docxDocument = WordprocessingMLPackage.createPackage();
		MainDocumentPart mainDocument = docxDocument.getMainDocumentPart();

		Enumeration imageKeys = valuesImages.keys();
		while (imageKeys.hasMoreElements()) {
			Object actualKey = imageKeys.nextElement();
			fieldValues.remove(actualKey);
		}

		Vector<String> paragraphs = this.createFieldString(fieldValues);
		for (String s : paragraphs) {
			mainDocument.addParagraphOfText(s);
		}

		Vector<Tbl> images = this.createImagesField(valuesImages, mainDocument);
		for (Tbl i : images) {
			mainDocument.addParagraphOfText("");
			mainDocument.addObject(i);

		}

		Vector<Tbl> tables = this.createTablesObject(valuesTable, mainDocument);
		for (Tbl t : tables) {
			mainDocument.addParagraphOfText("");
			this.addBorders(t);
			mainDocument.addObject(t);
		}

		mainDocument.addParagraphOfText("");

		docxDocument.save(template);
		template.deleteOnExit();

	}

	/**
	 * Return a {@link Vector} of {@link Tbl} to store the image placeholder
	 *
	 * @param valuesImages
	 *            : A {@link Hashtable} with the images in the form. As key, the placeholder to replace and as value, their image replace (like a {@link BytesBlock} or a
	 *            {@link BufferedImage})
	 * @param mainDocument
	 *            : A {@link MainDocumentPart} of the template file.
	 * @return a {@link Vector} of {@link Tbl} which contents the placeholder of the image.
	 */
	protected Vector<Tbl> createImagesField(Hashtable valuesImages, MainDocumentPart mainDocument) {
		Vector<Tbl> toRet = new Vector<Tbl>();

		Enumeration keys = valuesImages.keys();

		while (keys.hasMoreElements()) {
			Tbl table = DocxTemplateGenerator.factory.createTbl();
			Tr tableRowContent = DocxTemplateGenerator.factory.createTr();

			Object actualHeaderKey = keys.nextElement();
			Object actualFieldValue = valuesImages.get(actualHeaderKey);

			this.addTableCell(tableRowContent, DocxTemplateGenerator.OPEN_PLACEHOLDER + actualHeaderKey.toString().toUpperCase() + DocxTemplateGenerator.CLOSE_PLACEHOLDER,
					mainDocument);

			table.getContent().add(tableRowContent);

			toRet.add(table);
		}
		return toRet;
	}

	/**
	 * Build a Vector of single{@link String} with all of the fields data in the Hahstable <code>fieldHashtable</code>. This map stored the label of the field as <code>value</code>
	 * and the attribute of the field as <code>key</code>
	 *
	 * @param fieldHashtable
	 *            : The field value data
	 * @return A Vector of {@link String} with all of the field data stored in the <code>fieldHashtable</code>
	 */
	protected Vector<String> createFieldString(Hashtable fieldHashtable) {
		Vector<String> toRet = new Vector<String>();

		Enumeration keys = fieldHashtable.keys();
		while (keys.hasMoreElements()) {
			Object objKey = keys.nextElement();
			Object objValue = fieldHashtable.get(objKey);

			StringBuilder buffer = new StringBuilder();
			buffer.append(objValue.toString());
			buffer.append(": ");
			buffer.append(DocxTemplateGenerator.OPEN_PLACEHOLDER + objKey.toString().toUpperCase() + DocxTemplateGenerator.CLOSE_PLACEHOLDER);

			toRet.add(buffer.toString());
		}
		return toRet;
	}

	/**
	 * Create a single table with two rows. A header row, that has the name of the columns, and a body row, with the attribute of columns
	 *
	 * @param tableObjectKey
	 *
	 * @param valuesSingleTable
	 *            : A hashtable which has the name of the columns ad its respective attributes.
	 * @param mainDocument
	 *            : The {@link MainDocumentPart} of the template, where the table will be in.
	 * @return A single table with the built with the <code>valuesSingleTable</code> data
	 */
	protected Tbl createSingleTable(String tableObjectKey, Hashtable valuesSingleTable, MainDocumentPart mainDocument) {

		Tbl table = DocxTemplateGenerator.factory.createTbl();
		Tr tableRowEntity = DocxTemplateGenerator.factory.createTr();
		Tr tableRowHeader = DocxTemplateGenerator.factory.createTr();
		Tr tableRowContent = DocxTemplateGenerator.factory.createTr();

		Enumeration keys = valuesSingleTable.keys();
		boolean entity = true;
		while (keys.hasMoreElements()) {
			Object actualHeaderKey = keys.nextElement();
			Object actualFieldValue = valuesSingleTable.get(actualHeaderKey);

			if (entity) {
				this.addTableCell(tableRowEntity, DocxTemplateGenerator.OPEN_PLACEHOLDER + tableObjectKey.toUpperCase() + DocxTemplateGenerator.CLOSE_PLACEHOLDER, mainDocument);
			} else {
				this.addTableCell(tableRowEntity, "", mainDocument);
			}
			this.addTableCell(tableRowHeader, actualFieldValue.toString(), mainDocument);
			this.addTableCell(tableRowContent, DocxTemplateGenerator.OPEN_PLACEHOLDER + actualHeaderKey.toString().toUpperCase() + DocxTemplateGenerator.CLOSE_PLACEHOLDER,
					mainDocument);
			entity = false;
		}

		table.getContent().add(tableRowHeader);
		table.getContent().add(tableRowEntity);
		table.getContent().add(tableRowContent);

		return table;
	}

	/**
	 * Create a Vector of single {@link Tbl} table, which the data stored in <code>valuesTable</code>. See {@link #createSingleTable(Hashtable, MainDocumentPart)}
	 *
	 * @param valuesTable
	 *            : The object contains the table information to insert in the template. This map must have the table entity name as key and the value must be other Hashtable with
	 *            the columns attributes and names to show (column name - column label)
	 * @param mainDocument
	 *            : The {@link MainDocumentPart} of the template, where the table will be in.
	 * @return A Vector of {@link Tbl} tables.
	 */
	protected Vector<Tbl> createTablesObject(Hashtable valuesTable, MainDocumentPart mainDocument) {
		Vector<Tbl> vectorTables = new Vector<Tbl>();
		Enumeration keys = valuesTable.keys();
		while (keys.hasMoreElements()) {
			Object tableObjectKey = keys.nextElement();
			Hashtable valuesTableObjectKey = (Hashtable) valuesTable.get(tableObjectKey);
			Tbl tableForActualTableKey = this.createSingleTable(tableObjectKey.toString(), valuesTableObjectKey, mainDocument);
			vectorTables.add(tableForActualTableKey);
		}
		return vectorTables;
	}

	/**
	 * Add content to a cell, and then add it to the row
	 *
	 * @param tableRow
	 *            The table row where the cell will be added
	 * @param content
	 *            The content of the cell
	 * @param mainDocument
	 *            : The {@link MainDocumentPart} of the template, where the cell will be in.
	 */
	protected void addTableCell(Tr tableRow, String content, MainDocumentPart mainDocument) {
		Tc tableCell = DocxTemplateGenerator.factory.createTc();
		tableCell.getContent().add(mainDocument.createParagraphOfText(content));
		tableRow.getContent().add(tableCell);
	}

	/**
	 * Add single borders to the table.
	 *
	 * @param table
	 *            : The source table
	 */
	protected void addBorders(Tbl table) {
		table.setTblPr(new TblPr());
		CTBorder border = new CTBorder();
		border.setColor("auto");
		border.setSz(new BigInteger("4"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);

		TblBorders borders = new TblBorders();
		borders.setBottom(border);
		borders.setLeft(border);
		borders.setRight(border);
		borders.setTop(border);
		borders.setInsideH(border);
		borders.setInsideV(border);
		table.getTblPr().setTblBorders(borders);
	}
}
