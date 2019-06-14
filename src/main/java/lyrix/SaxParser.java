package lyrix;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxParser extends DefaultHandler {

    private String absolutePathToXml = "";

    public SaxParser(File xml) {
        absolutePathToXml = xml.getAbsolutePath();
    }
    /**
     * Parses an XML file into memory
     */
    public void parseXml() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(absolutePathToXml, this);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfigurationException: ");
            e.printStackTrace();
        } catch (SAXException e) {
            System.out.println("SAXException: ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException: ");
            e.printStackTrace();
        }
    }
    /**
     * Event: Parser starts reading an element
     */
    @Override
    public void startElement(String s1, String s2, String qName, Attributes attributes)
            throws SAXException {
        //print an element's name
        System.out.println("element: " + qName);
        //print all attributes for this element
        for(int i = 0; i < attributes.getLength(); i++) {
            System.out.println("attribute: "  + attributes.getQName(i));
        }
    }

}
