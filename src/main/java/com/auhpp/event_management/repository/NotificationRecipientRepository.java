package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.NotificationRecipient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Long> {
    @Query("SELECT nr FROM NotificationRecipient nr " +
            "WHERE nr.appUser.email = :email ")
    Page<NotificationRecipient> findAllByEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT COUNT(nr) FROM NotificationRecipient nr " +
            "WHERE nr.appUser.email = :email " +
            "AND nr.seen = false ")
    Integer countUnseen(@Param("email") String email);


}
