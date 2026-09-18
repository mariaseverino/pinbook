package com.mariaseverino.pinbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "bookings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_lane_start_time", columnNames = {"lane_id", "start_time"}),
                @UniqueConstraint(name = "uk_space_start_time", columnNames = {"space_id", "start_time"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = true)
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lane_id", nullable = true)
    private Lane lane;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @AssertTrue(message = "A reserva deve ter exatamente uma pista OU um espaço, não ambos nem nenhum")
    private boolean isValidTarget() {
        return (lane != null) ^ (space != null); // XOR: só um dos dois pode ser verdadeiro
    }

    @AssertTrue(message = "O horário de fim da reserva deve ser depois do horário de início")
    private boolean isValidSchedule() {
        return endTime != null && startTime != null && endTime.isAfter(startTime);
    }
}
