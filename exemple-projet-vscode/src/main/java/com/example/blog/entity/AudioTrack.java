package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.AudioTrackStatus;

@Entity
@Table(name = "audiotracks")
public class AudioTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "media_file_id")
    private Integer mediaFileId;

    @Column(name = "bitrate")
    private Integer bitrate;

    @Column(name = "waveform_data", columnDefinition = "JSON")
    private String waveformData;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AudioTrackStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AudioTrackStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Integer getBitrate() {
        return bitrate;
    }
    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getMediaFileId() {
        return mediaFileId;
    }
    public void setMediaFileId(Integer mediaFileId) {
        this.mediaFileId = mediaFileId;
    }
    public void setStatus(AudioTrackStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getWaveformData() {
        return waveformData;
    }
    public void setWaveformData(String waveformData) {
        this.waveformData = waveformData;
    }

    public void genererWaveform() {
        // TODO: Implement genererWaveform logic
    }

    public void suspend() {
        if (this.status != AudioTrackStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = AudioTrackStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != AudioTrackStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = AudioTrackStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
