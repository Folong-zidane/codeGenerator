package com.example.blog.service;

import com.example.blog.dto.AuthRequest;
import com.example.blog.dto.AuthResponse;
import com.example.blog.dto.RegisterRequest;
import com.example.blog.entity.Utilisateur;
import com.example.blog.enums.UserRole;
import com.example.blog.enums.UtilisateurStatus;
import com.example.blog.exception.ValidationException;
import com.example.blog.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;

    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Un compte avec cet email existe déjà");
        }

        Utilisateur user = new Utilisateur();
        user.setEmail(request.getEmail());
        user.setMotDePasse(request.getMotDePasse()); // TODO: Hash password
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setRole(UserRole.USER);
        user.setActif(true);
        user.setStatus(UtilisateurStatus.ACTIVE);
        user.setDateCreation(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());

        Utilisateur saved = utilisateurRepository.save(user);

        return AuthResponse.builder()
            .token("fake-jwt-token") // TODO: Generate real JWT
            .email(saved.getEmail())
            .role(saved.getRole())
            .message("Inscription réussie")
            .build();
    }

    public AuthResponse login(AuthRequest request) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            throw new ValidationException("Email ou mot de passe incorrect");
        }

        Utilisateur user = userOpt.get();
        
        if (!user.getActif()) {
            throw new ValidationException("Votre compte est désactivé");
        }

        // TODO: Verify password hash
        if (!user.getMotDePasse().equals(request.getMotDePasse())) {
            throw new ValidationException("Email ou mot de passe incorrect");
        }

        user.setDerniereConnexion(LocalDateTime.now());
        utilisateurRepository.save(user);

        return AuthResponse.builder()
            .token("fake-jwt-token") // TODO: Generate real JWT
            .email(user.getEmail())
            .role(user.getRole())
            .message("Connexion réussie")
            .build();
    }

    public AuthResponse createRedacteur(RegisterRequest request) {
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Un compte avec cet email existe déjà");
        }

        Utilisateur redacteur = new Utilisateur();
        redacteur.setEmail(request.getEmail());
        redacteur.setMotDePasse(request.getMotDePasse()); // TODO: Hash password
        redacteur.setNom(request.getNom());
        redacteur.setPrenom(request.getPrenom());
        redacteur.setRole(UserRole.REDACTEUR);
        redacteur.setActif(true);
        redacteur.setStatus(UtilisateurStatus.ACTIVE);
        redacteur.setDateCreation(LocalDateTime.now());
        redacteur.setCreatedAt(LocalDateTime.now());

        Utilisateur saved = utilisateurRepository.save(redacteur);

        return AuthResponse.builder()
            .token("fake-jwt-token")
            .email(saved.getEmail())
            .role(saved.getRole())
            .message("Rédacteur créé avec succès")
            .build();
    }

    @Transactional(readOnly = true)
    public void createSuperAdminIfNotExists() {
        if (utilisateurRepository.findByRole(UserRole.SUPER_ADMIN).isEmpty()) {
            Utilisateur superAdmin = new Utilisateur();
            superAdmin.setEmail("admin@blog.com");
            superAdmin.setMotDePasse("admin123"); // TODO: Hash password
            superAdmin.setNom("Super");
            superAdmin.setPrenom("Admin");
            superAdmin.setRole(UserRole.SUPER_ADMIN);
            superAdmin.setActif(true);
            superAdmin.setStatus(UtilisateurStatus.ACTIVE);
            superAdmin.setDateCreation(LocalDateTime.now());
            superAdmin.setCreatedAt(LocalDateTime.now());
            
            utilisateurRepository.save(superAdmin);
        }
    }
}