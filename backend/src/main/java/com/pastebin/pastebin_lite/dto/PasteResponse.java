package com.pastebin.pastebin_lite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasteResponse {
    private String content;
    
    @JsonProperty("remaining_views")
    private Integer remainingViews;
    
    @JsonProperty("expires_at")
    private String expiresAt;
    
    public PasteResponse() {
    }
    
    public PasteResponse(String content, Integer remainingViews, String expiresAt) {
        this.content = content;
        this.remainingViews = remainingViews;
        this.expiresAt = expiresAt;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getRemainingViews() {
        return remainingViews;
    }
    
    public void setRemainingViews(Integer remainingViews) {
        this.remainingViews = remainingViews;
    }
    
    public String getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
