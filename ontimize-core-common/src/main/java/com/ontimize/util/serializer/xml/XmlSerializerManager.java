package com.ontimize.util.serializer.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ontimize.util.serializer.ISerializerManager;

public class XmlSerializerManager implements ISerializerManager {

	public static final String ROOT_ELEMENT = "filters";
	public static final String ELEMENT_FILTER = "filter";
	public static final String ELEMENT_FIELD = "field";
	public static final String ELEMENT_VALUE = "value";

	@Override
	public String serializeMapToString(Map<String, Object> data) throws Exception {
		return this.convertFilterDataToString((Hashtable) data);
	}

	@Override
	public Map<String, Object> deserializeStringToMap(String data) throws Exception {
		return this.convertFilterDataToHashtable(data);
	}

	protected String convertFilterDataToString(Hashtable data) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(XmlFilterQuery.class, XmlFilterSearchValue.class, XmlFilterBasicExpression.class, XmlFilterValueSearchValue.class);
		XmlFilterQuery toMarshall = new XmlFilterQuery();
		toMarshall.setFiltersMap(data);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		StringWriter sw = new StringWriter();
		marshaller.marshal(toMarshall, sw);
		String toRet = sw.toString();
		return toRet;
	}

	protected Hashtable convertFilterDataToHashtable(String data) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(XmlFilterQuery.class, XmlFilterSearchValue.class, XmlFilterBasicExpression.class, XmlFilterValueSearchValue.class);
		Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
		StringReader stringReader = new StringReader(data);
		XmlFilterQuery wrapper = (XmlFilterQuery) jaxbUnmarshaller.unmarshal(stringReader);
		return new Hashtable<String, Object>(wrapper.getFiltersMap());
	}

}
