package com.ivo.codebin.service.implementation;

import com.ivo.codebin.model.Snippet;
import com.ivo.codebin.model.User;
import com.ivo.codebin.model.dto.SnippetDto;
import com.ivo.codebin.model.enumerations.Technology;
import com.ivo.codebin.model.exception.BadDtoException;
import com.ivo.codebin.model.exception.SnippetNotFoundException;
import com.ivo.codebin.repository.SnippetRepository;
import com.ivo.codebin.service.SnippetService;
import com.ivo.codebin.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SnippetServiceImplementation implements SnippetService {

    private final SnippetRepository snippetRepository;
    private final DateTimeFormatter formatter;
    private final UserService userService;

    @Override
    public Snippet findById(String id) {
        if(id == null) throw new BadDtoException("Id must not be null!");

        return this.snippetRepository.findById(id)
                .orElseThrow(() -> new SnippetNotFoundException("Snippet with ID " + id + " does not exist!"));
    }

    @Override
    public Snippet create(SnippetDto snippetDto, String accessToken) {
        String timestamp = this.formatter.format(LocalDateTime.now());
        LocalDateTime expires = LocalDateTime.now().plusMinutes(1); // FE will always send minutes
        User owner = this.userService.getUserData(accessToken);

        Snippet snippet =
                new Snippet(
                        snippetDto.getTitle(),
                        snippetDto.getDescription(),
                        snippetDto.getSnippet(),
                        timestamp,
                        timestamp,
                        expires,
                        0, 0,
                        snippetDto.getTechnology(),
                        owner);

        return this.snippetRepository.save(snippet);
    }

    @Override
    public Snippet edit(SnippetDto snippetDto) {
        Snippet existingSnippet = findById(snippetDto.getId());
        LocalDateTime expires = LocalDateTime.parse(existingSnippet.getCreatedAt(), this.formatter).plusMinutes(snippetDto.getExpiresIn());

        existingSnippet.setTitle(snippetDto.getTitle());
        existingSnippet.setDescription(snippetDto.getDescription());
        existingSnippet.setSnippet(snippetDto.getSnippet());
        existingSnippet.setLastModified(this.formatter.format(LocalDateTime.now()));
        existingSnippet.setExpiresIn(expires);
        existingSnippet.setTechnology(snippetDto.getTechnology());

        return this.snippetRepository.save(existingSnippet);
    }

    @Override
    public void delete(String id) {
        this.snippetRepository.delete(findById(id));
    }


    @Scheduled(fixedRate = 60000)
    public void deleteExpiredSnippets() {
        log.info("Deleting expired snippets...");
        List<Snippet> expiredSnippets = getExpiredSnippets();
        this.snippetRepository.deleteAll(expiredSnippets);
        log.info(String.format("Deleted %d snippets.", expiredSnippets.size()));
    }

    private List<Snippet> getExpiredSnippets() {
        return this.snippetRepository.findByExpiresInBefore(LocalDateTime.now());
    }
}
