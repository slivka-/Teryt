import java.io.UnsupportedEncodingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URLDecoder;

/**
 * @author Michał Śliwa
 */
@Path("/pl")
public class GUService
{
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public Response test()
    {
        return Response.ok().entity("Hello World!").build();
    }
    
    @GET
    @Path("/{streetName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFromCountry(@PathParam("streetName") String streetName) 
            throws UnsupportedEncodingException
    {
        String decodedName = URLDecoder.decode(streetName,"UTF-8");
        return Response.ok()
                       .entity(new CountryHitsCounter()
                                    .countCountryXMLHits(decodedName))
                       .build();
    }
    
    @GET
    @Path("/{WojPowCode}/{streetName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFromWojPowCode(@PathParam("WojPowCode")String wojPowCode,
            @PathParam("streetName") String streetName) 
            throws UnsupportedEncodingException
    {
        String decodedName = URLDecoder.decode(streetName,"UTF-8");
        
        switch (wojPowCode.length())
        {
            case 2:
            case 5:
                return Response.ok()
                        .entity(new WojHitsCounter()
                                .countWojXMLHits(decodedName, wojPowCode))
                        .build();
            case 4:
                return Response.ok()
                        .entity(new PowHitsCounter()
                                .countPowXMLHits(decodedName, 
                                        wojPowCode.substring(0, 2), 
                                        wojPowCode.substring(2)))
                        .build();
            case 7:
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
    
    @GET
    @Path("/{WojCode}/{PowCode}/{streetName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFromWojAndPowCode(@PathParam("WojCode")String wojCode, 
            @PathParam("PowCode")String powCode , 
            @PathParam("streetName") String streetName) 
            throws UnsupportedEncodingException
    {
        String decodedName = URLDecoder.decode(streetName,"UTF-8");
        return Response.ok()
                       .entity(new PowHitsCounter()
                               .countPowXMLHits(decodedName, wojCode, powCode))
                       .build();
    }
}
