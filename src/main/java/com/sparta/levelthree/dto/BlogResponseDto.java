package com.sparta.levelthree.dto;

import com.sparta.levelthree.entity.Blog;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
public class BlogResponseDto {
    private String title;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long id;
    private List<CommentResponseDto>  commentResponseDtos;

    public BlogResponseDto(Blog blog) {
        this.author = blog.getAuthor();
        this.content = blog.getContent();
        this.title = blog.getTitle();
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
        this.id = blog.getId();
        this.commentResponseDtos = blog.getComments()
                .stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .toList();
    }
}