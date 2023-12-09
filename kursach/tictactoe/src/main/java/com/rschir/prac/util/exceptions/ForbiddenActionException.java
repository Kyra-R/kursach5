package com.rschir.prac.util.exceptions;

public class ForbiddenActionException extends RuntimeException{
    private String message;

    public ForbiddenActionException(String game_problem) {
        this.message = game_problem;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
