package nic.project.onlinestore.dto;

import nic.project.onlinestore.models.Product;
import nic.project.onlinestore.models.User;

import java.util.HashMap;
import java.util.Map;

public class CartDTO {

    private User user;

    private Map<Product, Integer> items = new HashMap<>();

}
