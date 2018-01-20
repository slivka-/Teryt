import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Michał Śliwa
 */
public class DataLoader
{ 
    private static final String TERC_PATH = "http://cuda.iti.pk.edu.pl/"+
                                            "TERC_Urzedowy_2018-01-18.zip";
    private static final String ULIC_PATH = "http://cuda.iti.pk.edu.pl/"+
                                            "ULIC_Urzedowy_2018-01-18.zip";
         
    public static InputStream getTERCStream()
    {
        return getXmlStream(TERC_PATH);
    }
    
    public static InputStream getULICStream()
    {
        return  getXmlStream(ULIC_PATH);
    }
    
    private static InputStream getXmlStream(String path)
    {
        try
        {
            URL zipUrl = new URL(path);
            ZipInputStream zipStream = new ZipInputStream(zipUrl.openStream());
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null)
            {
                if (entry.getName().endsWith(".xml"))
                {
                    return zipStream;
                }
            }
            return null;
        }
        catch (IOException ex)
        {
            System.out.println(ex);
            return null;
        }
    }
}
