package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.CarouselSlideStatus;

@Entity
@Table(name = "carouselslides")
public class CarouselSlide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "featured_item_id")
    private Integer featuredItemId;

    @Column(name = "slide_num")
    private Integer slideNum;

    @Column(name = "caption")
    private String caption;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CarouselSlideStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CarouselSlideStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getFeaturedItemId() {
        return featuredItemId;
    }
    public void setFeaturedItemId(Integer featuredItemId) {
        this.featuredItemId = featuredItemId;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getSlideNum() {
        return slideNum;
    }
    public void setSlideNum(Integer slideNum) {
        this.slideNum = slideNum;
    }
    public void setStatus(CarouselSlideStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String genererSlide() {
        // TODO: Implement genererSlide logic
        return null;
    }

    public void suspend() {
        if (this.status != CarouselSlideStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = CarouselSlideStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != CarouselSlideStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = CarouselSlideStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
