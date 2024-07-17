package org.app.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum Status {

    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done"),
    DEFERRED("Deferred");

    private final String value;

}
