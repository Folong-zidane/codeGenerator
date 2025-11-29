// Generated Service Layer
package com.preslog.service;

import com.preslog.entity.Utilisateur;
import com.preslog.repository.UtilisateurRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UtilisateurService {
  private final UtilisateurRepository repository;

  public UtilisateurService(UtilisateurRepository repository) {
    this.repository = repository;
  }

  public Utilisateur create(Utilisateur entity) {
    return repository.save(entity);
  }

  @Transactional(
      readOnly = true
  )
  public Optional<Utilisateur> findById(UUID id) {
    return repository.findById(id);
  }

  @Transactional(
      readOnly = true
  )
  public List<Utilisateur> findAll() {
    return repository.findAll();
  }

  public Utilisateur update(Utilisateur entity) {
    return repository.save(entity);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }
}
