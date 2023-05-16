package nic.project.onlinestore.exception;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class FormErrorResponse {

    private final Map<String, String> errors;
    private final Date timestamp;

}
