package nic.project.onlinestore.dto;

import lombok.Data;
import nic.project.onlinestore.models.User;

import java.util.Map;

@Data
public class CartDTO {

    private User user;

    private Map<ProductDTO, Integer> items;

}
