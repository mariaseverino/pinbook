package com.mariaseverino.pinbook.controller;

import com.mariaseverino.pinbook.dto.*;
import com.mariaseverino.pinbook.service.LaneService;
import com.mariaseverino.pinbook.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/lane")
@RequiredArgsConstructor
public class LaneController {
    private final LaneService laneService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<SuccessResponse<CreateLaneResponse>> create(@Valid @RequestBody CreateLaneRequest request){

        return ApiResponse.created(
            laneService.createLane(request),
            "Pista criada com sucesso"
        );
    }

    @PutMapping("/update/{laneId}")
    @PreAuthorize("hasRole('OWNER') and @spaceRepository.existsByIdAndOwnerId(#spaceId, authentication.principal.userId)")
    public ResponseEntity<SuccessResponse<UpdateLaneResponse>> updateSpace(@PathVariable UUID laneId, @Valid @RequestBody UpdateLaneRequest request, @AuthenticationPrincipal AuthenticatedUser authenticatedUser){
        return ApiResponse.ok(
            laneService.updateLane(request, laneId),
            "Pista atualizada"
        );
    }

    @DeleteMapping("/delete/{laneId}")
    @PreAuthorize("hasRole('OWNER') and @spaceRepository.existsByIdAndOwnerId(#spaceId, authentication.principal.userId)")
    public ResponseEntity<SuccessResponse<Void>> deleteSpace(@PathVariable UUID laneId){
        laneService.deleteLane(laneId);
        return ApiResponse.ok(
            null,
            "Pista deletada com sucesso"
        );
    }
}
