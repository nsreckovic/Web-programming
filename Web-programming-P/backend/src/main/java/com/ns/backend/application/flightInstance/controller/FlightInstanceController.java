package com.ns.backend.application.flightInstance.controller;

import com.ns.backend.application.flightInstance.models.request.FlightInstanceRequestDto;
import com.ns.backend.application.flightInstance.models.response.FlightInstanceResponseDto;
import com.ns.backend.application.flightInstance.service.FlightInstanceService;
import com.ns.backend.application.flightInstance.service.FlightInstanceServiceImpl;
import com.ns.backend.application.user.security.SecurityService;
import com.ns.backend.utils.controller.ResponseMessageDto;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/flightInstances")
@RequiredArgsConstructor
public class FlightInstanceController {

    private SecurityService securityService = new SecurityService();

    private FlightInstanceService flightInstanceService = new FlightInstanceServiceImpl();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(FlightInstanceRequestDto flightInstance, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!flightInstance.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();

        FlightInstanceResponseDto responseFlightInstance = flightInstanceService.addFlightInstance(flightInstance);
        if (responseFlightInstance != null) return Response.ok(responseFlightInstance).build();
        return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Flight Instance with same parameters already exists!")).build();
    }

    @POST
    @Path("/update/{ID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("ID") String ID, FlightInstanceRequestDto flightInstance, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!flightInstance.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();

        FlightInstanceResponseDto responseFlightInstance = flightInstanceService.updateFlightInstance(ID, flightInstance);
        if (responseFlightInstance != null) return Response.ok(responseFlightInstance).build();
        return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error occurred while updating flight instance!")).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(@HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        List<FlightInstanceResponseDto> responseFlightInstances = flightInstanceService.getAll();
        if (responseFlightInstances != null) return Response.ok(responseFlightInstances).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: No Flight Instance were found!")).build();
    }

    @DELETE
    @Path("/delete/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFlightInstanceByID(@PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (flightInstanceService.deleteFlightInstanceByID(ID)) return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Error: Flight Instance not found!")).build();
    }
}
