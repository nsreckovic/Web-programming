package com.ns.backend.application.reservation.controller;

import com.ns.backend.application.reservation.models.request.ReservationFilterRequestDto;
import com.ns.backend.application.reservation.models.request.ReservationRequestDto;
import com.ns.backend.application.reservation.models.response.ReservationResponseDto;
import com.ns.backend.application.reservation.service.ReservationService;
import com.ns.backend.application.reservation.service.ReservationServiceImpl;
import com.ns.backend.application.user.security.SecurityService;
import com.ns.backend.utils.controller.ResponseMessageDto;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private SecurityService securityService = new SecurityService();

    private ReservationService reservationService = new ReservationServiceImpl();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(ReservationRequestDto reservationRequestDto, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isUSER(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();

        if (!reservationRequestDto.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();

        if (reservationService.addReservation(reservationRequestDto))
            return Response.ok(new ResponseMessageDto("Success: Reservation added!")).build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseMessageDto("Error: Internal Server Error!")).build();
    }

    @POST
    @Path("/update/{ID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(ReservationRequestDto reservationRequestDto, @HeaderParam("Authorization") String jwt, @PathParam("ID") String reservation_ID) {
        if (!securityService.isUSER(jwt))
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();
        if (!reservationRequestDto.checkData())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Missing fields!")).build();

        if (securityService.isADMIN(jwt)) {
            Boolean updated = reservationService.updateReservationByID(reservation_ID, reservationRequestDto);
            if (updated)
                return Response.ok(new ResponseMessageDto("Success: Reservation updated!")).build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseMessageDto("Error: Internal Server Error!")).build();
        } else if (securityService.getUserID(jwt).equals(reservationRequestDto.getUser_ID())) {
            Boolean updated = reservationService.updateReservationByID(reservation_ID, reservationRequestDto);
            if (updated)
                return Response.ok(new ResponseMessageDto("Success: Reservation updated!")).build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseMessageDto("Error: Internal Server Error!")).build();
        } else
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();
    }

    @POST
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(ReservationFilterRequestDto filter, @HeaderParam("Authorization") String jwt) {
        if (!securityService.isADMIN(jwt)) {
            if (!securityService.checkUsername(jwt, filter.getUsername()))
                return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();
        }
        List<ReservationResponseDto> reservationResponseDtos = reservationService.getAllReservations(filter);
        if (reservationResponseDtos != null) return Response.ok(reservationResponseDtos).build();
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessageDto("Warning: No reservations were found!")).build();
    }

    @GET
    @Path("/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByID(@HeaderParam("Authorization") String jwt, @PathParam("ID") String reservation_ID) {
        if (securityService.isADMIN(jwt)) {
            ReservationResponseDto responseDto = reservationService.getReservationByID(reservation_ID);
            if (responseDto != null) return Response.ok(responseDto).build();
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Bad request!")).build();
        } else if (securityService.isUSER(jwt)) {
            String username = securityService.getUsername(jwt);
            if (username != null) {
                ReservationResponseDto responseDto = reservationService.getReservationByIDforCustomer(username, reservation_ID);
                if (responseDto != null) return Response.ok(responseDto).build();
                return Response.status(Response.Status.BAD_REQUEST).entity(new ResponseMessageDto("Error: Bad request!")).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();
    }

    @DELETE
    @Path("/delete/{ID}")
    public Response deleteFlightByID(@PathParam("ID") String ID, @HeaderParam("Authorization") String jwt) {
        String username = securityService.getUsername(jwt);
        if (username != null) {
            Boolean delete = reservationService.deleteReservationByID(username, ID);
            if (delete != null) {
                if (delete) return Response.ok(new ResponseMessageDto("Success.")).build();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ResponseMessageDto("Error: Internal Server Error!")).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(new ResponseMessageDto("Error: Not authorized!")).build();
    }

}
