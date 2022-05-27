package com.example.demo.user;
import com.example.demo.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    // @ManyToOne => many (users) to one (role) => user can only have one role
    // @ManyToMany => many (users) to many (role) => user can have multiple roles
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name="user_roles",
        joinColumns = {@JoinColumn(name="fk_user_id", referencedColumnName="user_id")},
        inverseJoinColumns = {@JoinColumn(name="fk_role_id", referencedColumnName="role_id")})
    private Set<Role> roles;
}