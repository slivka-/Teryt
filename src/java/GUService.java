import java.io.UnsupportedEncodingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URLDecoder;

/**
 * Resr web service, provides street search functionality in different socpes
 * @author Michał Śliwa
 */
@Path("/pl")
public class GUService
{
    /**
     * Search for given street name in whole country
     * @param streetName
     * @return
     * @throws UnsupportedEncodingException 
     */
    @GET
    @Path("/{streetName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFromCountry(@PathParam("streetName") String streetName) 
            throws UnsupportedEncodingException
    {
        //Decode URL string in UTF-8 formatting
        String decodedName = URLDecoder.decode(streetName,"UTF-8");
        //return number of hits in whole country
        return Response.ok()
                       .entity(new CountryHitsCounter()
                                    .countCountryXMLHits(decodedName))
                       .build();
    }
    
    /**
     * Search for given street name in given voivodeship or county
     * @param wojPowCode
     * @param streetName
     * @return
     * @throws UnsupportedEncodingException 
     */
    @GET
    @Path("/{WojPowCode}/{streetName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFromWojPowCode(@PathParam("WojPowCode")String wojPowCode,
            @PathParam("streetName") String streetName) 
            throws UnsupportedEncodingException
    {
        //Decode URL string in UTF-8 formatting
        String decodedName = URLDecoder.decode(streetName,"UTF-8");
        
        switch (wojPowCode.length())
        {
            case 2:
            case 5:
                //if WojPowCode is 2 or 5 character long, search in voivodeship
                return Response.ok()
                        .entity(new WojHitsCounter()
                                .countWojXMLHits(decodedName, wojPowCode))
                        .build();
            case 4:
                //if WojPowCode is 4 character long, search in county
                return Response.ok()
                        .entity(new PowHitsCounter()
                                .countPowXMLHits(decodedName, 
                                        wojPowCode.substring(0, 2), 
                                        wojPowCode.substring(2)))
                        .build();
            case 7:
                //if WojPowCode is 7 character long, search in county
                return Response.ok()
                        .entity(new PowHitsCounter()
                                .countPowXMLHits(decodedName, 
                                        wojPowCode.substring(0, 5), 
                                        wojPowCode.substring(5)))
                        .build();
            default:
                return null;
        }
    }
    
    /**
     * Search for given street name in given county
     * @param wojCode
     * @param powCode
     * @param streetName
     * @return
     * @throws UnsupportedEncodingException 
     */
    @GET
    @Path("/{WojCode}/{PowCode}/{streetName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFromWojAndPowCode(@PathParam("WojCode")String wojCode, 
            @PathParam("PowCode")String powCode , 
            @PathParam("streetName") String streetName) 
            throws UnsupportedEncodingException
    {
        //Decode URL string in UTF-8 formatting
        String decodedName = URLDecoder.decode(streetName,"UTF-8");
        //return number of hits in county
        return Response.ok()
                       .entity(new PowHitsCounter()
                               .countPowXMLHits(decodedName, wojCode, powCode))
                       .build();
    }
}
