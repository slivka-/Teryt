import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * Counts occurences of given street name in given voivodeship and county
 * @author Michał Śliwa
 */
public class PowHitsCounter
{
    //counter of occurences
    private int hitCounter = 0;
    //current WOJ code
    private String wojCode = "";
    
    /**
     * 
     * @param streetName
     * @param wojCodeOrNum
     * @param powCode
     * @return 
     */
    public int countPowXMLHits(String streetName, String wojCodeOrNum, 
            String powCode)
    {
        hitCounter = 0;
        wojCode = "";
        //check if WOJ code has to be resolved to ID
        if (CanParseInt(wojCodeOrNum))
            wojCode = wojCodeOrNum;
        else
            wojCode = new WojIdResolver().resolveId(wojCodeOrNum);
        
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
                //is in WOJ tag
                boolean inWoj = false;
                //is in POW tag
                boolean inPow = false;
                //is in entry with right WOJ tag
                boolean isWojOk = false;
                //is in entry with right POW tag
                boolean isPowOk = false;
                
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
                    //if tag name is WOJ set inWoj to true
                    if(qName.equalsIgnoreCase("WOJ"))
                        inWoj = true;
                    //if tag name is POW set inPow to true
                    if(qName.equalsIgnoreCase("POW"))
                        inPow = true;
                    //if tag name is NAZWA_1 set inNazwa to true
                    if(qName.equalsIgnoreCase("NAZWA_1"))
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
                    //if in WOJ tag, check if content matches
                    if (inWoj)
                        if(new String(ch,start,length).equals(wojCode))
                            //set isWojOk to true
                            isWojOk = true;
                    //if in WOJ tag and WOJ conten matches,
                    //check if POW content matches
                    if (isWojOk && inPow)
                        if(new String(ch,start,length).equals(powCode))
                            //set isPowOk to true
                            isPowOk = true;
                    //if in NAZWA_1 tag and WOJ tag content matches and if POW 
                    //content matches, check if content contains street name
                    if (isWojOk && isPowOk && inNazwa)
                        if(new String(ch,start,length).contains(streetName))
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
                public void endElement(String uri, String localName, 
                        String qName) throws SAXException
                {
                    //if tag name is WOJ set inWoj to false
                    if(qName.equalsIgnoreCase("WOJ"))
                        inWoj = false;
                    //if tag name is NAZWA_1 set inNazwa to false
                    if(qName.equalsIgnoreCase("NAZWA_1"))
                        inNazwa = false;
                    //if tag name is POW set inPow to false
                    if(qName.equalsIgnoreCase("POW"))
                        inPow = false;
                    //if tag name is row reset WOJ and POW checks
                    if(qName.equalsIgnoreCase("row"))
                    {
                        isWojOk = false;
                        isPowOk = false;
                    }
                }               
            };
            
            //parse ULIC xml using defined handler
            saxParser.parse (DataLoader.getULICStream(), handler);
        }
        catch(IOException | ParserConfigurationException | SAXException ex)
        {
            System.out.println(ex);
        }
        return hitCounter;
    }
    
    /**
     * Checks if String can be parsed to Integer
     * @param toParse String to parse
     * @return true if can be parsed, otherwise false
     */
    private boolean CanParseInt (String toParse)
    {
        try
        {
            //try to parse int
            Integer.parseInt(toParse);
            //return true
            return true;
        }
        catch (NumberFormatException ex)
        {
            //on exception return false
            return false;
        }
    } 
}
