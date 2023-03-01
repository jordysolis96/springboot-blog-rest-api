package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Comment;
import com.springboot.blog.model.Post;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postDao;
    private ModelMapper mapper;
    CommentServiceImpl commentService;

    public PostServiceImpl(PostRepository postDao, ModelMapper mapper, CommentServiceImpl commentService){
        this.postDao = postDao;
        this.mapper = mapper;
        this.commentService = commentService;
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
        //using the modelMapper library to convert entity to dto
        // post is the source(first argument) and PostDto.class is the destination(second argument)
        PostDto postDto = mapper.map(post, PostDto.class);

        // setting the entity to the dto manually
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
//        Set<CommentDto> commentDtoSet = new HashSet<>();
//        for(Comment comment: post.getComments()){
//            commentDtoSet.add(mapToDTO(comment));
//        }
//        postDto.setComments(commentDtoSet);
        return postDto;
    }

    // converted DTO to Entity
    private Post mapToEntity(PostDto postDto){
        // using the modelMapper library to convert dto to entity
        // postDto is the source(first argument) and Post.class is the destination(second argument)
        Post post = mapper.map(postDto, Post.class);

        // setting the entity to the dto manually
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
//        Set<Comment> commentSet = new HashSet<>();
//        for(CommentDto commentDto: postDto.getComments()){
//            commentSet.add(mapToEntity(commentDto));
//        }
//        post.setComments(commentSet);

        return post;
    }


// -------------------------------------only need if the model mapper won't work----------------------------------------
//    private CommentDto mapToDTO(Comment comment){
//        //using the modelMapper library to convert entity to dto
//        // post is the source(first argument) and PostDto.class is the destination(second argument)
//        CommentDto commentDto = mapper.map(comment, CommentDto.class);
//
//        // setting the entity to the dto manually
////        CommentDto commentDto = new CommentDto();
////        commentDto.setId(comment.getId());
////        commentDto.setName(comment.getName());
////        commentDto.setEmail(comment.getEmail());
////        commentDto.setBody(comment.getBody());
//        return commentDto;
//    }
//
//    private Comment mapToEntity(CommentDto commentDto){
//        // using the modelMapper library to convert dto to entity
//        // postDto is the source(first argument) and Post.class is the destination(second argument)
//        Comment comment = mapper.map(commentDto, Comment.class);
//
//        // setting the entity to the dto manually
////        Comment comment = new Comment();
////        comment.setId(commentDto.getId());
////        comment.setName(commentDto.getName());
////        comment.setEmail(commentDto.getEmail());
////        comment.setBody(commentDto.getBody());
//        return comment;
//    }
}

