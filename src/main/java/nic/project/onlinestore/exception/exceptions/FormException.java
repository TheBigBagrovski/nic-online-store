package nic.project.onlinestore.exception.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class FormException extends RuntimeException {

    private final Map<String, String> errors;

    public FormException(Map<String, String> errors) {
        this.errors = errors;
    }

}
