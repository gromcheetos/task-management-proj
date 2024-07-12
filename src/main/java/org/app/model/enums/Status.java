package org.app.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    IN_PROGRESS("In Progress"),
    DONE("Done"),
    TO_DO("To Do"),
    DEFERRED("Deferred");

    private final String value;
}
