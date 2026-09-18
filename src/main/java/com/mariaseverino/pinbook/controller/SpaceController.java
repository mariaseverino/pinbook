package com.mariaseverino.pinbook.controller;

import com.mariaseverino.pinbook.dto.*;
import com.mariaseverino.pinbook.service.SpaceService;
import com.mariaseverino.pinbook.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/space")
@RequiredArgsConstructor
public class  SpaceController {
    private final SpaceService spaceService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<SuccessResponse<CreateSpaceResponse>> create(@Valid @RequestBody CreateSpaceRequest request, @AuthenticationPrincipal AuthenticatedUser authenticatedUser){

        return ApiResponse.created(
            spaceService.createSpace(request, authenticatedUser.userId()),
            "Espaco criado com sucesso"
        );
    }
    @GetMapping("/space/{spaceId}")
    public ResponseEntity<SuccessResponse<CreateSpaceResponse>> getSpace(@PathVariable UUID spaceId){
        return ApiResponse.ok(
            spaceService.getSpace(spaceId),
            "Espaço encontrado"
        );
    }
}
