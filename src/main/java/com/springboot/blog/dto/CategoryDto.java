package com.springboot.blog.dto;

import com.springboot.blog.model.Post;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private long id;
    private String name;
    private String description;
}
