package org.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.JobPositionNotFoundException;
import org.app.exceptions.UserNotFoundException;
import org.app.model.JobPosition;
import org.app.model.Project;
import org.app.model.User;
import org.app.model.enums.Roles;
import org.app.repository.JobPositionRepository;
import org.app.repository.ProjectRepository;
import org.app.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JobPositionRepository jobPositionRepository;

    private final BCryptPasswordEncoder encoder;

    private final ProjectRepository projectRepository;

    public User createUser(User user, String password) {
        user.setPassword(encoder.encode(password));
        return userRepository.save(user);
    }

    public User updateUserById(Integer userId, String name, String email, String username, String password) throws UserNotFoundException {
        User toUpdateUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No Found User"));
        toUpdateUser.setName(name);
        toUpdateUser.setEmail(email);
        toUpdateUser.setUsername(username);
        toUpdateUser.setPassword(password);
        return userRepository.save(toUpdateUser);
    }

    public User getUserById(Integer userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No Found User"));
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public User getCurrentUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Current user not found.");
            throw new UserNotFoundException("User not found.");
        }
        String username = authentication.getName();
        return getUserByUsername(username);
    }

    public void updateUserProject(int userId, Project projects) throws UserNotFoundException {
        User toUpdateUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No Found User"));
        toUpdateUser.getOwnedProjects().add(projects);
        log.info("The user is the project owner");
        toUpdateUser.getProjects().add(projects);
        projects.getTeamMembers().add(toUpdateUser);
        log.info("The owner is also a member of the project");
        toUpdateUser.setRoles(Roles.ADMIN);
        log.info("Project owner is ADMIN");
        toUpdateUser.setProjectId(projects.getProjectId());
        projectRepository.save(projects);
        userRepository.save(toUpdateUser);
    }

    public User joinProject(int userId, int projectId, String role) throws UserNotFoundException {
        User toUpdateUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No Found User"));
        Project projects = projectRepository.findById(projectId).orElseThrow(() -> new UserNotFoundException("No Found Project"));
        //toUpdateUser.setProjectId(projectId);
        toUpdateUser.getProjects().add(projects);
        toUpdateUser.setRoles(Roles.valueOf(role));
        return userRepository.save(toUpdateUser);
    }

    public void updateUserPosition(int userId, int jobPositionId) throws UserNotFoundException, JobPositionNotFoundException {
        User toUpdateUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No Found User"));
        JobPosition userPosition = jobPositionRepository.findById(jobPositionId).orElseThrow(() -> new JobPositionNotFoundException("No Found JobPosition"));
        toUpdateUser.setJobPosition(userPosition);
        userRepository.save(toUpdateUser);
    }

    public List<User> findByProjectId(int projectId){
        return userRepository.findAllUsersByProjectId(projectId);
    }

}

