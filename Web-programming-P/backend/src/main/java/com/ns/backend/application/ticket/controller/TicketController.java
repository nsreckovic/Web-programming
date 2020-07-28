package com.ns.backend.application.ticket.controller;

import com.ns.backend.application.ticket.models.request.FromFilterRequestDto;
import com.ns.backend.application.ticket.models.request.ReturnFilterRequestDto;
import com.ns.backend.application.ticket.models.request.TicketRequestDto;
import com.ns.backend.application.ticket.models.response.TicketResponseDto;
import com.ns.backend.application.ticket.service.TicketService;
import com.ns.backend.application.ticket.service.TicketServiceImpl;
import com.ns.backend.application.user.security.SecurityService;
import com.ns.backend.utils.controller.ResponseMessageDto;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private SecurityService securityService = new SecurityService();

    private TicketService ticketService = new TicketServiceImpl();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(TicketRequestDto ticketRequestDto, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!ticketRequestDto.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();
        if (!ticketRequestDto.checkCount())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Counter must be grater than 0!")).build();

        TicketResponseDto responseTicket = ticketService.addTicket(ticketRequestDto);
        if (responseTicket != null)
            return Response.ok(new ResponseMessageDto("Success: Ticket added!")).build();
        return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Ticket with the same flight instance already exists!")).build();
    }

    @POST
    @Path("/update/{ID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateByID(TicketRequestDto ticketRequestDto, @PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        TicketResponseDto responseTicket = ticketService.updateTicketByID(ID, ticketRequestDto);
        if (responseTicket != null)
            return Response.ok(new ResponseMessageDto("Success: Ticket updated!")).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: Ticket not found!")).build();
    }

    @GET
    @Path("/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketByID(@PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isUSER(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        TicketResponseDto responseTicket = ticketService.getTicketByID(ID);
        return Response.ok(responseTicket).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTickets(@HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<TicketResponseDto> responseTickets = ticketService.getAllTickets();
        if (responseTickets != null) return Response.ok(responseTickets).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/for_user/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicketsForUser(@PathParam("username") String username, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isUSER(jwt) || !securityService.getUsername(jwt).equals(username))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<TicketResponseDto> responseTickets = ticketService.getTicketsForUser(username);
        if (responseTickets != null) return Response.ok(responseTickets).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/airline/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTicketsByAirline(@HeaderParam("Authorization") String jwt, @PathParam("ID") String airline_id) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<TicketResponseDto> responseTickets = ticketService.getAllTicketsByAirline(airline_id);
        if (responseTickets != null) return Response.ok(responseTickets).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Warning: No available tickets.")).build();
    }

    @GET
    @Path("/allAvailable")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAvailableTickets(@HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<TicketResponseDto> responseTickets = ticketService.getAllAvailableTickets();
        if (responseTickets != null) return Response.ok(responseTickets).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Warning: No available tickets.")).build();
    }

    @POST
    @Path("/departureFiltered")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartureFilteredTickets(FromFilterRequestDto filter, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isUSER(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<TicketResponseDto> responseTickets = ticketService.getDepartureFilteredTickets(filter);
        if (responseTickets != null) return Response.ok(responseTickets).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Warning: No available tickets.")).build();
    }

    @POST
    @Path("/returnFiltered")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReturnFilteredTickets(ReturnFilterRequestDto filter, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isUSER(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<TicketResponseDto> responseTickets = ticketService.getReturnFilteredTickets(filter);
        if (responseTickets != null) return Response.ok(responseTickets).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Warning: No available tickets.")).build();
    }

    @DELETE
    @Path("/delete/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTicketByID(@PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (ticketService.deleteTicketByID(ID)) return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Warning: No available tickets.")).build();
    }

}
