package com.litb.search.eval.service.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class ResponseConverter {

	private static final Logger LOGGER = Logger.getLogger(ResponseConverter.class);

	public static String convertToIndexRequest(String response) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = domFactory.newDocumentBuilder();
			// InputSource is = new InputSource();
			// is.setCharacterStream(new StringReader(response));
			// Document responseDoc = builder.parse(is);
			Document responseDoc = builder.parse(new InputSource(new StringReader(response)));

			NodeList docs = responseDoc.getElementsByTagName("doc");

			Document indexDoc = builder.newDocument();
			Element rootDocument = indexDoc.createElement("add");
			for (int i = 0; i < docs.getLength(); i++) {
				indexDoc.importNode(docs.item(i), true);
				System.out.println("###" + docs.item(i).getFirstChild().getNodeName());
				// rootDocument.appendChild(docs.item(0));
			}

			return convertDocToString(indexDoc);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOGGER.error("Unable to parse the solr response: " + e.getMessage(), e);
		}
		return "";
	}

	private static String convertDocToString(Document doc) {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(domSource, result);
			writer.flush();
		} catch (TransformerException e) {
			LOGGER.error("Failed to convert doc to string: " + e.getMessage(), e);
			return null;
		}
		return writer.toString();
	}
}
