package com.pastebin.pastebin_lite.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pastes")
public class Paste {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @Column(name = "expires_at")
    private Instant expiresAt;
    
    @Column(name = "max_views")
    private Integer maxViews;
    
    @Column(name = "current_views", nullable = false)
    private Integer currentViews = 0;
    
    public Paste() {
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (currentViews == null) {
            currentViews = 0;
        }
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    
    public Instant getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public Integer getMaxViews() {
        return maxViews;
    }
    
    public void setMaxViews(Integer maxViews) {
        this.maxViews = maxViews;
    }
    
    public Integer getCurrentViews() {
        return currentViews;
    }
    
    public void setCurrentViews(Integer currentViews) {
        this.currentViews = currentViews;
    }
}
