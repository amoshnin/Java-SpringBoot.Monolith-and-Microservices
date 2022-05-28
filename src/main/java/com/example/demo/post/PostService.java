package com.example.demo.post;

import com.example.demo.configuration.exceptions.GenericException;
import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.configuration.pagination.PaginationObject;
import com.example.demo.configuration.pagination.SortObject;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public Post getItem(Long userId, Long postId) {
        User user = this.userService.getItem(userId);
        List<Post> postsWithGivenPostId = user.getPosts().stream().filter(post -> post.getId().equals(postId)).collect(Collectors.toList());
        if (postsWithGivenPostId.size() > 0) {
            return postsWithGivenPostId.get(0);
        } else {
            throw new NotFoundException(String.format("Post with ID: %s for user with ID: %s is not found", user.getId(), postId));
        }
    }

    public List<Post> getListByUserId(Long userId, PaginationObject pagination, Optional<SortObject> sortObject) {
        User user = this.userService.getItem(userId);
        Optional<Sort> sort = Optional.empty();
        if (sortObject.isPresent()) {
            if (sortObject.get().getDescendingSort()) {
                sort = Optional.of(Sort.by(sortObject.get().getSortField()).descending());
            } else {
                sort = Optional.of(Sort.by(sortObject.get().getSortField()).ascending());
            }
        }
        Pageable pager = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize());
        if (sort.isPresent()) {
            pager = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), sort.get());
        }
        return this.postRepository.findByUser_Id(user.getId(), pager);
    }

    public Post add(Long userId, Post post, Principal principal) {
        if (this.userService.checkIfPrincipalIsAdmin(principal) || this.userService.checkIfPrincipalIsUser(userId, principal)) {
            User principalUser = this.userService.getPrincipalData(principal);
            User ownerUser = this.userService.getItem(userId);

            post.setUser(ownerUser);
            post.setCreatedByUserId(principalUser.getId());
            return this.postRepository.save(post);
        } else {
            throw new GenericException("You don't have the authorities to perform this operation. You don't have the ADMIN role nor you are the owner of the data you are trying to perform an operation on");
        }
    }

    public void delete(Long postId) {}

    public void update(Post post) {}
}
