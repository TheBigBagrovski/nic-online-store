package nic.project.onlinestore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nic.project.onlinestore.dto.product.ProductShortResponse;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartContentResponse {

    private UserInfoResponse userInfoResponse;

    private Map<ProductShortResponse, Integer> items;

}
