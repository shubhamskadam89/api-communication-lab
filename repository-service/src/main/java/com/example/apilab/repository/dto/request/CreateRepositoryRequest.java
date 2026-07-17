package com.example.apilab.repository.dto.request;

import com.example.apilab.repository.entity.RepositoryVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRepositoryRequest {

    @NotBlank(message = "Repository name cannot be blank")
    @Size(max = 100, message = "Repository name cannot exceed 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Visibility cannot be null")
    private RepositoryVisibility visibility;

    @Size(max = 50, message = "Primary language cannot exceed 50 characters")
    private String primaryLanguage;
}
