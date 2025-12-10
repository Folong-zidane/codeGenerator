package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.VideoStreamStatus;

@Entity
@Table(name = "videostreams")
public class VideoStream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "media_file_id")
    private Integer mediaFileId;

    @Column(name = "segment_url")
    private String segmentUrl;

    @Column(name = "bitrate")
    private Integer bitrate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VideoStreamStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public VideoStreamStatus getStatus() {
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
    public String getSegmentUrl() {
        return segmentUrl;
    }
    public void setSegmentUrl(String segmentUrl) {
        this.segmentUrl = segmentUrl;
    }
    public void setStatus(VideoStreamStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void genererSegments() {
        // TODO: Implement genererSegments logic
    }

    public String adaptiveStreaming() {
        // TODO: Implement adaptiveStreaming logic
        return "";
    }

    public void suspend() {
        if (this.status != VideoStreamStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = VideoStreamStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != VideoStreamStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = VideoStreamStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
