import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Counts occurences of given street name in whole country
 * @author Michał Śliwa
 */
public class CountryHitsCounter
{
     //counter of occurences
    private int hitCounter = 0;
    
    /**
     * 
     * @param streetName
     * @return 
     */
    public int countCountryXMLHits(String streetName)
    {
        hitCounter = 0;
        try
        {
            //get new SAXParser factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //get new SAXParser instance
            SAXParser saxParser = factory.newSAXParser();
            
            //handler for reading xml file
            DefaultHandler handler = new DefaultHandler()
            {
                //is in NAZWA_1 tag
                boolean inNazwa = false;    
                
                /**
                 * Overriden method, detects opening of a tag
                 * @param uri
                 * @param localName
                 * @param qName
                 * @param attributes
                 * @throws SAXException 
                 */
                @Override
                public void startElement(String uri, String localName, 
                        String qName, Attributes attributes) throws SAXException
                {
                    //if tag name is NAZWA_1 set inNazwa to true
                    if (qName.equalsIgnoreCase("NAZWA_1"))
                        inNazwa = true;
                }
                
                /**
                 * Overriden method, handles coutent of a tag
                 * @param ch
                 * @param start
                 * @param length
                 * @throws SAXException 
                 */
                @Override
                public void characters(char[] ch, int start, int length) 
                        throws SAXException
                {
                    //if in NAZWA_1 tag check if content matches 
                    if (inNazwa)
                        if (new String(ch,start,length).contains(streetName))
                           //increment hitCounter
                           hitCounter++; 
                }
                
                /**
                 * Overriden method, handles closings of tags
                 * @param uri
                 * @param localName
                 * @param qName
                 * @throws SAXException 
                 */
                @Override
                public void endElement(String uri, 
                        String localName, String qName) throws SAXException
                {
                    //if tag name is NAZWA_1 set inNazwa to false
                    if (qName.equalsIgnoreCase("NAZWA_1"))
                        inNazwa = false;
                }               
            };
            
            //parse ULIC xml using defined handler
            saxParser.parse(DataLoader.getULICStream(), handler);
        }
        catch (IOException | ParserConfigurationException | SAXException ex)
        {
            System.out.println(ex);
        }
        return hitCounter;
    }
}
