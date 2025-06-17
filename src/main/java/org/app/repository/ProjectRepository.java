package org.app.repository;

import org.app.model.Project;
import org.app.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer> {

    List<Project> findProjectByProjectOwner(User projectOwner);
    Project findProjectByProjectName(String projectName);

}