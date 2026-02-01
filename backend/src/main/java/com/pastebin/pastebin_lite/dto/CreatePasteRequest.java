package com.pastebin.pastebin_lite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreatePasteRequest {
    
    @NotBlank(message = "Content cannot be empty")
    private String content;
    
    @JsonProperty("ttl_seconds")
    @Min(value = 1, message = "ttl_seconds must be at least 1")
    private Integer ttlSeconds;
    
    @JsonProperty("max_views")
    @Min(value = 1, message = "max_views must be at least 1")
    private Integer maxViews;
    
    public CreatePasteRequest() {
    }
    
    public CreatePasteRequest(String content, Integer ttlSeconds, Integer maxViews) {
        this.content = content;
        this.ttlSeconds = ttlSeconds;
        this.maxViews = maxViews;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getTtlSeconds() {
        return ttlSeconds;
    }
    
    public void setTtlSeconds(Integer ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }
    
    public Integer getMaxViews() {
        return maxViews;
    }
    
    public void setMaxViews(Integer maxViews) {
        this.maxViews = maxViews;
    }
}
