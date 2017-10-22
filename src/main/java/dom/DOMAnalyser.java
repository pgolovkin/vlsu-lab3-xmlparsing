package dom;

import java.io.IOException;
import java.text.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

public class DOMAnalyser {

  public static void main(String[] args) {
    try {
      DocumentBuilderFactory documentBuilderFactory =
              DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      documentBuilderFactory.setValidating(true);
      documentBuilderFactory.setAttribute(
              "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
              "http://www.w3.org/2001/XMLSchema"
      );
      documentBuilderFactory.setAttribute(
              "http://java.sun.com/xml/jaxp/properties/schemaSource",
              DOMAnalyser.class.getClassLoader().getResourceAsStream("children.xsd")
      );
      documentBuilderFactory.setIgnoringElementContentWhitespace(true);

      DocumentBuilder documentBuilder =
              documentBuilderFactory.newDocumentBuilder();

      documentBuilder.setErrorHandler(new ErrorHandler() {

        @Override
        public void warning(SAXParseException exception) throws SAXException {
          System.out.println("Warning!");
          System.out.println("line " + exception.getLineNumber() + " column " + exception.getColumnNumber());
          System.out.println(exception.getLocalizedMessage());
          exception.printStackTrace();
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
          System.out.println("Fatal Error!");
          System.out.println("line " + exception.getLineNumber() + " column " + exception.getColumnNumber());
          System.out.println(exception.getLocalizedMessage());
          exception.printStackTrace();
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
          System.out.println("Error!");
          System.out.println("line " + exception.getLineNumber() + " column " + exception.getColumnNumber());
          System.out.println(exception.getLocalizedMessage());
          exception.printStackTrace();
        }
      });

      Document sourceDocument = documentBuilder.parse(
              DOMAnalyser.class.getClassLoader().
                      getResourceAsStream("children.xml")
      );
      Document destinationDocument = documentBuilder.newDocument();
      Node node = destinationDocument.createElement("html");
      Node body = destinationDocument.createElement("body");
      Node table = destinationDocument.createElement("table");

      destinationDocument.appendChild(node);
      node.appendChild(body);
      body.appendChild(table);

      Node rootNode = sourceDocument.getFirstChild();

      NodeList childrenList = rootNode.getChildNodes();
      for (int i = 0; i < childrenList.getLength(); i++) {
        NodeList child = childrenList.item(i).getChildNodes();
        appendTableLine(child, table, destinationDocument);
      }
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (FactoryConfigurationError fe) {
      fe.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (SAXException ex) {
      ex.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void appendTableLine(
          NodeList sourceNodes,
          Node tableNode,
          Document destDocument
  ) throws ParseException {
    Node tr = destDocument.createElement("tr");
    Node td1 = destDocument.createElement("td");
    td1.setNodeValue(sourceNodes.item(0).getNodeValue());

    Node td2 = td1.cloneNode(false);
    Node td3 = td1.cloneNode(false);
    Integer totalApples = Integer.valueOf(sourceNodes.item(2).getNodeValue());
    td3.setNodeValue(totalApples.toString());

    Node td4 = td1.cloneNode(false);
    Integer eatenApples = Integer.valueOf(sourceNodes.item(3).getNodeValue());
    td4.setNodeValue(eatenApples.toString());

    Node td5 = td1.cloneNode(false);
    td5.setNodeValue(String.valueOf(totalApples - eatenApples));

    tr.appendChild(td1);
    tr.appendChild(td2);
    tr.appendChild(td3);
    tr.appendChild(td4);
    tr.appendChild(td5);

    tableNode.appendChild(tr);
  }
}
