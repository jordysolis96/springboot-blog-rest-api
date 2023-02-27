package com.springboot.blog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String email;

    private String body;

    //The FetchType.Lazy tells Hibernate to only the related entities from
    //the database when you use the relationship.
    @ManyToOne(fetch = FetchType.LAZY) // Many comments to one Post
    @JoinColumn(name = "post_id", nullable = false) // specifies foreign key for comments table named post_id
    private Post post;
}
