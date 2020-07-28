package com.ns.backend.application.reservation.repository;

import com.ns.backend.application.reservation.models.Reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation addReservation(Reservation reservation);

    Reservation updateReservationByID(String ID, Reservation newReservation);

    Reservation getReservationByID(String ID);

    List<Reservation> getAllReservations();

    List<Reservation> getAllReservationsByUserID(String user_ID);

    Boolean deleteReservationByID(String ID, int version);

}
