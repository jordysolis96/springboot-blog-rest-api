package com.springboot.blog.dto;

import com.springboot.blog.model.Comment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

@Data
public class PostDto {
    private long id;
    private String title;
    private String description;
    private String content;
    private Set<CommentDto> comments;
}
