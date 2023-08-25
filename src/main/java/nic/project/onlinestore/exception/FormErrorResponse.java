package nic.project.onlinestore.exception;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@Setter
public class FormErrorResponse {

    private final Map<String, String> errors;
    private final Date timestamp;

}
