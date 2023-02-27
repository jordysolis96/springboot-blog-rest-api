package com.springboot.blog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;

    // mappedBy will look for post property name in comments pojo
    // CascadeType.ALL will make sure that associated records get dropped along with owning object
    // try to refactor Set collection into a List collection
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) // Post is the owning side of the relationship
    private Set<Comment> comments = new HashSet<>();
}
