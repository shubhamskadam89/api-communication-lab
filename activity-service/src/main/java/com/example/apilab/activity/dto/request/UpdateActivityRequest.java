package com.example.apilab.activity.dto.request;

import com.example.apilab.activity.entity.ActivityVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateActivityRequest {

    @NotBlank(message = "Activity content cannot be blank")
    @Size(max = 2000, message = "Activity content cannot exceed 2000 characters")
    private String content;

    private ActivityVisibility visibility;

    private List<String> mediaUrls;
}
