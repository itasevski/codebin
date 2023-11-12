package com.ivo.codebin.model.dto;

import com.ivo.codebin.model.enumerations.Technology;
import lombok.Data;
import lombok.NonNull;

@Data
public class SnippetDto {

    private String id;
    @NonNull private String title;
    @NonNull private String description;
    @NonNull private String snippet;
    @NonNull private Long expiresIn;
    @NonNull private Technology technology;

}
