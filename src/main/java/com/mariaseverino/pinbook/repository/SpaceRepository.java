package com.mariaseverino.pinbook.repository;

import com.mariaseverino.pinbook.entity.Space;
import com.mariaseverino.pinbook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpaceRepository extends JpaRepository<Space, UUID> {
    Optional<Space> findById(String email);
    boolean existsByName(String name);
}
