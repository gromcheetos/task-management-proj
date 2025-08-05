package org.app.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DuplicateCheckDto {
    private int projectId;
    private List<String> positions;
}
