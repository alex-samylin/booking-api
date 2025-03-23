package com.hostfully.booking.controller;

import com.hostfully.booking.dto.CreateBlockDto;
import com.hostfully.booking.dto.UpdateBlockDto;
import com.hostfully.booking.model.Block;
import com.hostfully.booking.service.BlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor
@Slf4j
public class BlockController {

    private final BlockService blockService;

    @GetMapping("/{id}")
    public Block getBlock(@PathVariable Long id) {
        return blockService.getBlockById(id);
    }

    @PostMapping
    public Block createBlock(@RequestBody @Valid CreateBlockDto dto) {
        log.info("Creating block: {}", dto);
        return blockService.createBlock(dto);
    }

    @PutMapping("/{id}")
    public Block updateBlock(@PathVariable Long id, @RequestBody @Valid UpdateBlockDto dto) {
        log.info("Updating block with ID {}, {}", id, dto);
        return blockService.updateBlock(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteBlock(@PathVariable Long id) {
        log.info("Deleting block with ID {}", id);
        blockService.deleteBlock(id);
    }
}

