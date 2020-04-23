package cn.yang.cao.dataobject;

import cn.yang.cao.enums.ProductStatus;
import cn.yang.cao.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Proxy(lazy = false)
@DynamicUpdate //动态更新表中数据，如updateTime
public class ProductInfo {
    @Id
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    //剩余库存
    private Integer productStock;
    //商品描述
    private String productDescription;
    //商品小图
    private String productIcon;
    //商品状态,0正常，1下架。新增的商品默认已上架
    private Integer productStatus = ProductStatus.UP.getCode();
    //类目编号,商品使用类目编号与类目表关联
    private Integer categoryType;
    private Date createTime;
    private Date updateTime;

    @JsonIgnore
    public ProductStatus getProductStatusEnum(){
        return EnumUtil.getByCode(productStatus, ProductStatus.class);
    }
}
