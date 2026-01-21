package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
