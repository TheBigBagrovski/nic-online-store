package nic.project.onlinestore.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException (String message) {
        super(message);
    }

}
