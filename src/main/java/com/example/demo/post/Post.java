package com.example.demo.post;

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
}
