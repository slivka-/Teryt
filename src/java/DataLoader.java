import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author Michał Śliwa
 */
public class DataLoader
{
    private static final String TERC_PATH = "G:\\TERC_Urzedowy_2018-01-18.zip";
    private static final String ULIC_PATH = "G:\\ULIC_Urzedowy_2018-01-18.zip";
    
    public static String loadTest()
    {
        try
        {
            ZipFile TercZip = new ZipFile(TERC_PATH);
            for(Enumeration e = TercZip.entries(); e.hasMoreElements();)
            {
                ZipEntry entry = (ZipEntry)e.nextElement();
                if(!entry.isDirectory())
                {
                    if(entry.getName().endsWith(".xml"))
                        readXML(TercZip.getInputStream(entry));
                }
            }
        }
        catch(IOException ex)
        {
            System.out.println(ex);
        }
        return "";
    }
    
    public static String readXML(InputStream xmlInput)
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler()
            {
                boolean inRow = false;
                boolean inWoj = false;
                boolean inPow = false;
                boolean inGmi = false;
                boolean inRodz = false;
                boolean inNazwa = false;
                
                int currentWoj = 0;
                int currentPow = 0;
                int currentGmi = 0;
                int currentRodz = 0;
                
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
                {
                    if(qName.equalsIgnoreCase("row"))
                        inRow = true;
                    if(qName.equalsIgnoreCase("WOJ"))
                        inWoj = true;
                    if(qName.equalsIgnoreCase("POW"))
                        inPow = true;
                    if(qName.equalsIgnoreCase("GMI"))
                        inGmi = true;
                    if(qName.equalsIgnoreCase("RODZ"))
                        inRodz = true;
                    if(qName.equalsIgnoreCase("NAZWA"))
                        inNazwa = true;
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException
                {
                    if(inWoj)
                        currentWoj = length;
                    if(inPow)
                        currentPow = length;
                    if(inGmi)
                        currentGmi = length;
                    if(inRodz)
                        currentRodz = length;
                    if(inNazwa && currentGmi == 0 && currentPow == 0 && currentRodz == 0)
                        System.out.println(new String(ch,start,length));
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException
                {
                    if(qName.equalsIgnoreCase("row"))
                    {
                        inRow = false;
                        currentWoj = 0;
                        currentPow = 0;
                        currentGmi = 0;
                        currentRodz = 0;
                    }
                    if(qName.equalsIgnoreCase("WOJ"))
                    {    
                        inWoj = false;
                    }
                    if(qName.equalsIgnoreCase("POW"))
                    {
                        inPow = false;
                    }
                    if(qName.equalsIgnoreCase("GMI"))
                    {    
                        inGmi = false;
                    }
                    if(qName.equalsIgnoreCase("RODZ"))
                    {    
                        inRodz = false;
                    }
                    if(qName.equalsIgnoreCase("NAZWA"))
                    {
                        inNazwa = false;
                    }
                }
            };
            saxParser.parse(xmlInput, handler);
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return "";
    }
}
