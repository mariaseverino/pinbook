package com.mariaseverino.pinbook.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "spaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(nullable = false)
    private String cep;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer capacity;

    @NotNull
    @Column(name = "batch_maintenance_time", nullable = false)
    private Integer batchMaintenanceTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(
            mappedBy = "space",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OperatingHour> operatingHours = new ArrayList<>();

    @OneToMany(
            mappedBy = "space",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Lane> lanes = new ArrayList<>();

}
