package ru.imobilco.extensions.xalan;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

public class NodeSetExtensions {
	/**
	 * Преобразует набор нодов в JavaScript-строку
	 * @param nList
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 */
	public static String toJavaScriptString(org.w3c.dom.NodeList nList) throws TransformerFactoryConfigurationError, TransformerException {
		return escapeJSON(toString(nList));
	}

	/**
	 * Экранирует все символы в строке, делая её пригодной для вставки в JS
	 * @param string
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 */
	public static String toJavaScriptString(String string) {
		return escapeJSON(string);
	}

	public static String toString(org.w3c.dom.NodeList nList) throws TransformerFactoryConfigurationError, TransformerException {
		return toString(nList, "html");
	}

	public static String toString(String string) {
		return string;
	}

	public static String toString(org.w3c.dom.NodeList nList, String method) throws TransformerFactoryConfigurationError, TransformerException {
		StringWriter sw = new StringWriter();
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.setOutputProperty("omit-xml-declaration", "yes");
		serializer.setOutputProperty("indent", "no");
		serializer.setOutputProperty("method", method);

		for (int i = 0; i < nList.getLength(); i++) {
			serializer.transform(new DOMSource(nList.item(i)), new StreamResult(sw));
		}

		return sw.toString();
	}

	public static String test() {
		return "hello";
	}

	/**
     * @param s - Must not be null.
     * @param sb
     */
	public static String escapeJSON(String s) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F')
						|| (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Преобразует каждый нод из переданного набора в JSON-структуру, где ключом
	 * является имя элемента, а значением — его содержимое
	 * @param nList
	 * @return
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 */
	public static String toJSON(org.w3c.dom.NodeList nList) throws TransformerFactoryConfigurationError, TransformerException {
		StringBuilder sb = new StringBuilder();
		Node item = null;
		for (int i = 0; i < nList.getLength(); i++) {
			item = nList.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				if (sb.length() > 0) {
					sb.append(",\n");
				}
				sb.append("\"" + item.getNodeName() + "\": ");
				sb.append("\"" + toJavaScriptString(item.getChildNodes()) + "\"");
			}
		}
		return sb.toString();
	}
}
