package com.example.demo.user;

import com.example.demo.configuration.response.RestApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s not found", email)));
        UserDetailsPrincipal userDetailsPrincipal = new UserDetailsPrincipal(user);
        return userDetailsPrincipal;
    }

    public List<User> getList() {
        return this.userRepository.findAll();
    }

    public User getItem(Long userId) {
        Optional<User> row = this.userRepository.findById(userId);
        if (row.isPresent()) {
            return row.get();
        } else {
            throw new RestApiException(String.format("User with ID: %s doesn't exist", userId));
        }
    }

    public User add(User user) {
        if (this.userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new RestApiException(String.format("User with email: '%s' already exists. Emails must be unique.", user.getEmail()));
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public void delete(Long userId) {
        this.userRepository.deleteById(userId);
    }

    public void update(User user) {
        System.out.println("here" + user.getId() + user.getEmail());
        Optional<User> row = this.userRepository.findById(user.getId());
        if (row.isPresent()) {
            User item = row.get();
            if (!user.getEmail().isEmpty()) {
                item.setEmail(user.getEmail());
            }
            if (!user.getRoles().isEmpty()) {
                item.setRoles(user.getRoles());
            }
            if (!user.getPassword().isEmpty()) {
                item.setPassword(user.getPassword());
            }
            this.userRepository.save(item);
        } else {
            throw new RestApiException(String.format("User with ID: %s doesn't exist", user.getId()));
        }
    }
}
