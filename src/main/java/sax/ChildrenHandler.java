package sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ChildrenHandler extends DefaultHandler{
	
	private StringBuilder result = new StringBuilder("");
	
	private int totalApples = 0;
	private int eatenApples = 0;
	private int totalChildrenApples = 0;
	private int totalEatenApples = 0;
	private int totalRestApples = 0;
	
	private boolean isChildrens = false;
	private boolean isChildren = false;
	private boolean isFIO = false;
	private boolean isBirthday = false;
	private boolean isTotalApples = false;
	private boolean isEatenApples = false;
	
	public ChildrenHandler() {
		super();
	}
	
	
	@Override
	public void warning(SAXParseException e) throws SAXException {
		handleException(e, "warning");
		
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		handleException(e, "error");
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		handleException(e, "fatal error");
	}

	@Override
	public void startDocument() throws SAXException {
		result.append(
				"<html><body><table><tr><td>ФИО</td><td>Дата рождения</td><td>Общее число яблок</td><td>Скушано яблок</td><td>Отсалось яблок</td></tr>"
		);			
	}

	@Override
	public void endDocument() throws SAXException {
		result.append("</body></table></html>");
		System.out.println(result.toString());	
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("children")) {
			isChildrens = true;
		} else if (qName.equals("child")) {
			result.append("<tr>");
			isChildren = true;
		} else {
			result.append("<td>");
			isBirthday = qName.equals("birthday");
			isEatenApples = qName.equals("eatenApples");
			isFIO = qName.equals("fio");
			isTotalApples = qName.equals("totalApples");			
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("children")) {
			isChildrens = false;
			result.append(
				"<tr>" +
				"<td colspan = '2'>Всего съедено яблок</td>" +
				"<td colspan = '3'>" + totalEatenApples + "</td>" +
				"</tr>" +
				"<tr>" +
				"<td colspan = '2'>Всего было яблок</td>" +
				"<td colspan = '3'>" + totalChildrenApples + "</td>" +
				"</tr>" + 
				"<tr>" + 
				"<td colspan = '2'>Всего осталось яблок</td>" + 
				"<td colspan = '3'>" + totalRestApples + "</td>" + 
				"</tr>"
			);
		} else if (qName.equals("child")) {
			totalRestApples += totalApples - eatenApples;
			result.append("<td>" + (totalApples - eatenApples) + "</td>");
			result.append("</tr>");
			isChildren = false;
		} else {
			result.append("</td>");
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String str = new String(ch, start, length);
		str = str.trim();
		if (str.length() > 0) {
			if (isFIO || isBirthday) {
				result.append(str);
			} else if (isTotalApples) {
				result.append(str);
				totalApples = Integer.parseInt(str);
				totalChildrenApples += totalApples;
			} else if (isEatenApples) {
				result.append(str);
				eatenApples = Integer.parseInt(str);
				totalEatenApples += eatenApples;
			}
		}
	}
	
	public void handleException(SAXParseException ex, String message) throws SAXTerminationException {
		System.err.println("line : " + ex.getLineNumber());
		System.err.println("column : " + ex.getColumnNumber());
		System.err.println(message);
		ex.printStackTrace();
	}	
}
