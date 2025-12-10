package com.example.blog.service;

import com.example.blog.entity.MediaFile;
import com.example.blog.entity.MediaVariant;
import com.example.blog.repository.MediaFileRepository;
import com.example.blog.repository.MediaVariantRepository;
import com.example.blog.dto.MediaFileReadDto;
import com.example.blog.exception.EntityNotFoundException;
import com.example.blog.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MediaService {

    private final MediaFileRepository mediaFileRepository;
    private final MediaVariantRepository mediaVariantRepository;
    
    private static final String UPLOAD_DIR = "uploads/";
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp");
    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList("video/mp4", "video/avi", "video/mov");
    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList("application/pdf", "application/msword");
    private static final List<String> ALLOWED_AUDIO_TYPES = Arrays.asList("audio/mpeg", "audio/wav", "audio/ogg");

    public MediaFileReadDto uploadFile(MultipartFile file, String legende, String altText) {
        validateFile(file);
        
        try {
            String hash = calculateSHA256(file.getBytes());
            
            Optional<MediaFile> existing = mediaFileRepository.findByHashSha256(hash);
            if (existing.isPresent()) {
                return convertToReadDto(existing.get());
            }
            
            MediaFile mediaFile = new MediaFile();
            mediaFile.setNomOriginal(file.getOriginalFilename());
            mediaFile.setTypeMedia(determineMediaType(file.getContentType()));
            mediaFile.setTailleFichier(file.getSize());
            mediaFile.setTypeMime(file.getContentType());
            mediaFile.setHashSha256(hash);
            mediaFile.setLegende(legende);
            mediaFile.setAltText(altText);
            mediaFile.setDateCreation(LocalDateTime.now());
            
            String fileName = hash + getFileExtension(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            
            mediaFile.setCheminFichier(filePath.toString());
            mediaFile.setUrlAcces("/uploads/" + fileName);
            
            MediaFile saved = mediaFileRepository.save(mediaFile);
            
            if (ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
                createImageVariants(saved, file.getBytes());
            }
            
            return convertToReadDto(saved);
            
        } catch (Exception e) {
            throw new ValidationException("Erreur lors de l'upload: " + e.getMessage());
        }
    }
    
    public MediaFileReadDto getById(UUID id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MediaFile", id));
        return convertToReadDto(mediaFile);
    }
    
    public MediaFileReadDto getByHash(String hash) {
        MediaFile mediaFile = mediaFileRepository.findByHashSha256(hash)
            .orElseThrow(() -> new EntityNotFoundException("MediaFile avec hash: " + hash));
        return convertToReadDto(mediaFile);
    }
    
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("Le fichier est vide");
        }
        
        if (file.getSize() > 50 * 1024 * 1024) { // 50MB max
            throw new ValidationException("Le fichier est trop volumineux (max 50MB)");
        }
        
        String contentType = file.getContentType();
        if (!isAllowedFileType(contentType)) {
            throw new ValidationException("Type de fichier non supporté: " + contentType);
        }
    }
    
    private boolean isAllowedFileType(String contentType) {
        return ALLOWED_IMAGE_TYPES.contains(contentType) ||
               ALLOWED_VIDEO_TYPES.contains(contentType) ||
               ALLOWED_DOCUMENT_TYPES.contains(contentType) ||
               ALLOWED_AUDIO_TYPES.contains(contentType);
    }
    
    private String determineMediaType(String contentType) {
        if (ALLOWED_IMAGE_TYPES.contains(contentType)) return "IMAGE";
        if (ALLOWED_VIDEO_TYPES.contains(contentType)) return "VIDEO";
        if (ALLOWED_DOCUMENT_TYPES.contains(contentType)) return "DOCUMENT";
        if (ALLOWED_AUDIO_TYPES.contains(contentType)) return "AUDIO";
        return "OTHER";
    }
    
    private String calculateSHA256(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    private void createImageVariants(MediaFile mediaFile, byte[] originalData) {
        // Créer les variantes d'image (thumbnail, medium, large)
        int[] widths = {150, 400, 800};
        
        for (int width : widths) {
            MediaVariant variant = new MediaVariant();
            variant.setMediaFileId(mediaFile.getId());
            variant.setWidth(width);
            variant.setUrlS3(mediaFile.getUrlAcces().replace(".", "_" + width + "."));
            variant.setCreatedAt(LocalDateTime.now());
            mediaVariantRepository.save(variant);
        }
    }
    
    private MediaFileReadDto convertToReadDto(MediaFile mediaFile) {
        MediaFileReadDto dto = new MediaFileReadDto();
        dto.setId(mediaFile.getId());
        dto.setNomOriginal(mediaFile.getNomOriginal());
        dto.setTypeMedia(mediaFile.getTypeMedia());
        dto.setTailleFichier(mediaFile.getTailleFichier());
        dto.setTypeMime(mediaFile.getTypeMime());
        dto.setHashSha256(mediaFile.getHashSha256());
        dto.setUrlAcces(mediaFile.getUrlAcces());
        dto.setLegende(mediaFile.getLegende());
        dto.setAltText(mediaFile.getAltText());
        dto.setDateCreation(mediaFile.getDateCreation());
        return dto;
    }
}