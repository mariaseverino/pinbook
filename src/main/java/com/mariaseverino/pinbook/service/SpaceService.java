package com.mariaseverino.pinbook.service;

import com.mariaseverino.pinbook.dto.CreateSpaceRequest;
import com.mariaseverino.pinbook.dto.CreateSpaceResponse;
import com.mariaseverino.pinbook.entity.Space;
import com.mariaseverino.pinbook.entity.User;
import com.mariaseverino.pinbook.exception.ConflictException;
import com.mariaseverino.pinbook.exception.ResourceNotFoundException;
import com.mariaseverino.pinbook.repository.SpaceRepository;
import com.mariaseverino.pinbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;

    public CreateSpaceResponse createSpace(CreateSpaceRequest request, UUID ownerId){
        boolean spaceExists = spaceRepository.existsByName(request.name());

        if (spaceExists) {
            throw new ConflictException("Já existe um espaço com esse nome");
        }

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        Space newSpace = Space.builder()
                .name(request.name())
                .cep(request.cep())
                .description(request.description())
                .capacity(request.capacity())
                .batchMaintenanceTime(request.batchMaintenanceTime())
                .pricePerMinute(request.pricePerMinute())
                .owner(owner)
                .build();

        Space space = spaceRepository.save(newSpace);

        return new CreateSpaceResponse(
            space.getId(),
            space.getName(),
            space.getDescription(),
            space.getCep(),
            space.getCapacity(),
            space.getBatchMaintenanceTime(),
            space.getPricePerMinute());
    }

    public CreateSpaceResponse getSpace(UUID spaceId){
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));

        return new CreateSpaceResponse(
            space.getId(),
            space.getName(),
            space.getDescription(),
            space.getCep(),
            space.getCapacity(),
            space.getBatchMaintenanceTime(),
            space.getPricePerMinute());
    }

}
