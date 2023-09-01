package nic.project.onlinestore.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import nic.project.onlinestore.exception.exceptions.ImageUploadException;
import nic.project.onlinestore.exception.exceptions.ResourceAlreadyExistsException;
import nic.project.onlinestore.exception.exceptions.ResourceNotFoundException;
import nic.project.onlinestore.exception.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    private Map<String, String> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ResourceNotFoundException.class, ResourceAlreadyExistsException.class, ImageUploadException.class})
    private ErrorResponse handleResourceNotFoundException(Exception e) {
        return new ErrorResponse(
                e.getMessage(),
                new Date()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UserNotFoundException.class})
    private ErrorResponse handleUserNotFoundException(Exception e) {
        return new ErrorResponse(
                e.getMessage(),
                new Date()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, InvalidFormatException.class, JsonMappingException.class})
    private ErrorResponse handleWrongFormatException() { // когда в @RequestParam или @PathVariable вводится неверный формат
        return new ErrorResponse(
                "Произошла ошибка: неверный формат аргумента",
                new Date()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    private ErrorResponse handleImageSizeLimitException() {
        return new ErrorResponse(
                "Размер одного из изображений превышает 10 Мб или общий объем всех файлов больше 50 Мб",
                new Date()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    private ErrorResponse handleEmptyJsonRequestException() {
        return new ErrorResponse(
                "JSON-запрос не должен быть пустым",
                new Date()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonParseException.class)
    private ErrorResponse handleInvalidJsonException() {
        return new ErrorResponse(
                "Ошибка в JSON-запросе",
                new Date()
        );
    }

}
