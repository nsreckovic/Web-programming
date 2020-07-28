package com.ns.backend.application.airline.controller;

import com.ns.backend.application.airline.models.request.AirlineRequestDto;
import com.ns.backend.application.airline.models.response.AirlineResponseDto;
import com.ns.backend.application.airline.service.AirlineService;
import com.ns.backend.application.airline.service.AirlineServiceImpl;
import com.ns.backend.application.user.security.SecurityService;
import com.ns.backend.utils.controller.ResponseMessageDto;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/airlines")
@RequiredArgsConstructor
public class AirlineController {

    private SecurityService securityService = new SecurityService();

    private AirlineService airlineService = new AirlineServiceImpl();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAirline(AirlineRequestDto airline, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!airline.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();
        if (!airlineService.isNameAvailable(airline.getName()))
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Airline name is already taken!")).build();

        AirlineResponseDto responseAirline = airlineService.addAirline(airline);
        if (responseAirline != null) return Response.ok(responseAirline).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path("/update/{ID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAirline(AirlineRequestDto newAirline, @PathParam("ID") String airline_id, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        AirlineResponseDto responseAirline = airlineService.updateAirline(airline_id, newAirline);
        if (responseAirline != null) return Response.ok(responseAirline).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirlineByID(@PathParam("ID") String airline_id, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        AirlineResponseDto responseAirline = airlineService.getAirlineByID(airline_id);
        if (responseAirline != null) return Response.ok(responseAirline).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAirlines(@HeaderParam("Authorization") String jwt) {
        if (!securityService.isUSER(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<AirlineResponseDto> responseAirlines = airlineService.getAllAirlines();
        if (responseAirlines != null) return Response.ok(responseAirlines).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/delete/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAirlineByUsername(@PathParam("ID") String airline_id, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (airlineService.deleteAirlineByID(airline_id)) return Response.status(Response.Status.OK).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
