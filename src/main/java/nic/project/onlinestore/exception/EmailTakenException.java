package nic.project.onlinestore.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailTakenException extends AuthenticationException {

    public EmailTakenException (String message) {
        super(message);
    }

}
