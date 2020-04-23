package cn.yang.cao.repository;

import cn.yang.cao.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
    //查询已经上架的所有商品信息
    List<ProductInfo> findProductInfoByProductStatus(Integer status);
}
