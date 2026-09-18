package com.mariaseverino.pinbook.controller;

import com.mariaseverino.pinbook.dto.CreateSpaceBookingRequest;
import com.mariaseverino.pinbook.dto.CreateSpaceBookingResponse;
import com.mariaseverino.pinbook.dto.SuccessResponse;
import com.mariaseverino.pinbook.service.BookingService;
import com.mariaseverino.pinbook.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<SuccessResponse<CreateSpaceBookingResponse>> create(@Valid @RequestBody CreateSpaceBookingRequest request){
        return ApiResponse.created(
            bookingService.createSpaceBooking(request),
            "Reserva realizada com sucesso"
        );
    }
}
