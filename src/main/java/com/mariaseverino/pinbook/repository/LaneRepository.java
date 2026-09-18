package com.mariaseverino.pinbook.repository;

import com.mariaseverino.pinbook.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LaneRepository extends JpaRepository<Lane, UUID> {
    Optional<Lane> findById(String email);
    boolean existsByName(String name);
}
