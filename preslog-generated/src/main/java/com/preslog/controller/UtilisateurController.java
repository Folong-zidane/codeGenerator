// Generated REST Controller
package com.preslog.controller;

import com.preslog.entity.Utilisateur;
import com.preslog.service.UtilisateurService;
import java.lang.Void;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
  private final UtilisateurService service;

  public UtilisateurController(UtilisateurService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Utilisateur> create(@RequestBody Utilisateur entity) {
    Utilisateur created = service.create(entity);
    return ResponseEntity.ok(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Utilisateur> getById(@PathVariable UUID id) {
    Optional<Utilisateur> entity = service.findById(id);
    return entity.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<Utilisateur>> getAll() {
    List<Utilisateur> entities = service.findAll();
    return ResponseEntity.ok(entities);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Utilisateur> update(@PathVariable UUID id,
      @RequestBody Utilisateur entity) {
    Utilisateur updated = service.update(entity);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
