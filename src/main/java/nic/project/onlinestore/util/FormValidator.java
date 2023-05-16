package nic.project.onlinestore.util;

import nic.project.onlinestore.exception.FormException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Component
public class FormValidator {

    public void checkFormBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            throw new FormException(errors); // todo согласовать с фронтом обработку ошибок в форме
        }
    }

}
