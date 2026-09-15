package com.mariaseverino.pinbook.service;

import com.mariaseverino.pinbook.dto.CreateSpaceRequest;
import com.mariaseverino.pinbook.entity.Space;
import com.mariaseverino.pinbook.entity.User;
import com.mariaseverino.pinbook.repository.SpaceRepository;
import com.mariaseverino.pinbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;

    public void createSpace(CreateSpaceRequest request, UUID ownerId){
        boolean spaceExists = spaceRepository.existsByName(request.name());

        if (spaceExists){
            throw new BadCredentialsException("Este espaco ja existe");
        }

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new BadCredentialsException("Usuario não encontrado"));


        Space newSpace = Space.builder()
                .name(request.name())
                .cep(request.cep())
                .description(request.description())
                .capacity(request.capacity())
                .owner(owner)
                .build();


        spaceRepository.save(newSpace);

    }
}
