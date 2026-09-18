package com.mariaseverino.pinbook.service;

import com.mariaseverino.pinbook.dto.CreateOperatingHoursRequest;
import com.mariaseverino.pinbook.dto.CreateOperatingHoursResponse;
import com.mariaseverino.pinbook.entity.OperatingHour;
import com.mariaseverino.pinbook.entity.Space;
import com.mariaseverino.pinbook.exception.ResourceNotFoundException;
import com.mariaseverino.pinbook.repository.OperatingHourRepository;
import com.mariaseverino.pinbook.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperatingHourService {
    private final OperatingHourRepository operatingHourRepository;
    private final SpaceRepository spaceRepository;

    public List<CreateOperatingHoursResponse.OperatingHourItem> createBulk(CreateOperatingHoursRequest request,
                                                                           UUID spaceId){
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new ResourceNotFoundException("Espaço não encontrado"));

        List<CreateOperatingHoursRequest.OperatingHourItem> operatingHours = request.hours();

        Set<OperatingHour.WeekDay> weekDays = operatingHours.stream()
            .map(CreateOperatingHoursRequest.OperatingHourItem::weekDay)
            .collect(Collectors.toSet());

        if (weekDays.size() != operatingHours.size()) {
            throw new IllegalArgumentException("Não pode haver dias da semana duplicados");
        }

        List<OperatingHour> entitiesToSave = operatingHours.stream()
            .map(item -> OperatingHour.builder()
                .weekDay(item.weekDay())
                .openingTime(item.openingTime())
                .closingTime(item.closingTime())
                .active(item.active())
                .space(space)
                .build())
            .toList();

        List<OperatingHour> savedEntities = operatingHourRepository.saveAll(entitiesToSave);

        return savedEntities.stream()
            .map(this::toResponseItem)
            .toList();

    }

    public List<CreateOperatingHoursResponse.OperatingHourItem> list(UUID spaceId){
        List<OperatingHour> operatingHours = operatingHourRepository.findAllBySpaceId(spaceId);

        return operatingHours.stream()
            .map(this::toResponseItem)
            .toList();
    }

    private CreateOperatingHoursResponse.OperatingHourItem toResponseItem(OperatingHour entity) {
        return new CreateOperatingHoursResponse.OperatingHourItem(
            entity.getId(),
            entity.getWeekDay(),
            entity.isActive(),
            entity.getOpeningTime(),
            entity.getClosingTime()
        );
    }
}
