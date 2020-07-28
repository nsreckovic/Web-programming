package com.ns.backend.application.flight.controller;

import com.ns.backend.application.flight.models.request.FlightRequestDto;
import com.ns.backend.application.flight.models.response.FlightResponseDto;
import com.ns.backend.application.flight.service.FlightService;
import com.ns.backend.application.flight.service.FlightServiceImpl;
import com.ns.backend.application.user.security.SecurityService;
import com.ns.backend.utils.controller.ResponseMessageDto;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/flights")
@RequiredArgsConstructor
public class FlightController {

    private SecurityService securityService = new SecurityService();

    private FlightService flightService = new FlightServiceImpl();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(FlightRequestDto flight, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!flight.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();
        if (!flightService.isIDAvailable(flight.getId()))
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: ID is already taken!")).build();

        FlightResponseDto responseFlight = flightService.addFlight(flight);
        if (responseFlight != null) return Response.ok(responseFlight).build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/update/{ID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(FlightRequestDto newFlight, @PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        FlightResponseDto responseFlight = flightService.updateFlightByID(ID, newFlight);
        if (responseFlight != null) return Response.ok(responseFlight).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // NOT IN USE
    @GET
    @Path("/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFlightByID(@PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        FlightResponseDto responseFlight = flightService.getFlightByID(ID);
        if (responseFlight != null) return Response.ok(responseFlight).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFlights(@HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<FlightResponseDto> responseFlights = flightService.getAllFlights();
        if (responseFlights != null) return Response.ok(responseFlights).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // NOT IN USE
    @GET
    @Path("/departure_airport/{departure_airport_ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFlightsByDepartureAirport(@PathParam("departure_airport_ID") String departure_airport_ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<FlightResponseDto> responseFlights = flightService.getFlightsByDepartureAirport(departure_airport_ID);
        if (responseFlights != null) return Response.ok(responseFlights).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // NOT IN USE
    @GET
    @Path("/arrival_airport/{arrival_airport_ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFlightsByArrivalAirport(@PathParam("arrival_airport_ID") String arrival_airport_ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<FlightResponseDto> responseFlights = flightService.getFlightsByArrivalAirport(arrival_airport_ID);
        if (responseFlights != null) return Response.ok(responseFlights).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/delete/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFlightByID(@PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (flightService.deleteFlightByID(ID)) return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
