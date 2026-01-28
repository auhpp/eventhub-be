package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.entity.Attendee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long>, JpaSpecificationExecutor<Attendee> {
    Optional<Attendee> findByTicketCode(String ticketCode);

    @Query("SELECT a FROM Attendee a WHERE a.status = :status AND a.booking.appUser.email = :email")
    Page<Attendee> findAllByStatusAndEmailUser(AttendeeStatus status, String email, Pageable pageable);

    @Query("SELECT a FROM Attendee a WHERE a.booking.appUser.email = :email " +
            "AND a.ticket.eventSession.startDateTime > :currentDate ")
    Page<Attendee> findComingAllByEmailUser(LocalDateTime currentDate, String email, Pageable pageable);

    @Query("SELECT a FROM Attendee a WHERE a.booking.appUser.email = :email " +
            "AND a.ticket.eventSession.endDateTime < current_timestamp ")
    Page<Attendee> findPastAllByEmailUser(LocalDateTime currentDate, String email, Pageable pageable);


}
