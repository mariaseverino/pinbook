package com.mariaseverino.pinbook.controller;

import com.mariaseverino.pinbook.dto.*;
import com.mariaseverino.pinbook.service.BookingService;
import com.mariaseverino.pinbook.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/reservar-espaco")
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

    @PostMapping("/reservar-pista")
    public ResponseEntity<SuccessResponse<CreateLaneBookingResponse>> create(@Valid @RequestBody CreateLaneBookingRequest request){
        return ApiResponse.created(
            bookingService.createLaneBooking(request),
            "Reserva realizada com sucesso"
        );
    }
}
