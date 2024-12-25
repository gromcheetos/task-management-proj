package org.app.repository;

import org.app.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String userName, String email);
    User findUserByUsername(String username);
}
