package com.example.demo.post;

import com.example.demo.configuration.exceptions.GenericException;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path="api/users")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping(path="{userId}/posts/item/{postId}")
    public Post getItem(@PathVariable Long userId, @PathVariable Long postId) {
        return this.postService.getItem(userId, postId);
    }

    @GetMapping(path="{userId}/posts/list")
    public List<Post> getListByUserId(@PathVariable Long userId) {
        return this.postService.getListByUserId(userId);
    }

    @PostMapping(path="{userId}/posts/item")
    public ResponseEntity<Object> add(@PathVariable Long userId, @RequestBody Post post, Principal principal) {
        // test if userId is current principal or principal is an ADMIN
        if (this.userService.checkIfPrincipalIsAdmin(principal) || this.userService.checkIfPrincipalIsUser(userId, principal)) {
            User principalData = this.userService.getPrincipalData(principal);
            Post newPost = this.postService.add(userId, principalData.getId(), post);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{postId}")
                    .buildAndExpand(newPost.getId()).toUri();
            return ResponseEntity.created(location).build();
        } else {
            throw new GenericException("You don't have the authorities to perform this operation. You don't have the ADMIN role nor you are the owner of the data you are trying to perform an operation on");
        }
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
