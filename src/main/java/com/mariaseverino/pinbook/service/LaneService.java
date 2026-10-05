package com.mariaseverino.pinbook.service;

import com.mariaseverino.pinbook.dto.*;
import com.mariaseverino.pinbook.entity.Lane;
import com.mariaseverino.pinbook.entity.Space;
import com.mariaseverino.pinbook.exception.ConflictException;
import com.mariaseverino.pinbook.exception.ResourceNotFoundException;
import com.mariaseverino.pinbook.repository.BookingRepository;
import com.mariaseverino.pinbook.repository.LaneRepository;
import com.mariaseverino.pinbook.repository.SpaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LaneService {
    private final LaneRepository laneRepository;
    private final SpaceRepository spaceRepository;
    private  final BookingRepository bookingRepository;

    public CreateLaneResponse createLane(CreateLaneRequest request){
        Space space = spaceRepository.findById(request.spaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));

        boolean laneExists = laneRepository.existsByName(request.name());

        if (laneExists){
            throw new ResourceNotFoundException("Esta lane ja existe");
        }

        Lane newLane = Lane.builder().
                name(request.name())
                .capacity(request.capacity())
                .space(space)
                .pricePerMinute(request.pricePerMinute())
                .build();

        Lane lane = laneRepository.save(newLane);

        return new CreateLaneResponse(
            lane.getId(),
            lane.getName(),
            lane.getCapacity(),
            lane.getPricePerMinute(),
            lane.getSpace().getId()
        );
    }

    @Transactional
    public UpdateLaneResponse updateLane(UpdateLaneRequest request, UUID laneId) {
        Lane lane = laneRepository.findByIdWithLock(laneId).orElseThrow(() -> new ResourceNotFoundException("Pista não encontrada"));

        // Valida se o novo nome já pertence a outra pista
        boolean nameIsChanging = !lane.getName().equalsIgnoreCase(request.name());
        if (nameIsChanging && laneRepository.existsByName(request.name())) {
            throw new ConflictException("Já existe uma pista com esse nome");
        }

        Lane newLane = Lane.builder()
            .id(laneId)
            .name(request.name())
            .capacity(request.capacity())
            .space(lane.getSpace())
            .pricePerMinute(request.pricePerMinute())
            .build();

        Lane updatedLane = laneRepository.save(newLane);

        return new UpdateLaneResponse(
            updatedLane.getId(),
            updatedLane.getName(),
            updatedLane.getCapacity(),
            updatedLane.getPricePerMinute()
        );
    }

    @Transactional
    public void deleteLane(UUID laneId){
        Lane lane = laneRepository.findByIdWithLock(laneId)
            .orElseThrow(() -> new ResourceNotFoundException("Pista não encontrada"));

        boolean hasAnyBooking = bookingRepository.existsAnyBooking(laneId);

        if (hasAnyBooking) {
            throw new ConflictException(
                "Não é possível deletar. A pista possui um agendamento."
            );
        }

        laneRepository.delete(lane);

    }
}
