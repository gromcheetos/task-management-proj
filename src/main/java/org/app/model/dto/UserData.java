package org.app.model.dto;

import lombok.Data;

@Data
public class UserData {
    private int id;
    private String name;
    private String email;

    public UserData(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
