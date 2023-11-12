package com.ivo.codebin.model;

import com.ivo.codebin.model.base.BaseEntity;
import com.ivo.codebin.model.enumerations.Technology;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Snippet extends BaseEntity {

    private String title;

    private String description;

    private String snippet;

    private String createdAt;

    private String lastModified;

    private LocalDateTime expiresIn;

    private Integer stars;

    private Integer views;

    @Enumerated(EnumType.STRING)
    private Technology technology;

    @ManyToOne
    private User owner;

    public Snippet(String title, String description, String snippet, String createdAt, String lastModified, LocalDateTime expiresIn, Integer stars, Integer views, Technology technology, User owner) {
        this.title = title;
        this.description = description;
        this.snippet = snippet;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
        this.expiresIn = expiresIn;
        this.stars = stars;
        this.views = views;
        this.technology = technology;
        this.owner = owner;
    }

}
