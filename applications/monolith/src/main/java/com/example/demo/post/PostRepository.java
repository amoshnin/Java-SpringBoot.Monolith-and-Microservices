package com.example.demo.post;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    public List<Post> findByUser_Id(Long userId, Pageable pageable);
    public List<Post> findByUser_Id(Long userId);
}
