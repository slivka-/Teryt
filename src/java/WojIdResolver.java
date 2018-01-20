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
public class WojIdResolver
{
    private String wojId = "";
    
    public String resolveId (String wojCode)
    {
        wojId = "";
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            DefaultHandler handler = new DefaultHandler()
            {
                String currentWoj = "";
                
                boolean inWoj = false;
                boolean inPow = false;
                boolean inGmi = false;
                boolean inRodz = false;
                boolean inNazwa = false; 
                
                boolean toCheck = true;
                
                @Override
                public void startElement(String uri, String localName, 
                        String qName, Attributes attributes) throws SAXException
                {
                    if (qName.equalsIgnoreCase("WOJ"))
                        inWoj = true;
                    if (qName.equalsIgnoreCase("POW"))
                        inPow = true;
                    if (qName.equalsIgnoreCase("GMI"))
                        inGmi = true;
                    if (qName.equalsIgnoreCase("RODZ"))
                        inRodz = true;
                    if (qName.equalsIgnoreCase("NAZWA"))
                        inNazwa = true;
                }
                @Override
                public void characters (char[] ch, int start, int length) 
                        throws SAXException
                {
                    if (inWoj)
                        currentWoj = new String (ch, start, length);
                    if (inPow)
                        if(length != 0)
                            toCheck = false;
                    if (inGmi)
                        if(length != 0)
                            toCheck = false;
                    if (inRodz)
                        if(length != 0)
                            toCheck = false;
                    if (inNazwa)
                        if(toCheck && 
                                removePolishSigns (new String(ch,start,length)
                                        .toUpperCase())
                                        .contains (wojCode.toUpperCase()))
                           wojId = currentWoj; 
                }
                @Override
                public void endElement(String uri, 
                        String localName, String qName) throws SAXException
                {
                    if (qName.equalsIgnoreCase("WOJ"))
                        inWoj = false;
                    if (qName.equalsIgnoreCase("POW"))
                        inPow = false;
                    if (qName.equalsIgnoreCase("GMI"))
                        inGmi = false;
                    if (qName.equalsIgnoreCase("RODZ"))
                        inRodz = false;
                    if (qName.equalsIgnoreCase("NAZWA"))
                        inNazwa = false;
                    if (qName.equalsIgnoreCase("row"))
                    {
                        currentWoj = "";
                        toCheck = true;
                    }
                }               
            };
            
            saxParser.parse (DataLoader.getTERCStream(), handler);
        }
        catch (IOException | ParserConfigurationException | SAXException ex)
        {
            System.out.println(ex);
        }
        return wojId;
    }
    
    private String removePolishSigns (String input)
    {
        char[] inputTab = input.toCharArray();
        for (int i=0; i< inputTab.length;i++)
        {
            switch (inputTab[i])
            {
                case 'Ą':
                    inputTab[i] = 'A';
                    break;
                case 'Ę':
                    inputTab[i] = 'E';
                    break;
                case 'Ć':
                    inputTab[i] = 'C';
                    break;
                case 'Ń':
                    inputTab[i] = 'N';
                    break;
                case 'Ł':
                    inputTab[i] = 'L';
                    break;
                case 'Ś':
                    inputTab[i] = 'S';
                    break;
                case 'Ó':
                    inputTab[i] = 'O';
                    break;
                case 'Ż':
                case 'Ź':
                    inputTab[i] = 'Z';
                    break; 
            }
        }
        return new String(inputTab);
    }
}
