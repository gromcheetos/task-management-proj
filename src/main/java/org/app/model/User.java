package org.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.app.model.enums.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@Entity(name = "secured_users")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"projects", "boards", "jobPosition","ownedProjects"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    private String name;
    private String email;
    private String username;
    private String password;
    private Integer projectId;
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Board> boards;

    @OneToMany(mappedBy = "projectOwner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference(value = "owned-projects")
    private List<Project> ownedProjects = new ArrayList<>();

    @ManyToMany(mappedBy = "teamMembers", fetch = FetchType.EAGER)
    private List<Project> projects = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    @JsonBackReference(value = "job-position-user")
    private JobPosition jobPosition;

    public User(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(int id, String name, String email, String username, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String name, String email, String username) {
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

    public String getJobTitle(){
        if(jobPosition != null){
            return jobPosition.getTitle();
        }
        return null;
    }

    public String getInitials() {
        if (name == null || name.isBlank()) return "";

        String[] words = name.trim().split("\\s+");

        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty() && initials.length() < 2) {
                initials.append(Character.toUpperCase(word.charAt(0)));
            }
        }

        return initials.toString();
    }


}
