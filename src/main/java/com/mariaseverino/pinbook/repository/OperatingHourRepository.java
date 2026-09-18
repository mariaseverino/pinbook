package com.mariaseverino.pinbook.repository;

import com.mariaseverino.pinbook.entity.OperatingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperatingHourRepository extends JpaRepository<OperatingHour, UUID> {
    Optional<OperatingHour> findById(UUID id);
    List<OperatingHour> findAllBySpaceId(UUID id);

    @Query("""
        SELECT CASE WHEN COUNT(oh) > 0 THEN true ELSE false END
        FROM OperatingHour oh
        WHERE oh.space.id = :spaceId
        AND oh.weekDay = :weekDay
        AND oh.active = true
        AND oh.openingTime <= :requestedStart
        AND oh.closingTime >= :requestedEnd
    """)
    boolean isWithinOperatingHours(
        @Param("spaceId") UUID spaceId,
        @Param("weekDay") DayOfWeek weekDay,
        @Param("requestedStart") LocalTime requestedStart,
        @Param("requestedEnd") LocalTime requestedEnd
    );
}
