package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.ProductInfo;
import cn.yang.cao.dto.CartDTO;
import cn.yang.cao.enums.ProductStatus;
import cn.yang.cao.enums.ResultEnum;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.repository.ProductInfoRepository;
import cn.yang.cao.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String id) {
        return repository.findById(id).orElse(null);

    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void save(ProductInfo productInfo) {
        repository.save(productInfo);
    }

    //查找所有已上架商品
    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findProductInfoByProductStatus(ProductStatus.UP.getCode());
    }
    //加库存
    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
            }
            productInfo.setProductStock(cartDTO.getProductQuantity() + productInfo.getProductStock());
            repository.save(productInfo);
        }

    }
    //减库存
    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList){
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
            if (productInfo == null){
                throw  new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
            }
            Integer stock = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if (stock < 0){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(stock);
            repository.save(productInfo);// save包含更新的操作
        }

    }

    @Override
    //上架商品
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null){
            log.error("商品未找到 ProductID:{}", productId);
            throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        if (productInfo.getProductStatusEnum() == ProductStatus.UP){
            throw new SellException(ResultEnum.ALLREADY_ON_SALE);
        }
        productInfo.setProductStatus(ProductStatus.UP.getCode());
        return repository.save(productInfo);
    }

    @Override
    //下架商品
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null){
            log.error("商品未找到 ProductID:{}", productId);
            throw new SellException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        if (productInfo.getProductStatusEnum() == ProductStatus.DOWN){
            throw new SellException(ResultEnum.ALLREADY_OFF_SALE);
        }
        productInfo.setProductStatus(ProductStatus.DOWN.getCode());
        return repository.save(productInfo);
    }
}
