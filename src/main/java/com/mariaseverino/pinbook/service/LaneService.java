package com.mariaseverino.pinbook.service;

import com.mariaseverino.pinbook.dto.CreateLaneRequest;
import com.mariaseverino.pinbook.dto.CreateLaneResponse;
import com.mariaseverino.pinbook.entity.Lane;
import com.mariaseverino.pinbook.entity.Space;
import com.mariaseverino.pinbook.exception.ResourceNotFoundException;
import com.mariaseverino.pinbook.repository.LaneRepository;
import com.mariaseverino.pinbook.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LaneService {
    private final LaneRepository laneRepository;
    private final SpaceRepository spaceRepository;

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
}
