package com.pastebin.pastebin_lite.repository;

import com.pastebin.pastebin_lite.model.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasteRepository extends JpaRepository<Paste, String> {
    // Spring Data JPA automatically provides:
    // - save()
    // - findById()
    // - findAll()
    // - deleteById()
    // No need to write implementation!
}