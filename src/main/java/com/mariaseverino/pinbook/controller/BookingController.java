package com.mariaseverino.pinbook.controller;

import com.mariaseverino.pinbook.dto.*;
import com.mariaseverino.pinbook.service.BookingService;
import com.mariaseverino.pinbook.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * Endpoints para gerenciamento de reservas.
 */
@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Operações de reserva de espaços e raias")
public class BookingController {
    private final BookingService bookingService;

    @Operation(
        summary = "Reservar espaço",
        description = "Cria uma reserva para o espaço informado no período solicitado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reserva realizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Espaço não encontrado"),
        @ApiResponse(responseCode = "409", description = "Horário já reservado")
    })
    @PostMapping("/book-space")
    public ResponseEntity<SuccessResponse<CreateSpaceBookingResponse>> create(@Valid @RequestBody CreateSpaceBookingRequest request){
        return ApiResponse.created(
            bookingService.createSpaceBooking(request),
            "Reserva realizada com sucesso"
        );
    }

    @GetMapping("/available-times")
    public ResponseEntity<SuccessResponse<Map<DayOfWeek, List<LocalTime>>>> getAvailableTimes(@Valid @RequestBody GetAvailableTimesRequest request){
        return ApiResponse.ok(
            bookingService.getAvailableTimes(request),
            "Horarios disponiveis"
        );
    }

    @PostMapping("/book-lane")
    public ResponseEntity<SuccessResponse<CreateLaneBookingResponse>> create(@Valid @RequestBody CreateLaneBookingRequest request){
        return ApiResponse.created(
            bookingService.createLaneBooking(request),
            "Reserva realizada com sucesso"
        );
    }
}
