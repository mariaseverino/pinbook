package com.mariaseverino.pinbook.repository;

import com.mariaseverino.pinbook.entity.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT b FROM Booking b
        WHERE b.start_time < :endTime
        AND b.end_time > :startTime
        AND (
            b.lane.id = :laneId
            OR b.space.id = :spaceId
            OR b.lane.space.id = :spaceId
        )
    """)
    List<Booking> findConflicting(
        @Param("laneId") UUID laneId,
        @Param("spaceId") UUID spaceId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );
}
