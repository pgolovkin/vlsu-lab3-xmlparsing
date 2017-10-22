package stax;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public class StAXAnalyser {

  protected boolean isFatal = false;

  public void analyse() {
    try {
      XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
      xmlFactory.setXMLReporter(new XMLReporter() {

        @Override
        public void report(String message, String errorType,
                           Object relatedInformation, Location location)
                throws XMLStreamException {
          System.out.println(message + " " + errorType);
        }
      });
      XMLEventReader eventReader = xmlFactory
              .createXMLEventReader(StAXAnalyser.class.getClassLoader().getResourceAsStream("children.xml"));

      if (!isFatal) {
        StringBuilder str = new StringBuilder();
        int apples = 0;
        int eatenApples = 0;

        int totalEatenApples = 0;
        int totalApples = 0;

        while (eventReader.hasNext()) {

          XMLEvent event = eventReader.nextEvent();
          if (event.isStartDocument()) {
            str.append("<html><body><table><tr><td>ФИО</td><td>Дата рождения</td><td>Общее число яблок</td><td>Скушано яблок</td><td>Отсалось яблок</td></tr>");
          }

          if (event.isStartElement()) {
            if (("totalApples").equals(event.asStartElement().getName()
                    .getLocalPart())) {
              apples = Integer.valueOf(eventReader.nextEvent()
                      .asCharacters().getData());
              str.append("<td>").append(apples).append("</td>");
              totalApples += apples;
            }
            if (("eatenApples").equals(event.asStartElement().getName()
                    .getLocalPart())) {
              eatenApples = Integer.valueOf(eventReader.nextEvent()
                      .asCharacters().getData());
              str.append("<td>").append(eatenApples).append("</td>");
              totalEatenApples += eatenApples;
            }
          }
          if (event.isEndElement()) {
            if (("eatenApples").equals(event.asEndElement().getName()
                    .getLocalPart())) {
              str.append("<td>")
                      .append(String.valueOf(apples - eatenApples))
                      .append("</td>");
              str.append("</tr>");
            }
          }
          if (event.isEndDocument()) {
            str.append("<tr>")
                    .append("<td colspan = '3'>Всего скушано яблок </td>")
                    .append("<td>").append(totalEatenApples)
                    .append("</td></tr>");
            str.append("<tr>")
                    .append("<td colspan = '3'>Всего было яблок </td>")
                    .append("<td>").append(totalApples)
                    .append("</td></tr>");
            str.append("<tr>")
                    .append("<td colspan = '3'>Всего осталось яблок </td>")
                    .append("<td>").append(totalApples - totalEatenApples)
                    .append("</td></tr>");
            str.append("</table></body></html>");
            System.out.println(new String(str));
          }
          if (event.isCharacters()) {
            Characters characters = event.asCharacters();
            if (!characters.isWhiteSpace()) {
              str.append("<td>");
              str.append(characters.getData());
              str.append("</td>");
            }
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
