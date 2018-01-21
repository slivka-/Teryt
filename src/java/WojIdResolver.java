import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Resolves five letter WOJ code into id
 * @author Michał Śliwa
 */
public class WojIdResolver
{
    //external result variable, for SAXParser handler
    private String wojId = "";
    
    /**
     * Resolves five letter WOJ code into id
     * @param wojCode five letter WOJ code
     * @return WOJ id
     */
    public String resolveId (String wojCode)
    {
        // set wojId to empty string
        wojId = "";
        try
        {
            //get new SAXParser factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //get new SAXParser instance
            SAXParser saxParser = factory.newSAXParser();
            
            //handler for reading xml file
            DefaultHandler handler = new DefaultHandler()
            {
                //current WOJ code
                String currentWoj = "";
                
                //is in WOJ tag
                boolean inWoj = false;
                //is in POW tag
                boolean inPow = false;
                //is in GMI tag
                boolean inGmi = false;
                //is in RODZ tag
                boolean inRodz = false;
                //is in NAZWA tag
                boolean inNazwa = false; 
                //is current name eligible for check
                boolean toCheck = true;
                
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
                    if (qName.equalsIgnoreCase("WOJ"))
                        inWoj = true;
                    //if tag name is POW set inPow to true
                    if (qName.equalsIgnoreCase("POW"))
                        inPow = true;
                    //if tag name is GMI set inGmi to true
                    if (qName.equalsIgnoreCase("GMI"))
                        inGmi = true;
                    //if tag name is RODZ set inRodz to true
                    if (qName.equalsIgnoreCase("RODZ"))
                        inRodz = true;
                    //if tag name is NAZWA set inNazwa to true
                    if (qName.equalsIgnoreCase("NAZWA"))
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
                public void characters (char[] ch, int start, int length) 
                        throws SAXException
                {
                    //if in WOJ tag write content to currentWoj
                    if (inWoj)
                        currentWoj = new String (ch, start, length);
                    //if in POW tag check if it's empty, if not, mark entry
                    //as ineligible for check
                    if (inPow)
                        if(length != 0)
                            toCheck = false;
                    //if in GMI tag check if it's empty, if not, mark entry
                    //as ineligible for check
                    if (inGmi)
                        if(length != 0)
                            toCheck = false;
                    //if in RODZ tag check if it's empty, if not, mark entry
                    //as ineligible for check
                    if (inRodz)
                        if(length != 0)
                            toCheck = false;
                    //if in nazwa and eligible to check, read tag content to 
                    //upper case, remove polish letters, and check if it starts
                    //with given string
                    if (inNazwa)
                        if(toCheck && 
                                removePolishSigns (new String(ch,start,length))
                                        .startsWith(wojCode.toUpperCase()))
                           //if yes, set wojId to current WOJ tag content
                           wojId = currentWoj; 
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
                    //tag name is WOJ set inWoj to false
                    if (qName.equalsIgnoreCase("WOJ"))
                        inWoj = false;
                    //tag name is POW set inPow to false
                    if (qName.equalsIgnoreCase("POW"))
                        inPow = false;
                    //tag name is GMI set inGmi to false
                    if (qName.equalsIgnoreCase("GMI"))
                        inGmi = false;
                    //tag name is RODZ set inRodz to false
                    if (qName.equalsIgnoreCase("RODZ"))
                        inRodz = false;
                    //tag name is NAZWA set inNazwa to false
                    if (qName.equalsIgnoreCase("NAZWA"))
                        inNazwa = false;
                    //if tag name is row, clear currentWoj
                    //and check eligibility marking
                    if (qName.equalsIgnoreCase("row"))
                    {
                        currentWoj = "";
                        toCheck = true;
                    }
                }               
            };
            
            //parse TERC xml using defined handler
            saxParser.parse (DataLoader.getTERCStream(), handler);
        }
        catch (IOException | ParserConfigurationException | SAXException ex)
        {
            System.out.println(ex);
        }
        return wojId;
    }
    
    /**
     * Replaces polish diacritic signs with their english alphabet bases
     * @param input String with polish signs
     * @return String without polish signs
     */
    private String removePolishSigns (String input)
    {
        //get input as char array
        char[] inputTab = input.toUpperCase().toCharArray();
        //for each char check if it is a polish sign
        //than change it to english base
        for (int i=0; i< inputTab.length;i++)
        {
            switch (inputTab[i])
            {
                case '\u0104':
                    inputTab[i] = 'A';
                    break;
                case '\u0118':
                    inputTab[i] = 'E';
                    break;
                case '\u0106':
                    inputTab[i] = 'C';
                    break;
                case '\u0143':
                    inputTab[i] = 'N';
                    break;
                case '\u0141':
                    inputTab[i] = 'L';
                    break;
                case '\u015A':
                    inputTab[i] = 'S';
                    break;
                case '\u00D3':
                    inputTab[i] = 'O';
                    break;
                case '\u0179':
                case '\u017B':
                    inputTab[i] = 'Z';
                    break; 
            }
        }
        //return String without polish letters
        return new String(inputTab);
    }
}
