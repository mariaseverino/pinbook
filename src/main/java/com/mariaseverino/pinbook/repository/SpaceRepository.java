package com.mariaseverino.pinbook.repository;

import com.mariaseverino.pinbook.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpaceRepository extends JpaRepository<Space, UUID> {
    boolean existsByName(String name);
}
