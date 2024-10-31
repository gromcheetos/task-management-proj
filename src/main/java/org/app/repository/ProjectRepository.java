package org.app.repository;

import org.app.model.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer> {

    Project findProjectByProjectName(String projectName);
}
