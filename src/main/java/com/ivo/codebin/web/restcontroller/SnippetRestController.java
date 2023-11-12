package com.ivo.codebin.web.restcontroller;

import com.ivo.codebin.model.Snippet;
import com.ivo.codebin.model.dto.SnippetDto;
import com.ivo.codebin.service.SnippetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/snippet")
public class SnippetRestController {

    private final SnippetService snippetService;

    @GetMapping
    public Snippet findById(@RequestParam String id) {
        return this.snippetService.findById(id);
    }

    @PostMapping
    public Snippet createSnippet(@RequestBody SnippetDto snippetDto, @CookieValue("access-token") String accessToken) {
        return this.snippetService.create(snippetDto, accessToken);
    }

    @PutMapping
    public Snippet editSnippet(@RequestBody SnippetDto snippetDto) {
        return this.snippetService.edit(snippetDto);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSnippet(@RequestParam String id) {
        this.snippetService.delete(id);
        return ResponseEntity.ok(String.format("Successfully deleted snippet with id %s", id));
    }

}
