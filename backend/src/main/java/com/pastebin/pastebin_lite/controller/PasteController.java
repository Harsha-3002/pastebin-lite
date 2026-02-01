package com.pastebin.pastebin_lite.controller;

import com.pastebin.pastebin_lite.dto.*;
import com.pastebin.pastebin_lite.service.PasteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PasteController {

    @Autowired
    private PasteService pasteService;

    /**
     * Health check endpoint
     * GET /api/healthz
     */
    @GetMapping("/healthz")
    public ResponseEntity<HealthResponse> healthCheck() {
        // You can add database connectivity check here if needed
        HealthResponse response = new HealthResponse(true);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new paste
     * POST /api/pastes
     */
    @PostMapping("/pastes")
    public ResponseEntity<?> createPaste(
            @Valid @RequestBody CreatePasteRequest request,
            HttpServletRequest httpRequest) {
        try {
            CreatePasteResponse response = pasteService.createPaste(request, httpRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("creation_failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get paste by ID (API endpoint)
     * GET /api/pastes/:id
     */
    @GetMapping("/pastes/{id}")
    public ResponseEntity<?> getPaste(
            @PathVariable String id,
            HttpServletRequest httpRequest) {
        Optional<PasteResponse> pasteOpt = pasteService.getPaste(id, httpRequest);

        if (pasteOpt.isPresent()) {
            return ResponseEntity.ok(pasteOpt.get());
        } else {
            ErrorResponse error = new ErrorResponse("not_found", "Paste not found or expired");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}