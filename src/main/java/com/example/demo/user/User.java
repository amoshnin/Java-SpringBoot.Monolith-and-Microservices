package com.example.demo.user;
import com.example.demo.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.mockito.internal.util.collections.Sets;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Period;
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
    @Email(message = "Email is not correctly formatted")
    @Column(unique = true)
    private String email;
    @Size(min=6, message = "Password should have at least 6 characters")
    private String password;
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;
    // @ManyToOne => many (users) to one (role) => user can only have one role
    // @ManyToMany => many (users) to many (role) => user can have multiple roles
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name="user_roles",
        joinColumns = {@JoinColumn(name="fk_user_id", referencedColumnName="user_id")},
        inverseJoinColumns = {@JoinColumn(name="fk_role_id", referencedColumnName="role_id")})
    private Set<Role> roles;

    public User(String email, String password, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(Long id, String email, String password, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    private int getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }
}