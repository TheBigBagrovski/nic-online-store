package nic.project.onlinestore.exception;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    private final String message;
    private final Date timestamp;

}
