package content.source.linkedin;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XMLParser {
    public static Document getDocument(String xml) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            document = documentBuilder.parse(is);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static LinkedInPerson getPerson(String xml) {
        Document document = getDocument(xml);
        NodeList list = document.getElementsByTagName("person");
        for(int i = 0; i < list.getLength(); i++) {
            Element element = (Element)list.item(i);
        }
        return null;
    }

    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n" +
                " <person>\n" +
                "<first-name>person</first-name>\n" +
                "<last-name>personalizer</last-name>\n" +
                "<headline>Учащийся - Московский Государственный Технический Университет им. Н.Э. Баумана (МГТУ)</headline>\n" +
                " </person>";
        getPerson(xml);
    }

}
