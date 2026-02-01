package com.pastebin.pastebin_lite.controller;

import com.pastebin.pastebin_lite.service.PasteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ViewController {

    @Autowired
    private PasteService pasteService;

    /**
     * View paste as HTML page
     * GET /p/:id
     * This is required for automated tests
     */
    @GetMapping(value = "/p/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> viewPaste(
            @PathVariable String id,
            HttpServletRequest httpRequest) {

        Optional<String> contentOpt = pasteService.getPasteContentForView(id, httpRequest);

        if (contentOpt.isPresent()) {
            String content = contentOpt.get();

            // Simple HTML template with XSS protection (content is HTML-escaped)
            String html = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "    <meta charset=\"UTF-8\">" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "    <title>Paste - " + escapeHtml(id) + "</title>" +
                    "    <style>" +
                    "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
                    "        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f5f5f5; padding: 20px; }" +
                    "        .container { max-width: 900px; margin: 0 auto; background: white; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden; }" +
                    "        .header { background: #667eea; color: white; padding: 20px; }" +
                    "        .header h1 { font-size: 24px; }" +
                    "        .content { padding: 30px; }" +
                    "        pre { background: #f8f9fa; padding: 20px; border-radius: 6px; overflow-x: auto; white-space: pre-wrap; word-wrap: break-word; font-family: 'Courier New', monospace; font-size: 14px; line-height: 1.6; }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class=\"container\">" +
                    "        <div class=\"header\">" +
                    "            <h1>📋 Pastebin Lite</h1>" +
                    "        </div>" +
                    "        <div class=\"content\">" +
                    "            <pre>" + escapeHtml(content) + "</pre>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        } else {
            // Return 404 HTML page
            String errorHtml = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "    <meta charset=\"UTF-8\">" +
                    "    <title>404 - Paste Not Found</title>" +
                    "    <style>" +
                    "        body { font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; text-align: center; }" +
                    "        h1 { font-size: 72px; margin: 0; }" +
                    "        p { font-size: 24px; }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div>" +
                    "        <h1>404</h1>" +
                    "        <p>Paste not found or expired</p>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);
        }
    }

    /**
     * Escape HTML to prevent XSS attacks
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}