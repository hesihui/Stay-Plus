package com.staybooking.repository;

import com.staybooking.model.Reservation;
import com.staybooking.model.Stay;
import com.staybooking.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.staybooking.model.Stay;
import com.staybooking.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuest(User guest);

    List<Reservation> findByStay(Stay stay);

    List<Reservation> findByStayAndCheckoutDateAfter(Stay stay, LocalDate date);
}
