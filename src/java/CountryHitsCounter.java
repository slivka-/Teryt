import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Michał Śliwa
 */
public class CountryHitsCounter
{
    private int hitCounter = 0;
    
    public int countCountryXMLHits(String streetName)
    {
        hitCounter = 0;
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            DefaultHandler handler = new DefaultHandler()
            {
                boolean inNazwa = false;               
                @Override
                public void startElement(String uri, String localName, 
                        String qName, Attributes attributes) throws SAXException
                {
                    if (qName.equalsIgnoreCase("NAZWA_1"))
                        inNazwa = true;
                }
                @Override
                public void characters(char[] ch, int start, int length) 
                        throws SAXException
                {
                    if (inNazwa)
                        if (new String(ch,start,length).contains(streetName))
                           hitCounter++; 
                }
                @Override
                public void endElement(String uri, 
                        String localName, String qName) throws SAXException
                {
                    if (qName.equalsIgnoreCase("NAZWA_1"))
                        inNazwa = false;
                }               
            };
            
            saxParser.parse(DataLoader.getULICStream(), handler);
        }
        catch (IOException | ParserConfigurationException | SAXException ex)
        {
            System.out.println(ex);
        }
        return hitCounter;
    }
}
