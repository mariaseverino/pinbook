package com.mariaseverino.pinbook.repository;

import com.mariaseverino.pinbook.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.startTime < :endTime
        AND b.endTime > :startTime
        AND b.space.id = :spaceId
    """)
    boolean existsSpaceConflict(
        @Param("spaceId") UUID spaceId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );

    @Query("""
        SELECT COUNT(b) > 0
        FROM Booking b
        WHERE b.startTime < :endTime
        AND b.endTime > :startTime
        AND b.lane.id = :laneId
    """)
    boolean existsLaneConflict(
        @Param("laneId") UUID laneId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );

}
