package nic.project.onlinestore.exception.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailNotFoundException extends UsernameNotFoundException {

    public EmailNotFoundException(String message) {
        super(message);
    }

}