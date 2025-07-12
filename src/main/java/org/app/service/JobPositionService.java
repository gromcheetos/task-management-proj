package org.app.service;

import lombok.AllArgsConstructor;
import org.app.model.JobPosition;
import org.app.model.Project;
import org.app.repository.JobPositionRepository;
import org.app.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JobPositionService {

    private final JobPositionRepository jobPositionRepository;
    private final ProjectRepository projectRepository;

    public JobPosition createJobPosition(JobPosition jobPosition) {
        return jobPositionRepository.save(jobPosition);
    }

    public List<JobPosition> findJobPositionById(Integer projectId) {
        return jobPositionRepository.findAllByProject(projectRepository.findById(projectId).get());
    }
}
