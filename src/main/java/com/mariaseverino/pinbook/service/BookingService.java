package com.mariaseverino.pinbook.service;

import com.mariaseverino.pinbook.dto.*;
import com.mariaseverino.pinbook.entity.*;
import com.mariaseverino.pinbook.exception.ConflictException;
import com.mariaseverino.pinbook.exception.ResourceNotFoundException;
import com.mariaseverino.pinbook.repository.*;
import com.mariaseverino.pinbook.util.DateTimeUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;
    private final OperatingHourRepository operatingHourRepository;
    private final LaneRepository laneRepository;

    @Transactional
    public CreateSpaceBookingResponse createSpaceBooking(CreateSpaceBookingRequest request) {
        User user = userRepository.findById(request.clientId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        //  Busca e bloqueia o recurso (Space) para evitar Race Conditions simultâneas
        Space space = spaceRepository.findByIdWithLock(request.spaceId())
            .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));

        // Calcula horários reais da reserva e horário bloqueado com manutenção
        Instant bookingStart = request.startTime();
        Instant bookingEnd = bookingStart.plus(Duration.ofMinutes(request.durationMinutes()));

        // Tempo total retido no calendário (Duração + Manutenção)
        Instant totalBlockedEnd = bookingEnd.plus(Duration.ofMinutes(space.getBatchMaintenanceTime()));

        // Validação de horário de funcionamento
        verifyOperationHour(request.spaceId(), bookingStart, bookingEnd);

        // Checa sobreposição no banco considerando o tempo total bloqueado
        verifySpaceConflict(request.spaceId(), bookingStart, totalBlockedEnd);

        Booking booking = bookingRepository.save(Booking.builder()
            .startTime(bookingStart)
            .endTime(totalBlockedEnd)
            .price(space.getPricePerMinute() * request.durationMinutes())
            .client(user)
            .space(space)
            .build());

        return new CreateSpaceBookingResponse(
            booking.getId(),
            bookingStart,
            request.durationMinutes(),
            space.getPricePerMinute() * request.durationMinutes(),
            user.getName(),
            space.getName()
        );
    }

    public Map<DayOfWeek, List<LocalTime>> getAvailableTimes(GetAvailableTimesRequest request){
        Space space = spaceRepository.findById(request.spaceId())
            .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));

        List<OperatingHour> operatingHours = operatingHourRepository.findAllBySpaceId(request.spaceId());
        Map<DayOfWeek, List<LocalTime>> availableTimes = new EnumMap<>(DayOfWeek.class);

        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        for (OperatingHour operatingHour : operatingHours) { //monday
            LocalDate date = LocalDate.now();

            LocalDateTime opening = LocalDateTime.of(
                date,
                operatingHour.getOpeningTime()
            );

            LocalDateTime closing = LocalDateTime.of(
                date,
                operatingHour.getClosingTime()
            );

            // Se fecha depois da meia-noite, o fechamento é no dia seguinte
            if (!closing.isAfter(opening)) {
                closing = closing.plusDays(1);
            }

            int totalSlotMinutes =
                request.periodInMinutes() + space.getBatchMaintenanceTime();

            LocalDateTime current = opening;

            while (!current.plusMinutes(totalSlotMinutes).isAfter(closing)) {

                LocalDateTime slotEnd =
                    current.plusMinutes(totalSlotMinutes);

                Instant startUnavailableTime =
                    current
                        .atZone(zoneId)
                        .toInstant();

                Instant endUnavailableTime =
                    slotEnd
                        .atZone(zoneId)
                        .toInstant();

                boolean hasConflicts =
                    bookingRepository.existsSpaceConflict(
                        request.spaceId(),
                        startUnavailableTime,
                        endUnavailableTime
                    );

                if (!hasConflicts) {
                    availableTimes
                        .computeIfAbsent(
                            operatingHour.getWeekDay(),
                            key -> new ArrayList<>()
                        )
                        .add(current.toLocalTime());
                }

                current = slotEnd;
            }
        }

        return availableTimes;
    }

    @Transactional
    public CreateLaneBookingResponse createLaneBooking(CreateLaneBookingRequest request) {
        User user = userRepository.findById(request.clientId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Busca e bloqueia o recurso (Space e lane) para evitar Race Conditions simultâneas
        Space space = spaceRepository.findByIdWithLock(request.spaceId())
            .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));

        Lane lane = laneRepository.findByIdWithLock(request.laneId())
            .orElseThrow(() -> new ResourceNotFoundException("Lane não encontrada"));

        // Calcula horários reais da reserva e horário bloqueado com manutenção
        Instant bookingStart = request.startTime();
        Instant bookingEnd = bookingStart.plus(Duration.ofMinutes(request.durationMinutes()));

        // Tempo total retido no calendário (Duração + Manutenção)
        Instant totalBlockedEnd = bookingEnd.plus(Duration.ofMinutes(space.getBatchMaintenanceTime()));

        // Validação de horário de funcionamento
        verifyOperationHour(request.spaceId(), bookingStart, bookingEnd);

        // Checa sobreposição no banco considerando o tempo total bloqueado
        verifySpaceConflict(request.spaceId(), bookingStart, totalBlockedEnd);
        verifyLaneConflict(request.laneId(), bookingStart, totalBlockedEnd);

        Booking booking = bookingRepository.save(Booking.builder()
            .startTime(bookingStart)
            .endTime(totalBlockedEnd)
            .price(space.getPricePerMinute() * request.durationMinutes())
            .client(user)
            .space(space)
            .lane(lane)
            .build());

        return new CreateLaneBookingResponse(
            booking.getId(),
            bookingStart,
            request.durationMinutes(),
            space.getPricePerMinute() * request.durationMinutes(),
            user.getName(),
            space.getName(),
            lane.getName()
        );
    }

    private void verifyOperationHour(UUID spaceId, Instant bookingStart, Instant bookingEnd){
        ZonedDateTime localStart = bookingStart.atZone(DateTimeUtils.APP_ZONE);
        ZonedDateTime localEnd = bookingEnd.atZone(DateTimeUtils.APP_ZONE);

        boolean isOperatingHour = operatingHourRepository.isWithinOperatingHours(
            spaceId,
            localStart.getDayOfWeek(),
            localStart.toLocalTime(),
            localEnd.toLocalTime()
        );

        if (!isOperatingHour) {
            throw new ConflictException("Horário fora do funcionamento do espaço");
        }
    }

    private void verifySpaceConflict(UUID spaceId, Instant bookingStart, Instant totalBlockedEnd){
        boolean hasSpaceConflicts = bookingRepository.existsSpaceConflict(
            spaceId,
            bookingStart,
            totalBlockedEnd
        );

        if (hasSpaceConflicts) {
            throw new ConflictException("Horário indisponível");
        }
    }

    private void verifyLaneConflict(UUID laneId, Instant bookingStart, Instant totalBlockedEnd){
        boolean hasLaneConflicts = bookingRepository.existsLaneConflict(
            laneId,
            bookingStart,
            totalBlockedEnd
        );

        if (hasLaneConflicts) {
            throw new ConflictException("Horário indisponível");
        }

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