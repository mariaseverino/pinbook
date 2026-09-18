package com.mariaseverino.pinbook.repository;

import com.mariaseverino.pinbook.entity.OperatingHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperatingHourRepository extends JpaRepository<OperatingHour, UUID> {
    Optional<OperatingHour> findById(UUID id);
    List<OperatingHour> findAllBySpaceId(UUID id);
}
