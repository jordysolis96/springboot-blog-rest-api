package com.springboot.blog.service.impl;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Post;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();// if sortDir is in ascending order the return ascending order(Sort object) else return descending order(Sort object)

        // create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);//Sort.by default is going to sort in ascending order. Sort.by().descending will sort in descending order

        Page<Post> posts = postDao.findAll(pageable);

        // get content for page object(will turn Page data structure of objects(posts) into List data structure of objects(posts))
        List<Post> listOfPost = posts.getContent();

        //will convert list of posts into a list postDTO
        List<PostDto> content = listOfPost.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        // only converting to Post Response object because there are query params(if there is not pagination or sorting I'd return just the list of DTO's(List<PostDTO>))
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
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

