package nic.project.onlinestore.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException (String message) {
        super(message);
    }

}