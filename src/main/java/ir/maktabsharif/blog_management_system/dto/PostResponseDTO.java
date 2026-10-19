package ir.maktabsharif.blog_management_system.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class PostResponseDTO {

    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;
}
