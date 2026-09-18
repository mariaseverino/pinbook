package com.mariaseverino.pinbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "operating_hours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatingHour {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day", nullable = false)
    private WeekDay weekDay;

    @Column(nullable = false)
    private boolean active;

    @NotNull
    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @NotNull
    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    public enum WeekDay {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

}


