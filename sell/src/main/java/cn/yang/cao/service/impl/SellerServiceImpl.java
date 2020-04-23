package cn.yang.cao.service.impl;

import cn.yang.cao.dataobject.SellerInfo;
import cn.yang.cao.enums.ResultEnum;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.repository.SellerInfoRepository;
import cn.yang.cao.service.SellerService;
import cn.yang.cao.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Override
    public SellerInfo findSellerInfoById(String id) {
        return sellerInfoRepository.findById(id).orElse(null);
    }

    @Override
    public SellerInfo findSellerInfoByPhone(String phone) {
        return sellerInfoRepository.findSellerInfoByPhone(phone);
    }

    @Override
    public void create(SellerInfo sellerInfo) {
        if (sellerInfoRepository.findSellerInfoByPhone(sellerInfo.getPhone()) != null){
            throw new SellException(ResultEnum.USER_EXISTS);
        }
        sellerInfo.setPassword(HashUtil.hash(sellerInfo.getPassword()));
        sellerInfoRepository.save(sellerInfo);
    }
}
