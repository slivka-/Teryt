import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Provides data streams for remote xml sources
 * @author Michał Śliwa
 */
public class DataLoader
{ 
    //Path to TERC database xml
    private static final String TERC_PATH = "http://cuda.iti.pk.edu.pl/"+
                                            "TERC_Urzedowy_2018-01-18.zip";
    //Path to ULIC database xml
    private static final String ULIC_PATH = "http://cuda.iti.pk.edu.pl/"+
                                            "ULIC_Urzedowy_2018-01-18.zip";
    
    /**
     * Gets input stream to TERC database xml
     * @return InputStream to TERC xml
     */
    public static InputStream getTERCStream()
    {
        return getXmlStream(TERC_PATH);
    }
    
    /**
     * Gets input stream to ULIC database xml
     * @return InputStream to TERC xml
     */
    public static InputStream getULICStream()
    {
        return  getXmlStream(ULIC_PATH);
    }
    
    /**
     * Gets input stream to xml file inside of a zip archive, from given path
     * @param path path to zip archive
     * @return InputStream to xml file
     */
    private static InputStream getXmlStream(String path)
    {
        try
        {
            //definie new url to zip archive
            URL zipUrl = new URL(path);
            //open input stream to archive
            ZipInputStream zipStream = new ZipInputStream(zipUrl.openStream());
            ZipEntry entry;
            //search zip file entries
            while ((entry = zipStream.getNextEntry()) != null)
            {
                //when xml file is found, return input stream
                if (entry.getName().endsWith(".xml"))
                {
                    return zipStream;
                }
            }
            //if no xml files found return null
            return null;
        }
        catch (IOException ex)
        {
            //on exception return null
            System.out.println(ex);
            return null;
        }
    }
}
