package com.mariaseverino.pinbook.controller;

import com.mariaseverino.pinbook.dto.CreateOperatingHoursRequest;
import com.mariaseverino.pinbook.dto.CreateOperatingHoursResponse;
import com.mariaseverino.pinbook.dto.SuccessResponse;
import com.mariaseverino.pinbook.service.OperatingHourService;
import com.mariaseverino.pinbook.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{spaceId}/operating-hours")
@RequiredArgsConstructor
public class OperatingHourController {
    private final OperatingHourService operatingHourService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<SuccessResponse<List<CreateOperatingHoursResponse.OperatingHourItem>>> createOperatingHours (
        @PathVariable UUID spaceId,
        @Valid @RequestBody CreateOperatingHoursRequest request) {

        return ApiResponse.created(
            operatingHourService.createBulk(request, spaceId),
            "Horário de funcionamento criado"
        );
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CreateOperatingHoursResponse.OperatingHourItem>>> list (@PathVariable UUID spaceId){
        return ApiResponse.ok(
            operatingHourService.list(spaceId),
            "Horários de funcionamento do espaco"
        );
    }
}
