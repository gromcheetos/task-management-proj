package org.app.model.dto;

import lombok.Data;
import org.app.model.JobPosition;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class JobPositionDto {

    private int jobPositionId;
    private String title;

    public static JobPositionDto fromEntity(JobPosition entity) {
        JobPositionDto dto = new JobPositionDto();
        dto.setJobPositionId(entity.getJobPositionId());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public static List<JobPositionDto> fromEntity(List<JobPosition> entities) {
        return entities.stream()
                .map(JobPositionDto::fromEntity)
                .collect(Collectors.toList());
    }

}
