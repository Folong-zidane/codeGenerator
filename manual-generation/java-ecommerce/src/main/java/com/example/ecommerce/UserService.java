package com.example.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
    
    public User save(User user) {
        if (!user.validateEmail()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return userRepository.save(user);
    }
    
    public User createUser(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = new User(username, email, password);
        return userRepository.save(user);
    }
    
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
    
    public User activateUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.activate();
        return userRepository.save(user);
    }
    
    public User suspendUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.suspend();
        return userRepository.save(user);
    }
}
