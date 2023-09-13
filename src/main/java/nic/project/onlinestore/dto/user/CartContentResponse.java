package nic.project.onlinestore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nic.project.onlinestore.dto.catalog.ProductShortResponse;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartContentResponse {

    private UserInfoResponse userInfoResponse;
    private Map<ProductShortResponse, Integer> items;
    private Integer totalQuantity;
    private BigDecimal totalPrice;

}
