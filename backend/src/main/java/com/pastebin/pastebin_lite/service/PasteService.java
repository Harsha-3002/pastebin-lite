package com.pastebin.pastebin_lite.service;

import com.pastebin.pastebin_lite.dto.CreatePasteRequest;
import com.pastebin.pastebin_lite.dto.CreatePasteResponse;
import com.pastebin.pastebin_lite.dto.PasteResponse;
import com.pastebin.pastebin_lite.model.Paste;
import com.pastebin.pastebin_lite.repository.PasteRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class PasteService {

    @Autowired
    private PasteRepository pasteRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${test.mode:false}")
    private boolean testMode;

    /**
     * Creates a new paste
     */
    public CreatePasteResponse createPaste(CreatePasteRequest request, HttpServletRequest httpRequest) {
        Paste paste = new Paste();
        paste.setContent(request.getContent());
        paste.setCurrentViews(0);

        // Set TTL if provided
        if (request.getTtlSeconds() != null) {
            Instant now = getCurrentTime(httpRequest);
            paste.setExpiresAt(now.plusSeconds(request.getTtlSeconds()));
        }

        // Set max views if provided
        if (request.getMaxViews() != null) {
            paste.setMaxViews(request.getMaxViews());
        }

        Paste savedPaste = pasteRepository.save(paste);

        String url = baseUrl + "/p/" + savedPaste.getId();
        return new CreatePasteResponse(savedPaste.getId(), url);
    }

    /**
     * Gets a paste by ID and increments view count
     * Returns empty if paste is expired or view limit exceeded
     */
    @Transactional
    public Optional<PasteResponse> getPaste(String id, HttpServletRequest httpRequest) {
        Optional<Paste> pasteOpt = pasteRepository.findById(id);

        if (pasteOpt.isEmpty()) {
            return Optional.empty();
        }

        Paste paste = pasteOpt.get();
        Instant now = getCurrentTime(httpRequest);

        // Check if expired by time
        if (paste.getExpiresAt() != null && now.isAfter(paste.getExpiresAt())) {
            return Optional.empty();
        }

        // Check if view limit exceeded
        if (paste.getMaxViews() != null && paste.getCurrentViews() >= paste.getMaxViews()) {
            return Optional.empty();
        }

        // Increment view count
        paste.setCurrentViews(paste.getCurrentViews() + 1);
        pasteRepository.save(paste);

        // Build response
        PasteResponse response = new PasteResponse();
        response.setContent(paste.getContent());

        // Calculate remaining views
        if (paste.getMaxViews() != null) {
            int remaining = paste.getMaxViews() - paste.getCurrentViews();
            response.setRemainingViews(Math.max(remaining, 0)); // Never negative
        } else {
            response.setRemainingViews(null); // Unlimited
        }

        // Set expires_at
        if (paste.getExpiresAt() != null) {
            response.setExpiresAt(paste.getExpiresAt().toString());
        } else {
            response.setExpiresAt(null); // No expiry
        }

        return Optional.of(response);
    }

    /**
     * Gets paste content for HTML view (doesn't increment view count)
     */
    public Optional<String> getPasteContentForView(String id, HttpServletRequest httpRequest) {
        Optional<Paste> pasteOpt = pasteRepository.findById(id);

        if (pasteOpt.isEmpty()) {
            return Optional.empty();
        }

        Paste paste = pasteOpt.get();
        Instant now = getCurrentTime(httpRequest);

        // Check if expired by time
        if (paste.getExpiresAt() != null && now.isAfter(paste.getExpiresAt())) {
            return Optional.empty();
        }

        // Check if view limit exceeded
        if (paste.getMaxViews() != null && paste.getCurrentViews() >= paste.getMaxViews()) {
            return Optional.empty();
        }

        return Optional.of(paste.getContent());
    }

    /**
     * Gets current time - supports deterministic time for testing
     * Uses x-test-now-ms header if TEST_MODE=true
     */
    private Instant getCurrentTime(HttpServletRequest request) {
        if (testMode) {
            String testNowHeader = request.getHeader("x-test-now-ms");
            if (testNowHeader != null && !testNowHeader.isEmpty()) {
                try {
                    long millis = Long.parseLong(testNowHeader);
                    return Instant.ofEpochMilli(millis);
                } catch (NumberFormatException e) {
                    // Fall back to system time if header is invalid
                }
            }
        }
        return Instant.now();
    }
}