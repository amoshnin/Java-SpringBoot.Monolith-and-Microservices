package com.example.demo.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping(path="item/{postId}")
    public Post getItem(@PathVariable Long postId) {
        return new Post();
    }

    @GetMapping(path="list/{userId}")
    public List<Post> getListByUserId() {
        List<Post> q = new ArrayList<>();
        q.add(new Post());
        return q;
    }

    @PostMapping(path="item")
    public void add(Post post) {

    }

    @PutMapping(path="item")
    public void update(Post post) {

    }

    @DeleteMapping(path="item/{postId}")
    public void dalete(@PathVariable Long postId) {

    }
}
