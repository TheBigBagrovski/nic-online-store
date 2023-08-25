package nic.project.onlinestore.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import nic.project.onlinestore.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    private ResponseEntity<ErrorResponse> handleUserNotFoundException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    private ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceAlreadyExistsException.class})
    private ResponseEntity<ErrorResponse> handleAlreadyExistsException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ImageUploadException.class})
    private ResponseEntity<ErrorResponse> handleImageUploadException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, InvalidFormatException.class, JsonMappingException.class})
    private ResponseEntity<ErrorResponse> handleWrongFormatException() { // когда в @RequestParam или @PathVariable вводится неверный формат
        ErrorResponse response = new ErrorResponse(
                "Произошла ошибка: неверный формат аргумента",
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FormException.class)
    private ResponseEntity<FormErrorResponse> handleFormException(FormException e) {
        FormErrorResponse response = new FormErrorResponse(
                e.getErrors(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    private ResponseEntity<ErrorResponse> handleImageSizeLimitException() {
        ErrorResponse response = new ErrorResponse(
                "Размер одного из изображений превышает 10 Мб или общий объем всех файлов больше 50 Мб",
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    private ResponseEntity<ErrorResponse> handleEmptyJsonRequestException() {
        ErrorResponse response = new ErrorResponse(
                "JSON-запрос не должен быть пустым",
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonParseException.class)
    private ResponseEntity<ErrorResponse> handleInvalidJsonException() {
        ErrorResponse response = new ErrorResponse(
                "Ошибка в JSON-запросе",
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<ErrorResponse> handleRuntimeException(Exception e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
