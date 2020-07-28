package com.ns.backend.application.airport.controller;

import com.ns.backend.application.airport.models.request.AirportRequestDto;
import com.ns.backend.application.airport.models.response.AirportResponseDto;
import com.ns.backend.application.airport.service.AirportService;
import com.ns.backend.application.airport.service.AirportServiceImpl;
import com.ns.backend.application.user.security.SecurityService;
import com.ns.backend.utils.controller.ResponseMessageDto;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/airports")
@RequiredArgsConstructor
public class AirportController {

    private SecurityService securityService = new SecurityService();

    private AirportService airportService = new AirportServiceImpl();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAirport(AirportRequestDto airport, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!airport.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();
        if (!airportService.isIDAvailable(airport.getId()))
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Name is already taken!")).build();

        AirportResponseDto responseAirport = airportService.addAirport(airport);
        if (responseAirport != null) return Response.ok(responseAirport).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path("/update/{ID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAirport(AirportRequestDto newAirport, @PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!newAirport.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();

        AirportResponseDto responseAirport = airportService.updateAirport(ID, newAirport);
        if (responseAirport != null) return Response.ok(responseAirport).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: Airport not found!")).build();
    }

    // NOT IN USE
    @GET
    @Path("/ID/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirportByID(@PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        AirportResponseDto responseAirport = airportService.getAirportByID(ID);
        if (responseAirport != null) return Response.ok(responseAirport).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: Airport not found!")).build();
    }

    // NOT IN USE
    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirportByName(@PathParam("name") String name, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        AirportResponseDto responseAirport = airportService.getAirportByName(name);
        if (responseAirport != null) return Response.ok(responseAirport).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: Airport not found!")).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAirports(@HeaderParam("Authorization") String jwt) {
        if (!securityService.isUSER(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<AirportResponseDto> responseAirports = airportService.getAllAirports();
        if (responseAirports != null) return Response.ok(responseAirports).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: No airports were found!")).build();
    }

    @DELETE
    @Path("/delete/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAirportByID(@PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (airportService.deleteAirportByID(ID)) return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: Airport not found!")).build();
    }

}
