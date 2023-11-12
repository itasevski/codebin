package com.ivo.codebin.repository;

import com.ivo.codebin.model.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, String> {

    List<Snippet> findByExpiresInBefore(LocalDateTime now);

}
