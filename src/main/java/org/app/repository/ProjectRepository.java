package org.app.repository;

import org.app.model.Board;
import org.app.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Integer> {

    List<Project> findProjectsByUserId(int userId);
    Project findProjectByProjectName(String projectName);
}
