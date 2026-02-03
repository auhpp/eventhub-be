package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.SocialType;
import com.auhpp.event_management.entity.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
    Optional<SocialLink> findByTypeAndAppUserId(SocialType type, Long appUserId);
}
