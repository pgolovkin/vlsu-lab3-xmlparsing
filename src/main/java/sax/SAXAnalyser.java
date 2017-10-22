package sax;

import java.io.IOException;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXAnalyser extends JFrame {

  public void analyse() {
    try {

      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      SAXParser parser = saxParserFactory.newSAXParser();
      parser.parse(SAXAnalyser.class.getClassLoader().getResourceAsStream("children.xml"), new ChildrenHandler());
    } catch (SAXTerminationException ex) {
      ex.printStackTrace();
    } catch (SAXException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }

  }

}
