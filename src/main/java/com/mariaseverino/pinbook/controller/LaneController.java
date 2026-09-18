package com.mariaseverino.pinbook.controller;

import com.mariaseverino.pinbook.dto.CreateLaneRequest;
import com.mariaseverino.pinbook.dto.CreateLaneResponse;
import com.mariaseverino.pinbook.dto.SuccessResponse;
import com.mariaseverino.pinbook.service.LaneService;
import com.mariaseverino.pinbook.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}
