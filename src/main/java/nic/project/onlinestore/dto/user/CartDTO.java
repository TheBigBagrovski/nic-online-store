package nic.project.onlinestore.dto.user;

import lombok.Data;
import nic.project.onlinestore.dto.product.ProductShortResponse;
import nic.project.onlinestore.model.User;

import java.util.Map;

@Data
public class CartDTO {

    private User user;

    private Map<ProductShortResponse, Integer> items;

}
