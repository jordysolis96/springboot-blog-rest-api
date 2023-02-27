package com.springboot.blog.service;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.model.Post;

import java.util.List;

public interface PostService {

    PostDto creatPost(PostDto postDto);

    List<PostDto> getAllPosts(int pageNo, int pageSize);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto, long id);

    void deletePostById(long id);
}
