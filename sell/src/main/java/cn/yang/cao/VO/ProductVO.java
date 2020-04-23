package cn.yang.cao.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 类目，包含商品
 */
@Data
public class ProductVO {

    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    //包含同一个类目下的所有商品
    @JsonProperty("product")
    private List<ProductInfoVO> productInfoVOList;
}
