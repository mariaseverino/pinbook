package com.mariaseverino.pinbook.repository;

import com.mariaseverino.pinbook.entity.Lane;
import com.mariaseverino.pinbook.entity.Space;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LaneRepository extends JpaRepository<Lane, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lane l WHERE l.id = :id")
    Optional<Lane> findByIdWithLock(@Param("id") UUID id);
    Optional<Lane> findById(UUID id);
    boolean existsByName(String name);
}
