package com.example.demo.post;

import com.example.demo.configuration.exceptions.NotFoundException;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Post getItem(Long userId, Long postId) {
        Optional<User> row = this.userRepository.findById(userId);
        if (!row.isPresent()) {
            throw new NotFoundException(String.format("User with ID: %s doesn't exist", userId));
        }
        User user = row.get();
        List<Post> postsWithGivenPostId = user.getPosts().stream().filter(post -> post.getId().equals(postId)).collect(Collectors.toList());
        if (postsWithGivenPostId.size() > 0) {
            return postsWithGivenPostId.get(0);
        } else {
            throw new NotFoundException(String.format("Post with ID: %s for user with ID: %s is not found", userId, postId));
        }
    }

    public List<Post> getListByUserId(Long userId) {
        Optional<User> row = this.userRepository.findById(userId);
        if (!row.isPresent()) {
            throw new NotFoundException(String.format("User with ID: %s doesn't exist", userId));
        }
        User user = row.get();
        return user.getPosts();
    }

    public Post add(Long userId, Post post) {
        Optional<User> row = this.userRepository.findById(userId);
        if (!row.isPresent()) {
            throw new NotFoundException(String.format("User with ID: %s doesn't exist", userId));
        }
        User user = row.get();
        post.setUser(user);
        return this.postRepository.save(post);
    }
}
