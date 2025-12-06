package com.test.controller;

import com.test.entity.User;
import com.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User entity) {
        if (service.findById(id).isPresent()) {
            return ResponseEntity.ok(service.save(entity));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<User> suspend(@PathVariable Long id) {
        return ResponseEntity.ok(service.suspendUser(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<User> activate(@PathVariable Long id) {
        return ResponseEntity.ok(service.activateUser(id));
    }

}
