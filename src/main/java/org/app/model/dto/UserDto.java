package org.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String username;
    private String name;
    private String roles;
    private String jobTitle;
}