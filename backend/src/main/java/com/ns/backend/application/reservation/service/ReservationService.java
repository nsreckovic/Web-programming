package com.ns.backend.application.reservation.service;

import com.ns.backend.application.reservation.models.request.ReservationFilterRequestDto;
import com.ns.backend.application.reservation.models.request.ReservationRequestDto;
import com.ns.backend.application.reservation.models.response.ReservationResponseDto;

import java.util.List;

public interface ReservationService {

    Boolean addReservation(ReservationRequestDto reservationRequestDto);

    Boolean updateReservationByID(String ID, ReservationRequestDto reservationRequestDto);

    ReservationResponseDto getReservationByID(String ID);

    ReservationResponseDto getReservationByIDforCustomer(String username, String ID);

    List<ReservationResponseDto> getAllReservations(ReservationFilterRequestDto filter);

    Boolean deleteReservationByID(String username, String ID);

}
