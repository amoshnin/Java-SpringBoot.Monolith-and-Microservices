package com.example.demo.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="api/posts")
public class PostController {
    @Autowired
    private PostService postService;
}
