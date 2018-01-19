import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


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
}
