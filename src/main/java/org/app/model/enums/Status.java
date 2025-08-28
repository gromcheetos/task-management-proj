package org.app.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done"),
    DEFERRED("Deferred");

    private final String value;

    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
