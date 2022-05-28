package com.example.demo.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="api/users")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping(path="{userId}/posts/item/{postId}")
    public Post getItem(@PathVariable Long userId, @PathVariable Long postId) {
        return this.postService.getItem(userId, postId);
    }

    @GetMapping(path="{userId}/posts/list")
    public List<Post> getListByUserId(@PathVariable Long userId) {
        return this.postService.getListByUserId(userId);
    }

    @PostMapping(path="{userId}/posts/item")
    public ResponseEntity<Object> add(@PathVariable Long userId, @RequestBody Post post) {
        Post newPost = this.postService.add(userId, post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(newPost.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

//    @PutMapping(path="item")
//    public void update(Post post) {
//
//    }
//
//    @DeleteMapping(path="item/{postId}")
//    public void dalete(@PathVariable Long postId) {
//
//    }
}
