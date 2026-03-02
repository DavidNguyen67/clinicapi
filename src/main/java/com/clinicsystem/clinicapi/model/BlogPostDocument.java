package com.clinicsystem.clinicapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "blog_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostDocument {

    @Id
    private String id;

    @Indexed
    private String categoryId; // Reference to BlogCategory

    @Indexed
    @NotNull(message = "Author is required")
    private String authorId; // Reference to User UUID

    @NotBlank(message = "Title is required")
    private String title;

    @Indexed(unique = true)
    @NotBlank(message = "Slug is required")
    private String slug;

    private String excerpt;

    @NotBlank(message = "Content is required")
    private String content;

    private String featuredImage;

    private Integer viewCount = 0;

    @Indexed
    private PostStatus status = PostStatus.draft;

    @Indexed
    private LocalDateTime publishedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public enum PostStatus {
        draft, published, archived
    }

    // Helper methods
    public void incrementViewCount() {
        this.viewCount++;
    }

    public void publish() {
        this.status = PostStatus.published;
        if (this.publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
    }
}
