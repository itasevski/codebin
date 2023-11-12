package com.ivo.codebin.service;

import com.ivo.codebin.model.Snippet;
import com.ivo.codebin.model.User;
import com.ivo.codebin.model.dto.SnippetDto;
import com.ivo.codebin.model.enumerations.Technology;

public interface SnippetService {

    Snippet findById(String id);

    Snippet create(SnippetDto snippetDto, String accessToken);

    Snippet edit(SnippetDto snippetDto);

    void delete(String id);

}
