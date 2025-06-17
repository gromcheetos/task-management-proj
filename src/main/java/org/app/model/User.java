package org.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.app.model.enums.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@Entity(name = "secured_users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private Integer projectId;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Board> boards;

    @OneToMany(mappedBy = "projectOwner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Project> projects;

    public User(String name, String email, String username, String password){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    public User(int id, String name, String email, String username, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String name, String userEmail, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(Roles.USER.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", name='" + name + '\'' +
            ", username='" + username + '\'' +
            '}';
    }
}
