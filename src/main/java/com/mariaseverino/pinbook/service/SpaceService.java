package com.mariaseverino.pinbook.service;

import com.mariaseverino.pinbook.dto.CreateSpaceRequest;
import com.mariaseverino.pinbook.dto.CreateSpaceResponse;
import com.mariaseverino.pinbook.dto.UpdateSpaceRequest;
import com.mariaseverino.pinbook.dto.UpdateSpaceResponse;
import com.mariaseverino.pinbook.entity.Space;
import com.mariaseverino.pinbook.entity.User;
import com.mariaseverino.pinbook.exception.ConflictException;
import com.mariaseverino.pinbook.exception.ResourceNotFoundException;
import com.mariaseverino.pinbook.repository.BookingRepository;
import com.mariaseverino.pinbook.repository.SpaceRepository;
import com.mariaseverino.pinbook.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;
    private  final BookingRepository bookingRepository;

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

    @Transactional
    public UpdateSpaceResponse updateSpace(UpdateSpaceRequest request, UUID spaceId) {
        Space space = spaceRepository.findByIdWithLock(spaceId)
            .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));

        // Valida se o novo nome já pertence a outro espaço
        boolean nameIsChanging = !space.getName().equalsIgnoreCase(request.name());
        if (nameIsChanging && spaceRepository.existsByName(request.name())) {
            throw new ConflictException("Já existe um espaço com esse nome");
        }

        Space updateSpace = Space.builder()
            .id(spaceId)
            .name(request.name())
            .cep(space.getCep())
            .description(request.description())
            .capacity(request.capacity())
            .batchMaintenanceTime(request.batchMaintenanceTime())
            .pricePerMinute(request.pricePerMinute())
            .owner(space.getOwner())
            .build();

        Space updatedSpace = spaceRepository.save(updateSpace);

        return new UpdateSpaceResponse(
            updatedSpace.getId(),
            updatedSpace.getName(),
            updatedSpace.getDescription(),
            updatedSpace.getCep(),
            updatedSpace.getCapacity(),
            updatedSpace.getBatchMaintenanceTime(),
            updatedSpace.getPricePerMinute()
        );
    }

    @Transactional
    public void deleteSpace(UUID spaceId){
        Space space = spaceRepository.findByIdWithLock(spaceId)
            .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));

        boolean hasAnyBooking = bookingRepository.existsAnyBooking(spaceId);

        if (hasAnyBooking) {
            throw new ConflictException(
                "Não é possível deletar. O espaço possui um agendamento."
            );
        }

        spaceRepository.delete(space);

    }
}
