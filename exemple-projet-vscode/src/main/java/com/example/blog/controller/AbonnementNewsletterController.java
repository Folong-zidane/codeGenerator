package com.example.blog.controller;

import com.example.blog.dto.AbonnementNewsletterCreateDto;
import com.example.blog.dto.AbonnementNewsletterReadDto;
import com.example.blog.dto.AbonnementNewsletterUpdateDto;
import com.example.blog.service.AbonnementNewsletterService;
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
@RequestMapping("/api/v1/abonnementnewsletters")
@RequiredArgsConstructor
@Slf4j
public class AbonnementNewsletterController {

    private final AbonnementNewsletterService service;

    @GetMapping
    public ResponseEntity<Page<AbonnementNewsletterReadDto>> findAll(Pageable pageable) {
        log.info("GET /api/v1/abonnementnewsletters - Finding all AbonnementNewsletters");
        Page<AbonnementNewsletterReadDto> result = service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AbonnementNewsletterReadDto>> findAllList() {
        log.info("GET /api/v1/abonnementnewsletters/all - Finding all AbonnementNewsletters as list");
        List<AbonnementNewsletterReadDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbonnementNewsletterReadDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/abonnementnewsletters/{} - Finding AbonnementNewsletter by id", id);
        AbonnementNewsletterReadDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AbonnementNewsletterReadDto> create(@Valid @RequestBody AbonnementNewsletterCreateDto createDto) {
        log.info("POST /api/v1/abonnementnewsletters - Creating new AbonnementNewsletter: {}", createDto);
        AbonnementNewsletterReadDto created = service.create(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbonnementNewsletterReadDto> update(@PathVariable Long id, @Valid @RequestBody AbonnementNewsletterUpdateDto updateDto) {
        log.info("PUT /api/v1/abonnementnewsletters/{} - Updating AbonnementNewsletter: {}", id, updateDto);
        AbonnementNewsletterReadDto updated = service.update(id, updateDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/abonnementnewsletters/{} - Deleting AbonnementNewsletter", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<AbonnementNewsletterReadDto> suspend(@PathVariable Long id) {
        log.info("PATCH /api/v1/abonnementnewsletters/{}/suspend - Suspending AbonnementNewsletter", id);
        AbonnementNewsletterReadDto suspended = service.suspendAbonnementNewsletter(id);
        return ResponseEntity.ok(suspended);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<AbonnementNewsletterReadDto> activate(@PathVariable Long id) {
        log.info("PATCH /api/v1/abonnementnewsletters/{}/activate - Activating AbonnementNewsletter", id);
        AbonnementNewsletterReadDto activated = service.activateAbonnementNewsletter(id);
        return ResponseEntity.ok(activated);
    }

}
