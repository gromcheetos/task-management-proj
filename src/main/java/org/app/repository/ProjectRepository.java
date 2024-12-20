package org.app.repository;

import org.app.model.Project;
import org.app.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer> {

    List<Project> findProjectByProjectOwner(User projectOwner);
    Project findProjectByProjectName(String projectName);
}