package com.example.demo.user;


import static org.mockito.Mockito.when;

import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    public void getList() {
        when(this.repository.findAll())
                .thenReturn(
                        Stream.of(
                                new User(1L, "alex@gmail.com", "123456", Sets.newHashSet()),
                                new User(2L, "tom@gmail.com", "123456", Sets.newHashSet())
                        ).collect(Collectors.toList()));
        assertEquals(2, this.service.getList().size());
    }

    @Test
    public void add() {
        User user = new User("alex@gmail.com", "123456");
        when(this.repository.save(user)).thenReturn(user);
        assertEquals(user, this.service.add(user));
    }

    @Test
    public void getItem() {
        User user = new User(1L, "alex@gmail.com", "123456", Sets.newHashSet());
        when(this.repository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(user, this.service.getItem(user.getId()));
    }
}