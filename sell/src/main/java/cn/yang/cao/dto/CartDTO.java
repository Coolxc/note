package cn.yang.cao.dto;

import lombok.Data;

/**
 * 购物车。 DTO类似于数据库的中间表
 */
@Data
public class CartDTO {
    private String productId;
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
