package sax;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@SuppressWarnings("serial")
public class SAXTerminationException extends SAXException {

  public SAXTerminationException() {

  }

  public SAXTerminationException(SAXParseException cause) {
    super(cause);
  }

  public SAXTerminationException(String message, SAXParseException cause) {
    super(message, cause);
  }
}
