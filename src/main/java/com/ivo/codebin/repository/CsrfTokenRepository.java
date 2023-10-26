package com.ivo.codebin.repository;

import com.ivo.codebin.model.CsrfToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsrfTokenRepository extends JpaRepository<CsrfToken, Integer> {
}
