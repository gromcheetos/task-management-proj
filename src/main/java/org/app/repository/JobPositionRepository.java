package org.app.repository;

import org.app.model.JobPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPositionRepository extends CrudRepository<JobPosition, Integer> {
    List<JobPosition> findAllByProjectId(int projectId);

}
