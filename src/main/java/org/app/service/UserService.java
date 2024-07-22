package org.app.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.app.exceptions.UserNotFoundException;
import org.app.model.TodoTask;
import org.app.model.User;
import org.app.repository.TaskRepository;
import org.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final BCryptPasswordEncoder encoder;

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

    public void deleteUserById(Integer userId) throws UserNotFoundException{
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException("No Found User");
        }
        userRepository.deleteById(userId);
    }

    public List<TodoTask> getTasksByUserId(Integer userId) throws UserNotFoundException {
        List<TodoTask> tasks = taskRepository.findTodoTaskByUserId(userId);
        if(tasks.isEmpty()){
            throw new UserNotFoundException("No Found User");
        }
        return tasks;
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

}

