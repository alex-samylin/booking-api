package com.hostfully.booking.service;

import com.hostfully.booking.dto.CreateBlockDto;
import com.hostfully.booking.model.RentalProperty;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.hostfully.booking.dto.UpdateBlockDto;
import com.hostfully.booking.model.Block;
import com.hostfully.booking.repository.BlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockService {

    private final BookingAvailabilityService bookingAvailabilityService;
    private final BlockRepository blockRepository;
    private final RentalPropertyService rentalPropertyService;

    public Block getBlockById(Long blockId) {
        return blockRepository.findById(blockId)
                .orElseThrow(() -> new EntityNotFoundException("Block not found by id: " + blockId));
    }

    public Block createBlock(CreateBlockDto dto) {
        RentalProperty property = rentalPropertyService.getRentalPropertyById(dto.getRentalPropertyId());

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date!");
        }

        bookingAvailabilityService.validatePeriodAvailable(property.getId(), dto.getStartDate(), dto.getEndDate());

        Block block = Block.builder()
                .rentalPropertyId(property.getId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .source(dto.getSource())
                .build();

        return blockRepository.save(block);
    }

    public Block updateBlock(Long blockId, UpdateBlockDto dto) {
        Block block = getBlockById(blockId);

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date!");
        }

        bookingAvailabilityService.validatePeriodAvailable(block.getRentalPropertyId(), dto.getStartDate(), dto.getEndDate(), empty(), of(blockId));

        block.setStartDate(dto.getStartDate());
        block.setEndDate(dto.getEndDate());
        block.setReason(dto.getReason());

        return blockRepository.save(block);
    }

    public void deleteBlock(Long blockId) {
        blockRepository.deleteById(blockId);
    }
}

