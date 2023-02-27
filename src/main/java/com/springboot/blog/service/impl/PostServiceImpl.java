package com.springboot.blog.service.impl;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Post;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postDao;

    public PostServiceImpl(PostRepository postDao){
        this.postDao = postDao;
    }

    @Override
    public PostDto creatPost(PostDto postDto) {

        //convert DTO to entity
       Post post = mapToEntity(postDto);
        //will save to database
        Post newPost = postDao.save(post);

        //convert entity to DTO
        PostDto postResponse = mapToDto(newPost);

        //will return this as response to client to see what they created
        return postResponse;
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postDao.findAll();
        //will convert list of posts into a list postDTO
        return posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(long id) {
        // get post by id from the database
        Post post = postDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        // get post by id from the database
        Post post = postDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postDao.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        // get post by id from the database
        Post post = postDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postDao.delete(post);
    }


    // convert Entity to DTO
    private PostDto mapToDto(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());

        return postDto;
    }

    // converted DTO to Entity
    private Post mapToEntity(PostDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        return post;
    }
}

