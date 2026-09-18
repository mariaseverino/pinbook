package com.mariaseverino.pinbook.service;

import com.mariaseverino.pinbook.dto.CreateSpaceBookingRequest;
import com.mariaseverino.pinbook.dto.CreateSpaceBookingResponse;
import com.mariaseverino.pinbook.entity.Booking;
import com.mariaseverino.pinbook.entity.Space;
import com.mariaseverino.pinbook.entity.User;
import com.mariaseverino.pinbook.exception.ConflictException;
import com.mariaseverino.pinbook.exception.ResourceNotFoundException;
import com.mariaseverino.pinbook.repository.*;
import com.mariaseverino.pinbook.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;
    private final LaneRepository laneRepository;
    private final OperatingHourRepository operatingHourRepository;

    public CreateSpaceBookingResponse createSpaceBooking(CreateSpaceBookingRequest request) {
        Space space = spaceRepository.findById(request.spaceId())
            .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));

        Instant endTime = request.startTime().plus(Duration.ofMinutes(request.durationMinutes()));

        DayOfWeek weekDay = DateTimeUtils.toDayOfWeek(request.startTime());
        LocalTime requestedStart = DateTimeUtils.toLocalTime(request.startTime());
        LocalTime requestedEnd = DateTimeUtils.toLocalTime(endTime);

        boolean isOperatingHour = operatingHourRepository.isWithinOperatingHours(
            request.spaceId(), weekDay, requestedStart, requestedEnd
        );

        if (!isOperatingHour) {
            throw new ConflictException("Horário fora do funcionamento do espaço");
        }

        List<Booking> conflicts = bookingRepository.findConflicting(null, request.spaceId(), request.startTime(), endTime);

        if (!conflicts.isEmpty()) {
            throw new ConflictException("Horário indisponível");
        }

        User user = userRepository.findById(request.clientId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Booking booking = bookingRepository.save(Booking.builder()
            .startTime(request.startTime())
            .endTime(endTime)
            .client(user)
            .space(space)
            .build());

        return  new CreateSpaceBookingResponse(
            booking.getId(),
            booking.getStartTime(),
            request.durationMinutes(),
            request.durationMinutes() * space.getPricePerMinute(),
            user.getName(), space.getName());
    }


}


// TODO: 1. quero reservar um espaco e outra pessoa tambem quer no mesmo dia e horario
// TODO: 2. quero reservar um espaco no mesmo dia e horario que alguem ja reservou uma pista
// TODO: 3. quero reservar uma pista no mesmo dia e horario q alguem ja reservou um espaco
// TODO: 4. quero reservar uma pista no mesmo dia e horaio que alguem ja reservou a pista


// TODO: 5. so pode ser possivel reservar uma pista q existe
// TODO: 6. so pode ser possivel reservar um espaco q existe
// TODO: 7 so pode ser possivel reservar em horario de funcionamento
// TODO: 8 o fim da reserva tem q ser antes do fim do horario de funcionamento