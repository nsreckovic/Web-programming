package application.ticket;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController() {
        this.ticketService = new TicketService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketDto> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GET
    @Path("/one-way")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketDto> getOneWayTickets() {
        return ticketService.getOneWayTickets();
    }

    @GET
    @Path("/round-trip")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TicketDto> getRoundTripTickets() {
        return ticketService.getRoundTripTickets();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TicketDto addTicket(TicketDto ticketDto) { return ticketService.addTicket(ticketDto); }

}
