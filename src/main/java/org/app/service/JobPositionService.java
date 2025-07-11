package org.app.service;

import lombok.AllArgsConstructor;
import org.app.model.JobPosition;
import org.app.repository.JobPositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JobPositionService {

    private final JobPositionRepository jobPositionRepository;

    public JobPosition createJobPosition(JobPosition jobPosition) {
        return jobPositionRepository.save(jobPosition);
    }

    public List<JobPosition> findJobPositionById(Integer jobPositionId) {
        return jobPositionRepository.findAllByProjectId(jobPositionId);
    }
}
