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
public class PowHitsCounter
{
    private int hitCounter = 0;
    private String wojCode = "";
    
    public int countPowXMLHits(String streetName, String wojCodeOrNum, String powCode)
    {
        hitCounter = 0;
        
        wojCode = "";
        if(CanParseInt(wojCodeOrNum))
            wojCode = wojCodeOrNum;
        else
            wojCode = "";//SEARCH FOR WOJ IN TERC
        
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            DefaultHandler handler = new DefaultHandler()
            {
                boolean inNazwa = false;
                boolean inWoj = false;
                boolean inPow = false;
                boolean isWojOk = false;
                boolean isPowOk = false;
                @Override
                public void startElement(String uri, String localName, 
                        String qName, Attributes attributes) throws SAXException
                {
                    
                    if(qName.equalsIgnoreCase("WOJ"))
                        inWoj = true;
                    if(qName.equalsIgnoreCase("POW"))
                        inPow = true;
                    if(qName.equalsIgnoreCase("NAZWA_1"))
                        inNazwa = true;
                }
                @Override
                public void characters(char[] ch, int start, int length) 
                        throws SAXException
                {
                    if(inWoj)
                        if(new String(ch,start,length).equals(wojCode))
                            isWojOk = true;
                    if(isWojOk && inPow)
                        if(new String(ch,start,length).equals(powCode))
                            isPowOk = true;
                    if(isWojOk && isPowOk && inNazwa)
                        if(new String(ch,start,length).contains(streetName))
                           hitCounter++; 
                }
                @Override
                public void endElement(String uri, String localName, 
                        String qName) throws SAXException
                {
                    if(qName.equalsIgnoreCase("WOJ"))
                        inWoj = false;
                    if(qName.equalsIgnoreCase("NAZWA_1"))
                        inNazwa = false;
                    if(qName.equalsIgnoreCase("POW"))
                        inPow = false;
                    if(qName.equalsIgnoreCase("row"))
                    {
                        isWojOk = false;
                        isPowOk = false;
                    }
                }               
            };
            
            saxParser.parse(DataLoader.getULICStream(), handler);
        }
        catch(IOException | ParserConfigurationException | SAXException ex)
        {
            System.out.println(ex);
        }
        return hitCounter;
    }
    
    private boolean CanParseInt(String toParse)
    {
        try
        {
            Integer.parseInt(toParse);
            return true;
        }
        catch(NumberFormatException ex)
        {
            return false;
        }
    } 
}
