package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Comment;
import com.springboot.blog.model.Post;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // set post to comment entity
        comment.setPost(post);

        // save comment entity to DB
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        // convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        // I'd have to turn the long primitive into a object of Long if I want this snippet to work
//        if(!comment.getPost().getId.equals(post.getId)){
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
//        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        // I'd have to turn the long primitive into a object of Long if I want this snippet to work
//        if(!comment.getPost().getId.equals(post.getId)){
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
//        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        // I'd have to turn the long primitive into a object of Long if I want this snippet to work
//        if(!comment.getPost().getId.equals(post.getId)){
//            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
//        }

        commentRepository.delete(comment);

    }

    private CommentDto mapToDTO(Comment comment){
        //using the modelMapper library to convert entity to dto
        // post is the source(first argument) and PostDto.class is the destination(second argument)
        CommentDto commentDto = mapper.map(comment, CommentDto.class);

        // setting the entity to the dto manually
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        // using the modelMapper library to convert dto to entity
        // postDto is the source(first argument) and Post.class is the destination(second argument)
        Comment comment = mapper.map(commentDto, Comment.class);

        // setting the entity to the dto manually
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;
    }
}
