package com.example.demo.post;

import com.example.demo.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String description;
    @JsonIgnore // we will see list of posts in user. but we don't want to see a user when requesting a post.
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fk_user_id", referencedColumnName="user_id")
    private User user;
}
