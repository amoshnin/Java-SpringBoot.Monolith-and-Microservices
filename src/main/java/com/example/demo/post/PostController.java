package com.example.demo.post;

import com.example.demo.configuration.pagination.PaginationObject;
import com.example.demo.configuration.pagination.SortObject;
import com.example.demo.configuration.response.PaginatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/users")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping(path="{userId}/posts/item/{postId}")
    public Post getItem(@PathVariable Long userId, @PathVariable Long postId) {
        return this.postService.getItem(userId, postId);
    }

    @GetMapping(value={"{userId}/posts/list", "{userId}/posts/list/{offset}/{pageSize}"})
    public PaginatedResponse<List<Post>> getListByUserId(
            @PathVariable Long userId,
            @PathVariable(required=false) Optional<Integer> offset,
            @PathVariable(required=false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        List<Post> result = this.postService.getListByUserId(userId, pagination, Optional.empty());
        return new PaginatedResponse<>(result);
    }

    @GetMapping(value={"{userId}/posts/list/sorted/{sortField}/{descendingSort}", "{userId}/posts/list/sorted/{sortField}/{descendingSort}/{offset}/{pageSize}"})
    public PaginatedResponse<List<Post>> getListByUserIdSorted(
            @PathVariable(required=true) Long userId,
            @PathVariable(required=true) String sortField,
            @PathVariable(required=true) boolean descendingSort,
            @PathVariable(required=false) Optional<Integer> offset,
            @PathVariable(required=false) Optional<Integer> pageSize) {
        PaginationObject pagination = new PaginationObject(offset, pageSize);
        List<Post> posts = this.postService.getListByUserId(userId, pagination, Optional.of(new SortObject(sortField, descendingSort)));
        return new PaginatedResponse<>(posts);
    }

    @PostMapping(path="{userId}/posts/item")
    public ResponseEntity<Object> add(@PathVariable Long userId, @RequestBody Post post, Principal principal) {
        // test if userId is current principal or principal is an ADMIN
        Post newPost = this.postService.add(userId, post, principal);
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
//    public void delete(@PathVariable Long postId) {
//
//    }
}
