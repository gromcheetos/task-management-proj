package org.app.model.enums;

public enum Status {
    IN_PROGRESS("In Progress"), DONE("Done"), TO_DO("To Do"), DEFERRED("Deferred");

    private final String userStatus;
    private Status(final String userStatus) {
        this.userStatus = userStatus;
    }
    public String getUserStatus(){
        return userStatus;
    }
}
