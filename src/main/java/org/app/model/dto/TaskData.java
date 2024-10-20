package org.app.model.dto;

import lombok.Data;

@Data
public class TaskData {

    private String title;
    private String description;
    private String priority;
    private String deadline;

}
