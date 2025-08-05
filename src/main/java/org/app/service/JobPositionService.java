package org.app.service;

import lombok.AllArgsConstructor;
import org.app.model.JobPosition;
import org.app.model.User;
import org.app.repository.JobPositionRepository;
import org.app.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JobPositionService {

    private final JobPositionRepository jobPositionRepository;
    private final ProjectRepository projectRepository;

    public void addJobPosition(JobPosition jobPosition, int projectId) {
        jobPosition.setProject(projectRepository.findById(projectId).get());
        jobPositionRepository.save(jobPosition);
    }

    public List<JobPosition> findJobPositionById(Integer projectId) {
        return jobPositionRepository.findAllByProject(projectRepository.findById(projectId).get());
    }

    public JobPosition updateJobPosition(Integer jobPositionId, User user) {
        JobPosition jobPosition = jobPositionRepository.findById(jobPositionId).get();
        jobPosition.setUser(user);
        return jobPositionRepository.save(jobPosition);
    }

    public List<String> findExistingTitles(int projectId, List<String> titles) {
        List<String> jobPositions = new ArrayList<>();
        for(String title : titles) {
            if(jobPositionRepository.existsByProjectAndTitle(projectRepository.findById(projectId).get(), title)) {
                jobPositions.add(title);
            }
        }
        return jobPositions;
    }

}
