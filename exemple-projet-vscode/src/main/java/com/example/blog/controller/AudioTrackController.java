package com.example.blog.controller;

import com.example.blog.dto.AudioTrackCreateDto;
import com.example.blog.dto.AudioTrackReadDto;
import com.example.blog.dto.AudioTrackUpdateDto;
import com.example.blog.service.AudioTrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audiotracks")
@RequiredArgsConstructor
@Slf4j
public class AudioTrackController {

    private final AudioTrackService service;

    @GetMapping
    public ResponseEntity<Page<AudioTrackReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/audiotracks - Finding all AudioTracks");
        Page<AudioTrackReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AudioTrackReadDto>> findAllList() {
        log.info("GET /api/v1/audiotracks/all - Finding all AudioTracks as list");
        List<AudioTrackReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudioTrackReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/audiotracks/{} - Finding AudioTrack by id", id);
        AudioTrackReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AudioTrackReadDto> create(@Valid @RequestBody AudioTrackCreateDto createDto) {
        log.info("POST /api/v1/audiotracks - Creating new AudioTrack: {}", createDto);
        AudioTrackReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AudioTrackReadDto> update(@PathVariable Long id, @Valid @RequestBody AudioTrackUpdateDto updateDto) {
        log.info("PUT /api/v1/audiotracks/{} - Updating AudioTrack: {}", id, updateDto);
        AudioTrackReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/audiotracks/{} - Deleting AudioTrack", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<AudioTrackReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/audiotracks/{}/suspend - Suspending AudioTrack", id);
        AudioTrackReadDto suspended = service.suspendAudioTrack(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AudioTrackReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/audiotracks/{}/activate - Activating AudioTrack", id);
        AudioTrackReadDto activated = service.activateAudioTrack(id);
        return ResponseEntity.ok(activated);
    }

}
