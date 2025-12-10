package com.example.blog.controller;

import com.example.blog.dto.VideoStreamCreateDto;
import com.example.blog.dto.VideoStreamReadDto;
import com.example.blog.dto.VideoStreamUpdateDto;
import com.example.blog.service.VideoStreamService;
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
@RequestMapping("/api/v1/videostreams")
@RequiredArgsConstructor
@Slf4j
public class VideoStreamController {

    private final VideoStreamService service;

    @GetMapping
    public ResponseEntity<Page<VideoStreamReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/videostreams - Finding all VideoStreams");
        Page<VideoStreamReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<VideoStreamReadDto>> findAllList() {
        log.info("GET /api/v1/videostreams/all - Finding all VideoStreams as list");
        List<VideoStreamReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoStreamReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/videostreams/{} - Finding VideoStream by id", id);
        VideoStreamReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<VideoStreamReadDto> create(@Valid @RequestBody VideoStreamCreateDto createDto) {
        log.info("POST /api/v1/videostreams - Creating new VideoStream: {}", createDto);
        VideoStreamReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoStreamReadDto> update(@PathVariable Long id, @Valid @RequestBody VideoStreamUpdateDto updateDto) {
        log.info("PUT /api/v1/videostreams/{} - Updating VideoStream: {}", id, updateDto);
        VideoStreamReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/videostreams/{} - Deleting VideoStream", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<VideoStreamReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/videostreams/{}/suspend - Suspending VideoStream", id);
        VideoStreamReadDto suspended = service.suspendVideoStream(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<VideoStreamReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/videostreams/{}/activate - Activating VideoStream", id);
        VideoStreamReadDto activated = service.activateVideoStream(id);
        return ResponseEntity.ok(activated);
    }

}
