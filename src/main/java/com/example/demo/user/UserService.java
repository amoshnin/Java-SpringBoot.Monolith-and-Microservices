package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(username);
        UserDetailsPrincipal userDetailsPrincipal = new UserDetailsPrincipal(user);
        return userDetailsPrincipal;
    }

    public void add(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        this.userRepository.save(user);
    }
}
