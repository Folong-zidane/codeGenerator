package com.analysis.test.service;

import com.analysis.test.entity.User;
import com.analysis.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import com.analysis.test.enums.UserStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public User save(User entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public User suspendUser(Long id) {
        User entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        entity.suspend();
        return repository.save(entity);
    }

    public User activateUser(Long id) {
        User entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        entity.activate();
        return repository.save(entity);
    }

}
